(ns webchange.interpreter.renderer.overlays.skip-menu
  (:require
    [re-frame.core :as re-frame]
    [webchange.common.events :as ce]
    [webchange.i18n.translate :as i18n]
    [webchange.interpreter.renderer.overlays.utils :as utils]
    [webchange.interpreter.renderer.state.scene :as scene]
    [webchange.interpreter.renderer.scene.modes.modes :as modes]))

(defn show-overlay?
  [mode]
  (some #{mode} [::modes/game ::modes/game-with-nav ::modes/sandbox]))

(def menu-padding {:y 20})
(def button-name :skip-menu-button)
(def button-size {:width  364
                  :height 102})

(defn- get-button-position
  [viewport]
  (utils/get-coordinates {:viewport   viewport
                          :vertical   "bottom"
                          :horizontal "center"
                          :object     button-size
                          :padding    menu-padding}))

(defn create
  [{:keys [viewport]}]
  (let [skip-button (merge {:type        "button"
                            :object-name button-name
                            :on-click    ce/skip
                            :text        @(re-frame/subscribe [::i18n/t [:skip]])}
                           (get-button-position viewport))]
    {:type        "group"
     :object-name :skip-menu
     :visible     false
     :children    [skip-button]}))

(defn update-viewport
  [{:keys [viewport]}]
  (re-frame/dispatch [::scene/change-scene-object button-name [[:set-position (get-button-position viewport)]]]))
