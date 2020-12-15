(ns webchange.editor-v2.course-table.fields.activities.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.course-table.state.data :as data-state]
    [webchange.editor-v2.course-table.state.db :as db]
    [webchange.editor-v2.course-table.state.edit-common :as common]
    [webchange.editor-v2.course-table.state.edit-utils :as utils]
    [webchange.editor-v2.course-table.state.selection :as selection]
    [webchange.subs :as subs]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:edit-from :activity])
       (db/path-to-db)))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [db]} [_ {:keys [activity]}]]
    (let [current-value (keyword activity)]
      {:db       (assoc-in db (path-to-db [:initial-value]) current-value)
       :dispatch [::reset-current-activity current-value]})))

(re-frame/reg-sub
  ::activities
  (fn []
    [(re-frame/subscribe [::data-state/course-activities])])
  (fn [[available-activities]]
    (->> available-activities
         (sort-by :name))))

;; Current

(defn- current-activity
  [db]
  (get-in db (path-to-db [:current])))

(re-frame/reg-sub ::current-activity current-activity)

(re-frame/reg-event-fx
  ::reset-current-activity
  (fn [{:keys [db]} [_ activity]]
    {:db (assoc-in db (path-to-db [:current]) activity)}))

;; Save

(defn- update-activity
  [course-data current-activity selection-data]
  (let [path (utils/get-activity-path course-data selection-data)]
    (assoc-in course-data (conj path :activity) (clojure.core/name current-activity))))

(re-frame/reg-event-fx
  ::save
  (fn [{:keys [db]} [_]]
    (let [initial-activity (get-in db (path-to-db [:initial-value]))
          current-activity (current-activity db)]
      (if-not (= current-activity initial-activity)
        (let [course-id (data-state/course-id db)
              selection-data (-> db selection/selection :data)
              course-data (-> (subs/course-data db)
                              (update-activity current-activity selection-data))]
          {:dispatch [::common/update-course course-id course-data]})
        {}))))