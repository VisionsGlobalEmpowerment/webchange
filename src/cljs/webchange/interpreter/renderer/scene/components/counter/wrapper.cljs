(ns webchange.interpreter.renderer.scene.components.counter.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.timer.utils :refer [time->min-sec]]
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]))

(defn- inc-counter
  [state {:keys [set-value]}]
  (swap! state update :value inc)
  (set-value (:value @state)))

(defn wrap
  [type name container state methods]
  (create-wrapper {:name   name
                   :type   type
                   :object container
                   :inc    #(inc-counter state methods)}))
