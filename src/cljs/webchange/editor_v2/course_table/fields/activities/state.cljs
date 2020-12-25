(ns webchange.editor-v2.course-table.fields.activities.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.course-data-utils.utils :as utils]
    [webchange.editor-v2.course-table.state.data :as data-state]
    [webchange.editor-v2.course-table.state.db :as db]
    [webchange.editor-v2.course-table.state.edit-common :as common]
    [webchange.editor-v2.course-table.state.selection :as selection]
    [webchange.subs :as subs]))

(defn path-to-db
  [relative-path component-id]
  (->> relative-path
       (concat [:edit-from :activity component-id])
       (db/path-to-db)))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [db]} [_ {:keys [activity]} component-id]]
    (let [current-value (keyword activity)
          selection-data (selection/selection db)]
      {:db       (-> db
                     (assoc-in (path-to-db [:initial-value] component-id) current-value)
                     (assoc-in (path-to-db [:selection-data] component-id) selection-data))
       :dispatch [::reset-current-activity current-value component-id]})))

(re-frame/reg-sub
  ::activities
  (fn []
    [(re-frame/subscribe [::data-state/course-activities])])
  (fn [[available-activities]]
    (->> available-activities
         (sort-by :name))))

;; Current

(defn- current-activity
  [db component-id]
  (get-in db (path-to-db [:current] component-id)))

(re-frame/reg-sub
  ::current-activity
  (fn [db [_ component-id]]
    (current-activity db component-id)))

(re-frame/reg-event-fx
  ::reset-current-activity
  (fn [{:keys [db]} [_ activity component-id]]
    {:db (assoc-in db (path-to-db [:current] component-id) activity)}))

;; Save

(re-frame/reg-event-fx
  ::save
  (fn [{:keys [db]} [_ component-id]]
    (let [initial-activity (get-in db (path-to-db [:initial-value] component-id))
          current-activity (current-activity db component-id)]
      (if-not (= current-activity initial-activity)
        (let [course-id (data-state/course-id db)
              selection-data (get-in db (path-to-db [:selection-data] component-id))
              course-data (-> (subs/course-data db)
                              (utils/update-activity selection-data {:activity current-activity}))]
          {:dispatch [::common/update-course course-id course-data]})
        {}))))
