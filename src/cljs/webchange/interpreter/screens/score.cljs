(ns webchange.interpreter.screens.score
  (:require
    [re-frame.core :as re-frame]
    [react-konva :refer [Group Text]]
    [webchange.common.kimage :refer [kimage]]
    [webchange.interpreter.core :refer [get-data-as-url]]
    [webchange.interpreter.events :as ie]
    [webchange.interpreter.utils.position :refer [top-right]]
    [webchange.interpreter.variables.subs :as vars.subs]
    [webchange.subs :as subs]))

(defn- star
  [result idx]
  (let [full (* idx 2)
        half (- (* idx 2) 1)]
    (cond
      (>= result full) "/raw/img/ui/star_03.png"
      (>= result half) "/raw/img/ui/star_02.png"
      :else "/raw/img/ui/star_01.png")))

(defn score-screen
  []
  (let [top-right (top-right)
        scene-id (re-frame/subscribe [::subs/current-scene])
        successes (re-frame/subscribe [::vars.subs/variable @scene-id "successes"])
        fails (re-frame/subscribe [::vars.subs/variable @scene-id "fails"])
        result (* (/ @successes (+ @successes @fails)) 10)]
    [:> Group
     [kimage {:src (get-data-as-url "/raw/img/bg.jpg")}]

     [:> Group top-right
      [kimage {:src (get-data-as-url "/raw/img/ui/close_button_01.png")
               :x        (- 108) :y 20
        :on-click #(re-frame/dispatch [::ie/next-scene])
        :on-tap   #(re-frame/dispatch [::ie/next-scene])}]]

     [kimage {:src (get-data-as-url "/raw/img/ui/form.png") :x 639 :y 155}]
     [kimage {:src (get-data-as-url "/raw/img/ui/clear.png") :x 829 :y 245}]

     [kimage {:src (get-data-as-url (star result 1)) :x 758 :y 363}]
     [kimage {:src (get-data-as-url (star result 2)) :x 836 :y 363}]
     [kimage {:src (get-data-as-url (star result 3)) :x 913 :y 363}]
     [kimage {:src (get-data-as-url (star result 4)) :x 991 :y 363}]
     [kimage {:src (get-data-as-url (star result 5)) :x 1068 :y 363}]

     [:> Text {:x            880 :y 490 :text (str (Math/floor result) "/10")
               :font-size    80 :font-family "Luckiest Guy" :fill "white"
               :shadow-color "#1a1a1a" :shadow-offset {:x 5 :y 5} :shadow-blur 5 :shadow-opacity 0.5}]

     [kimage {:src (get-data-as-url "/raw/img/ui/vera_238x280.png") :x 851 :y 581}]
     [kimage {:src      (get-data-as-url "/raw/img/ui/reload_button_01.png")
              :x        679 :y 857
              :on-click #(re-frame/dispatch [::ie/restart-scene])
              :on-tap   #(re-frame/dispatch [::ie/restart-scene])}]
     [kimage {:src      (get-data-as-url "/raw/img/ui/next_button_01.png")
              :x        796 :y 857
              :on-click #(re-frame/dispatch [::ie/next-scene])
              :on-tap   #(re-frame/dispatch [::ie/next-scene])}]]))