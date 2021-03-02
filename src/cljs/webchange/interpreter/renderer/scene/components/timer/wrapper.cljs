(ns webchange.interpreter.renderer.scene.components.timer.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.timer.utils :refer [time->min-sec]]
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]
    [webchange.interpreter.utils.counter :refer [set-countdown]]))

(defn- time->progress
  [{:keys [initial-time current-time]}]
  (/ current-time initial-time))

(defn- start-timer
  [state {:keys [set-clock-value set-progress set-clock-activated on-end]}]
  (let [ticks (:initial-time @state)]
    (swap! state assoc :current-time (:initial-time @state))
    (set-clock-activated true)
    (set-countdown {:interval    1000
                    :count       ticks
                    :on-progress #(do (swap! state update :current-time dec)
                                      (set-clock-value (time->min-sec (:current-time @state)))
                                      (set-progress (time->progress @state)))
                    :on-end      #(do (set-clock-activated false)
                                      (on-end))})))

(defn wrap
  [type name container state methods]
  (create-wrapper {:name   name
                   :type   type
                   :object container
                   :start  (fn []
                             (start-timer state methods))}))
