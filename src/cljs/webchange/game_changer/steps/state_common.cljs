(ns webchange.game-changer.steps.state-common
  (:require
    [re-frame.core :as re-frame]))

(re-frame/reg-event-fx
  ::redirect-to-editor
  (fn [{:keys [_]} [_ {:keys [course-slug scene-slug]}]]
    {:redirect [:course-editor-scene :id course-slug :scene-id scene-slug]}))
