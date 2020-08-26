(ns webchange.interpreter.components
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
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
    [webchange.common.anim :refer [anim prepare-anim-object-params]]
    [webchange.common.text :refer [chunked-text]]
    [webchange.common.carousel :refer [carousel]]
    [webchange.common.scene-components.components :as components]
    [webchange.interpreter.components.video :refer [video]]
    [webchange.interpreter.core :refer [get-data-as-url]]
    [webchange.interpreter.events :as ie]
    [webchange.interpreter.executor :as e]
    [webchange.interpreter.variables.subs :as vars.subs]
    [webchange.interpreter.variables.events :as vars.events]
    [webchange.interpreter.utils.position :refer [compute-x compute-y compute-scale get-viewbox top-left top-right bottom-center]]
    [webchange.common.events :as ce]
    [webchange.interpreter.screens.activity-finished :refer [activity-finished-screen]]
    [webchange.interpreter.screens.course-loading :refer [course-loading-screen]]
    [webchange.interpreter.screens.preloader :refer [preloader-screen]]
    [webchange.interpreter.screens.settings :refer [settings-screen]]
    [webchange.interpreter.screens.state :as screens]
    [webchange.common.core :refer [prepare-anim-rect-params
                                   prepare-colors-palette-params
                                   prepare-group-params
                                   prepare-painting-area-params
                                   prepare-animated-svg-path-params
                                   with-origin-offset
                                   with-filter-transition]]
    [react-konva :refer [Stage Layer Group Rect Text Custom]]
    [konva :as k]
    [webchange.interpreter.utils.i18n :refer [t]]
    [webchange.interpreter.resources-manager.scene-parser :refer [get-scene-resources]]
    [webchange.interpreter.renderer.stage :refer [stage]]
    [webchange.interpreter.subs :as isubs]))

(defn empty-filter [] {:filters []})

(defn grayscale-filter
  []
  {:filters [{:name "grayscale"}]})

(defn brighten-filter
  [{:keys [brightness transition]}]
  (->
    {:filters    [{:name  "brighten"
                   :value brightness}]
     :transition transition}
    with-filter-transition))

(defn- with-highlight
  [image-params object-params]
  (if (contains? object-params :highlight)
    (update-in image-params [:filters] conj {:name "glow"})
    image-params))

(defn- with-pulsation
  [image-params object-params]
  (if (:eager object-params)
    (update-in image-params [:filters] conj {:name "pulsation"})
    image-params))

(defn filter-params [{:keys [filter] :as params}]
  (-> (case filter
        "grayscale" (grayscale-filter)
        "brighten" (brighten-filter params)
        (empty-filter))
      (with-highlight params)
      (with-pulsation params)))

(defn close-button
  [x y]
  [:> Group {:x        x :y y
             :on-click #(re-frame/dispatch [::ie/open-student-dashboard])
             :on-tap   #(re-frame/dispatch [::ie/open-student-dashboard])}
   [kimage {:src (get-data-as-url "/raw/img/ui/close_button_01.png")}]])

(defn back-button
  [x y]
  (let [back-button-state @(re-frame/subscribe [::subs/current-scene-back-button])]
    [:> Group {:x        x :y y
               :on-click #(re-frame/dispatch [::ie/close-scene])
               :on-tap   #(re-frame/dispatch [::ie/close-scene])}
     [kimage (merge {:src (get-data-as-url "/raw/img/ui/back_button_01.png")}
                    (filter-params back-button-state))]]))

(defn settings-button
  [x y]
  [:> Group {:x        x :y y
             :on-click #(re-frame/dispatch [::ie/open-settings])
             :on-tap   #(re-frame/dispatch [::ie/open-settings])}
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
       [components/button-interpreter {:text (t "skip")}]])))

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
(declare layered-background)
(declare empty-component)

(defn lock-object [o]
  (let [o (-> o
              (assoc :actions {})
              (dissoc :highlight)
              (assoc :filter "grayscale"))]
    o))

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
  (let [navigation-mode @(re-frame/subscribe [::subs/navigation-mode])
        activities (if (= navigation-mode :lesson) (get-lesson-based-open-activity) (get-activity-based-open-activity))
        scene-list @(re-frame/subscribe [::subs/scene-list])
        all-activities (set (flatten (map #(find-path scene-id % scene-list) activities)))
        outs (set (flatten (map #(:name %) (:outs ((keyword scene-id) scene-list)))))]
    (if (contains? outs object-name)
      (if (contains? all-activities object-name) o (lock-object o))
      o)))

(defn draw-object
  ([scene-id name]
   (draw-object scene-id name {}))
  ([scene-id name props]
   (let [o (merge @(re-frame/subscribe [::subs/scene-object-with-var scene-id name]) props)
         type (keyword (:type o))
         o (add-navigation-params scene-id name o)
         o (if (contains? o :actions) o (assoc o :listening false))]
     (case type
       :background [background scene-id name o]
       ;:layered-background [layered-background scene-id name o]
       ;:button [button scene-id name o]
       :image [image scene-id name o]
       ;:transparent [:> Group (prepare-group-params o)
       ;              [:> Rect {:x 0 :width (:width o) :height (:height o)}]]
       ;:group [group scene-id name o]
       ;:placeholder [placeholder scene-id name o]
       :animation [animation scene-id name o]
       ;:text [text scene-id name o]
       ;:carousel [carousel-object scene-id name o]
       ;:painting-area (get-painting-area scene-id name o)
       ;:copybook [copybook o]
       ;:colors-palette (get-colors-palette scene-id name o)
       ;:video [video o]
       ;:animated-svg-path [animated-svg-path (prepare-animated-svg-path-params o)]
       ;:svg-path [svg-path o]
       ;:matrix [matrix-object scene-id name o draw-object]
       ;:propagate [empty-component]
       (do (.warn js/console "[RENDERER]" (str "Object with type " type " can not be drawn because it is not defined"))
           nil)
       ;(throw (js/Error. (str "Object with type " type " can not be drawn because it is not defined")))
       ))))

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

;(defn group
;  [scene-id name object]
;  [:> Group (prepare-group-params object)
;   (for [child (:children object)]
;     ^{:key (str scene-id child)} [draw-object scene-id child])])

(defn matrix-object
  [scene-id name object d]
  [:> Group (prepare-group-params object)
   (matrix-objects-list object scene-id d)])

;(defn image
;  [scene-id name object]
;  [renderer/image (merge {:src (:src object)}
;                         (prepare-group-params object)
;                         (filter-params object))])

(defn button
  [scene-id name object]
  [components/button-interpreter (-> object
                                     (merge {:name name :scene-id scene-id})
                                     (prepare-group-params))])

;(defn background
;  [scene-id name object]
;  [renderer/image (merge {:src       (:src object)
;                          :listening false}
;                         (filter-params object))])

(defn layered-background
  [scene-id name object]
  (let [layers (map (fn [key] {:key key
                               :src (get-data-as-url (get-in object [key :src]))})
                    [:background :surface :decoration])]
    [:> Group {}
     (for [{:keys [key src]} layers]
       (when src
         ^{:key key}
         [kimage (merge {:src       src
                         :listening false}
                        (filter-params (first (:data object))))]))]))

;(defn animation
;  [_ _ object]
;  (let [anim-object (prepare-anim-object-params object)
;        animation-name (or (:scene-name anim-object) (:name anim-object))
;        animation-params (-> anim-object
;                             (merge (prepare-group-params anim-object))
;                             (assoc :on-mount #(re-frame/dispatch [::ie/register-animation animation-name %])))
;        filtered-animation-params (reduce (fn [params param-to-remove]
;                                            (dissoc params param-to-remove))
;                                          animation-params
;                                          [:actions :listening :scene-name :states :transition :type :width :height :origin])]
;    [renderer/animation filtered-animation-params]))

(defn carousel-object [scene-id name object]
  [:> Group (prepare-group-params object)
   [carousel object]])

(defn navigation-helper []
  (let [position @(re-frame/subscribe [::subs/navigation])]
    (when position
      [:> Group (select-keys position [:x :y])
       [kimage {:src (get-data-as-url "/raw/img/ui/hand.png")}]])))

(defn overlay
  [{:keys [scene-id scene-ready?]}]
  (let [ui-screen @(re-frame/subscribe [::screens/ui-screen])]
    [:> Layer
     (if-not scene-ready?
       [preloader-screen]
       (case ui-screen
         :activity-finished [activity-finished-screen]
         :course-loading [course-loading-screen]
         :settings [settings-screen]
         [:> Group
          [menu]
          [skip-menu]
          ;[triggers scene-id]
          ]))]))

(defn layer
  [{:keys [scene-id layer-id layer background?]}]
  (let [layer-params (if background?
                       {:listening       false
                        :clearBeforeDraw false}
                       {})]
    ;[renderer/layer layer-params
    ; (for [object layer
    ;       :let [object-id (str layer-id "-" object)]]
    ;   ^{:key object-id}
    ;   [draw-object scene-id object])]
    )

  ;[renderer/animation]
  )

;(defn stage
;  []
;  (let []
;    (r/create-class
;      {:display-name        "web-gl-stage"
;
;       :component-did-mount (fn []
;                              (do-start))
;
;       :reagent-render
;                            (fn [{:keys [viewport scene-id scene-objects scene-ready? scene-resources]}]
;                              (let [status @(re-frame/subscribe [::vars.subs/variable scene-id "status"])]
;                                (when-not (empty? scene-objects)
;                                  [renderer/stage {:viewport  (get-stage-params viewport)
;                                                   :resources scene-resources
;                                                   :on-ready  #(trigger status scene-ready?)}
;                                   ;(when scene-ready?
;                                   ;  )
;
;                                   (for [[layer-number scene-layer] (map-indexed vector scene-objects)
;                                         :let [layer-id (str scene-id "-layer-" layer-number)]]
;                                     ^{:key layer-id}
;                                     [layer {:scene-id    scene-id
;                                             :layer-id    layer-id
;                                             :layer       scene-layer
;                                             :background? (= layer-number 0)}])
;
;                                   ;^{:key "overlay"}
;                                   ;[overlay {:scene-id     scene-id
;                                   ;          :scene-ready? scene-ready?}]
;                                   ])))})))

(defn- filter-extra-props
  [props extra-props-names]
  (reduce (fn [props prop-to-remove]
            (dissoc props prop-to-remove))
          props
          extra-props-names))

(defn group
  [scene-id name object]
  [:> Group (prepare-group-params object)
   (for [child (:children object)]
     ^{:key (str scene-id child)} [draw-object scene-id child])])

(defn- get-object-data
  [scene-id name]
  (let [o @(re-frame/subscribe [::subs/scene-object-with-var scene-id name])
        type (keyword (:type o))
        object (add-navigation-params scene-id name o)]
    (case type
      :background (-> (merge object
                             {:object-name (keyword name)}
                             (filter-params object))
                      (filter-extra-props [:brightness :filter :transition]))
      ;:layered-background [layered-background scene-id name o]
      ;:button [button scene-id name o]
      :image (-> (merge object
                        {:object-name (keyword name)}
                        (prepare-group-params object)
                        (filter-params object))
                 (filter-extra-props [:actions :brightness :filter :highlight :listening :states :transition :width :height :eager :origin :scale-x :scale-y]))
      ;:transparent [:> Group (prepare-group-params o)
      ;              [:> Rect {:x 0 :width (:width o) :height (:height o)}]]
      :group (let [group-params (prepare-group-params object)
                   children-params (map (fn [name] (get-object-data scene-id name)) (:children object))]
               (-> (merge object
                          group-params
                          {:object-name (keyword name)
                           :children    children-params})
                   (filter-extra-props [:transition :width :height])))
      ;:placeholder [placeholder scene-id name o]
      :animation (let [anim-object (prepare-anim-object-params object)
                       animation-name (or (:scene-name anim-object) (:name anim-object))]
                   (-> anim-object
                       (merge (prepare-group-params anim-object))
                       (assoc :object-name (keyword name))
                       (assoc :on-mount #(re-frame/dispatch [::ie/register-animation animation-name %]))
                       (filter-extra-props [:actions :listening :scene-name :states :transition :width :height :origin :scale-x :scale-y :meshes])))
      ;:text [text scene-id name o]
      ;:carousel [carousel-object scene-id name o]
      ;:painting-area (get-painting-area scene-id name o)
      ;:copybook [copybook o]
      ;:colors-palette (get-colors-palette scene-id name o)
      ;:video [video o]
      ;:animated-svg-path [animated-svg-path (prepare-animated-svg-path-params o)]
      ;:svg-path [svg-path o]
      ;:matrix [matrix-object scene-id name o draw-object]
      ;:propagate [empty-component]
      (do (.warn js/console "[RENDERER]" (str "Object with type " type " can not be drawn because it is not defined"))
          nil)
      ;(throw (js/Error. (str "Object with type " type " can not be drawn because it is not defined")))
      )))

(defn- get-layer-objects-data
  [scene-id layer-objects]
  (reduce (fn [result object-name]
            (conj result (get-object-data scene-id object-name)))
          []
          layer-objects))

(defn- get-scene-objects-data
  [scene-id scene-layers]
  (->> scene-layers
       (reduce (fn [scene-objects-data scene-layer]
                 (concat scene-objects-data (get-layer-objects-data scene-id scene-layer)))
               [])
       (remove nil?)))

(defn- scene-started?
  [scene-id]
  (let [scene-data @(re-frame/subscribe [::subs/scene scene-id])
        scene-started @(re-frame/subscribe [::subs/scene-started])
        auto-start (get-in scene-data [:metadata :autostart] false)
        course-started @(re-frame/subscribe [::subs/playing])]
    (and course-started
         (or scene-started auto-start))))


(defn- get-scene-data
  [scene-id scene-data scene-objects dataset-items]
  (cond
    (nil? scene-id) nil
    (empty? scene-objects) nil
    (empty? scene-data) nil
    (empty? dataset-items) nil                              ;; ToDo: actually do not stat scene until datasets are loaded
    :else {:scene-id  scene-id
           :objects   (get-scene-objects-data scene-id scene-objects)
           :resources (get-scene-resources scene-id scene-data)
           :started?  (scene-started? scene-id)}))


(defn- start-scene
  []
  (e/init)
  (re-frame/dispatch [::ie/start-playing]))

(defn- start-triggers
  [scene-id]
  (let [status (re-frame/subscribe [::vars.subs/variable scene-id "status"])]
    (if (not= @status :running)
      (do
        (re-frame/dispatch [::vars.events/execute-set-variable {:var-name "status" :var-value :running}])
        (re-frame/dispatch [::ie/trigger :start])))))

(defn- stage-wrapper
  [{:keys [scene-id]}]
  (let [scene-data @(re-frame/subscribe [::subs/scene scene-id])
        scene-objects @(re-frame/subscribe [::subs/scene-objects scene-id])
        dataset-items @(re-frame/subscribe [::isubs/dataset-items])]
    ^{:key scene-id}
    [stage {:scene-data     (get-scene-data scene-id scene-data scene-objects dataset-items)
            :on-ready       #(start-triggers scene-id)
            :on-start-click start-scene}]))

(defn course
  [_]
  (let [scene-id @(re-frame/subscribe [::subs/current-scene])]
    [stage-wrapper {:scene-id scene-id}]))
