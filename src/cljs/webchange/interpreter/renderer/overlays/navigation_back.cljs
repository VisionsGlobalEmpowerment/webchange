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

(def home-button-name :home)

(defn- get-home-button-position
  [viewport]
  (utils/get-coordinates {:viewport   viewport
                          :vertical   "top"
                          :horizontal "left"
                          :object     (get-in utils/common-elements [:home-button :size])
                          :padding    {:x 20 :y 20}}))

(defn create
  [{:keys [viewport]}]
  (let [close-button (merge {:type        "image"
                             :src         (get-in utils/common-elements [:home-button :src])
                             :object-name home-button-name
                             :on-click    #(re-frame/dispatch [::ie/open-student-dashboard])}
                            (get-home-button-position viewport))]
    {:type        "group"
     :object-name :navigation-menu
     :children    [close-button]}))

(defn update-viewport
  [{:keys [viewport]}]
  (re-frame/dispatch [::scene/change-scene-object home-button-name [[:set-position (get-home-button-position viewport)]]]))
