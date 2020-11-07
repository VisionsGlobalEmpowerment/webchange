(ns webchange.interpreter.renderer.state.overlays
  (:require
    [re-frame.core :as re-frame]
    [webchange.student-dashboard.subs :as subs]
    [webchange.interpreter.renderer.state.scene :as scene]
    [webchange.interpreter.renderer.scene.components.wrapper-interface :as w]))

(re-frame/reg-event-fx
  ::show-navigation-menu
  (fn [_]
    {:dispatch [::scene/change-scene-object :navigation-menu [[:set-visibility {:visible true}]]]}))

(re-frame/reg-event-fx
  ::hide-navigation-menu
  (fn [_]
    {:dispatch [::scene/change-scene-object :navigation-menu [[:set-visibility {:visible false}]]]}))

(defn- set-scene-interactive
  [db interactive?]
  (-> db
      (scene/get-scene-object :scene)
      (w/set-interactive interactive?)))

(re-frame/reg-event-fx
  ::show-settings
  (fn [{:keys [db]}]
    (let [music-volume (get-in db [:settings :music-volume])
          effects-volume (get-in db [:settings :effects-volume])]
      (set-scene-interactive db false)
      {:dispatch-n (list [::hide-navigation-menu]
                         [::scene/change-scene-object :settings-overlay [[:set-visibility {:visible true}]]]
                         [::scene/change-scene-object :music-slider [[:set-value music-volume]]]
                         [::scene/change-scene-object :effects-slider [[:set-value effects-volume]]])})))

(re-frame/reg-event-fx
  ::hide-settings
  (fn [{:keys [db]}]
    (set-scene-interactive db true)
    {:dispatch-n (list [::show-navigation-menu]
                       [::scene/change-scene-object :settings-overlay [[:set-visibility {:visible false}]]])}))

(re-frame/reg-event-fx
  ::show-activity-finished
  (fn [{:keys [db]}]
    (let [next-activity (subs/next-activity db)
          lesson-progress (subs/lesson-progress db)]
      (set-scene-interactive db false)
      {:dispatch-n (list [::hide-navigation-menu]
                         [::scene/change-scene-object :activity-finished-overlay [[:set-visibility {:visible true}]]]
                         [::scene/change-scene-object :overall-progress-bar [[:set-value lesson-progress]]]
                         [::scene/change-scene-object :next-activity-card-image [[:set-src {:src (:image next-activity)}]]]
                         [::scene/change-scene-object :next-activity-card-name [[:set-text {:text (:name next-activity)}]]])})))

(re-frame/reg-event-fx
  ::hide-activity-finished
  (fn [{:keys [db]}]
    (set-scene-interactive db true)
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
