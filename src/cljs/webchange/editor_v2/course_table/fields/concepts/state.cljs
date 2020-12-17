(ns webchange.editor-v2.course-table.fields.concepts.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.course-table.state.db :as db]
    [webchange.editor-v2.course-table.state.edit-utils :as utils]
    [webchange.editor-v2.course-table.state.selection :as selection]
    [webchange.interpreter.events :as interpreter.events]
    [webchange.interpreter.subs :as interpreter.subs]
    [webchange.subs :as subs]
    [webchange.warehouse :as warehouse]))

(defn path-to-db
  [relative-path component-id]
  (->> relative-path
       (concat [:edit-from :concepts component-id])
       (db/path-to-db)))

(defn- get-current-lesson-names
  [level-data lesson-data]
  (let [lesson-type (-> (:type lesson-data) (keyword))
        scheme (->> (get-in level-data [:scheme lesson-type :lesson-sets])
                    (map (fn [name] [(keyword name) nil]))
                    (into {}))]
    (-> scheme
        (merge (:lesson-sets lesson-data))
        (select-keys (keys scheme)))))

(defn- lesson-set-name->lesson-set-items
  [lesson-set-name db]
  (-> (interpreter.subs/lesson-sets-data db [lesson-set-name])
      (first)
      (get-in [:item-ids])))

(defn- get-current-value
  [db current-lesson-names]
  (->> current-lesson-names
       (map (fn [[concepts-name lesson-set-name]]
              [concepts-name (lesson-set-name->lesson-set-items lesson-set-name db)]))
       (into {})))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [db]} [_ selection component-id]]
    (let [course-data (subs/course-data db)
          current-lesson-names (get-current-lesson-names (utils/get-level-data course-data selection)
                                                         (utils/get-lesson-data course-data selection))
          current-value (get-current-value db current-lesson-names)
          selection-data (-> db selection/selection :data)]
      {:db       (-> db
                     (assoc-in (path-to-db [:initial-value] component-id) current-value)
                     (assoc-in (path-to-db [:selection-data] component-id) selection-data)
                     (assoc-in (path-to-db [:lesson-names] component-id) current-lesson-names))
       :dispatch [::reset-current-lesson-sets current-value component-id]})))

(re-frame/reg-sub
  ::available-dataset-items
  (fn [[_ lesson-set-name component-id]]
    [(re-frame/subscribe [::interpreter.subs/dataset-items {:exclude-items-fields [:data]}])
     (re-frame/subscribe [::selected-dataset-items lesson-set-name component-id])])
  (fn [[dataset-items selected-items]]
    (->> dataset-items
         (map second)
         (filter (fn [{:keys [id]}] (->> selected-items (some #(= (:id %) id)) (not))))
         (sort-by :name))))

;; Current value

(defn- current-lesson-sets
  [db component-id]
  (get-in db (path-to-db [:current-lesson-sets] component-id) {}))

(re-frame/reg-sub
  ::current-lesson-sets
  (fn [db [_ component-id]]
    (current-lesson-sets db component-id)))

(re-frame/reg-event-fx
  ::reset-current-lesson-sets
  (fn [{:keys [db]} [_ current-lesson-sets component-id]]
    {:db (assoc-in db (path-to-db [:current-lesson-sets] component-id) current-lesson-sets)}))

(re-frame/reg-sub
  ::selected-dataset-items
  (fn [[_ _ component-id]]
    [(re-frame/subscribe [::current-lesson-sets component-id])
     (re-frame/subscribe [::interpreter.subs/dataset-items {:exclude-items-fields [:data]}])])
  (fn [[lesson-sets dataset-items] [_ lesson-set-name]]
    (->> (get lesson-sets lesson-set-name)
         (map (fn [id] (get dataset-items id)))
         (sort-by :name))))

(re-frame/reg-event-fx
  ::add-dataset-item
  (fn [{:keys [db]} [_ item-id lesson-set-name component-id]]
    {:db (update-in db (path-to-db [:current-lesson-sets lesson-set-name] component-id) conj item-id)}))

(re-frame/reg-event-fx
  ::remove-dataset-item
  (fn [{:keys [db]} [_ item-id lesson-set-name component-id]]
    {:db (update-in db (path-to-db [:current-lesson-sets lesson-set-name] component-id) (fn [l] (remove #(= % item-id) l)))}))

;; Save

(defn- update-lesson-sets
  [course-data lesson-sets selection-data]
  (let [lesson-path (utils/get-lesson-path course-data selection-data)]
    (assoc-in course-data (conj lesson-path :lesson-sets) lesson-sets)))

(re-frame/reg-event-fx
  ::save
  (fn [{:keys [db]} [_ component-id]]
    (let [initial-value (get-in db (path-to-db [:initial-value] component-id))
          current-value (current-lesson-sets db component-id)]
      (if-not (= initial-value current-value)
        (let [lesson-names (get-in db (path-to-db [:lesson-names] component-id))
              changes (->> (keys current-value)
                           (filter (fn [concept-name] (not= (get initial-value concept-name)
                                                            (get current-value concept-name))))
                           (map (fn [concept-name]
                                  (let [lesson (interpreter.subs/lesson-set-data db (get lesson-names concept-name))
                                        item-ids (get current-value concept-name)]
                                    {:id   (:id lesson)
                                     :name (:name lesson)
                                     :data {:name  (:name lesson)
                                            :items (map (fn [id] {:id id}) item-ids)}}))))]
          {:dispatch-n (map (fn [{:keys [id data name]}]
                              [::warehouse/update-lesson-set
                               {:id   id
                                :data data}
                               {:on-success [::save-lesson-set-success name]}])
                            changes)})
        {}))))

(re-frame/reg-event-fx
  ::save-lesson-set-success
  (fn [{:keys [_]} [_ lesson-name {:keys [lesson]}]]
    {:dispatch [::interpreter.events/update-course-lessons [(assoc lesson :name lesson-name)]]}))
