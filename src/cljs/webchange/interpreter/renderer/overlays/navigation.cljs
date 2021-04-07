(ns webchange.interpreter.renderer.overlays.navigation
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.events :as ie]
    [webchange.interpreter.renderer.state.overlays :as state]
    [webchange.interpreter.renderer.state.scene :as scene]
    [webchange.interpreter.renderer.scene.modes.modes :as modes]
    [webchange.interpreter.renderer.overlays.utils :as utils]))

(defn show-overlay?
  [mode]
  (some #{mode} [::modes/game]))

(def menu-padding {:x 20 :y 20})

(def back-button-name :back)
(def back-button-size {:width 97 :height 99})

(def settings-button-name :settings)
(def settings-button-size {:width 100 :height 103})

(def close-button-name :close)

(defn- get-back-button-position
  [viewport]
  (utils/get-coordinates {:viewport   viewport
                          :vertical   "top"
                          :horizontal "left"
                          :object     back-button-size
                          :padding    {:x (:x menu-padding)
                                       :y (:y menu-padding)}}))

(defn- get-settings-button-position
  [viewport]
  (utils/get-coordinates {:viewport   viewport
                          :vertical   "top"
                          :horizontal "right"
                          :object     settings-button-size
                          :padding    {:x (+ (:width (get-in utils/common-elements [:close-button :size]))
                                             (* 2 (:x menu-padding)))
                                       :y (:y menu-padding)}}))

(defn- get-close-button-position
  [viewport]
  (utils/get-coordinates {:viewport   viewport
                          :vertical   "top"
                          :horizontal "right"
                          :object     (get-in utils/common-elements [:close-button :size])
                          :padding    menu-padding}))

(defn create
  [{:keys [viewport]}]
  (let [back-button (merge {:type        "image"
                            :src         "/raw/img/ui/back_button_01.png"
                            :object-name back-button-name
                            :on-click    #(re-frame/dispatch [::ie/back-scene])}
                           (get-back-button-position viewport))
        settings-button (merge {:type        "image"
                                :src         "/raw/img/ui/settings_button_01.png"
                                :object-name settings-button-name
                                :on-click    #(re-frame/dispatch [::state/show-settings])}
                               (get-settings-button-position viewport))
        close-button (merge {:type        "image"
                             :src         (get-in utils/common-elements [:close-button :src])
                             :object-name close-button-name
                             :on-click    #(re-frame/dispatch [::ie/open-student-dashboard])}
                            (get-close-button-position viewport))]
    {:type        "group"
     :object-name :navigation-menu
     :children    [back-button settings-button close-button]}))

(defn update-viewport
  [{:keys [viewport]}]
  (re-frame/dispatch [::scene/change-scene-object back-button-name [[:set-position (get-back-button-position viewport)]]])
  (re-frame/dispatch [::scene/change-scene-object settings-button-name [[:set-position (get-settings-button-position viewport)]]])
  (re-frame/dispatch [::scene/change-scene-object close-button-name [[:set-position (get-close-button-position viewport)]]]))
