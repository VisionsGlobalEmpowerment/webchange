(ns webchange.interpreter.renderer.state.overlays
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.renderer.state.db :refer [path-to-db]]
    [webchange.interpreter.renderer.state.scene :as scene]
    [webchange.interpreter.renderer.scene.components.wrapper-interface :as w]
    [webchange.state.state-course :as state-course]
    [webchange.logger.index :as logger]))

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
  ::show-question
  (fn [{:keys [db]}]
      (set-scene-interactive db false)
      {:dispatch-n (list [::hide-navigation-menu]
                         [::scene/change-scene-object :question-overlay [[:set-visibility {:visible true}]]]
                         )}))

(re-frame/reg-event-fx
  ::hide-question
  (fn [{:keys [db]}]
    (set-scene-interactive db true)
    {:dispatch-n (list [::show-navigation-menu]
                       [::scene/change-scene-object :question-overlay [[:set-visibility {:visible false}]]])}))

(re-frame/reg-event-fx
  ::show-activity-finished
  (fn [{:keys [db]}]
    (let [course-finished? (state-course/course-finished? db)]
      (set-scene-interactive db false)
      (js/setTimeout #(re-frame/dispatch [::scene/change-scene-object :form-shooting-star [[:set-visibility {:visible false}]]])
                     3500)
      {:dispatch-n (list [::hide-navigation-menu]
                         [::scene/change-scene-object :form-shooting-star [[:generic-handler [[:start-animation]]]]]
                         [::scene/change-scene-object :activity-finished-overlay [[:set-visibility {:visible true}]]]
                         [::scene/change-scene-object :form-next [[:set-visibility {:visible (not course-finished?)}]]])})))

(re-frame/reg-event-fx
  ::hide-activity-finished
  (fn [{:keys [db]}]
    (set-scene-interactive db true)
    {:dispatch-n (list [::show-navigation-menu]
                       [::scene/change-scene-object :activity-finished-overlay [[:set-visibility {:visible false}]]])}))

(re-frame/reg-event-fx
  ::show-skip-menu
  (fn [_]
    (logger/trace "show skip menu")
    {:dispatch [::scene/change-scene-object :skip-menu [[:set-visibility {:visible true}]]]}))

(re-frame/reg-event-fx
  ::hide-skip-menu
  (fn [_]
    (logger/trace "hide skip menu")
    {:dispatch [::scene/change-scene-object :skip-menu [[:set-visibility {:visible false}]]]}))

(re-frame/reg-event-fx
  ::show-waiting-screen
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db (path-to-db [:overlays :waiting-screen]) true)}))

(re-frame/reg-event-fx
  ::hide-waiting-screen
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db (path-to-db [:overlays :waiting-screen]) false)}))

(re-frame/reg-sub
  ::show-waiting-screen?
  (fn [db]
    (get-in db (path-to-db [:overlays :waiting-screen]) false)))


(re-frame/reg-event-fx
  ::show-goodbye-screen
  (fn [{:keys [db]}]
    (set-scene-interactive db false)
    {:dispatch-n (list 
                  [::scene/change-scene-object :goodbye-overlay [[:set-visibility {:visible true}]]])}))
