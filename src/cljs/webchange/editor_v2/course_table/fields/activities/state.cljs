(ns webchange.editor-v2.course-table.fields.activities.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.course-data-utils.utils :as utils]
    [webchange.editor-v2.course-table.state.data :as data-state]
    [webchange.editor-v2.course-table.state.db :as db]
    [webchange.editor-v2.course-table.state.edit-common :as common]
    [webchange.editor-v2.course-table.state.selection :as selection]
    [webchange.subs :as subs]
    [webchange.events :as events]
    [webchange.warehouse :as warehouse]))

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

;; Create

(defn- add-error
  [db component-id key message]
  (assoc-in db (path-to-db [:errors key] component-id) message))

(defn- clear-errors
  [db component-id]
  (assoc-in db (path-to-db [:errors] component-id) {}))

(re-frame/reg-event-fx
  ::create
  (fn [{:keys [db]} [_ name component-id]]
    (let [course-id (data-state/course-id db)
          activity {:name name}]
      (if (not (empty? name))
        {:dispatch [::warehouse/create-activity-placeholder
                    {:course-id course-id
                     :data      activity}
                    {:on-success [::create-success component-id]}]}
        {:db (add-error db component-id :new-activity "Required")}))))

(defn- add-scene
  [course-data scene-slug activity-data]
  (assoc-in course-data [:scene-list (keyword scene-slug)] activity-data))

(re-frame/reg-event-fx
  ::create-success
  (fn [{:keys [db]} [_ component-id {:keys [name scene-slug]}]]
    (let [activity-data {:name name}
          course-id (data-state/course-id db)
          selection-data (get-in db (path-to-db [:selection-data] component-id))
          course-data (-> (subs/course-data db)
                          (add-scene scene-slug activity-data)
                          (utils/update-activity selection-data {:activity name}))]
      {:db (clear-errors db component-id)
       :dispatch-n (list [::common/update-course course-id course-data]
                         [::reset-current-activity scene-slug component-id])})))

(re-frame/reg-sub
  ::errors
  (fn [db [_ component-id]]
    (get-in db (path-to-db [:errors] component-id))))

(re-frame/reg-event-fx
  ::open-configured-wizard
  (fn [{:keys [db]} [_ activity-id]]
    (let [course-id (data-state/course-id db)]
      {:dispatch [::events/redirect :wizard-configured :course-slug course-id :scene-slug activity-id]})))
