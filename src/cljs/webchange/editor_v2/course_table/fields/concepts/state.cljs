(ns webchange.editor-v2.course-table.fields.concepts.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.course-table.state.data :as data-state]
    [webchange.editor-v2.course-table.state.db :as db]
    [webchange.editor-v2.course-table.state.edit-common :as common]
    [webchange.editor-v2.course-table.state.edit-utils :as utils]
    [webchange.editor-v2.course-table.state.selection :as selection]
    [webchange.interpreter.subs :as interpreter.subs]
    [webchange.subs :as subs]))

(defn path-to-db
  [relative-path component-id]
  (->> relative-path
       (concat [:edit-from :concepts component-id])
       (db/path-to-db)))

(defn- get-current-value
  [level-data lesson-data]
  (let [lesson-type (-> (:type lesson-data) (keyword))
        scheme (->> (get-in level-data [:scheme lesson-type :lesson-sets])
                    (map (fn [name] [(keyword name) nil]))
                    (into {}))]
    (-> scheme
        (merge (:lesson-sets lesson-data))
        (select-keys (keys scheme)))))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [db]} [_ selection component-id]]
    (let [course-data (subs/course-data db)
          lesson-data (utils/get-lesson-data course-data selection)
          level-data (utils/get-level-data course-data selection)
          current-value (get-current-value level-data lesson-data)
          selection-data (-> db selection/selection :data)]
      {:db       (-> db
                     (assoc-in (path-to-db [:initial-value] component-id) current-value)
                     (assoc-in (path-to-db [:selection-data] component-id) selection-data))
       :dispatch [::reset-current-lesson-sets current-value component-id]})))

(re-frame/reg-sub
  ::available-sets
  (fn []
    [(re-frame/subscribe [::interpreter.subs/lessons-data {:exclude-items-fields [:data]}])])
  (fn [[lessons]]
    (->> (vals lessons)
         (map (fn [lesson]
                (select-keys lesson [:name :items]))))))

;; Current value

(defn- current-lesson-sets
  [db component-id]
  (get-in db (path-to-db [:current-lesson-sets] component-id) {}))

(re-frame/reg-sub
  ::current-lesson-sets
  (fn [db [_ component-id]]
    (current-lesson-sets db component-id)))

(re-frame/reg-event-fx
  ::set-current-lesson-set
  (fn [{:keys [db]} [_ name lesson-set-name component-id]]
    {:db (assoc-in db (path-to-db [:current-lesson-sets name] component-id) lesson-set-name)}))

(re-frame/reg-event-fx
  ::reset-current-lesson-sets
  (fn [{:keys [db]} [_ current-lesson-sets component-id]]
    {:db (assoc-in db (path-to-db [:current-lesson-sets] component-id) current-lesson-sets)}))

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
        (let [selection-data (get-in db (path-to-db [:selection-data] component-id))
              course-id (data-state/course-id db)
              course-data (-> (subs/course-data db)
                              (update-lesson-sets current-value selection-data))]
          {:dispatch [::common/update-course course-id course-data]})
        {}))))
