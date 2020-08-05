(ns webchange.interpreter.screens.preloader
  (:require
    [re-frame.core :as re-frame]
    [react-konva :refer [Group Rect]]
    [webchange.common.kimage :refer [kimage]]
    [webchange.interpreter.core :refer [get-data-as-url]]
    [webchange.interpreter.events :as ie]
    [webchange.interpreter.executor :as e]
    [webchange.interpreter.utils.position :refer [top-right]]
    [webchange.subs :as subs]
    [webchange.common.scene-components.components :as components]
    [webchange.interpreter.utils.i18n :refer [t]]))

(defn- do-start
  []
  (e/init)
  (re-frame/dispatch [::ie/start-playing]))

(defn preloader-screen
  []
  (let [scene-id (re-frame/subscribe [::subs/current-scene])
        progress (re-frame/subscribe [::subs/scene-loading-progress @scene-id])
        loaded (re-frame/subscribe [::subs/scene-loading-complete @scene-id])]
    [:> Group
     [kimage {:src (get-data-as-url "/raw/img/bg.jpg")}]
     [:> Group {:x 628 :y 294}
      [kimage {:src "/raw/img/ui/logo.png"}]]
     (if @loaded
       [:> Group {:x 829 :y 750 :on-click do-start :on-tap do-start}
        [components/button-interpreter {:text (t "play")}]]
       [:> Group {:x 719 :y 780}
        [:> Rect {:x 1 :width 460 :height 24 :fill "#ffffff" :corner-radius 25}]
        [:> Group {:clip-x 0 :clip-y 0 :clip-width (+ 0.1 (* @progress 4.62)) :clip-height 24}
         [:> Rect {:width 462 :height 24 :fill "#2c9600" :corner-radius 25}]]])]))
