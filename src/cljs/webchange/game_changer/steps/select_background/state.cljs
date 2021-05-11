(ns webchange.game-changer.steps.select-background.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.state.state :as state]))

(re-frame/reg-event-fx
  ::change-background
  (fn [{:keys [_]} [_ data]]
    (let [{:keys [course-slug scene-slug]} (:activity @data)
          background (:background @data)]
      {:dispatch [::state/update-scene-object
                  {:course-id         course-slug
                   :scene-id          scene-slug
                   :object-name       (:name background)
                   :object-data-patch (:data background)}
                  {:on-success [::redirect-to-editor data]}]})))

(re-frame/reg-event-fx
  ::redirect-to-editor
  (fn [{:keys [_]} [_ data]]
    (let [{:keys [course-slug scene-slug]} (:activity @data)]
      {:redirect [:course-editor-v2-scene :id course-slug :scene-id scene-slug]})))
