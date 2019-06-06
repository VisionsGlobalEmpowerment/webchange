(ns webchange.common.colors-palette
  (:require
    [react-konva :refer [Group]]
    [webchange.common.colors-palette-item :refer [colors-palette-item]]))

(defn get-item-position-params
  [{:keys [width height items-number padding]}]
  (let [total-items-height (+ (* width items-number) (* padding (- items-number 1)))
        top-list-margin (/ (- height total-items-height) 2)
        items (for [idx (range items-number)]
                {:x 0
                 :y (+ top-list-margin (* idx (+ width padding)))
                 :width width
                 :height width})]
    (vec items)))

(defn colors-palette
  [{:keys [x y width height colors on-change]}]
  (let [items-padding 10
        position-params (get-item-position-params {:width width
                                                   :height height
                                                   :items-number (count colors)
                                                   :padding items-padding})
        items-params (map-indexed (fn [idx position] (merge position {:color    (get colors idx)
                                                                     :on-click on-change})) position-params)]
    [:> Group {:x x
               :y y}
     (for [item-params items-params]
       ^{:key (:color item-params)}
       [colors-palette-item item-params])]))
