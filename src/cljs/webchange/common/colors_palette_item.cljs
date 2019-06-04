(ns webchange.common.colors-palette-item
  (:require
    [react-konva :refer [Circle Group]]
    [reagent.core :as r]))

(defonce state (atom {:shape nil}))

(defn set-stage-cursor
  [stage cursor]
  (set! (-> (.container stage)
            (.-style)
            (.-cursor)) cursor))

(defn component-did-mount
  [this]
  (let [shape (:shape @state)
        stage (.getStage shape)
        {:keys [color on-click]} (r/props this)]
    (.on shape "mouseenter" #(set-stage-cursor stage "pointer"))
    (.on shape "mouseleave" #(set-stage-cursor stage "default"))
    (.on shape "click" #(when (->> on-click nil? not) (on-click {:color color})))))

(defn colors-palette-item-render
  [{:keys [x y width color]}]
  (let [padding 20
        radius-outer (/ width 2)
        radius-inner (- radius-outer padding)
        shadow-position (+ radius-outer (/ padding 3))]
    [:> Group {:x x
               :y y}
     [Circle {:x      shadow-position
              :y      shadow-position
              :radius radius-outer
              :fill   "#e0e0e0"}]
     [Circle {:x      radius-outer
              :y      radius-outer
              :radius radius-outer
              :fill   "#ffffff"}]
     [Circle {:x      radius-outer
              :y      radius-outer
              :radius radius-inner
              :fill   color
              :ref    #(swap! state assoc :shape %)}]
     ]))

(def colors-palette-item
  (with-meta colors-palette-item-render
             {:component-did-mount component-did-mount}))
