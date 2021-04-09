(ns webchange.interpreter.renderer.overlays.settings
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.events :as ie]
    [webchange.interpreter.renderer.overlays.utils :as utils]
    [webchange.interpreter.renderer.scene.modes.modes :as modes]
    [webchange.interpreter.renderer.state.scene :as scene]))

(defn show-overlay?
  [mode]
  (some #{mode} [::modes/game]))

(def menu-padding {:x 20 :y 20})

(def close-button-name :close-settings)

(defn- get-close-button-position
  [viewport]
  (utils/get-coordinates {:viewport   viewport
                          :vertical   "top"
                          :horizontal "right"
                          :object     (get-in utils/common-elements [:close-button :size])
                          :padding    menu-padding}))

(defn- get-close-button
  [{:keys [viewport]}]
  (merge {:type        "image"
          :src         (get-in utils/common-elements [:close-button :src])
          :object-name close-button-name
          :on-click    #(re-frame/dispatch [::ie/close-settings])}
         (get-close-button-position viewport)))

(defn- get-music-setting
  [{:keys [x y font-size]}]
  {:type        "group"
   :object-name :music-settings
   :x           x
   :y           y
   :children    [{:type        "image"
                  :src         "/raw/img/ui/settings/music_icon.png"
                  :object-name :music-icon
                  :x           0
                  :y           0}
                 {:x              100
                  :y              15
                  :type           "text"
                  :text           "MUSIC"
                  :object-name    :music-title
                  :align          "left"
                  :vertical-align "top"
                  :font-size      font-size
                  :font-family    "Luckiest Guy"
                  :fill           0xffffff}
                 {:type        "slider"
                  :object-name :music-slider
                  :x           303
                  :y           40
                  :width       352
                  :value       0
                  :on-slide    #(re-frame/dispatch [::ie/set-music-volume (Math/round %)])
                  :on-change   #(re-frame/dispatch [::ie/save-settings])}]})

(defn- get-sound-settings
  [{:keys [x y font-size]}]
  {:type        "group"
   :object-name :sound-settings
   :x           x
   :y           y
   :children    [{:type        "image"
                  :src         "/raw/img/ui/settings/sound_fx_icon.png"
                  :object-name :sound-icon
                  :x           0
                  :y           0}
                 {:x              100
                  :y              0
                  :type           "text"
                  :text           "SOUND FX"
                  :object-name    :sound-title
                  :align          "left"
                  :vertical-align "top"
                  :font-size      font-size
                  :font-family    "Luckiest Guy"
                  :fill           0xffffff}
                 {:type        "slider"
                  :object-name :effects-slider
                  :x           398
                  :y           25
                  :width       352
                  :value       00
                  :on-slide    #(re-frame/dispatch [::ie/set-effects-volume (Math/round %)])
                  :on-change   #(re-frame/dispatch [::ie/save-settings])}]})

(defn- get-settings-block
  [{:keys [x y]}]
  (let [title-font-size 80
        label-font-size 60]
    {:type        "group"
     :object-name :settings-block
     :x           x
     :y           y
     :children    [{:x              210
                    :y              0
                    :type           "text"
                    :text           "SETTINGS"
                    :object-name    :settings-title
                    :align          "left"
                    :vertical-align "top"
                    :font-size      title-font-size
                    :font-family    "Luckiest Guy"
                    :fill           0xffffff}
                   (get-music-setting {:x 96 :y 171 :font-size label-font-size})
                   (get-sound-settings {:x 1 :y 302 :font-size label-font-size})]}))

(defn create
  [{:keys [viewport]}]
  {:type        "group"
   :object-name :settings-overlay
   :visible     false
   :children    [{:type        "image"
                  :src         "/raw/img/bg.png"
                  :object-name :settings-background}
                 (get-close-button {:viewport viewport})
                 (get-settings-block {:x 580 :y 345})]})

(defn update-viewport
  [{:keys [viewport]}]
  (re-frame/dispatch [::scene/change-scene-object close-button-name [[:set-position (get-close-button-position viewport)]]]))
