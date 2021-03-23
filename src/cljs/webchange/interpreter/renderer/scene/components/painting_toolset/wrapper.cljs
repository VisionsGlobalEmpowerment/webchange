(ns webchange.interpreter.renderer.scene.components.painting-toolset.wrapper
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]))

(defn wrap
  [type name container]
  (create-wrapper {:name          name
                   :type          type
                   :object        container}))
