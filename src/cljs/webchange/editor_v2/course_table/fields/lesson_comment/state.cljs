(ns webchange.editor-v2.course-table.fields.lesson-comment.state
  (:require
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
       (concat [:edit-from :comment component-id])
       (db/path-to-db)))

(defn- get-component-data
  [db component-id]
  (get-in db (path-to-db [] component-id) {}))

(re-frame/reg-sub
  ::component-data
  (fn [db [_ component-id]]
    (get-component-data db component-id)))

(re-frame/reg-event-fx
  ::set-component-data
  (fn [{:keys [db]} [_ component-id data-key data-value]]
    {:db (assoc-in db (path-to-db [data-key] component-id) data-value)}))


(re-frame/reg-event-fx
  ::init
  (fn [{:keys [db]} [_ selection component-id]]
    (let [course-data (subs/course-data db)
          current-value (utils/get-lesson-comment course-data selection)
          selection-data (selection/selection db)]
      {:dispatch-n [[::set-current-value component-id current-value]
                    [::set-initial-value component-id current-value]
                    [::set-initial-selection component-id selection-data]]})))

;; Current value

(defn- get-current-value
  [db component-id]
  (-> (get-component-data db component-id)
      (get :current-value "")))

(re-frame/reg-sub
  ::current-value
  (fn [[_ component-id]]
    [(re-frame/subscribe [::component-data component-id])])
  (fn [[component-data]]
    (get component-data :current-value "")))

(re-frame/reg-event-fx
  ::set-current-value
  (fn [{:keys [_]} [_ component-id value]]
    {:dispatch [::set-component-data component-id :current-value value]}))

;; Initial value

(defn- get-initial-value
  [db component-id]
  (-> (get-component-data db component-id)
      (get :initial-value "")))

(re-frame/reg-event-fx
  ::set-initial-value
  (fn [{:keys [_]} [_ component-id value]]
    {:dispatch [::set-component-data component-id :initial-value value]}))

;; Initial selection

(defn- get-initial-selection
  [db component-id]
  (-> (get-component-data db component-id)
      (get :initial-selection)))

(re-frame/reg-event-fx
  ::set-initial-selection
  (fn [{:keys [_]} [_ component-id value]]
    {:dispatch [::set-component-data component-id :initial-selection value]}))


(re-frame/reg-event-fx
  ::save
  (fn [{:keys [db]} [_ component-id]]
    (let [initial-value (get-initial-value db component-id)
          current-value (get-current-value db component-id)]
      (if-not (= initial-value current-value)
        (let [selection (get-initial-selection db component-id)
              course-id (data-state/course-id db)
              course-data (-> (subs/course-data db)
                              (utils/update-lesson-comment selection current-value))]
          {:dispatch [::common/update-course course-id course-data]})
        {}))))
