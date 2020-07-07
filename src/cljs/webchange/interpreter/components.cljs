(ns webchange.interpreter.components
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
    [re-frame.core :as re-frame]
    [webchange.subs :as subs]
    [webchange.student-dashboard.subs :as student-dashboard-subs]
    [webchange.interpreter.utils.find-exit :refer [find-exit-position find-path]]
    [webchange.common.kimage :refer [kimage]]
    [webchange.common.painting-area :refer [painting-area]]
    [webchange.common.copybook :refer [copybook]]
    [webchange.common.svg-path :refer [svg-path]]
    [webchange.common.colors-palette :refer [colors-palette]]
    [webchange.common.animated-svg-path :refer [animated-svg-path]]
    [webchange.common.matrix :refer [matrix-objects-list]]
    [webchange.common.anim :refer [anim]]
    [webchange.common.text :refer [chunked-text]]
    [webchange.common.carousel :refer [carousel]]
    [webchange.common.scene-components.components :as components]
    [webchange.interpreter.components.video :refer [video]]
    [webchange.interpreter.core :refer [get-data-as-url]]
    [webchange.interpreter.events :as ie]
    [webchange.interpreter.variables.subs :as vars.subs]
    [webchange.interpreter.variables.events :as vars.events]
    [webchange.interpreter.utils.position :refer [compute-x compute-y compute-scale get-viewbox top-left top-right bottom-center]]
    [webchange.common.events :as ce]
    [webchange.interpreter.executor :as e]
    [webchange.interpreter.screens.activity-finished :refer [activity-finished-screen]]
    [webchange.interpreter.screens.course-loading :refer [course-loading-screen]]
    [webchange.interpreter.screens.preloader :refer [preloader-screen]]
    [webchange.interpreter.screens.score :refer [score-screen]]
    [webchange.interpreter.screens.settings :refer [settings-screen]]
    [webchange.interpreter.screens.state :as screens]
    [webchange.common.core :refer [prepare-anim-rect-params
                                   prepare-colors-palette-params
                                   prepare-group-params
                                   prepare-painting-area-params
                                   prepare-animated-svg-path-params
                                   with-origin-offset
                                   with-filter-transition]]
    [webchange.common.image-modifiers.animation-eager :refer [animation-eager]]
    [webchange.common.image-modifiers.filter-outlined :refer [filter-outlined]]
    [webchange.logger :as logger]
    [react-konva :refer [Stage Layer Group Rect Text Custom]]
    [webchange.editor-v2.translator.translator-form.state.graph :as translator-form.graph]
    [konva :as k]))

(defn empty-filter [] {:filters []})

(defn grayscale-filter
  []
  {:filters [k/Filters.Grayscale]})

(defn brighten-filter
  [{:keys [brightness transition]}]
  (->
    {:filters [k/Filters.Brighten] :brightness brightness :transition transition}
    with-filter-transition))

(defn- with-highlight
  [image-params object-params]
  (if (contains? object-params :highlight)
    (update-in image-params [:filters] conj filter-outlined)
    image-params))

(defn- with-pulsation
  [image-params object-params]
  (if (:eager object-params)
    (assoc image-params :animation animation-eager)
    image-params))

(defn filter-params [{:keys [filter] :as params}]
  (-> (case filter
        "grayscale" (grayscale-filter)
        "brighten" (brighten-filter params)
        (empty-filter))
      (with-highlight params)
      (with-pulsation params)))

(defn score
  []
  (let [scene-id (re-frame/subscribe [::subs/current-scene])
        score-var (re-frame/subscribe [::vars.subs/variable @scene-id "score"])]
    (if (:visible @score-var)
      [score-screen])))

(defn close-button
  [x y]
  [:> Group {:x x :y y
             :on-click #(re-frame/dispatch [::ie/open-student-dashboard])
             :on-tap #(re-frame/dispatch [::ie/open-student-dashboard])}
   [kimage {:src (get-data-as-url "/raw/img/ui/close_button_01.png")}]])

(defn back-button
  [x y]
  (let [back-button-state @(re-frame/subscribe [::subs/current-scene-back-button])]
    [:> Group {:x        x :y y
               :on-click #(re-frame/dispatch [::ie/close-scene])
               :on-tap   #(re-frame/dispatch [::ie/close-scene])}
     [kimage (merge {:src (get-data-as-url "/raw/img/ui/back_button_01.png")}
                    (filter-params back-button-state)) ]]))

(defn settings-button
  [x y]
  [:> Group {:x x :y y
             :on-click #(re-frame/dispatch [::ie/open-settings])
             :on-tap #(re-frame/dispatch [::ie/open-settings])}
   [kimage {:src (get-data-as-url "/raw/img/ui/settings_button_01.png")}]])

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

(defn skip-menu
  []
  (let [show-skip @(re-frame/subscribe [::subs/show-skip])
        {:keys [x y]} (bottom-center)]
    (when show-skip
      [:> Group {:x        (- x 150)
                 :y        (- y 200)
                 :on-click #(re-frame/dispatch [::ce/skip])
                 :on-tap   #(re-frame/dispatch [::ce/skip])}
       [components/button-interpreter {:text "Skip"}]])))

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
(declare matrix-object)
(declare placeholder)
(declare button)
(declare image)
(declare animation)
(declare text)
(declare carousel-object)
(declare get-painting-area)
(declare get-colors-palette)
(declare background)
(declare empty-component)

(defn lock-object [o]
  (let [o (-> o
    (assoc :actions {})
    (assoc :filter "grayscale"))]
  o
))

(defn get-lesson-based-open-activity []
  (let [{:keys [id]} @(re-frame/subscribe [::student-dashboard-subs/next-activity])
        finished-level-lesson-activities @(re-frame/subscribe [::student-dashboard-subs/finished-level-lesson-activities])
        activities (conj finished-level-lesson-activities id)]
    activities))

(defn get-activity-based-open-activity []
  (let [{:keys [id]} @(re-frame/subscribe [::student-dashboard-subs/next-activity])
        finished-activities (set (map #(:id %) @(re-frame/subscribe [::student-dashboard-subs/finished-activities])))
        activities (conj finished-activities id)]
    activities))

(defn add-navigation-params [scene-id object-name o]
  (let [
        navigation-mode @(re-frame/subscribe [::subs/navigation-mode])
        activities (if (= navigation-mode :lesson) (get-lesson-based-open-activity) (get-activity-based-open-activity))
        scene-list @(re-frame/subscribe [::subs/scene-list])
        all-activities (set (flatten (map #(find-path scene-id % scene-list) activities)))
        outs  (set (flatten (map #(:name %) (:outs ((keyword scene-id) scene-list)))))]
     (if (contains? outs object-name)
       (if (contains? all-activities object-name) o (lock-object o))
        o )))

(defn draw-object
  ([scene-id name]
   (draw-object scene-id name {}))
  ([scene-id name props]
   (let [o (merge @(re-frame/subscribe [::subs/scene-object-with-var scene-id name]) props)
         type (keyword (:type o))
         o (add-navigation-params scene-id name o)
         ]


     (case type
       :background [background scene-id name o]
       :button [button scene-id name o]
       :image [image scene-id name o]
       :transparent [:> Group (prepare-group-params o)
                     [:> Rect {:x 0 :width (:width o) :height (:height o)}]]
       :group [group scene-id name o]
       :placeholder [placeholder scene-id name o]
       :animation [animation scene-id name o]
       :text [text scene-id name o]
       :carousel [carousel-object scene-id name o]
       :painting-area (get-painting-area scene-id name o)
       :copybook [copybook o]
       :colors-palette (get-colors-palette scene-id name o)
       :video [video o]
       :animated-svg-path [animated-svg-path (prepare-animated-svg-path-params o)]
       :svg-path [svg-path o]
       :matrix [matrix-object scene-id name o draw-object]
       :propagate [empty-component]
       (throw (js/Error. (str "Object with type " type " can not be drawn because it is not defined")))))))

(defn empty-component [_] nil)

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

(defn matrix-object
  [scene-id name object d]
  [:> Group (prepare-group-params object)
   (matrix-objects-list object scene-id d)])

(defn image
  [scene-id name object]
  [:> Group (prepare-group-params object)
   [kimage (merge {:src (get-data-as-url (:src object))}
                  (filter-params object))]])

(defn button
  [scene-id name object]
  [components/button-interpreter (-> object
                                     (merge {:name name :scene-id scene-id})
                                     (prepare-group-params))])

(defn background
  [scene-id name object]
  [kimage (merge {:src (get-data-as-url (:src object))}
                 (filter-params object))])

(defn animation
  [scene-id name object]
  (let [params (prepare-group-params object)
        rect-params (prepare-anim-rect-params object)
        animation-name (or (:scene-name object) (:name object))]
    [:> Group params
     [anim (-> object
               (assoc :on-mount #(re-frame/dispatch [::ie/register-animation animation-name %])))]
     [:> Rect rect-params]]))

(defn carousel-object [scene-id name object]
  [:> Group (prepare-group-params object)
   [carousel object]])

(defn navigation-helper []
  (let [position @(re-frame/subscribe [::subs/navigation])]
    (when position
      [:> Group (select-keys position [:x :y])
       [kimage {:src (get-data-as-url "/raw/img/ui/hand.png")}]])))

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
     [skip-menu]
     [triggers scene-id]
     ]))

(defn current-scene
  []
  (let [scene-id (re-frame/subscribe [::subs/current-scene])
        scene-ready (scene-ready @scene-id)]
    (if scene-ready
      [scene @scene-id]
      [preloader-screen])))

(defn overlay-screens
  []
  (let [ui-screen @(re-frame/subscribe [::screens/ui-screen])]
    [:> Layer
       (case ui-screen
         :activity-finished [activity-finished-screen]
         :course-loading [course-loading-screen]
         :preloader [preloader-screen]
         :score [score-screen]
         :settings [settings-screen]
         (when-not (nil? ui-screen)
           (logger/error (str "Overlay screen '" ui-screen "' is not implemented"))))]))

(defn course
  [course-id]
  (fn [course-id]
    (let [viewport (re-frame/subscribe [::subs/viewport])
          viewbox (get-viewbox @viewport)]
      [:div
       [:style "html, body {margin: 0; max-width: 100%; overflow: hidden;}"]
       [:> Stage {:width (:width @viewport) :height (:height @viewport) :x (compute-x viewbox) :y (- (compute-y viewbox))
                  :scale-x (compute-scale @viewport) :scale-y (compute-scale @viewport)}
        [:> Layer [current-scene]]
        [overlay-screens]]])))
