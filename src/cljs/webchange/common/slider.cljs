(ns webchange.common.slider
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [react-konva :refer [Group Rect]]))

(defn relative-click-x
  [e]
  (let [mouse-x (-> e .-evt .-x)
        group-x (-> e .-target .-parent .getAbsolutePosition .-x)
        x (- mouse-x group-x)]
    x))

(defn normalize-slider
  [value max]
  (let [bounded (cond
                  (< value 0) 0
                  (> value max) max
                  :default value)]
    (Math/round (/ bounded (/ max 100)))))

(defn set-volume-from-event
  [event max]
  (fn [e]
    (let [x (relative-click-x e)]
      (re-frame/dispatch [event (normalize-slider x max)]))))

(defn slider
  [{:keys [x y width height event sub]}]
  (let [value (re-frame/subscribe [sub])
        coef (/ width 100)]
    [:> Group {:x x :y y}
     [:> Rect {:x 1 :width (- width 2) :height height :fill "#ffffff" :corner-radius 25}]
     [:> Group {:clip-x 0 :clip-y 0 :clip-width (+ 0.1 (* @value coef)) :clip-height height}
      [:> Rect {:width width :height height :fill "#2c9600" :corner-radius 25}]]
     (let [this (r/current-component)]
       [:> Rect {:width width :height height :opacity 0
                 :draggable true :drag-distance 0 :drag-bound-func (fn [pos] (this-as that #js {:x (.-x pos) :y (-> that .getAbsolutePosition .-y)}))
                 :on-mouse-down (set-volume-from-event event width)
                 :on-drag-move (set-volume-from-event event width)
                 :on-drag-end (fn [e]
                                (-> e .-target (.position #js {:x 0 :y 0}))
                                (r/force-update this))}]
       )]))