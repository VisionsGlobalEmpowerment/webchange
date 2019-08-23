(ns webchange.common.copybook
  (:require
    [react-konva :refer [Group Line]]))

(def line-params
  {:stroke       "#323232"
   :stroke-width 1
   :line-cap     "round"})

(def second-line-params
  (merge
    line-params
    {:stroke "#acacac"
     :dash   [20 10]}))

(defn copybook
  [{:keys [x y width height line-height padding-height]}]
  [:> Group {:x      x
             :y      y
             :width  width
             :height height}
   (let [row-height (+ line-height (* padding-height 2))]
     (for [index (range (/ height row-height))]
       ^{:key index}
       [:> Group {:x      0
                  :y      (* row-height index)}
        [:> Line (merge line-params {:points [0 0 width 0]})]
        [:> Line (merge second-line-params {:points [0 padding-height width padding-height]})]
        [:> Line (merge line-params {:points [0 (+ line-height padding-height) width (+ line-height padding-height)]})]]))])
