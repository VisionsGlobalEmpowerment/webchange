(ns webchange.interpreter.renderer.scene.filters.filters
  (:require
    [webchange.interpreter.pixi :refer [shared-ticker ColorMatrixFilter DropShadowFilter GlowFilter OutlineFilter]]
    [webchange.interpreter.renderer.scene.filters.filters-pulsation :refer [animation-eager]]
    [webchange.interpreter.renderer.scene.filters.utils :as utils]
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

(defn apply-glow-filter
  [container]
  (let [glow-params (clj->js {:quality       0.1
                              :distance      10
                              :innerStrength 0
                              :outerStrength 4
                              :color         0xffffff})]
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
        (remove-ticker (get @pulsation-fns container))
        (swap! pulsation-fns assoc container nil))
       (do
         (move-pivot-to-center container)
         (swap! pulsation-fns assoc container (fn [] (animation-eager container {:time (.now js/Date)} state speed no-interval)))
         (create-ticker (get @pulsation-fns container)))))))

(defn- remove-all-filters
  [container]
  (apply-pulsation-filter container {:remove true})
  (aset container "filters" nil))

(defn has-filter-by-name
  [container filter-name]
  (case filter-name
    "pulsation" (not (nil? (get @pulsation-fns container)))
    (not (nil? (get-filter-by-name container filter-name)))))

(defn apply-filters
  [container filters]
  (doseq [{:keys [name] :as filter-params} filters]
    (case name
      "brightness" (apply-brighten-filter container filter-params)
      "glow" (apply-glow-filter container)
      "grayscale" (apply-grayscale-filter container)
      "outline" (apply-outline-filter container filter-params)
      "pulsation" (apply-pulsation-filter container)
      "shadow" (apply-shadow-filter container filter-params)
      (logger/warn "[Filters]" (str "Filter with type <" name "> can not be drawn because it is not defined")))))

(defn set-filter
  [container name params]
  (case name
    "brightness" (apply-brighten-filter container params)
    "glow" (apply-glow-filter container)
    "grayscale" (apply-grayscale-filter container)
    "outline" (apply-outline-filter container params)
    "pulsation" (apply-pulsation-filter container params)
    "shadow" (apply-shadow-filter container params)
    "" (remove-all-filters container)
    (logger/warn "[Filters]" (str "Filter with type <" name "> can not be set because it is not defined"))))

(defn get-filter-value
  [container filter-name]
  (->> (get-filter-by-name container filter-name)
       (.-value)))

(defn set-filter-value
  [container filter-name value]
  (if-let [filter (get-filter-by-name container filter-name)]
    (case filter-name
      "brightness" (set-brightness filter value)
      (logger/warn "[Filters]" (str "Filter with type <" filter-name "> can not be updated")))
    (logger/warn "[Filters]" (str "Filter with type <" filter-name "> was not found"))))
