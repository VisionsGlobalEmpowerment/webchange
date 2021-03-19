(ns webchange.interpreter.renderer.scene.components.text-tracing-pattern.component
  (:require
    [webchange.interpreter.pixi :refer [Container Graphics string2hex]]
    [webchange.interpreter.renderer.scene.components.text-tracing-pattern.wrapper :refer [wrap]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.scene.components.image.component :as image]
    [webchange.interpreter.renderer.scene.components.svg-path.component :as a]
    [webchange.logger.index :as logger]
    ))

(def default-props {:x         {:default 0}
                    :y         {:default 300}
                    :width     {:default 1920}
                    :height    {:default 680}
                    :text      {}
                    :name      {}
                    :on-change {}
                    :scale     {:default {:x 1 :y 1}}
                    :debug     {:default false}})

(def base-height 150)
(def base-mid-height 74)
(def base-width 150)
(def line-height 10)

(def alphabet-path {"a" "M144.76,92.43a37.5,37.5,0,1,0,0,39.28m0-57.21v75"
                    "b" "M77.77 0.71v149M77.77,92.64a37.5,37.5,0,1,1,0,39.28"
                    "c" "M147.23,92.35a37.51,37.51,0,1,0,0,39.27"
                    "d" "M147.23,91.88a37.5,37.5,0,1,0,0,39.28M147.23,0v150"
                    "e" "M77,112h73a37.5,37.5,0,1,0-11.21,26.74"
                    "f" "M156.12,13.21A36.35,36.35,0,0,0,128.36.29h0A36.49,36.49,0,0,0,92,36.68V149M68.88,74.78H116"
                    "g" "M147.23,92.62a37.5,37.5,0,1,0,0,39.28M147.23,74.86V181a34.73,34.73,0,0,1-67.65,11.09"
                    "h" "M77.77 0.71v149M77.77,92.64a37.51,37.51,0,0,1,69.46,19.64v37.5"
                    "i" "M101.7,74.26v75M107.7,34.78a6,6,0,1,1-6-6A6,6,0,0,1,107.7,34.78Z"
                    "j" "M106.5,74V180.1a34.73,34.73,0,0,1-67.64,11.09M112.5,34.21a6,6,0,1,1-6-6A6,6,0,0,1,112.5,34.21Z"
                    "k" "M112.5-0.22v150M162.63,74.78l-48,29.06l61.55,45.94"
                    "l" "M112.5 0.19v150"
                    "m" "M71.86,75v75M71.85,91.79C79.19,80,93.46,76.45,101.17,78.27c11.89,2.8,19.93,12.51,19.93,23.27V150M119.85,91.79c7.34,-11.77,21.61,-15.34,29.32,-13.52c11.89,2.8,19.93,12.51,19.93,23.27V150"
                    "n" "M82.05,74.42v75M82.05,91.55A32.35,32.35,0,0,1,143,106.77v42.65"
                    "o" "M110.65,74a37.5,37.5,0,1,0,37.5,37.5A37.5,37.5,0,0,0,110.65,74Z"
                    "p" "M77.77,74.37v149M77.77,93.1a37.5,37.5,0,1,1,0,39.28"
                    "q" "M148.58,92.61a37.5,37.5,0,1,0,0,39.27M148.58,74.37v149"
                    "r" "M85.86,75.66v75M85.85,92.79a32.37,32.37,0,0,1,53.3-5.63"
                    "s" "M131.91,90.75c1.33-9.39-10.49-14.17-18.28-14.39A21.71,21.71,0,0,0,97.69,82.7a16.08,16.08,0,0,0-3.89,16c1.65,5.12,6.13,8.58,10.93,10.57c6.67,2.77,13.9,4,20.26,7.62c7,3.94,10.77,12.34,8,20.14c-5.06,14.06-27.62,17.67-37.73,5.77a18.91,18.91,0,0,1-4.11-10.2"
                    "t" "M112.5,35.29v115M89.4,75.29h46.2"
                    "u" "M82.05,75v42.65A32.35,32.35,0,0,0,143,132.87M143,74.38V149.6"
                    "v" "M79.56,75l32.88,75M112.56,150l32.88-75"
                    "w" "M73.71,75.66l18.58,75M93.71,150.66l18.58-75M112.71,75.66l18.58,75M132.71,150.66l18.58-75"
                    "x" "M81.53,75.66l61.94,75M143.47,75.66l-61.94,75"
                    "y" "M78.19,74.78l34.84,75M146.81,74.78,96.61,188.91"
                    "z" "M79.86,75.66h61.29l-57.29,75h61.28"
                    "ñ" "M82,74.83v75M82,92a32.36,32.36,0,0,1,60.9,15.23v42.65M86,48.06s4.12-11.83,13.63-7.88c3.71,1.54,7.49,2.94,11.29,4.27c10.27,3.6,22.52,2.17,28-8.67"})

(defn- create-container
  [{:keys [x y scale]}]
  (doto (Container.)
    (utils/set-position {:x x :y y})
    (utils/set-scale scale)))

#_(defn- activate
  [state active-tool]
  (doall
    (for [[tool-name {:keys [set-position]}] (:tools @state)]
      (if (= active-tool tool-name)
        (set-position (get-in tools-definitions [tool-name :active]))
        (set-position (get-in tools-definitions [tool-name :inactive]))))))

#_(defn- create-tool!
  [group type state {:keys [on-change object-name]}]
  (let [defaults (get-in tools-definitions [type :defaults])
        on-click (fn []
                   (activate state type)
                   (when on-change
                     (on-change {:tool (name type)})))
        tool (image/create (assoc defaults
                             :filters [{:name  "brightness"
                                        :value 0}]
                             :object-name (str object-name "-tool-" (name type))
                             :transition (str object-name "-tool-" (name type))
                             :parent group
                             :on-click on-click))]
    (swap! state assoc-in [:tools type] tool)))

(defn- path->letter
  [letter-width letter-height scale path]
  #_{:type         "animated-svg-path",
   :fill         "transparent",
   :line-cap     "round",
   :path         path,
   :stroke       "#323232",
   :stroke-width 10}

  {:type         "svg-path",
   :dash         [7 7],
   :width letter-width
   :height letter-height
   :data         path,
   :scale {:x scale :y scale}
   :line-cap     "round",
   :stroke       "#898989",
   :stroke-width 4})

(defn draw!
  [group {:keys [text traceable width height] :as props}]
  (let [length (count text)
        base-total-width (* length base-width)
        scale (min (/ width base-total-width) (-> height (- (* 2 line-height)) (/ base-height)))
        letter-height (* scale base-height)
        letter-width (* scale base-width)
        topline-y (-> (- height letter-height) (/ 2))
        midline-y (-> topline-y (+ line-height) (+ (* base-height scale)))
        baseline-y (-> topline-y (+ (* base-mid-height scale)))
        positions (->> (range length)
                       (map (fn [pos]
                              {:x (+ (* 20 pos) (* letter-width pos))
                               :y topline-y})))
        letters (->> text
                     (map alphabet-path)
                     (map #(path->letter letter-width letter-height scale %))
                     (map merge positions))]
    (logger/trace "draw! text-tracing-pattern: letter width - height" letter-width letter-height)
    (doall
      (for [letter letters]
        (a/create (assoc letter
                    :object-name (str "text-tracing-patter-" (:x letter))
                    :parent group))))))

(def component-type "text-tracing-pattern")

(defn create
  "Create `text-tracing-pattern` component.

  Props params:
  :x - component x-position.
  :y - component y-position.
  :scale - image scale. Default: {:x 1 :y 1}.
  :name - component name that will be set to sprite and container with corresponding suffixes.
  :on-change - on change event handler."
  [{:keys [parent type object-name traceable] :as props}]
  (let [group (create-container props)
        state (atom {})
        wrapped-group (wrap type object-name group state)]
    (logger/trace-folded "Create text-tracing-pattern" props)

    (comment
      (create-tool! group :brush state props)
      (create-tool! group :felt-tip state props)
      (create-tool! group :pencil state props)
      (create-tool! group :eraser state props))

    (draw! group props)
    (.addChild parent group)

    wrapped-group))
