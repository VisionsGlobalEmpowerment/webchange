(ns webchange.interpreter.renderer.overlays.index
  (:require
    [webchange.interpreter.renderer.overlays.activity-finished :as activity-finished]
    [webchange.interpreter.renderer.overlays.goodbye :as goodbye]
    [webchange.interpreter.renderer.overlays.guide :as guide]
    [webchange.interpreter.renderer.overlays.navigation :as navigation]
    [webchange.interpreter.renderer.overlays.navigation-back :as navigation-back]
    [webchange.interpreter.renderer.overlays.settings :as settings]
    [webchange.interpreter.renderer.overlays.skip-menu :as skip-menu]
    [webchange.interpreter.renderer.scene.components.create-component :refer [create-component]]))

(defn create-overlays
  [{:keys [parent viewport mode metadata]}]
  (create-component {:type        "group"
                     :parent      parent
                     :object-name :overlays
                     :children    (cond-> []
                                          (skip-menu/show-overlay? mode) (conj (skip-menu/create {:viewport viewport}))
                                          (guide/show-overlay? mode) (conj (guide/create {:viewport viewport :metadata metadata}))
                                          (goodbye/show-overlay? mode) (conj (goodbye/create {:viewport viewport}))
                                          (navigation/show-overlay? mode) (conj (navigation/create {:viewport viewport}))
                                          (navigation-back/show-overlay? mode) (conj (navigation-back/create {:viewport viewport}))
                                          (activity-finished/show-overlay? mode) (conj (activity-finished/create {:viewport viewport}))
                                          (settings/show-overlay? mode) (conj (settings/create {:viewport viewport})))}))

(defn update-viewport
  [viewport]
  (skip-menu/update-viewport {:viewport viewport})
  (guide/update-viewport {:viewport viewport})
  (goodbye/update-viewport {:viewport viewport})
  (navigation/update-viewport {:viewport viewport})
  (navigation-back/update-viewport {:viewport viewport})
  (activity-finished/update-viewport {:viewport viewport})
  (settings/update-viewport {:viewport viewport}))
