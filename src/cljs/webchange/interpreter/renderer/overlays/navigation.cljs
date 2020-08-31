(ns webchange.interpreter.renderer.overlays.navigation
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.events :as ie]
    [webchange.interpreter.renderer.scene.components.group.component :as group]
    [webchange.interpreter.renderer.overlays.utils :as utils]))

(defn create-navigation-overlay
  [{:keys [parent viewport]}]
  (let [menu-padding {:x 20 :y 20}
        back-button-size {:width 97 :height 99}
        settings-button-size {:width 100 :height 103}

        back-button (merge {:type        "image"
                            :src         "/raw/img/ui/back_button_01.png"
                            :object-name :back
                            :on-click    #(re-frame/dispatch [::ie/close-scene])}
                           (utils/get-coordinates {:viewport   viewport
                                                   :vertical   "top"
                                                   :horizontal "left"
                                                   :object     back-button-size
                                                   :padding    {:x (:x menu-padding)
                                                                :y (:y menu-padding)}}))
        settings-button (merge {:type        "image"
                                :src         "/raw/img/ui/settings_button_01.png"
                                :object-name :settings
                                :on-click    #(re-frame/dispatch [::ie/open-settings])}
                               (utils/get-coordinates {:viewport   viewport
                                                       :vertical   "top"
                                                       :horizontal "right"
                                                       :object     settings-button-size
                                                       :padding    {:x (+ (:width (get-in utils/common-elements [:close-button :size]))
                                                                          (* 2 (:x menu-padding)))
                                                                    :y (:y menu-padding)}}))
        close-button (merge {:type        "image"
                             :src         (get-in utils/common-elements [:close-button :src])
                             :object-name :close
                             :on-click    #(re-frame/dispatch [::ie/open-student-dashboard])}
                            (utils/get-coordinates {:viewport   viewport
                                                    :vertical   "top"
                                                    :horizontal "right"
                                                    :object     (get-in utils/common-elements [:close-button :size])
                                                    :padding    menu-padding}))]
    (group/create parent {:parent      parent
                          :object-name :navigation-menu
                          :children    [back-button settings-button close-button]})))
