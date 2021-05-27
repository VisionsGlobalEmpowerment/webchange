(ns webchange.game-changer.steps.select-background.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.game-changer.steps.state-common]
    [webchange.state.state :as state]))

(re-frame/reg-event-fx
  ::change-background
  (fn [{:keys [_]} [_ data callback]]
    (let [{:keys [course-slug scene-slug]} (:activity @data)
          background (:background @data)]
      {:dispatch [::state/update-scene-object
                  {:course-id         course-slug
                   :scene-id          scene-slug
                   :object-name       (:name background)
                   :object-data-patch (:data background)}
                  {:on-success [::change-background-success callback]}]})))

(re-frame/reg-event-fx
  ::change-background-success
  (fn [{:keys [_]} [_ callback]]
    {:callback callback}))
