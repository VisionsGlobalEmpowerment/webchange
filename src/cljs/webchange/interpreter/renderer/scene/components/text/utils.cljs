(ns webchange.interpreter.renderer.scene.components.text.utils
  (:require
    [webchange.interpreter.pixi :refer [Text TextMetrics TextStyle]]
    [webchange.interpreter.renderer.scene.modes.modes :as modes]))

(defn- text?
  [display-object]
  (instance? Text display-object))

(defn set-text
  [text-object value]
  (aset text-object "text" value))

(defn- get-style-property
  [text-object property]
  (if (text? text-object)
    (aget (.-style text-object) property)
    (-> (str "Object is not a PIXI.Text") js/Error. throw)))

(defn- set-style-property
  [text-object property value]
  (if (text? text-object)
    (aset (.-style text-object) property value)
    (-> (str "Object is not a PIXI.Text") js/Error. throw)))

(defn set-align
  [text-object align]
  (set-style-property text-object "align" align))

(defn get-fill
  [text-object]
  (get-style-property text-object "fill"))

(defn set-fill
  [text-object fill]
  (set-style-property text-object "fill" fill))

(defn set-font-size
  [text-object font-size]
  (set-style-property text-object "fontSize" font-size))

(defn set-font-family
  [text-object font-family]
  (set-style-property text-object "fontFamily" font-family))

(defn set-text-props
  [text-object props]
  (let [prop-setters {:fill        set-fill
                      :font-family set-font-family
                      :font-size   set-font-size}]
    (doseq [[key value] props]
      (-> (get prop-setters key)
          (apply [text-object value])))))

(defn check-text-placeholder
  [text-object {:keys [mode placeholder text] :as props}]
  (let [placeholder-style {:fill "#bebebe"}
        show-placeholder? (and (= mode ::modes/editor)
                               (empty? text))]
    (if show-placeholder?
      (do (set-text text-object placeholder)
          (set-text-props text-object placeholder-style))
      (do (set-text text-object text)
          (->> (keys placeholder-style)
               (select-keys props)
               (set-text-props text-object))))))

(defn set-align-anchor
  [text align]
  (case align
    ;; horizontal
    "left" (aset (.-anchor text) "x" 0)
    "center" (aset (.-anchor text) "x" 0.5)
    "right" (aset (.-anchor text) "x" 1)
    ;; vertical
    "top" (aset (.-anchor text) "y" 0)
    "middle" (aset (.-anchor text) "y" 0.5)
    "bottom" (aset (.-anchor text) "y" 1)
    nil))

(defn- get-font-props
  [{:keys [font-family font-size font-weight]}]
  (cond-> {:fontFamily font-family
           :fontWeight font-weight
           :fontSize   font-size}))

(defn calculate-position
  [{:keys [x y width height align vertical-align text] :as props}]
  (let [style (TextStyle. (clj->js (get-font-props props)))
        metrics (.measureText TextMetrics text style)
        top-empty-line (- (.-lineHeight metrics)
                          (.. metrics -fontProperties -ascent))]
    {:x (if (number? width)
          (case align
            "left" x
            "center" (+ x (/ width 2))
            "right" (+ x width))
          x)
     :y (if (number? height)
          (case vertical-align
            "top" (- y top-empty-line)
            "middle" (-> y
                         (+ (/ height 2))
                         (- (/ top-empty-line 2)))
            "bottom" (+ y height))
          y)}))

