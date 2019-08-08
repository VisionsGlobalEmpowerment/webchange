(ns webchange.dashboard.events
  (:require
    [re-frame.core :as re-frame]
    [webchange.dashboard.classes.events :as classes-events]
    [webchange.dashboard.students.events :as students-events]
    [webchange.validation.validate :refer [validate]]))

(re-frame/reg-event-fx
  ::set-main-content
  (fn [{:keys [db]} [_ screen {:keys [class-id student-id]}]]
    (let [events (atom (list))]
      (when class-id (swap! events conj [::classes-events/set-current-class class-id]))
      (when student-id (swap! events conj [::students-events/set-current-student student-id]))
      {:db (assoc-in db [:dashboard :current-main-content] screen)
       :dispatch-n @events})))

(re-frame/reg-event-fx
  ::open-class-profile
  (fn [{:keys [db]} [_ class-id course-name]]
    {:dispatch-n (list
                   [::classes-events/load-classes]
                   [::students-events/load-students class-id]
                   [::classes-events/load-class-profile class-id course-name])}))

(re-frame/reg-event-fx
  ::open-student-profile
  (fn [{:keys [db]} [_ student-id course-name]]
    {:dispatch-n (list
                   [::classes-events/load-classes]
                   [::students-events/load-student student-id]
                   [::students-events/load-student-profile student-id course-name])}))

(re-frame/reg-event-fx
  ::open-students
  (fn [{:keys [db]} [_ class-id]]
    {:dispatch-n (list
                   [::classes-events/load-classes]
                   [::students-events/load-students class-id])}))

(re-frame/reg-event-fx
  ::open-classes
  (fn [{:keys [db]} _]
    {:dispatch-n (list
                   [::classes-events/load-classes])}))

(re-frame/reg-cofx
  :validate
  (fn [co-effects entity-type]
    (let [[_ _ entity-data] (:event co-effects)]
      (assoc co-effects :validation-errors (validate entity-type entity-data)))))
