(ns webchange.interpreter.renderer.question.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]
    [webchange.interpreter.renderer.scene.filters.filters :as f]
    [webchange.resources.manager :as resources]
    [webchange.interpreter.renderer.scene.filters.filters :as f]))


(defn wrap
  [type name container]
  (create-wrapper {:name           name
                   :type           type
                   :object         container
                   :container      container}))
