(ns webchange.interpreter.renderer.state.overlays
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.renderer.state.scene :as scene]))

(re-frame/reg-event-fx
  ::open-settings
  (fn [{:keys [db]}]
    (let [music-volume (get-in db [:settings :music-volume])
          effects-volume (get-in db [:settings :effects-volume])]
      {:dispatch-n (list [::scene/change-scene-object :settings-overlay [[:set-visibility {:visible true}]]]
                         [::scene/change-scene-object :music-slider [[:set-value music-volume]]]
                         [::scene/change-scene-object :effects-slider [[:set-value effects-volume]]])})))

(re-frame/reg-event-fx
  ::close-settings
  (fn []
    {:dispatch [::scene/change-scene-object :settings-overlay [[:set-visibility {:visible false}]]]}))
