(ns webchange.interpreter.screens.settings
  (:require
    [re-frame.core :as re-frame]
    [react-konva :refer [Group]]
    [webchange.common.kimage :refer [kimage]]
    [webchange.common.slider :refer [slider]]
    [webchange.interpreter.core :refer [get-data-as-url]]
    [webchange.interpreter.events :as ie]
    [webchange.interpreter.utils.position :refer [top-right]]
    [webchange.subs :as subs]))

(defn settings-screen
  []
  (let [top-right (top-right)]
    [:> Group
       [kimage {:src (get-data-as-url "/raw/img/bg.jpg")}]

       [:> Group top-right
        [kimage
         {:src (get-data-as-url "/raw/img/ui/close_button_01.png")
          :x (- 108) :y 20
          :on-click #(re-frame/dispatch [::ie/close-settings])
          :on-tap #(re-frame/dispatch [::ie/close-settings])}]]

       [kimage {:src (get-data-as-url "/raw/img/ui/settings/settings.png") :x 779 :y 345}]
       [kimage {:src (get-data-as-url "/raw/img/ui/settings/music_icon.png") :x 675 :y 516}]
       [kimage {:src (get-data-as-url "/raw/img/ui/settings/music.png") :x 763 :y 528}]
       [kimage {:src (get-data-as-url "/raw/img/ui/settings/sound_fx_icon.png") :x 581 :y 647}]
       [kimage {:src (get-data-as-url "/raw/img/ui/settings/sound_fx.png") :x 669 :y 645}]

       [slider {:x 979 :y 556 :width 352 :height 24 :event ::ie/set-music-volume :sub ::subs/get-music-volume}]
       [slider {:x 979 :y 672 :width 352 :height 24 :event ::ie/set-effects-volume :sub ::subs/get-effects-volume}]]))
