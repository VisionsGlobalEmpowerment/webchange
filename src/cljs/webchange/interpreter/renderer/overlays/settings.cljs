(ns webchange.interpreter.renderer.overlays.settings
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.events :as ie]
    [webchange.interpreter.renderer.scene.components.group.group :refer [create-group]]
    [webchange.interpreter.renderer.overlays.utils :as utils]))

(defn- get-close-button
  [{:keys [viewport menu-padding]}]
  (merge {:type        "image"
          :src         (get-in utils/common-elements [:close-button :src])
          :object-name :close-settings
          :on-click    #(re-frame/dispatch [::ie/close-settings])}
         (utils/get-coordinates {:viewport   viewport
                                 :vertical   "top"
                                 :horizontal "right"
                                 :object     (get-in utils/common-elements [:close-button :size])
                                 :padding    menu-padding})))

(defn- get-music-setting
  [{:keys [x y]}]
  {:type        "group"
   :object-name :music-settings
   :x           x
   :y           y
   :children    [{:type        "image"
                  :src         "/raw/img/ui/settings/music_icon.png"
                  :object-name :music-icon
                  :x           0
                  :y           0}
                 {:type        "image"
                  :src         "/raw/img/ui/settings/music.png"
                  :object-name :music-title
                  :x           88
                  :y           12}
                 {:type        "slider"
                  :object-name :music-slider
                  :x           303
                  :y           40
                  :width       352
                  :value       0
                  :on-change   #(re-frame/dispatch [::ie/set-music-volume (Math/round %)])}]})

(defn- get-sound-settings
  [{:keys [x y]}]
  {:type        "group"
   :object-name :sound-settings
   :x           x
   :y           y
   :children    [{:type        "image"
                  :src         "/raw/img/ui/settings/sound_fx_icon.png"
                  :object-name :sound-icon
                  :x           0
                  :y           0}
                 {:type        "image"
                  :src         "/raw/img/ui/settings/sound_fx.png"
                  :object-name :sound-title
                  :x           88
                  :y           -2}
                 {:type        "slider"
                  :object-name :effects-slider
                  :x           398
                  :y           25
                  :width       352
                  :value       00
                  :on-change   #(re-frame/dispatch [::ie/set-effects-volume (Math/round %)])}]})

(defn- get-settings-block
  [{:keys [x y]}]
  {:type        "group"
   :object-name :settings-block
   :x           x
   :y           y
   :children    [{:type        "image"
                  :src         "/raw/img/ui/settings/settings.png"
                  :object-name :settings-title
                  :x           199
                  :y           0}
                 (get-music-setting {:x 96 :y 171})
                 (get-sound-settings {:x 1 :y 302})]})

(defn create-settings-overlay
  [{:keys [parent viewport]}]
  (create-group parent {:parent      parent
                        :object-name :settings-overlay
                        :visible     false
                        :children    [{:type        "image"
                                       :src         "/raw/img/bg.jpg"
                                       :object-name :settings-background}
                                      (get-close-button {:menu-padding {:x 20 :y 20}
                                                         :viewport     viewport})
                                      (get-settings-block {:x 580 :y 345})]}))
