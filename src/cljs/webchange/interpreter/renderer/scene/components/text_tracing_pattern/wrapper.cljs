(ns webchange.interpreter.renderer.scene.components.text-tracing-pattern.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]
    [webchange.logger.index :as logger]
    [webchange.interpreter.core :as i]))

(defn wrap
  [type name container props state draw!]
  (create-wrapper {:name          name
                   :type          type
                   :object        container
                   :set-text      (fn [text]
                                    (draw! container (assoc props :text text) state))}))
