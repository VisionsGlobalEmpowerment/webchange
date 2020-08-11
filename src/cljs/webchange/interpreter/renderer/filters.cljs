(ns webchange.interpreter.renderer.filters
  (:require
    [cljsjs.pixi]
    [pixi-custom-filters]
    [webchange.interpreter.renderer.filters-pulsation :refer [animation-eager]]
    [webchange.logger :as logger]))

(def ColorMatrixFilter (.. js/PIXI -filters -ColorMatrixFilter))
(def GlowFilter (.. js/PIXI -filters -GlowFilter))

(defn- init-filters
  [container]
  (when (nil? (.-filters container))
    (aset container "filters" (clj->js []))))

(defn- add-filter
  [filter container]
  (.push (.-filters container) filter))

(defn- apply-brighten-filter
  [container {:keys [value]}]
  (let [filter (ColorMatrixFilter.)
        brightness (+ 1 value)]
    (add-filter filter container)
    (.brightness filter brightness)))

(defn- apply-glow-filter
  [container]
  (let [glow-params (clj->js {:quality       0.1
                              :distance      10
                              :innerStrength 0
                              :outerStrength 4
                              :color         0xffffff})
        filter (GlowFilter. glow-params)]
    (add-filter filter container)))

(defn- apply-grayscale-filter
  [container]
  (let [filter (ColorMatrixFilter.)]
    (add-filter filter container)
    (.desaturate filter 0)))

(defn- create-ticker
  [animate]
  (let [ticker (.. js/PIXI -Ticker -shared)]
    (.add ticker animate)
    ticker))

(defn- apply-pulsation-filter
  [container]
  (let [state (atom nil)]
    ;; ToDo: prettify it:
    (aset (.-pivot container) "x" (/ (.-width container) 2))
    (aset container "x" (+ (aget container "x") (/ (.-width container) 2)))
    (aset (.-pivot container) "y" (/ (.-height container) 2))
    (aset container "y" (+ (aget container "y") (/ (.-height container) 2)))

    (create-ticker (fn []
                     (animation-eager container {:time (.now js/Date)} state)))))

(defn apply-filters
  [container filters]
  (init-filters container)
  (doseq [{:keys [name] :as filter-params} filters]
    (case name
      "brighten" (apply-brighten-filter container filter-params)
      "glow" (apply-glow-filter container)
      "grayscale" (apply-grayscale-filter container)
      "pulsation" (apply-pulsation-filter container)
      (logger/warn "[Filters]" (str "Filter with type <" name "> can not be drawn because it is not defined")))))

