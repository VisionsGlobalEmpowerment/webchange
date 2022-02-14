(ns webchange.dashboard.students.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.dashboard.state :as parent-state]
    [webchange.state.warehouse :as warehouse]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:students-page])
       (parent-state/path-to-db)))

;; Students

(re-frame/reg-event-fx
  ::load-students
  (fn [{:keys [_]} [_ class-id]]
    {:dispatch [::warehouse/load-class-students {:class-id class-id} {:on-success [::load-students-success]}]}))

(re-frame/reg-event-fx
  ::load-students-success
  (fn [{:keys [_]} [_ {:keys [students]}]]
    {:dispatch [::set-students students]}))

(def students-path (path-to-db [:students]))

(defn get-students
  [db]
  (get-in db students-path))

(re-frame/reg-sub
  ::students
  get-students)

(re-frame/reg-event-fx
  ::set-students
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db students-path data)}))

(re-frame/reg-event-fx
  ::add-student
  (fn [{:keys [_]} [_ {:keys [data class-id]} handlers]]
    {:dispatch [::warehouse/add-student
                {:data data}
                (-> handlers
                    (assoc :on-success [::update-students-list class-id handlers]))]}))

(re-frame/reg-event-fx
  ::edit-student
  (fn [{:keys [_]} [_ {:keys [id data class-id]} handlers]]
    {:dispatch [::warehouse/edit-student
                {:student-id id
                 :data       data}
                (-> handlers
                    (assoc :on-success [::update-students-list class-id handlers]))]}))

(re-frame/reg-event-fx
  ::update-students-list
  (fn [{:keys [_]} [_ class-id {:keys [on-success]}]]
    {:dispatch-n (cond-> [[::load-students class-id]]
                         (some? on-success) (conj on-success))}))
