(ns webchange.interpreter.renderer.overlays.index
  (:require
    [webchange.interpreter.renderer.overlays.activity-finished :as activity-finished]
    [webchange.interpreter.renderer.overlays.navigation :as navigation]
    [webchange.interpreter.renderer.overlays.settings :as settings]
    [webchange.interpreter.renderer.overlays.skip-menu :as skip-menu]
    [webchange.interpreter.renderer.scene.components.create-component :refer [create-component]]))

(defn create-overlays
  [{:keys [parent viewport]}]
  (create-component {:type        "group"
                     :parent      parent
                     :object-name :overlays
                     :children    [(skip-menu/create {:viewport viewport})
                                   (navigation/create {:viewport viewport})
                                   (activity-finished/create {:viewport viewport})
                                   (settings/create {:viewport viewport})]}))

(defn update-viewport
  [viewport]
  (skip-menu/update-viewport {:viewport viewport})
  (navigation/update-viewport {:viewport viewport})
  (activity-finished/update-viewport {:viewport viewport})
  (settings/update-viewport {:viewport viewport}))
