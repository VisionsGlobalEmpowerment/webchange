(ns webchange.interpreter.renderer.overlays.navigation-back
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.events :as ie]
    [webchange.interpreter.renderer.state.scene :as scene]
    [webchange.interpreter.renderer.scene.modes.modes :as modes]
    [webchange.interpreter.renderer.overlays.utils :as utils]))

(defn show-overlay?
  [mode]
  (some #{mode} [::modes/game]))

(def menu-padding {:x 20 :y 20})

(def close-button-name :close)

(defn- get-close-button-position
  [viewport]
  (utils/get-coordinates {:viewport   viewport
                          :vertical   "top"
                          :horizontal "right"
                          :object     (get-in utils/common-elements [:close-button :size])
                          :padding    menu-padding}))

(defn create
  [{:keys [viewport]}]
  (let [close-button (merge {:type        "image"
                             :src         (get-in utils/common-elements [:close-button :src])
                             :object-name close-button-name
                             :on-click    #(re-frame/dispatch [::ie/open-student-dashboard])}
                            (get-close-button-position viewport))]
    {:type        "group"
     :object-name :navigation-menu
     :children    [close-button]}))

(defn update-viewport
  [{:keys [viewport]}]
  (re-frame/dispatch [::scene/change-scene-object close-button-name [[:set-position (get-close-button-position viewport)]]]))
