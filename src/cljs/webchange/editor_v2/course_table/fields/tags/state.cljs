(ns webchange.editor-v2.course-table.fields.tags.state
  (:require
    [camel-snake-kebab.core :refer [->kebab-case]]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.course-table.course-data-utils.utils :as utils]
    [webchange.editor-v2.course-table.state.data :as data-state]
    [webchange.editor-v2.course-table.state.db :as db]
    [webchange.editor-v2.course-table.state.edit-common :as common]
    [webchange.editor-v2.course-table.state.selection :as selection]
    [webchange.subs :as subs]))

(defn path-to-db
  [relative-path component-id]
  (->> relative-path
       (concat [:edit-from :tags component-id])
       (db/path-to-db)))

(defn- activity->tags-appointment
  [activity-data]
  (->> activity-data
       (map (fn [[tag-name [score-low score-high]]]
              [tag-name {:tag        tag-name
                         :score-low  score-low
                         :score-high score-high}]))
       (into {})))

(defn- activity->selected-tags-restriction
  [activity-data]
  (->> activity-data
       (map keyword)
       (map (fn [tag] [tag true]))
       (into {})))

(defn- tags-appointment
  ([db component-id]
   (->> (get-in db (path-to-db [:appointment] component-id) {})
        (tags-appointment)))
  ([data]
   (->> data
        (vals)
        (sort-by :tag))))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [db]} [_ selection component-id]]
    (let [course-data (subs/course-data db)
          current-tags-appointment (-> (utils/get-activity-tags-appointment course-data selection)
                                       (activity->tags-appointment))
          current-tags-restriction (-> (utils/get-activity-tags-restriction course-data selection)
                                       (activity->selected-tags-restriction))
          selection-data (selection/selection db)]
      {:db         (-> db
                       (assoc-in (path-to-db [:initial-tags-appointment] component-id) (tags-appointment current-tags-appointment))
                       (assoc-in (path-to-db [:initial-tags-restriction] component-id) current-tags-restriction)
                       (assoc-in (path-to-db [:selection-data] component-id) selection-data))
       :dispatch-n (list [::reset-tags-appointment current-tags-appointment component-id]
                         [::reset-selected-restriction current-tags-restriction component-id])})))

;; Tags appointment

(re-frame/reg-sub
  ::tags-appointment
  (fn [db [_ component-id]]
    (tags-appointment db component-id)))

(re-frame/reg-event-fx
  ::add-tag-appointment
  (fn [{:keys [db]} [_ component-id]]
    (let [tag (->> (tags-appointment db component-id) (count) (str "new-tag-") (keyword))]
      {:db (assoc-in db (path-to-db [:appointment tag] component-id) {:tag        tag
                                                                      :score-low  0
                                                                      :score-high 100})})))

(re-frame/reg-event-fx
  ::edit-tag-appointment
  (fn [{:keys [db]} [_ tag tag-data component-id]]
    (if (some? (:tag tag-data))
      (let [new-tag (-> tag-data :tag ->kebab-case keyword)
            tag-data (-> (get-in db (path-to-db [:appointment tag] component-id))
                         (merge tag-data {:tag new-tag}))]
        {:db (-> db
                 (assoc-in (path-to-db [:appointment new-tag] component-id) tag-data)
                 (update-in (path-to-db [:appointment] component-id) dissoc tag))})
      {:db (update-in db (path-to-db [:appointment tag] component-id) merge tag-data)})))

(re-frame/reg-event-fx
  ::delete-tag-appointment
  (fn [{:keys [db]} [_ tag component-id]]
    {:db (update-in db (path-to-db [:appointment] component-id) dissoc tag)}))

(re-frame/reg-event-fx
  ::reset-tags-appointment
  (fn [{:keys [db]} [_ tags-appointment component-id]]
    {:db (assoc-in db (path-to-db [:appointment] component-id) tags-appointment)}))

;; Tags restriction

(re-frame/reg-event-fx
  ::add-restriction-tag
  (fn [{:keys [_]} [_ tag component-id]]
    (let [fixed-tag (-> tag ->kebab-case keyword)]
      {:dispatch [::add-selected-restriction fixed-tag component-id]})))

(defn- selected-tags-restriction
  [db component-id]
  (get-in db (path-to-db [:restriction :selected] component-id) {}))

(re-frame/reg-sub
  ::selected-tags-restriction
  (fn [db [_ component-id]]
    (selected-tags-restriction db component-id)))

(re-frame/reg-event-fx
  ::reset-selected-restriction
  (fn [{:keys [db]} [_ tags component-id]]
    {:db (assoc-in db (path-to-db [:restriction :selected] component-id) tags)}))

(re-frame/reg-event-fx
  ::add-selected-restriction
  (fn [{:keys [db]} [_ tag component-id]]
    {:db (assoc-in db (path-to-db [:restriction :selected tag] component-id) true)}))

(re-frame/reg-event-fx
  ::remove-selected-restriction
  (fn [{:keys [db]} [_ tag component-id]]
    {:db (update-in db (path-to-db [:restriction :selected] component-id) dissoc tag)}))

(re-frame/reg-sub
  ::tags-restriction
  (fn [[_ component-id]]
    [(re-frame/subscribe [::selected-tags-restriction component-id])])
  (fn [[selected-tags]]
    (keys selected-tags)))

;; Save

(defn- update-tags-appointment
  [course-data appointments selection-data]
  (let [tags-data (->> appointments
                       (map (fn [{:keys [tag score-low score-high]}]
                              [tag [score-low score-high]]))
                       (into {}))]
    (utils/update-activity course-data selection-data {:tags-by-score tags-data})))

(defn- update-tags-restriction
  [course-data restrictions selection-data]
  (let [tags-data (keys restrictions)]
    (utils/update-activity course-data selection-data {:only tags-data})))

(re-frame/reg-event-fx
  ::save-tags
  (fn [{:keys [db]} [_ component-id]]
    (let [initial-tags-appointment (get-in db (path-to-db [:initial-tags-appointment] component-id))
          initial-tags-restriction (get-in db (path-to-db [:initial-tags-restriction] component-id))
          current-tags-appointment (tags-appointment db component-id)
          current-tags-restriction (selected-tags-restriction db component-id)]
      (if-not (and (= initial-tags-appointment current-tags-appointment)
                   (= initial-tags-restriction current-tags-restriction))
        (let [course-id (data-state/course-id db)
              selection-data (get-in db (path-to-db [:selection-data] component-id))
              course-data (-> (subs/course-data db)
                              (update-tags-appointment current-tags-appointment selection-data)
                              (update-tags-restriction current-tags-restriction selection-data))]
          {:dispatch [::common/update-course course-id course-data]})
        {}))))
