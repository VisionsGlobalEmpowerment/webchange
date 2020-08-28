(ns webchange.interpreter.renderer.overlays.index
  (:require
    [webchange.interpreter.renderer.overlays.activity-finished :refer [create-activity-finished-overlay]]
    [webchange.interpreter.renderer.overlays.navigation :refer [create-navigation-overlay]]
    [webchange.interpreter.renderer.overlays.settings :refer [create-settings-overlay]]))

(defn create-overlays
  [props]
  (create-navigation-overlay props)
  (create-activity-finished-overlay props)
  (create-settings-overlay props))
