(ns webchange.interpreter.renderer.scene.components.svg-path.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]))

(defn wrap
  [type name group-name container]
  (create-wrapper {:name       name
                   :group-name group-name
                   :type       type
                   :object     container
                   :set-data   (fn [data]
                                 (print "--- set-data" data))}))
