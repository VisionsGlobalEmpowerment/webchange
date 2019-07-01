(ns webchange.dashboard.events
  (:require
    [re-frame.core :as re-frame]
    [webchange.dashboard.classes.events :as classes-events]
    [webchange.dashboard.students.events :as students-events]))

(re-frame/reg-event-fx
  ::set-main-content
  (fn [{:keys [db]} [_ screen {:keys [class-id student-id]}]]
    (let [events (atom (list))]
      (when class-id (swap! events conj [::classes-events/set-current-class class-id]))
      (when student-id (swap! events conj [::students-events/set-current-student student-id]))
      {:db (assoc-in db [:dashboard :current-main-content] screen)
       :dispatch-n @events})))
