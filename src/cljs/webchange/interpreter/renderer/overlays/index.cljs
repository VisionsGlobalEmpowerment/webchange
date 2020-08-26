(ns webchange.interpreter.renderer.overlays.index
  (:require
    [webchange.interpreter.renderer.overlays.navigation :refer [create-navigation]]
    [webchange.interpreter.renderer.overlays.settings :refer [create-settings-overlay]]))

(defn create-overlays
  [props]
  (create-navigation props)
  (create-settings-overlay props))
