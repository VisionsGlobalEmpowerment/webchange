(ns webchange.interpreter.renderer.scene.components.text-tracing-pattern.component
  (:require
    [clojure.string :as str]
    [webchange.interpreter.pixi :refer [Container]]
    [webchange.interpreter.renderer.scene.components.text-tracing-pattern.wrapper :refer [wrap]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.scene.components.svg-path.component :as s]
    [webchange.interpreter.renderer.scene.components.text-tracing-pattern.utils :refer [set-enable!]]
    [webchange.interpreter.renderer.scene.components.animated-svg-path.component :as a]
    [webchange.logger.index :as logger]))

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

(def base-height 225)
(def midline-offset 70)
(def baseline-offset 150)

(def base-width 225)
(def line-height 10)

(def safe-padding 60)

(def alphabet-path {"a" "M 144.76 92.43 A 37.5 37.5 0 1 0 144.76 131.71 L 144.76 74.5 M 144.76 74.5 v 75"
                    "b" "M 77 7 v 142 M 77 92.64 a 37.5 37.5 0 1 1 0 39.28"
                    "c" "M 147 92 a 37.51 37.51 0 1 0 0 39.27"
                    "d" "M 147 92 a 38 38 0 1 0 0 39 L 147 7 M 147 7 v 145"
                    "e" "M 77 112 h 74 M 151 112 A 32 32 0 1 0 77 112 Q 77 145 109 151 Q 135 155 151 142"
                    "f" "M 124 42 Q 124 24 108 24 T 92 42 V 154 M 69 85 H 116"
                    "g" "M 147 93 A 38 38 0 1 0 147 132 L 147 75 M 147 75 V 181 A 35 35 0 0 1 79 192"
                    "h" "M 77 7 v 142 M 77 92.64 a 37.51 37.51 0 0 1 69.46 19.64 v 37.5"
                    "i" "M 102 74 V 149 M 102 36 L 102 35"
                    "j" "M 107 74 V 180 A 35 35 0 0 1 39 191 M 107 35 L 107 34"
                    "k" "M 113 22 V 161 M 162 76 L 113 114 M 113 114 L 161 161"
                    "l" "M 112 7 v 143"
                    "m" "M 72 75 v 75 M 72 150 L 72 92 C 72 81 89 76 101.17 78.27 C 113.06 81.07 121.1 90.78 121.1 101.54 V 150 M 121 150 L 121 91 c 0 -10 15 -16 29.32 -13.52 c 11.89 2.8 19.93 12.51 19.93 23.27 V 150"
                    "n" "M 82 74.42 v 75 M 82 149 L 82 92 A 32.35 32.35 0 0 1 143 106.77 v 42.65"
                    "o" "M110.65,74a37.5,37.5,0,1,0,37.5,37.5A37.5,37.5,0,0,0,110.65,74Z"
                    "p" "M 77.77 74.37 v 140 M 77.77 93.1 a 37.5 37.5 0 1 1 0 39.28"
                    "q" "M 149 93 A 38 38 0 1 0 149 132 V 74 M 149 74 V 207 A 1 1 0 0 0 172 207"
                    "r" "M85.86,75.66v75M85.85,92.79a32.37,32.37,0,0,1,53.3-5.63"
                    "s" "M131.91,90.75c1.33-9.39-10.49-14.17-18.28-14.39A21.71,21.71,0,0,0,97.69,82.7a16.08,16.08,0,0,0-3.89,16c1.65,5.12,6.13,8.58,10.93,10.57c6.67,2.77,13.9,4,20.26,7.62c7,3.94,10.77,12.34,8,20.14c-5.06,14.06-27.62,17.67-37.73,5.77a18.91,18.91,0,0,1-4.11-10.2"
                    "t" "M112.5,35.29v115M89.4,75.29h46.2"
                    "u" "M 82 75 V 120 a 20 20 0 0 0 58 0 V 75 M 140 75 V 149"
                    "v" "M79.56,75l32.88,75M112.56,150l32.88-75"
                    "w" "M73.71,75.66l18.58,75M93.71,150.66l18.58-75M112.71,75.66l18.58,75M132.71,150.66l18.58-75"
                    "x" "M81.53,75.66l61.94,75M143.47,75.66l-61.94,75"
                    "y" "M 78 75 L 113 150 M 147 75 L 97 189"
                    "z" "M 80 76 H 141 M 141 76 L 80 151 M 80 151 H 145"
                    "ñ" "M82,74.83v75M82,92a32.36,32.36,0,0,1,60.9,15.23v42.65M86,48.06s4.12-11.83,13.63-7.88c3.71,1.54,7.49,2.94,11.29,4.27c10.27,3.6,22.52,2.17,28-8.67"})

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
   :dash         [7 7],
   :width        base-width
   :height       base-height
   :data         path,
   :scale        {:x scale :y scale}
   :line-cap     "round",
   :stroke       "#898989",
   :stroke-width 4})

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
     :stroke-width 10}))

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
                     (str/lower-case)
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
                     (str/lower-case)
                     (map alphabet-path)
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
                     (map alphabet-path)
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
      (draw-pattern! group props scale topline-y)
      (when traceable
        (draw-traceable-pattern! group props scale topline-y state)))

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
