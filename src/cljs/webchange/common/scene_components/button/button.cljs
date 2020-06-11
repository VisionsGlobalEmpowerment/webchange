(ns webchange.common.scene-components.button.button
  (:require
    [react-konva :refer [Group Rect Text]]
    [reagent.core :as r]))

(def default-props
  {:background-color "#ff9000"
   :border-radius    48
   :text             "Button"
   :text-color       "#ffffff"
   :align            "center"
   :font-size        68
   :font-family      "Luckiest Guy"
   :font-weight      "bold"
   :padding-top      20
   :padding-bottom   20
   :padding-left     68
   :padding-right    68
   :shadow-color     "#ba4f02"
   :shadow-offset    {:x 5 :y 5}
   :shadow-blur      6
   :shadow-opacity   0.8})

(defn- get-text-height-k
  "Relation between font height and real size on screen"
  [font-family]
  (case font-family
    "Luckiest Guy" 0.73
    1))

(defn- update-props
  [{:keys [padding-left padding-right padding-top padding-bottom font-family] :as props} {:keys [text-size]}]
  (let [height-k (get-text-height-k font-family)
        background-width (-> (:width text-size)
                             (+ padding-left padding-right))
        background-height (-> (:height text-size)
                              (* height-k)
                              (+ padding-top padding-bottom))]
    (-> props
        (assoc :width (if (contains? props :width) (:width props) background-width))
        (assoc :height (if (contains? props :height) (:height props) background-height)))))

(defn- get-group-props
  [props]
  (-> props
      (select-keys [:x
                    :y
                    :on-click
                    :on-tap])))

(defn- get-background-props
  [props]
  (-> props
      (select-keys [:width :height])
      (assoc :x 0)
      (assoc :y 0)
      (assoc :fill (:background-color props))
      (assoc :corner-radius (:border-radius props))))

(defn- set-text-horizontal-position
  [props origin-props]
  (-> props
      (assoc :x 0)
      (assoc :width (:width origin-props))
      (assoc :align "center")))

(defn- set-text-vertical-position
  [props origin-props]
  (let [text-height-k (get-text-height-k (:font-family props))
        button-height (:height origin-props)
        top-margin (-> button-height
                       (* (- 1 text-height-k))
                       (/ 2))]
    (-> props
        (assoc :y top-margin)
        (assoc :height button-height)
        (assoc :vertical-align "middle"))))

(defn- get-text-props
  [props]
  (-> props
      (select-keys [:font-family
                    :font-size
                    :font-weight
                    :text
                    :shadow-color
                    :shadow-offset
                    :shadow-blur
                    :shadow-opacity])
      (assoc :fill (:text-color props))
      (set-text-horizontal-position props)
      (set-text-vertical-position props)))

(defn button
  [props]
  (r/with-let [text-element (r/atom nil)]
              (let [custom-props (-> default-props
                                     (merge props)
                                     (update-props {:text-size (when @text-element {:width  (.-textWidth @text-element)
                                                                                    :height (.-textHeight @text-element)})}))]
               ;(.log js/console "@text-element" @text-element)
                [:> Group (get-group-props custom-props)
                 [:> Rect (get-background-props custom-props)]
                 [:> Text (merge (get-text-props custom-props)
                                 {:ref #(when % (reset! text-element %))})]])))

(defn button-editor
  [props]
  [button props])

(defn button-interpreter
  [props]
  [button props])
