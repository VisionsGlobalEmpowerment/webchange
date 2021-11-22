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

(def base-height 275)
(def midline-offset 70)
(def baseline-offset 150)

(def base-width 225)
(def line-height 10)
(def letter-offset-y -34)

(def safe-padding 60)

(def alphabet-path
  {"a" "M 162 125 a 42 42 0 1 0 0 52 m 0 -69 l 0 85"
   "A" "M 62 190 l 57 -150 q 6 -10 13 0 l 56 151 m -108 -47 l 90 0"
   "b" "M 87 36 v 155 m 0 -64 a 42 42 0 1 1 0 48"
   "B" "M 71 36 l -1 154 m 1 -154 l 59 0 a 32 30 0 0 1 0 75 l -59 0 m 59 0 a 36 30 0 0 1 3 79 l -63 0"
   "c" "M 162 129 a 41 42 0 1 0 1 45"
   "C" "M 184 52 a 73 80 0 1 0 -2 127"
   "d" "M 163 127 a 41 42 0 1 0 0 48 m 0 -141 l 0 159"
   "D" "M 63 36 l -1 153 m 1 -153 h 57 a 26 30 0 0 1 0 153 l -58 0"
   "e" "M 86 149 h 78 m 0 0 a 32 32 0 1 0 -78 0 q 0 33 33 41 q 26 4 46 -10"
   "E" "M 83 36 v 154 m 0 -154 h 86 m -86 75 h 72 m -72 79 h 86"
   "f" "M 159 54 q 0 -18 -17 -18 t -17 18 v 138 m -26 -81 h 53"
   "F" "M 86 36 v 154 m 0 -154 h 90 m -90 77 h 79"
   "g" "M 163 129 a 41 40 0 1 0 0 43 m 0 -62 v 119 q -1 35 -35 35 t -38 -25"
   "G" "M 179 51 q -19 -17 -48 -17 q -77 0 -77 79 q 0 79 75 79 q 67 0 67 -67 m 0 0 h -53"
   "h" "M 87 35 v 156 m 0 -64 q 12 -18 37 -18 q 38 0 38 36 v 46"
   "H" "M 67 34 v 158 m 0 -79 h 116 m 0 -79 v 158"
   "i" "M 125 109 v 83 m 0 -122 l 0 -1"
   "I" "M 125 35 v 155 m -32 -155 h 63 m -63 155 h 63"
   "j" "M 126 111 v 121 q 0 31 -27 31 q -25 0 -29 -23 m 56 -169 l 0 -1"
   "J" "M 160 35 v 121 q 0 36 -35 36 q -25 0 -39 -19"
   "k" "M 102 33 v 159 m 49 -85 l -49 38 m 0 0 l 48 47"
   "K" "M 79 33 v 159 m 87 -159 l -87 89 m 0 0 l 93 70"
   "l" "M 125 34 v 157"
   "L" "M 83 34 v 157 m 0 0 h 86"
   "m" "M 71 105 v 86 m 0 0 m 0 -58 c 0 -11 13 -24 27 -22 c 14 2 23 10 27 18 v 62 m 0 0 m 0 -59 c 0 -10 10 -23 27 -22 c 17 2 27 12 27 25 v 56"
   "M" "M 54 191 v -156 m 0 0 l 71 103 m 0 0 l 71 -103 m 0 0 v 156"
   "n" "M 92 107 v 86 m 0 0 m 0 -64 q 13 -19 33 -19 q 33 0 33 33 v 50"
   "N" "M 63 192 v -157 m 0 0 l 123 157 m 0 0 v -157"
   "o" "M 125 110 a 41 41 0 1 0 1 0 z"
   "O" "M 125 32 a 75 80 0 1 0 1 0 z"
   "p" "M 87 109 v 155 m 0 -137 a 42 41 0 1 1 0 47"
   "P" "M 76 35 v 155 m 0 -155 h 55 a 43 43 0 0 1 0 86 H 76"
   "q" "M 160 131 a 40 40 0 1 0 0 39 m 0 -62 v 146 q 0 10 11 10 q 6 0 12 -10"
   "Q" "M 125 32 a 75 80 0 1 0 1 0 z m 7 111 l 53 72"
   "r" "M 100 107 v 86 m 0 -67 a 32 25 0 0 1 58 -2"
   "R" "M 73 36 v 155 m 0 -155 h 59 a 1 1 0 0 1 0 86 h -59 m 62 0 l 39 69"
   "s" "M 146 126 c 1.1 -9.9 -11 -15.4 -19.8 -15.4 a 24.2 24.2 90 0 0 -17.6 6.6 a 17.6 17.6 90 0 0 -4.4 17.6 c 2.2 5.5 6.6 9.9 12.1 12.1 c 7.7 3.3 15.4 4.4 22 8.8 c 7.7 4.4 12.1 13.2 8.8 22 c -5.5 15.4 -30.8 19.8 -41.8 6.6 a 20.9 20.9 90 0 1 -4.4 -11"
   "S" "M 170 50 c -16 -15 -22 -16 -41 -17 a 48 48 90 0 0 -38 16 a 35 35 90 0 0 -9 35 c 4 11 13 20 26 24 c 15 7 31 9 47 17 c 15 9 24 26 15 46 c -9 21 -62 40 -96 -3"
   "t" "M 126 33 v 158 m -28 -80 h 56"
   "T" "M 129 35 v 157 m -52 -157 h 104"
   "u" "M 91 109 v 52 a 23 23 90 0 0 67 0 m 0 -52 v 85"
   "U" "M 67 36 v 96 a 40.25 40.25 90 0 0 117.25 0 v -96"
   "v" "M 89 110 l 36 80 m 0 0 l 36 -80"
   "V" "M 61 35 l 64 155 m 0 0 l 64 -155"
   "w" "M 83 109 l 21 82 m 0 0 l 21 -82 m 0 0 l 21 82 m 0 0 l 21 -82"
   "W" "M 23 35 l 49 155 m 0 0 l 52 -122 m 0 0 l 52 122 m 0 0 l 49 -155"
   "x" "M 91 110 l 68 82 m 0 -82 l -68 82"
   "X" "M 73 35 l 106 155 m 0 -155 l -106 155"
   "y" "M 86 110 l 40 82 m 37 -82 l -67 154"
   "Y" "M 68 35 l 58 81 m 0 0 l 58 -81 m -58 81 v 76"
   "z" "M 90 111 h 65 m 0 0 l -66 80 m 0 0 h 72"
   "Z" "M 75 35 h 103 m 0 0 l -112 157 m 0 0 h 116"
   "ñ" "M82,74.83v75M82,92a32.36,32.36,0,0,1,60.9,15.23v42.65M86,48.06s4.12-11.83,13.63-7.88c3.71,1.54,7.49,2.94,11.29,4.27c10.27,3.6,22.52,2.17,28-8.67"})

(def alphabet-traceable-path
  {"a" "M 162 125 a 42 42 0 1 0 0 52 l 0 -57 m 0 -12 v 85"
   "A" "M 62 190 l 57 -150 q 6 -10 13 0 l 56 151 m -108 -47 l 90 0"
   "b" "M 87 36 v 155 m 0 0 l 0 -64 a 42 42 0 1 1 0 48"
   "B" "M 71 36 l -1 154 m 1 -154 l 59 0 a 32 30 0 0 1 0 75 l -59 0 l 59 0 a 36 30 0 0 1 3 79 l -63 0"
   "c" "M 162 129 a 41 42 0 1 0 1 45"
   "C" "M 184 52 a 73 80 0 1 0 -2 127"
   "d" "M 163 127 a 41 42 0 1 0 0 48 l 0 -141 m 0 0 v 159"
   "D" "M 63 36 l -1 153 m 1 -153 h 57 a 26 30 0 0 1 0 153 l -58 0"
   "e" "M 86 149 h 78 m 0 0 a 32 32 0 1 0 -78 0 q 0 33 33 41 q 26 4 46 -10"
   "E" "M 83 36 v 154 m 0 -154 h 86 m -86 75 h 72 m -72 79 h 86"
   "f" "M 159 54 q 0 -18 -17 -18 t -17 18 v 138 m -26 -81 h 53"
   "F" "M 86 36 v 154 m 0 -154 h 90 m -90 77 h 79"
   "g" "M 163 129 a 41 40 0 1 0 0 43 l 0 -62 m 0 0 v 119 q -1 35 -35 35 t -38 -25"
   "G" "M 179 51 q -19 -17 -48 -17 q -77 0 -77 79 q 0 79 75 79 q 67 0 67 -67 m 0 0 h -53"
   "h" "M 87 35 v 156 m 0 0 l 0 -64 q 12 -18 37 -18 q 38 0 38 36 v 46"
   "H" "M 67 34 v 158 m 0 -79 h 116 m 0 -79 v 158"
   "i" "M 125 109 v 83 m 0 -122 l 0 -1"
   "I" "M 125 35 v 155 m -32 -155 h 63 m -63 155 h 63"
   "j" "M 126 111 v 121 q 0 31 -27 31 q -25 0 -29 -23 m 56 -169 l 0 -1"
   "J" "M 160 35 v 121 q 0 36 -35 36 q -25 0 -39 -19"
   "k" "M 102 33 v 159 m 49 -85 l -49 38 m 0 0 l 48 47"
   "K" "M 79 33 v 159 m 87 -159 l -87 89 m 0 0 l 93 70"
   "l" "M 125 34 v 157"
   "L" "M 83 34 v 157 m 0 0 h 86"
   "m" "M 71 105 v 86 m 0 0 l 0 -58 c 0 -11 13 -24 27 -22 c 14 2 23 10 27 18 v 62 m 0 0 l 0 -59 c 0 -10 10 -23 27 -22 c 17 2 27 12 27 25 v 56"
   "M" "M 54 191 v -156 m 0 0 l 71 103 m 0 0 l 71 -103 m 0 0 v 156"
   "n" "M 92 107 v 86 m 0 0 l 0 -64 q 13 -19 33 -19 q 33 0 33 33 v 50"
   "N" "M 63 192 v -157 m 0 0 l 123 157 m 0 0 v -157"
   "o" "M 125 110 a 41 41 0 1 0 1 0 z"
   "O" "M 125 32 a 75 80 0 1 0 1 0 z"
   "p" "M 87 109 v 155 m 0 0 l 0 -137 a 42 41 0 1 1 0 47"
   "P" "M 76 35 v 155 m 0 -155 h 55 a 43 43 0 0 1 0 86 H 76"
   "q" "M 160 131 a 40 40 0 1 0 0 39 v -62 m 0 0 v 146 q 0 10 11 10 q 6 0 12 -10"
   "Q" "M 125 32 a 75 80 0 1 0 1 0 z m 7 111 l 53 72"
   "r" "M 100 107 v 86 M 100 193 l 0 -67 a 32 25 0 0 1 58 -2"
   "R" "M 73 36 v 155 m 0 -155 h 59 a 1 1 0 0 1 0 86 h -59 m 62 0 l 39 69"
   "s" "M 146 126 c 1.1 -9.9 -11 -15.4 -19.8 -15.4 a 24.2 24.2 90 0 0 -17.6 6.6 a 17.6 17.6 90 0 0 -4.4 17.6 c 2.2 5.5 6.6 9.9 12.1 12.1 c 7.7 3.3 15.4 4.4 22 8.8 c 7.7 4.4 12.1 13.2 8.8 22 c -5.5 15.4 -30.8 19.8 -41.8 6.6 a 20.9 20.9 90 0 1 -4.4 -11"
   "S" "M 170 50 c -16 -15 -22 -16 -41 -17 a 48 48 90 0 0 -38 16 a 35 35 90 0 0 -9 35 c 4 11 13 20 26 24 c 15 7 31 9 47 17 c 15 9 24 26 15 46 c -9 21 -62 40 -96 -3"
   "t" "M 126 33 v 158 m -28 -80 h 56"
   "T" "M 129 35 v 157 m -52 -157 h 104"
   "u" "M 91 109 v 52 a 23 23 90 0 0 67 0 v -52 m 0 0 v 85"
   "U" "M 67 36 v 96 a 40.25 40.25 90 0 0 117.25 0 v -96"
   "v" "M 89 110 l 36 80 m 0 0 l 36 -80"
   "V" "M 61 35 l 64 155 m 0 0 l 64 -155"
   "w" "M 83 109 l 21 82 m 0 0 l 21 -82 m 0 0 l 21 82 m 0 0 l 21 -82"
   "W" "M 23 35 l 49 155 m 0 0 l 52 -122 m 0 0 l 52 122 m 0 0 l 49 -155"
   "x" "M 91 110 l 68 82 m 0 -82 l -68 82"
   "X" "M 73 35 l 106 155 m 0 -155 l -106 155"
   "y" "M 86 110 l 40 82 m 37 -82 l -67 154"
   "Y" "M 68 35 l 58 81 m 0 0 l 58 -81 m -58 81 v 76"
   "z" "M 90 111 h 65 m 0 0 l -66 80 m 0 0 h 72"
   "Z" "M 75 35 h 103 m 0 0 l -112 157 m 0 0 h 116"
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
