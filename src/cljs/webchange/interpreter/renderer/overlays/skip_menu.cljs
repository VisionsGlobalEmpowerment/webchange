(ns webchange.interpreter.renderer.overlays.skip-menu
  (:require
    [re-frame.core :as re-frame]
    [webchange.common.events :as ce]
    [webchange.interpreter.renderer.scene.components.group.component :as group]
    [webchange.interpreter.renderer.overlays.utils :as utils]
    [webchange.interpreter.utils.i18n :refer [t]]))

(defn create-skip-menu-overlay
  [{:keys [parent viewport]}]
  (let [menu-padding {:y 20}
        skip-button-size {:width  364
                          :height 102}
        skip-button (merge {:type        "button"
                            :object-name :skip-menu-button
                            :on-click    #(re-frame/dispatch [::ce/skip])
                            :text        (t "skip")}
                           (utils/get-coordinates {:viewport   viewport
                                                   :vertical   "bottom"
                                                   :horizontal "center"
                                                   :object     skip-button-size
                                                   :padding    menu-padding}))]
    (group/create parent {:parent      parent
                          :object-name :skip-menu
                          :visible     false
                          :children    [skip-button]})))
