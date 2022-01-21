(ns webchange.parent-dashboard.students-list.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.events :as events]
    [webchange.parent-dashboard.state :as parent-state]
    [webchange.state.warehouse :as warehouse]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:add-student-form])
       (parent-state/path-to-db)))

;; Students

(def students-list-path (path-to-db [:students-list]))

(re-frame/reg-event-fx
  ::load-students
  (fn [{:keys [_]} [_]]
    {:dispatch [::warehouse/load-parent-students
                {:on-success [::set-students-list]}]}))

(re-frame/reg-event-fx
  ::set-students-list
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db students-list-path data)}))

(re-frame/reg-sub
  ::students-list
  (fn [db]
    (get-in db students-list-path (->> {:name   "Ivan"
                                        :level  1
                                        :lesson 1}
                                       (repeat 6)
                                       (map-indexed (fn [idx data]
                                                      (assoc data :id idx)))))))

;; Buttons

(re-frame/reg-event-fx
  ::open-add-form
  (fn [{:keys [_]} [_]]
    {:dispatch [::events/redirect :parent-add-student]}))

(re-frame/reg-event-fx
  ::open-student-dashboard
  (fn [{:keys [_]} [_ user-id]]
    (print "::open-student-dashboard" user-id)
    {}))

(re-frame/reg-event-fx
  ::delete-student
  (fn [{:keys [_]} [_ user-id]]
    {:dispatch [::warehouse/delete-parent-student {:id user-id}
                {:on-success [::load-students]}]}))
