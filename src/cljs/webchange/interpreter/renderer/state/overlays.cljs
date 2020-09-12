(ns webchange.interpreter.renderer.state.overlays
  (:require
    [re-frame.core :as re-frame]
    [webchange.student-dashboard.subs :as subs]
    [webchange.interpreter.renderer.state.scene :as scene]))

(re-frame/reg-event-fx
  ::show-navigation-menu
  (fn [_]
    {:dispatch [::scene/change-scene-object :navigation-menu [[:set-visibility {:visible true}]]]}))

(re-frame/reg-event-fx
  ::hide-navigation-menu
  (fn [_]
    {:dispatch [::scene/change-scene-object :navigation-menu [[:set-visibility {:visible false}]]]}))

(re-frame/reg-event-fx
  ::show-settings
  (fn [{:keys [db]}]
    (let [music-volume (get-in db [:settings :music-volume])
          effects-volume (get-in db [:settings :effects-volume])]
      {:dispatch-n (list [::hide-navigation-menu]
                         [::scene/change-scene-object :settings-overlay [[:set-visibility {:visible true}]]]
                         [::scene/change-scene-object :music-slider [[:set-value music-volume]]]
                         [::scene/change-scene-object :effects-slider [[:set-value effects-volume]]])})))

(re-frame/reg-event-fx
  ::hide-settings
  (fn []
    {:dispatch-n (list [::show-navigation-menu]
                       [::scene/change-scene-object :settings-overlay [[:set-visibility {:visible false}]]])}))

(re-frame/reg-event-fx
  ::open-activity-finished
  (fn [{:keys [db]}]
    (let [next-activity (subs/next-activity db)
          lesson-progress (subs/lesson-progress db)]
      {:dispatch-n (list [::hide-navigation-menu]
                         [::scene/change-scene-object :activity-finished-overlay [[:set-visibility {:visible true}]]]
                         [::scene/change-scene-object :overall-progress-bar [[:set-value lesson-progress]]]
                         [::scene/change-scene-object :next-activity-card-image [[:set-src {:src (:image next-activity)}]]]
                         [::scene/change-scene-object :next-activity-card-name [[:set-text {:text (:name next-activity)}]]])})))

(re-frame/reg-event-fx
  ::close-activity-finished
  (fn []
    {:dispatch-n (list [::show-navigation-menu]
                       [::scene/change-scene-object :activity-finished-overlay [[:set-visibility {:visible false}]]])}))

(re-frame/reg-event-fx
  ::show-skip-menu
  (fn [_]
    {:dispatch [::scene/change-scene-object :skip-menu [[:set-visibility {:visible true}]]]}))

(re-frame/reg-event-fx
  ::hide-skip-menu
  (fn [_]
    {:dispatch [::scene/change-scene-object :skip-menu [[:set-visibility {:visible false}]]]}))
