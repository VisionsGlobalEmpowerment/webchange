(ns webchange.dashboard.classes.class-profile.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.state.warehouse :as warehouse]))

(re-frame/reg-event-fx
  ::load-course
  (fn [{:keys [_]} [_ course-id]]
    {:dispatch [::warehouse/load-course-info course-id {:on-success [::set-course]}]}))

(def course-path [:dashboard :class-profile :course])

(re-frame/reg-sub
  ::course
  (fn [db]
    (get-in db course-path)))

(re-frame/reg-sub
  ::course-name
  (fn []
    (re-frame/subscribe [::course]))
  (fn [course]
    (get course :name "")))

(re-frame/reg-event-fx
  ::set-course
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db course-path data)}))