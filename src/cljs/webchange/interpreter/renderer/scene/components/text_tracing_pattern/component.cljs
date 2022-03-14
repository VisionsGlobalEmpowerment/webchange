(ns webchange.interpreter.renderer.scene.components.text-tracing-pattern.component
  (:require
    [clojure.string :as str]
    [webchange.interpreter.pixi :refer [Container]]
    [webchange.interpreter.renderer.scene.components.text-tracing-pattern.wrapper :refer [wrap]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.scene.components.svg-path.component :as s]
    [webchange.interpreter.renderer.scene.components.text-tracing-pattern.utils :refer [set-enable!]]
    [webchange.interpreter.renderer.scene.components.animated-svg-path.component :as a]
    [webchange.interpreter.renderer.scene.components.image.component :as i]
    [webchange.interpreter.renderer.scene.components.letters-path :refer [alphabet-path alphabet-traceable-path]]
    [webchange.logger.index :as logger]
    [webchange.interpreter.renderer.scene.components.animated-svg-path.tracing :as tracing]))

(def default-props {:x                        {:default 0}
                    :y                        {:default 300}
                    :width                    {:default 1920}
                    :height                   {:default 680}
                    :text                     {}
                    :name                     {}
                    :on-change                {}
                    :traceable                {}
                    :repeat-text              {}
                    :on-next-letter-activated {}
                    :on-finish                {}
                    :on-click                 {}
                    :debug                    {:default false}
                    :enable?                  {:default true}})

(def base-height 275)
(def midline-offset 70)
(def baseline-offset 150)

(def base-width 225)
(def line-height 10)
(def letter-offset-y -34)

(def safe-padding 60)

(def line-props {:type         "svg-path"
                 :x            0,
                 :width        1920,
                 :height       10,
                 :data         "M0,5h1920",
                 :scale        {:x 1 :y 1}
                 :stroke-width 4})

(defn- create-container
  [{:keys [x y]}]
  (doto (Container.)
    (utils/set-position {:x x :y y})))

(defn- path->letter
  [scale path]
  {:type         "svg-path",
   :dash         [],
   :width        base-width
   :height       base-height
   :data         path,
   :scale        {:x scale :y scale}
   :line-cap     "round",
   :stroke       "#898989",
   :stroke-width 12})

(defn- path->animated-letter
  [letter-scale padding-scale path]
  (let [padding (if (> padding-scale 0)
                  (* safe-padding padding-scale)
                  0)]
    {:type         "animated-svg-path",
     :duration     1000
     :width        (- base-width (* 2 padding))
     :height       base-height
     :path         path,
     :traceable    true
     :scale        {:x letter-scale :y letter-scale}
     :offset       {:x (- padding) :y 0}
     :line-cap     "round",
     :stroke       "#323232",
     :stroke-width 12}))

(defn- draw-pattern!
  [group {:keys [width text repeat-text]} {letter-scale :letter padding-scale :padding} topline-y]
  (let [text (if repeat-text (apply str (repeat repeat-text text)) text)
        length (count text)
        letter-width (* letter-scale base-width)
        letter-padding (* letter-scale padding-scale safe-padding)
        positions (->> (range length)
                       (map (fn [pos]
                              (if (> letter-padding 0)
                                {:x (- (* letter-width pos) (* letter-padding pos 2))
                                 :y topline-y}
                                {:x (+ (* letter-width pos) (/ (- width (* letter-width length)) 2))
                                 :y topline-y}))))
        letters (->> text
                     (map alphabet-path)
                     (map #(path->letter letter-scale %))
                     (map merge positions))]
    (doall
      (for [letter letters]
        (s/create (assoc letter
                         :object-name (str "text-tracing-pattern-" (:x letter))
                         :parent group))))))

(defn- activate-next-letter
  ([state]
   (activate-next-letter state {}))
  ([state {:keys [on-next-letter-activated on-finish]}]
   (let [first-inactive (->> @state :letters first)]
     (if first-inactive
       (do
         (logger/trace-folded "text-tracing-pattern activate-next-letter!" first-inactive)
         (swap! state update :letters #(drop 1 %))
         ((:activate first-inactive))
         (when on-next-letter-activated
           (on-next-letter-activated)))
       (when on-finish
         (logger/trace-folded "text-tracing-pattern on-finish!")
         (on-finish))))))

(defn- draw-traceable-pattern!
  [group {:keys [width text repeat-text] :as props} {letter-scale :letter padding-scale :padding} topline-y state]
  (let [text (if repeat-text (apply str (repeat repeat-text text)) text)
        length (count text)
        letter-width (* letter-scale base-width)
        letter-padding (* padding-scale safe-padding)
        reduced-letter-width (- letter-width (* 2 letter-padding letter-scale))
        positions (->> (range length)
                       (map (fn [pos]
                              (if (> padding-scale 0)
                                {:x (+ (* reduced-letter-width pos) (* letter-padding letter-scale))
                                 :y topline-y}
                                {:x (+ (* letter-scale base-width pos) (/ (- width (* letter-scale base-width length)) 2))
                                 :y topline-y}))))

        letters (->> text
                     (map alphabet-traceable-path)
                     (map #(path->animated-letter letter-scale padding-scale %))
                     (map merge positions))]
    (doall
      (for [letter letters]
        (let [animated-svg-path (a/create (assoc letter
                                            :active false
                                            :on-finish #(activate-next-letter state props)
                                            :object-name (str "text-tracing-pattern-animated-" (:x letter))
                                            :parent group))]
          (swap! state update :letters conj animated-svg-path)
          (swap! state update :all-letters conj animated-svg-path))))
    (reset! tracing/arrow (i/create {:type "image"
                                     :x -100
                                     :y 100
                                     :width 100
                                     :height 100
                                     :src "/raw/clipart/writing/arrow.png"
                                     :origin {:type "center-center"}
                                     :object-name "text-tracing-pattern-arrow"
                                     :parent group}))
    (activate-next-letter state)))

(defn- padding-scale
  [length scale-by-height width]
  (let [letter-width (* scale-by-height base-width)
        total-width (* length letter-width)
        actual-padding-width (- total-width width)
        estimated-padding-width (* safe-padding (dec length) 2 scale-by-height)]
    (if (= estimated-padding-width 0)
      -1
      (/ actual-padding-width estimated-padding-width))))

(defn- get-scale
  [{:keys [text repeat-text width height]}]
  (let [text (if repeat-text (apply str (repeat repeat-text text)) text)
        length (count text)
        scale-by-height (-> height (- (* 2 line-height)) (/ base-height))
        base-total-width (- (* length base-width) (* (dec length) safe-padding 2))
        scale-by-width (/ width base-total-width)]
    (if (> scale-by-width scale-by-height)
      {:type    :by-height
       :letter  scale-by-height
       :padding (padding-scale length scale-by-height width)}
      {:type    :by-width
       :letter  scale-by-width
       :padding 1})))

(comment
  (let [width 1920
        topline-y 0
        text "ono"
        {letter-scale :letter padding-scale :padding} (get-scale {:text text :width 1920 :height 680})
        length (count text)
        letter-width (* letter-scale base-width)
        letter-padding (* letter-scale padding-scale safe-padding)
        positions (->> (range length)
                       (map (fn [pos]
                              (if (> padding-scale 0)
                                {:x (- (* letter-width pos) (* letter-padding pos 2))
                                 :y topline-y}
                                {:x (+ (* letter-width pos) (/ (- width (* letter-width length)) 2))
                                 :y topline-y}))))
        letters (->> text
                     (map alphabet-path)
                     (map #(path->letter letter-scale %))
                     (map merge positions))]
    (->> letters
         (map #(select-keys % [:x :scale]))))

  (let [width 1920
        topline-y 0
        text "uu"
        {letter-scale :letter padding-scale :padding} (get-scale {:text text :width 1920 :height 680})

        length (count text)
        letter-width (* letter-scale base-width)
        letter-padding (* padding-scale safe-padding)
        letter-width (- letter-width (* 2 letter-padding letter-scale))
        positions (->> (range length)
                       (map (fn [pos]
                              (if (> padding-scale 0)
                                {:x (+ (* letter-width pos) (* letter-padding letter-scale))
                                 :y topline-y}
                                {:x (+ (* letter-width pos) (/ (- width (* (* letter-scale base-width) length)) 2))
                                 :y topline-y}))))
        letters (->> text
                     (map alphabet-traceable-path)
                     (map #(path->animated-letter letter-scale padding-scale %))
                     (map merge positions))]
    (->> letters
         (map #(select-keys % [:x :width :offset :scale]))))
  )

(defn- reset-group!
  [group]
  (let [children (.removeChildren group)]
    (doall
      (for [child children]
        (.destroy child)))))

(defn draw!
  [group {:keys [traceable height text] :as props} state]
  (let [{letter-scale :letter :as scale} (get-scale props)
        letter-height (* letter-scale base-height)
        topline-y (-> (- height letter-height) (/ 2))
        midline-y (-> topline-y (+ (* midline-offset letter-scale)))
        baseline-y (-> topline-y (+ (* baseline-offset letter-scale)))
        text-offset-y (* letter-offset-y letter-scale)
        has-text? (not (clojure.string/blank? text))]
    (swap! state assoc :letters [])
    (swap! state assoc :all-letters [])
    (reset-group! group)

    (logger/trace-folded "text-tracing-pattern draw!" scale topline-y midline-y baseline-y)
    (s/create (merge line-props {:object-name (str "text-tracint-pattern-topline")
                                 :parent      group
                                 :y           (- topline-y 5),
                                 :stroke      "#323232"}))
    (s/create (merge line-props {:object-name (str "text-tracint-pattern-midline")
                                 :parent      group
                                 :y           midline-y,
                                 :dash        [7 7]
                                 :stroke      "#898989"}))
    (s/create (merge line-props {:object-name (str "text-tracint-pattern-baseline")
                                 :parent      group
                                 :y           baseline-y,
                                 :stroke      "#323232"}))

    (when has-text?
      (draw-pattern! group props scale text-offset-y)
      (when traceable
        (draw-traceable-pattern! group props scale text-offset-y state)))

    (set-enable! state)))

(def component-type "text-tracing-pattern")

(defn create
  "Create `text-tracing-pattern` component.

  Props params:
  :x - component x-position.
  :y - component y-position.
  :traceable - for traceable overlay based on animated-svg-path
  :name - component name that will be set to sprite and container with corresponding suffixes.
  :on-change - on change event handler."
  [{:keys [parent type object-name on-click enable?] :as props}]
  (let [group (create-container props)
        state (atom {:enable? enable?})
        wrapped-group (wrap type object-name group props state draw!)]
    (logger/trace-folded "Create text-tracing-pattern" props)

    (draw! group props state)
    (.addChild parent group)

    (when-not (nil? on-click) (utils/set-handler group "click" on-click))

    wrapped-group))
