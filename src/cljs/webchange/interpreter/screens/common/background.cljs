(ns webchange.interpreter.screens.common.background
  (:require
    [react-konva :refer [Group Rect]]
    [webchange.common.kimage :refer [kimage]]
    [webchange.interpreter.core :refer [get-data-as-url]]))

(defn background
  [{:keys [x y width height]}]
  (let [border-width 20]
    [:> Group
     [kimage {:src (get-data-as-url "/raw/img/bg.jpg")}]
     [:> Rect {:x             (- x (/ border-width 2))
               :y             (- y (/ border-width 2))
               :width         (+ width border-width)
               :height        (+ height border-width)
               :stroke        "#bd13c7"
               :stroke-width  border-width
               :corner-radius 56
               :fill          "#da12ea"
               :shadow-color  "#75016e"
               :shadow-blur   15}]]))
