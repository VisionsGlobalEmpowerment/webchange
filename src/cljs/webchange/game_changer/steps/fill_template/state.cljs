(ns webchange.game-changer.steps.fill-template.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.game-changer.steps.state-common]
    [webchange.state.state-activity :as state-activity]
    [webchange.state.state-course :as state-course]
    [webchange.state.warehouse :as warehouse]))

(defn- data->course-data
  [data]
  {:name (get-in data [:course :name])
   :lang (get data :lang)})

(defn- data->activity-data
  [data]
  (merge {:name        (get-in data [:activity :name])
          :skills      []
          :template-id (get-in data [:template :id])}
         (get data :options {})))

(re-frame/reg-event-fx
  ::create-activity
  (fn [{:keys [_]} [_ data callback]]
    {:dispatch [::state-course/create-course
                {:course-data (data->course-data @data)}
                {:on-success [::create-activity-step-2 data callback]}]}))

(re-frame/reg-event-fx
  ::create-activity-step-2
  (fn [{:keys [_]} [_ data callback course-data]]
    {:dispatch [::state-activity/create-activity
                {:course-slug   (:slug course-data)
                 :activity-data (data->activity-data @data)}
                {:on-success [::create-activity-step-3 data callback course-data]}]}))

(re-frame/reg-event-fx
  ::create-activity-step-3
  (fn [{:keys [_]} [_ data callback course-data activity-data]]
    (swap! data update :course merge course-data)
    (swap! data update :activity merge activity-data)
    {:dispatch [::warehouse/load-scene
                (select-keys activity-data [:course-slug :scene-slug])
                {:on-success [::create-activity-step-4 data callback]}]}))

(re-frame/reg-event-fx
  ::create-activity-step-4
  (fn [{:keys [_]} [_ data callback scene-data]]
    (swap! data assoc :scene-data scene-data)
    {:callback callback}))
