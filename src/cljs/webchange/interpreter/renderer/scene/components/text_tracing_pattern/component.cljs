(ns webchange.interpreter.renderer.scene.components.text-tracing-pattern.component
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.pixi :refer [Container]]
    [webchange.interpreter.renderer.scene.components.text-tracing-pattern.wrapper :refer [wrap]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.scene.components.svg-path.component :as s]
    [webchange.interpreter.renderer.scene.components.text-tracing-pattern.utils :refer [set-enable!]]
    [webchange.interpreter.renderer.scene.components.animated-svg-path.component :as a]
    [webchange.interpreter.renderer.scene.components.animated-svg-path.tracing :as tracing]
    [webchange.interpreter.renderer.scene.components.image.component :as i]
    [webchange.interpreter.renderer.scene.components.svg-path :refer [get-svg-path]]
    [webchange.interpreter.renderer.state.scene :as state]
    [webchange.interpreter.renderer.scene.filters.filters :refer [apply-filters]]
    [webchange.logger.index :as logger]))

(def default-props {:x                        {:default 0}
                    :y                        {:default 300}
                    :width                    {:default 1920}
                    :height                   {:default 680}
                    :x-offset                 {:default nil}
                    :spacing                  {:default 20}
                    :dashed                   {:default false}
                    :show-lines               {:default true}
                    :text                     {}
                    :name                     {}
                    :on-change                {}
                    :traceable                {}
                    :repeat-text              {}
                    :on-next-letter-activated {}
                    :on-finish                {}
                    :on-click                 {}
                    :debug                    {:default false}
                    :enable?                  {:default true}
                    :filters                  {:default []}})

(def base-height 275)
(def midline-offset 76)
(def baseline-offset 162)

(def base-width 225)
(def line-height 10)
(def letter-offset-y -26)

(def safe-padding 60)

(def line-props {:type         "svg-path"
                 :x            0,
                 :width        1920,
                 :height       10,
                 :data         "M0,5h1920",
                 :scale        {:x 1 :y 1}
                 :stroke-width 4})

(defn- create-container
  [{:keys [x y filters]}]
  (doto (Container.)
    (utils/set-position {:x x :y y})
    (apply-filters filters)))

(defn- path->letter
  [scale path dashed]
  {:type         "svg-path",
   :dash         (if dashed [7 7] [])
   :width        base-width
   :height       base-height
   :data         path,
   :scale        {:x scale :y scale}
   :line-cap     "round",
   :stroke       "#898989",
   :stroke-width (if dashed 4 12)
   :filters [{:name "brightness" :value 0}
             {:name "glow" :outer-strength 0 :color 0xffd700}]})

(defn- path->animated-letter
  [letter-scale path]
    {:type         "animated-svg-path",
     :duration     1000
     :width        base-width
     :height       base-height
     :path         path,
     :traceable    true
     :scale        {:x letter-scale :y letter-scale}
     :line-cap     "round",
     :stroke       "#323232",
     :stroke-width 12})

(defn get-path-width [data]
  (let [svg-node (.createElementNS js/document "http://www.w3.org/2000/svg" "svg")
        path-node (.createElementNS js/document "http://www.w3.org/2000/svg" "path")
        body (.-body js/document)]
    (.setAttribute path-node "d" data)
    (.appendChild svg-node path-node)
    (.appendChild body svg-node)
    (let [width (.-width (.getBBox svg-node))]
      (.removeChild body svg-node)
      width)))

(defn accumulate [list]
  (let [rec (fn [acc lst]
              (if (empty? lst)
                acc
                (recur (conj acc (+ (last acc) (first lst)))
                       (rest lst))))]
    (rec [0] list)))

(defn- draw-pattern!
  [group {:keys [width text repeat-text x-offset spacing dashed]} {letter-scale :letter} topline-y]
  (let [text (if repeat-text (apply str (repeat repeat-text text)) text)
        length (count text)
        widths (accumulate (map #(* letter-scale (+ spacing (get-path-width (get-svg-path %)))) text))
        total (- (last widths) spacing)
        to-offset "FfiíU"
        positions (->> (range length)
                       (map (fn [pos]
                              {:x (int (+ (nth widths pos) (or x-offset (/ (- width total) 2))))
                               :y topline-y
                               :dash-offset (if (some #{(nth text pos)} to-offset) 4 0)
                               :index pos})))
        letters (->> text
                     (map #(get-svg-path % {:trace? false}))
                     (map #(path->letter letter-scale % dashed))
                     (map merge positions))]
    (doall
      (for [letter letters]
        (let [component (s/create (assoc letter
                                         :object-name (keyword (str "text-tracing-pattern-" (:index letter)))
                                         :parent group))]
          (re-frame/dispatch [::state/register-object component]))))))

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
  [group {:keys [width text repeat-text x-offset spacing] :as props} {letter-scale :letter} topline-y state]
  (let [text (if repeat-text (apply str (repeat repeat-text text)) text)
        length (count text)
        widths (accumulate (map #(* letter-scale (+ spacing (get-path-width (get-svg-path %)))) text))
        total (- (last widths) spacing)
        positions (->> (range length)
                       (map (fn [pos]
                              {:x (int (+ (nth widths pos) (or x-offset (/ (- width total) 2))))
                               :y topline-y})))

        letters (->> text
                     (map #(get-svg-path % {:trace? true}))
                     (map #(path->animated-letter letter-scale %))
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

    (reset! tracing/hint {:arrow (i/create {:type "image"
                                            :x -100
                                            :y 100
                                            :width 100
                                            :height 100
                                            :src "/raw/clipart/writing/arrow.png"
                                            :origin {:type "center-center"}
                                            :object-name "text-tracing-pattern-arrow"
                                            :parent group})
                          :dot (i/create {:type "image"
                                          :x -100
                                          :y 100
                                          :width 100
                                          :height 100
                                          :src "/raw/clipart/writing/dot.png"
                                          :origin {:type "center-center"}
                                          :object-name "text-tracing-pattern-dot"
                                          :parent group})})
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

(defn- reset-group!
  [group]
  (let [children (.removeChildren group)]
    (doall
      (for [child children]
        (.destroy child)))))

(defn draw!
  [group {:keys [traceable height text show-lines dashed] :as props} state]
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
    (when show-lines
      (s/create (merge line-props {:object-name (str "text-tracint-pattern-topline")
                                   :parent      group
                                   :y           (+ topline-y (if dashed 0 -5))
                                   :stroke      "#323232"}))
      (s/create (merge line-props {:object-name (str "text-tracint-pattern-midline")
                                   :parent      group
                                   :y           (+ midline-y (if dashed 0 -5))
                                   :dash        [7 7]
                                   :stroke      "#898989"}))
      (s/create (merge line-props {:object-name (str "text-tracint-pattern-baseline")
                                   :parent      group
                                   :y           (+ baseline-y (if dashed 0 10))
                                   :stroke      "#323232"})))

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
