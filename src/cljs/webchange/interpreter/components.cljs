(ns webchange.interpreter.components
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.subs :as subs]
    [webchange.events :as events]
    [webchange.common.kimage :refer [kimage]]
    [webchange.common.painting-area :refer [painting-area]]
    [webchange.common.colors-palette :refer [colors-palette]]
    [webchange.common.animated-svg-path :refer [animated-svg-path]]
    [webchange.common.anim :refer [anim]]
    [webchange.common.text :refer [chunked-text]]
    [webchange.common.carousel :refer [carousel]]
    [webchange.common.slider :refer [slider]]
    [webchange.interpreter.components.video :refer [video]]
    [webchange.interpreter.core :refer [get-data-as-url]]
    [webchange.interpreter.events :as ie]
    [webchange.interpreter.variables.subs :as vars.subs]
    [webchange.interpreter.variables.events :as vars.events]
    [webchange.common.events :as ce]
    [webchange.interpreter.executor :as e]
    [webchange.common.core :refer [prepare-colors-palette-params
                                   prepare-group-params
                                   prepare-painting-area-params
                                   prepare-animated-svg-path-params
                                   with-origin-offset]]

    [react-konva :refer [Stage Layer Group Rect Text Custom]]
    [konva :as k]))

(defn get-viewbox
  [viewport]
  (let [width 1920
        height 1080
        original-ratio (/ width height)
        window-ratio (/ (:width viewport) (:height viewport))]
    #_{:width width :height height}
    (if (< original-ratio window-ratio)
      {:width width :height (Math/round (* height (/ original-ratio window-ratio)))}
      {:width (Math/round (* width (/ original-ratio window-ratio))) :height height})))

(defn compute-x
  [viewbox]
  (let [width 1920]
    (/ (- width (:width viewbox)) 4)))

(defn compute-y
  [viewbox]
  (let [height 1080]
    (/ (- height (:height viewbox)) 2)))

(defn compute-scale [viewport]
  (let [width 1920
        height 1080
        original-ratio (/ width height)
        window-ratio (/ (:width viewport) (:height viewport))]
    (if (> original-ratio window-ratio)
      (/ (:height viewport) height)
      (/ (:width viewport) width))))

(defn do-start
  []
  (e/init)
  (re-frame/dispatch [::ie/start-playing]))

(defn top-left
  []
  (let [viewport (re-frame/subscribe [::subs/viewport])
        viewbox (get-viewbox @viewport)
        scale (/ (:width viewbox) (:width @viewport))
        x (Math/round (* (compute-x viewbox) scale))
        y (Math/round (* (compute-y viewbox) scale))]
    {:x x :y y}))

(defn top-right
  []
  (let [viewport (re-frame/subscribe [::subs/viewport])
        viewbox (get-viewbox @viewport)
        scale (/ (:width viewbox) (:width @viewport))
        x (+ (Math/round (* (* (compute-x viewbox) scale) 4)) (:width viewbox))
        y (Math/round (* (compute-y viewbox) scale))]
    {:x x :y y}))

(defn settings
  []
  (let [top-right (top-right)
        ui-screen @(re-frame/subscribe [::subs/ui-screen])]
    (when (= ui-screen :settings)
      [:> Group
       [kimage "/raw/img/bg.jpg"]

       [:> Group top-right
        [kimage (get-data-as-url "/raw/img/ui/close_button_01.png")
         {:x (- 108) :y 20
          :on-click #(re-frame/dispatch [::ie/close-settings])
          :on-tap #(re-frame/dispatch [::ie/close-settings])}]]

       [kimage (get-data-as-url "/raw/img/ui/settings/settings.png") {:x 779 :y 345}]
       [kimage (get-data-as-url "/raw/img/ui/settings/music_icon.png") {:x 675 :y 516}]
       [kimage (get-data-as-url "/raw/img/ui/settings/music.png") {:x 763 :y 528}]
       [kimage (get-data-as-url "/raw/img/ui/settings/sound_fx_icon.png") {:x 581 :y 647}]
       [kimage (get-data-as-url "/raw/img/ui/settings/sound_fx.png") {:x 669 :y 645}]

       [slider {:x 979 :y 556 :width 352 :height 24 :event ::ie/set-music-volume :sub ::subs/get-music-volume}]
       [slider {:x 979 :y 672 :width 352 :height 24 :event ::ie/set-effects-volume :sub ::subs/get-effects-volume}]

       ])
    )
  )

(defn star
  [result idx]
  (let [full (* idx 2)
        half (- (* idx 2) 1)]
    (cond
      (>= result full) "/raw/img/ui/star_03.png"
      (>= result half) "/raw/img/ui/star_02.png"
      :else "/raw/img/ui/star_01.png")))

(defn score-screen
  []
  (let [top-right (top-right)
        scene-id (re-frame/subscribe [::subs/current-scene])
        successes (re-frame/subscribe [::vars.subs/variable @scene-id "successes"])
        fails (re-frame/subscribe [::vars.subs/variable @scene-id "fails"])
        result (* (/ @successes (+ @successes @fails)) 10)]
    [:> Group
     [kimage "/raw/img/bg.jpg"]

     [:> Group top-right
      [kimage (get-data-as-url "/raw/img/ui/close_button_01.png")
       {:x (- 108) :y 20
        :on-click #(re-frame/dispatch [::ie/next-scene])
        :on-tap #(re-frame/dispatch [::ie/next-scene])}]]

     [kimage (get-data-as-url "/raw/img/ui/form.png") {:x 639 :y 155}]
     [kimage (get-data-as-url "/raw/img/ui/clear.png") {:x 829 :y 245}]

     [kimage (get-data-as-url (star result 1)) {:x 758 :y 363}]
     [kimage (get-data-as-url (star result 2)) {:x 836 :y 363}]
     [kimage (get-data-as-url (star result 3)) {:x 913 :y 363}]
     [kimage (get-data-as-url (star result 4)) {:x 991 :y 363}]
     [kimage (get-data-as-url (star result 5)) {:x 1068 :y 363}]

     [:> Text {:x 880 :y 490 :text (str (Math/floor result) "/10")
               :font-size 80 :font-family "Luckiest Guy" :fill "white"
               :shadow-color "#1a1a1a" :shadow-offset {:x 5 :y 5} :shadow-blur 5 :shadow-opacity 0.5}]

     [kimage (get-data-as-url "/raw/img/ui/vera.png") {:x 851 :y 581}]
     [kimage (get-data-as-url "/raw/img/ui/reload_button_01.png") {:x 679 :y 857
                                                                   :on-click #(re-frame/dispatch [::ie/restart-scene])
                                                                   :on-tap #(re-frame/dispatch [::ie/restart-scene])}]
     [kimage (get-data-as-url "/raw/img/ui/next_button_01.png") {:x 796 :y 857
                                                                 :on-click #(re-frame/dispatch [::ie/next-scene])
                                                                 :on-tap #(re-frame/dispatch [::ie/next-scene])}]

     ]))

(defn score
  []
  (let [scene-id (re-frame/subscribe [::subs/current-scene])
        score-var (re-frame/subscribe [::vars.subs/variable @scene-id "score"])]
    (if (:visible @score-var)
      [score-screen])))

(defn preloader
  []
  (let [scene-id (re-frame/subscribe [::subs/current-scene])
        progress (re-frame/subscribe [::subs/scene-loading-progress @scene-id])
        loaded (re-frame/subscribe [::subs/scene-loading-complete @scene-id])]
    [:> Group
     [kimage "/raw/img/bg.jpg"]


     [:> Group {:x 628 :y 294}
      [kimage "/raw/img/ui/logo.png"]]
     [:> Group {:x 729 :y 750}
      [:> Rect {:x 1 :width 460 :height 24 :fill "#ffffff" :corner-radius 25}]
      [:> Group {:clip-x 0 :clip-y 0 :clip-width (+ 0.1 (* @progress 4.62)) :clip-height 24}
        [:> Rect {:width 462 :height 24 :fill "#2c9600" :corner-radius 25}]]
      ]
     (if @loaded
       [:> Group {:x 779 :y 863 :on-click do-start :on-tap do-start}
        [kimage "/raw/img/ui/play_button_01.png"]])
     ]))

(defn course-loading-screen []
  [:> Group
   [kimage "/raw/img/bg.jpg"]

   [:> Group {:x 628 :y 294}
    [kimage "/raw/img/ui/logo.png"]]
   ])

(defn close-button
  [x y]
  [:> Group {:x x :y y
             :on-click #(re-frame/dispatch [::ie/open-student-dashboard])
             :on-tap #(re-frame/dispatch [::ie/open-student-dashboard])}
   [kimage (get-data-as-url "/raw/img/ui/close_button_01.png")]])

(defn back-button
  [x y]
  [:> Group {:x x :y y
             :on-click #(re-frame/dispatch [::ie/close-scene])
             :on-tap #(re-frame/dispatch [::ie/close-scene])}
   [kimage (get-data-as-url "/raw/img/ui/back_button_01.png")]])

(defn settings-button
  [x y]
  [:> Group {:x x :y y
             :on-click #(re-frame/dispatch [::ie/open-settings])
             :on-tap #(re-frame/dispatch [::ie/open-settings])}
   [kimage (get-data-as-url "/raw/img/ui/settings_button_01.png")]])

(defn menu
  []
  (let [top-right (top-right)
        top-left (top-left)]
    [:> Group {}
     [:> Group top-left
      [back-button 20 20]]
     [:> Group top-right
      [settings-button (- 216) 20]
      [close-button (- 108) 20]]
     ]

    ))

(defn scene-started
  [scene-data]
  (let [scene-started (re-frame/subscribe [::subs/scene-started])]
    (or
      @scene-started
      (get-in scene-data [:metadata :autostart] false))))

(defn scene-ready
  [scene-id]
  (let [scene-data (re-frame/subscribe [::subs/scene scene-id])
        loaded (re-frame/subscribe [::subs/scene-loading-complete scene-id])
        course-started (re-frame/subscribe [::subs/playing])]
    (and @loaded @course-started (scene-started @scene-data))))

(declare group)
(declare placeholder)
(declare image)
(declare animation)
(declare text)
(declare carousel-object)
(declare get-painting-area)
(declare get-colors-palette)

(defn draw-object
  [scene-id name]
  (let [o @(re-frame/subscribe [::subs/scene-object-with-var scene-id name])
        type (keyword (:type o))]
    (case type
      :background [kimage (get-data-as-url (:src o))]
      :image [image scene-id name o]
      :transparent [:> Group (prepare-group-params o)
                                 [:> Rect {:x 0 :width (:width o) :height (:height o)}]]
      :group [group scene-id name o]
      :placeholder [placeholder scene-id name o]
      :animation [animation scene-id name o]
      :text [text scene-id name o]
      :carousel [carousel-object scene-id name o]
      :painting-area (get-painting-area scene-id name o)
      :colors-palette (get-colors-palette scene-id name o)
      :video [video o]
      :animated-svg-path [animated-svg-path (prepare-animated-svg-path-params o)]
      (throw (js/Error. (str "Object with type " type " can not be drawn because it is not defined"))))))

(defn placeholder
  [scene-id name {item :var :as object}]
  [image scene-id name (cond-> object
                               :always (assoc :type "image")
                               (contains? object :image-src) (assoc :src (get item (-> object :image-src keyword)))
                               (contains? object :image-width) (assoc :width (get item (-> object :image-width keyword)))
                               (contains? object :image-height) (assoc :height (get item (-> object :image-height keyword))))])

(defn text
  [scene-id name object]
  [:> Group (prepare-group-params object)
    (if (:chunks object)
      (let [on-mount #(re-frame/dispatch [::ie/register-text %])]
        [chunked-text scene-id name (assoc object :on-mount on-mount)])
      [:> Text (dissoc object :x :y)])])

(defn get-painting-area
  [_ _ params]
  [painting-area (prepare-painting-area-params params)])

(defn get-colors-palette
  [_ _ params]
  [colors-palette (prepare-colors-palette-params params)])

(defn group
  [scene-id name object]
  [:> Group (prepare-group-params object)
   (for [child (:children object)]
     ^{:key (str scene-id child)} [draw-object scene-id child])])

(defn filter-param [{:keys [filter]}]
  (case filter
    "grayscale" {:filters [k/Filters.Grayscale]}
    {:filters nil}))

(defn image
  [scene-id name object]
  [:> Group (prepare-group-params object)
   [kimage (get-data-as-url (:src object)) (filter-param object)]])

(defn animation
  [scene-id name object]
  (let [params (prepare-group-params object)
        animation-name (or (:scene-name object) (:name object))]
    [:> Group params
     [anim (-> object
               (assoc :on-mount #(re-frame/dispatch [::ie/register-animation animation-name %])))]
     [:> Rect (-> {:width (:width params)
                   :height (:height params)
                   :opacity 0
                   :origin {:type "center-top"}
                   :scale-y -1}
                  with-origin-offset)]]))

(defn carousel-object [scene-id name object]
  [:> Group (prepare-group-params object)
   [carousel object]])

(defn navigation-helper []
  (let [position @(re-frame/subscribe [::subs/navigation])]
    (when position
      [:> Group (select-keys position [:x :y])
       [kimage (get-data-as-url "/raw/img/ui/hand.png")]])))

(defn triggers
  [scene-id]
  (let [status (re-frame/subscribe [::vars.subs/variable scene-id "status"])]
    (if (not= @status :running)
      (do
        (re-frame/dispatch [::vars.events/execute-set-variable {:var-name "status" :var-value :running}])
        (re-frame/dispatch [::ie/trigger :start])))))

(defn scene
  [scene-id]
  (let [scene-objects (re-frame/subscribe [::subs/scene-objects scene-id])]
    [:> Group
      (for [layer @scene-objects
            name layer]
        ^{:key (str scene-id name)} [draw-object scene-id name])
     [score]
     [menu]
     [navigation-helper]
     [triggers scene-id]
     ]))

(defn current-scene
  []
  (let [scene-id (re-frame/subscribe [::subs/current-scene])
        scene-ready (scene-ready @scene-id)]
    (if scene-ready
      [scene @scene-id]
      [preloader])
    ))

(defn course
  [course-id]
  (fn [course-id]
    (let [viewport (re-frame/subscribe [::subs/viewport])
          viewbox (get-viewbox @viewport)
          ui-screen @(re-frame/subscribe [::subs/ui-screen])]
      [:div
       [:style "html, body {margin: 0; max-width: 100%; overflow: hidden;}"]
       [:> Stage {:width (:width @viewport) :height (:height @viewport) :x (compute-x viewbox) :y (- (compute-y viewbox))
                  :scale-x (compute-scale @viewport) :scale-y (compute-scale @viewport)}
        [:> Layer
         (case ui-screen
           :course-loading [course-loading-screen]
           [current-scene]
           )
         [settings]]]])))