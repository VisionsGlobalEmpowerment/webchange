(ns webchange.interpreter.renderer.scene.components.text-tracing-pattern.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.text-tracing-pattern.utils :refer [set-enable!]]
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]))

(defn wrap
  [type name container props state draw!]
  (create-wrapper {:name       name
                   :type       type
                   :object     container
                   :set-text   (fn [text]
                                 (draw! container (assoc props :text text) state))
                   :set-dashed (fn [dashed]
                                 (draw! container (assoc props :dashed dashed) state))
                   :set-show-lines (fn [show-lines]
                                     (draw! container (assoc props :show-lines show-lines) state))
                   :set-enable (fn [value]
                                 (swap! state assoc :enable? value)
                                 (set-enable! state))}))
