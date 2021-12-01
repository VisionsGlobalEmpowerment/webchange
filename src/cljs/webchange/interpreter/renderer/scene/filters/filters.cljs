(ns webchange.interpreter.renderer.scene.filters.filters
  (:require
    [webchange.interpreter.pixi :refer [shared-ticker ColorMatrixFilter DropShadowFilter GlowFilter OutlineFilter]]
    [webchange.interpreter.renderer.scene.filters.filters-pulsation :refer [animation-eager apply-transformation]]
    [webchange.interpreter.renderer.scene.filters.filters-alpha-pulsation :as fap]
    [webchange.interpreter.renderer.scene.filters.utils :as utils]
    [webchange.interpreter.renderer.scene.filters.utils-pulsation :refer [pulsation reset-pulsation]]
    [webchange.interpreter.renderer.scene.components.utils :as components-utils]
    [webchange.logger.index :as logger]))

(defn- init-filters
  [container]
  (when (nil? (.-filters container))
    (aset container "filters" (clj->js []))))

(defn- add-filter
  [filter container]
  (init-filters container)
  (.push (.-filters container) filter))

(defn- get-filter-by-name
  [container filter-name]
  (->> (.-filters container)
       (js->clj)
       (some (fn [filter]
               (and (= (.-name filter) filter-name) filter)))))

(defn- set-brightness
  [filter value]
  (doto filter
    (.brightness (+ 1 value))
    (aset "value" value)))

(defn apply-brighten-filter
  [container {:keys [value]}]
  (doto (ColorMatrixFilter.)
    (aset "name" "brightness")
    (set-brightness value)
    (add-filter container)))

(defn- set-hue
  [filter value]
  (doto filter
    (.saturate value)
    (aset "value" value)))

(defn apply-hue-filter
  [container {:keys [value]}]
  (doto (ColorMatrixFilter.)
    (aset "name" "hue")
    (set-hue value)
    (add-filter container)))

(defn- set-glow-outer-strength
  [filter value]
  (doto filter
    (aset "outerStrength" value)
    (aset "value" value)))

(defn apply-glow-filter
  [container {:keys [quality distance inner-strength outer-strength color]
              :or   {quality        0.1
                     distance       10
                     inner-strength 0
                     outer-strength 4
                     color          0xffffff}}]
  (let [glow-params (clj->js {:quality       quality
                              :distance      distance
                              :innerStrength inner-strength
                              :outerStrength outer-strength
                              :color         color})]
    (doto (GlowFilter. glow-params)
      (aset "name" "glow")
      (add-filter container))))

(defn apply-grayscale-filter
  [container]
  (doto (ColorMatrixFilter.)
    (.desaturate 0)
    (aset "name" "grayscale")
    (add-filter container)))

(defn apply-outline-filter
  [container {:keys [width color quality]
              :or   {width   1
                     color   0x000000
                     quality 0.1}}]
  (doto (OutlineFilter. width color quality)
    (aset "name" "outline")
    (aset "padding" width)
    (add-filter container)))

(defn apply-shadow-filter
  [container {:keys [distance color blur]
              :or   {distance 3
                     color    0x000000
                     blur     2}}]
  (doto (DropShadowFilter.)
    (aset "distance" distance)
    (aset "color" color)
    (aset "blur" blur)
    (aset "name" "shadow")
    (add-filter container)))

(defn- create-ticker
  [animate]
  (.add shared-ticker animate)
  shared-ticker)

(defn- remove-ticker
  [animate]
  (.remove shared-ticker animate))

(def pulsation-fns (atom {}))
(def pulsation-default-scale (atom {}))

(def alpha-pulsation-fns (atom {}))
(def alpha-pulsation-default-scale (atom {}))

(defn destroy
  []
  (doseq [[_ fn] @pulsation-fns]
    (remove-ticker fn)))

(defn- move-pivot-to-center
  [container]
  (let [half-width (/ (.-width container) 2)
        half-height (/ (.-height container) 2)
        pivot (utils/get-pivot container)]
    (let [position (utils/get-position container)
          dx (- half-width (:x pivot))
          dy (- half-height (:y pivot))]
      (utils/set-pivot container {:x half-width
                                  :y half-height})
      (utils/set-position container {:x (+ (:x position) dx)
                                     :y (+ (:y position) dy)}))))

(defn apply-pulsation-filter
  ([container]
   (apply-pulsation-filter container {}))
  ([container {:keys [remove speed no-interval]}]
   (let [state (atom nil)]
     (if remove
       (do
         (when (get @pulsation-default-scale container)
           (components-utils/set-scale container (get @pulsation-default-scale container)))
         (remove-ticker (get @pulsation-fns container))
         (swap! pulsation-fns assoc container nil))
       (do
         (swap! pulsation-default-scale assoc container (components-utils/get-scale container))
         (move-pivot-to-center container)
         (swap! pulsation-fns assoc container (fn [] (animation-eager container {:time (.now js/Date)} state speed no-interval)))
         (create-ticker (get @pulsation-fns container)))))))

(defn apply-alpha-pulsation-filter
  ([container]
   (apply-alpha-pulsation-filter container {}))
  ([container {:keys [remove speed no-interval]}]
   (let [state (atom nil)]
     (if remove
       (do
         (when (get @alpha-pulsation-default-scale container)
           (components-utils/set-opacity container (get @alpha-pulsation-default-scale container)))
         (remove-ticker (get @alpha-pulsation-fns container))
         (swap! alpha-pulsation-fns assoc container nil))
       (do
         (swap! alpha-pulsation-default-scale assoc container (components-utils/get-opacity container))
         (swap! alpha-pulsation-fns assoc container (fn [] (fap/animation-eager container {:time (.now js/Date)} state speed no-interval)))
         (create-ticker (get @alpha-pulsation-fns container)))))))

(defn- remove-all-filters
  [container]
  (apply-pulsation-filter container {:remove true})
  (apply-alpha-pulsation-filter container {:remove true})
  (aset container "filters" nil))

(defn has-filter-by-name
  [container filter-name]
  (case filter-name
    "pulsation" (not (nil? (get @pulsation-fns container)))
    (not (nil? (get-filter-by-name container filter-name)))))


(defn get-filter-value
  [container filter-name]
  (->> (get-filter-by-name container filter-name)
       (.-value)))

(defn set-filter-value
  [container filter-name value]
  (if-let [filter (get-filter-by-name container filter-name)]
    (case filter-name
      "brightness" (set-brightness filter value)
      "hue" (set-hue filter value)
      "glow" (set-glow-outer-strength filter value)
      (logger/warn "[Filters]" (str "Filter with type <" filter-name "> can not be updated")))
    (logger/warn "[Filters]" (str "Filter with type <" filter-name "> was not found"))))

(defonce glow-pulsation-fns (atom {}))

(defn- apply-glow-pulsation-filter
  [container params]
  (let [handler-key (.-name container)
        handler (fn [_]
                  (->> (pulsation handler-key params)
                       (set-filter-value container "glow")))]

    (when-not (has-filter-by-name container "glow")
      (apply-glow-filter container {:outer-strength 0}))

    (reset-pulsation handler-key)
    (create-ticker handler)

    (swap! glow-pulsation-fns assoc handler-key handler)))

(defn- remove-glow-pulsation-filter
  [container]
  (let [handler-key (.-name container)
        handler (get @glow-pulsation-fns handler-key)]
    (remove-ticker handler)
    (swap! glow-pulsation-fns dissoc handler-key)
    (set-filter-value container "glow" 0)))

(defn apply-filters
  [container filters]
  (logger/trace-folded "apply Filter" container filters)
  (doseq [{:keys [name] :as filter-params} filters]
    (case name
      "brightness" (apply-brighten-filter container filter-params)
      "hue" (apply-hue-filter container filter-params)
      "glow" (apply-glow-filter container filter-params)
      "grayscale" (apply-grayscale-filter container)
      "outline" (apply-outline-filter container filter-params)
      "pulsation" (apply-pulsation-filter container)
      "alpha-pulsation" (apply-alpha-pulsation-filter container)
      "glow-pulsation" (apply-glow-pulsation-filter container filter-params)
      "shadow" (apply-shadow-filter container filter-params)
      (logger/warn "[Filters]" (str "Filter with type <" name "> can not be drawn because it is not defined")))))

(defn set-filter
  [container name params]
  (logger/trace-folded "set Filter" container name params)
  (case name
    "brightness" (apply-brighten-filter container params)
    "hue" (apply-hue-filter container params)
    "glow" (apply-glow-filter container params)
    "grayscale" (apply-grayscale-filter container)
    "outline" (apply-outline-filter container params)
    "pulsation" (apply-pulsation-filter container params)
    "alpha-pulsation" (apply-alpha-pulsation-filter container params)
    "glow-pulsation" (apply-glow-pulsation-filter container params)
    "shadow" (apply-shadow-filter container params)
    "" (remove-all-filters container)
    (logger/warn "[Filters]" (str "Filter with type <" name "> can not be set because it is not defined"))))

(defn remove-filter
  [container name]
  (case name
    "glow-pulsation" (remove-glow-pulsation-filter container)
    nil))
