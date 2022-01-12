(ns webchange.interpreter.renderer.scene.components.group.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]))

(defn wrap
  [type name container additional-props]
  (create-wrapper (merge {:name      name
                          :type      type
                          :object    container
                          :container container}
                         additional-props)))
