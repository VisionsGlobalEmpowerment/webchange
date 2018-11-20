(ns webchange.interpreter.components
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.subs :as subs]
    [webchange.events :as events]
    [webchange.common.kimage :refer [kimage]]
    [webchange.interpreter.core :refer [get-data-as-url]]
    [webchange.interpreter.events :as ie]
    [webchange.interpreter.executor :as e]
    [react-konva :refer [Stage Layer Group Rect]]
    ))

(defn get-viewbox
  [viewport]
  (let [width 1920
        height 1080
        original-ratio (/ width height)
        window-ratio (/ (:width viewport) (:height viewport))]
    (if (< original-ratio window-ratio)
      {:width width :height (Math/round (* height (/ original-ratio window-ratio)))}
      {:width (Math/round (* width (/ original-ratio window-ratio))) :height height})))

(defn compute-x
  [viewbox]
  (let [width 1920]
    (/ (- width (:width viewbox)) 2)))

(defn compute-y
  [viewbox]
  (let [height 1080]
    (/ (- height (:height viewbox)) 2)))

(defn do-start
  []
  (e/init)
  (re-frame/dispatch [::events/start-playing]))

(defn top-right
  []
  (let [viewport (re-frame/subscribe [::subs/viewport])
        viewbox (get-viewbox @viewport)
        scale (/ (:width viewbox) (:width @viewport))
        x (+ (Math/round (* (compute-x viewbox) scale)) (:width viewbox))
        y (Math/round (* (compute-y viewbox) scale))]
    {:x x :y y}))

(defn relative-click-x
  [e]
  (let [mouse-x (-> e .-evt .-x)
        group-x (-> e .-target .-parent .getAbsolutePosition .-x)
        x (- mouse-x group-x)]
    x))

(defn normalize-slider
  [value max]
  (let [bounded (cond
                  (< value 0) 0
                  (> value max) max
                  :default value)]
    (Math/round (/ bounded (/ max 100)))))

(defn set-volume-from-event
  [event max]
  (fn [e]
    (let [x (relative-click-x e)]
      (re-frame/dispatch [event (normalize-slider x max)]))))

(defn slider
  [{:keys [x y width height event sub]}]
  (let [value (re-frame/subscribe [sub])
        coef (/ width 100)]
    [:> Group {:x x :y y}
     [:> Rect {:x 1 :width (- width 2) :height height :fill "#ffffff" :corner-radius 25}]
     [:> Group {:clip-x 0 :clip-y 0 :clip-width (+ 0.1 (* @value coef)) :clip-height height}
      [:> Rect {:width width :height height :fill "#2c9600" :corner-radius 25}]]
     (let [this (r/current-component)]
       [:> Rect {:width width :height height :opacity 0
                 :draggable true :drag-distance 0 :drag-bound-func (fn [pos] (this-as that #js {:x (.-x pos) :y (-> that .getAbsolutePosition .-y)}))
                 :on-mouse-down (set-volume-from-event event width)
                 :on-drag-move (set-volume-from-event event width)
                 :on-drag-end (fn [e]
                                (-> e .-target (.position #js {:x 0 :y 0}))
                                (r/force-update this))}]
       )]))

(defn settings
  []
  (let [top-right (top-right)]
    [:> Group
     [kimage "/raw/img/bg.jpg"]

     [:> Group top-right
      [kimage (get-data-as-url "/raw/img/ui/close_button_01.png")
       {:x (- 108) :y 20 :on-click #(re-frame/dispatch [::events/close-settings])}]]

     [kimage "/raw/img/ui/settings/settings.png" {:x 779 :y 345}]
     [kimage "/raw/img/ui/settings/music_icon.png" {:x 675 :y 516}]
     [kimage "/raw/img/ui/settings/music.png" {:x 763 :y 528}]
     [kimage "/raw/img/ui/settings/sound_fx_icon.png" {:x 581 :y 647}]
     [kimage "/raw/img/ui/settings/sound_fx.png" {:x 669 :y 645}]

     [slider {:x 979 :y 556 :width 352 :height 24 :event ::ie/set-music-volume :sub ::subs/get-music-volume}]
     [slider {:x 979 :y 672 :width 352 :height 24 :event ::ie/set-effects-volume :sub ::subs/get-effects-volume}]

     ]
    )
  )

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
       [:> Group {:x 779 :y 863 :on-click do-start}
        [kimage "/raw/img/ui/play_button_01.png"]]
       )
     ]))

(defn close-button
  [x y]
  [:> Group {:x x :y y} [kimage (get-data-as-url "/raw/img/ui/close_button_01.png")]])

(defn settings-button
  [x y]
  [:> Group {:x x :y y :on-click #(re-frame/dispatch [::events/open-settings])} [kimage (get-data-as-url "/raw/img/ui/settings_button_01.png")]])

(defn menu
  []
  (let [top-right (top-right)]
    [:> Group top-right
     [settings-button (- 216) 20]
     [close-button (- 108) 20]]
    ))

(defn scene-start-auto
  [scene-data]
  (get-in scene-data [:metadata :autostart] false))

(defn scene-ready
  [scene-id]
  (let [scene-data (re-frame/subscribe [::subs/scene scene-id])
        loaded (re-frame/subscribe [::subs/scene-loading-complete scene-id])
        course-started (re-frame/subscribe [::subs/playing])]
    (and @loaded @course-started (scene-start-auto @scene-data))))

(defn layers
  [objects]
  (->> objects
       (reduce-kv #(conj %1 (get %3 :layer 0)) #{})
       (#(conj %1 0))))

(defn from-layer
  [objects layer]
  (filter (fn [[k v]] (= layer (get v :layer 0))) objects))

(defn prepare-action
  [action]
  {(keyword (str "on-" (:on action))) #(re-frame/dispatch [::ie/execute-action action])})

(defn prepare-group-params
  [object]
  (reduce (fn [o [k v]] (merge o (prepare-action v))) object (:actions object)))

(declare transition)

(defn draw-object
  [scene-id name]
  (let [o (re-frame/subscribe [::subs/scene-object scene-id name])
        type (keyword (:type @o))]
    (js/console.log (str "render object: " type " on scene: " scene-id))
    (js/console.log @o)
    (case type
      :background [kimage (get-data-as-url (:src @o))]
      :image [:> Group (prepare-group-params @o)
                           [kimage (get-data-as-url (:src @o))]]
      :transparent [:> Group (prepare-group-params @o)
                                 [:> Rect {:x 0 :width (:width @o) :height (:height @o)}]]
      :transition [transition scene-id name @o]
      )))

(defn transition
  [scene-id name object]
  (let [component (r/atom nil)
        params (assoc object :ref (fn [ref] (reset! component ref)))]
    (re-frame/dispatch [::ie/register-transition scene-id name component])
    (fn [scene-id name object]
      [:> Group params
       [draw-object scene-id (:object object)]])))

(defn scene
  [scene-id]
  (js/console.log "render scene!")
  (let [scene-objects (re-frame/subscribe [::subs/scene-objects scene-id])]
    [:> Group
      (for [layer @scene-objects
            name layer]
        ^{:key (str scene-id name)} [draw-object scene-id name])
     [menu]
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
  (re-frame/dispatch [::ie/start-course course-id])
  (fn [course-id]
    (let [viewport (re-frame/subscribe [::subs/viewport])
          viewbox (get-viewbox @viewport)
          ui-screen (re-frame/subscribe [::subs/ui-screen])]
      [:> Stage {:width (:width @viewport) :height (:height @viewport) :x (- (compute-x viewbox)) :y (- (compute-y viewbox))
                 :scale-x (/ (:width @viewport) (:width viewbox)) :scale-y (/ (:height @viewport) (:height viewbox))}
       [:> Layer
        (if (= @ui-screen :settings)
          [settings]
          [current-scene]
          )
        ]]
      )))
