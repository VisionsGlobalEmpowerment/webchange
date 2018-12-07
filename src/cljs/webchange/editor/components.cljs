(ns webchange.editor.components
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.subs :as subs]
    [webchange.common.kimage :refer [kimage]]
    [webchange.common.anim :refer [anim]]
    [webchange.interpreter.variables.subs :as vars.subs]
    [webchange.interpreter.core :refer [get-data-as-url]]
    [webchange.interpreter.components :refer [scene with-origin-offset] :rename {scene play-scene}]
    [webchange.interpreter.events :as ie]
    [webchange.editor.events :as events]
    [webchange.editor.subs :as es]
    [konva :refer [Transformer]]
    [react-konva :refer [Stage Layer Group Rect Text Custom]]
    [sodium.core :as na]))

(declare background)
(declare image)
(declare transparent)
(declare group)
(declare placeholder)
(declare animation)
(declare text)

(def transform-state (atom))

(defn object
  [type]
  (case type
    :background background
    :image image
    :transparent transparent
    :group group
    :placeholder placeholder
    :animation animation
    :text text
    ))

(defn update-object
  [scene-id name state]
  (re-frame/dispatch [::events/edit-object {:scene-id scene-id :target name
                                            :state {:x (:x state)
                                                    :y (:y state)
                                                    :rotation (:rotation state)
                                                    :scale-x (:scale-x state)
                                                    :scale-y (:scale-y state)}}]))

(defn update-current-scene-object
  [name state]
  (re-frame/dispatch [::events/edit-current-scene-object {:target name
                                                          :state {:x (:x state)
                                                                  :y (:y state)
                                                                  :rotation (:rotation state)
                                                                  :scale-x (:scale-x state)
                                                                  :scale-y (:scale-y state)}}]))

(defn to-props
  [konva-node]
  {:x (.x konva-node)
   :y (.y konva-node)
   :rotation (.rotation konva-node)
   :scale-x (.scaleX konva-node)
   :scale-y (.scaleY konva-node)})

(defn reset-transform
  []
  (let [{:keys [transformer target]} @transform-state]
    (when target (.draggable target false))
    (when transformer (.destroy transformer))))

(defn transform
  [scene-id name target]
  (reset-transform)
  (let [transformer (Transformer.)]
    (reset! transform-state {:transformer transformer :target target})
    (.draggable target true)
    (.on target "dragmove" (fn [e] (update-object scene-id name (-> e .-currentTarget to-props))))
    (.on target "transform" (fn [e] (update-object scene-id name (-> e .-currentTarget to-props))))
    (.attachTo transformer target)
    (.add (.getParent target) transformer)))

(defn rect-params
  [scene-id name object]
  {:width (:width object)
   :height (:height object)
   :stroke "green"
   :stroke-width 4
   :on-click (fn [e] (transform scene-id name (-> e .-target .getParent)))})

(defn object-params
  [object]
  (-> object with-origin-offset))

(defn draw-object
  [scene-id name]
  (let [o (re-frame/subscribe [::subs/current-scene-object name])
        type (keyword (:type @o))]
    (js/console.log "redraw! " scene-id " " name)
    [(object type) scene-id name @o]))

(defn background
  [scene-id name object]
  [:> Group (object-params object)
   [:> Group {:on-click reset-transform}
    [kimage (get-data-as-url (:src object))]]])

(defn image
  [scene-id name object]
  [:> Group (object-params object)
   [kimage (get-data-as-url (:src object))]
   [:> Rect (rect-params scene-id name object)]])

(defn transparent
  [scene-id name object]
  [:> Group (object-params object)
   [:> Rect (rect-params scene-id name object)]])

(defn group
  [scene-id name object]
  [:> Group (object-params object)
   (for [child (:children object)]
    ^{:key (str scene-id child)} [draw-object scene-id child])
   [:> Rect (rect-params scene-id name object)]])

(defn placeholder
  [scene-id name object]
  (let [item (re-frame/subscribe [::vars.subs/variable scene-id (:var-name object)])
        prepared (cond-> object
                         :always (assoc :type "image")
                         (contains? object :image-src) (assoc :src (get @item (-> object :image-src keyword)))
                         (contains? object :image-width) (assoc :width (get @item (-> object :image-width keyword)))
                         (contains? object :image-height) (assoc :height (get @item (-> object :image-height keyword)))
                         :always (assoc :var @item))
        rect-params (rect-params scene-id name prepared)
        object-params (object-params prepared)]
    [image scene-id name object-params]
    [:> Rect rect-params]))

(defn animation
  [scene-id name object]
  (let [params (object-params object)]
    [:> Group params
     [anim (:name object) (:anim object) (:speed object) #(re-frame/dispatch [::ie/register-animation (:name object) %])]
     [:> Rect (-> (rect-params scene-id name object)
                  (assoc :origin {:type "center-bottom"})
                  with-origin-offset)]]))

(defn text
  [scene-id name object]
  [:> Group
   [:> Text (object-params object)]
   [:> Rect (rect-params scene-id name object)]])

(defn scene
  []
  (let [scene-id @(re-frame/subscribe [::subs/current-scene])
        scene-objects (re-frame/subscribe [::subs/current-scene-objects])]
    [:> Group
     (for [layer @scene-objects
           name layer]
       ^{:key (str scene-id name)} [draw-object scene-id name])]
    ))

(defn preloader
  []
  (let [scene-id (re-frame/subscribe [::subs/current-scene])
        progress (re-frame/subscribe [::subs/scene-loading-progress @scene-id])]
    [:> Group
     [kimage "/raw/img/bg.jpg"]

     [kimage "/raw/img/ui/logo.png" {:x 628 :y 294}]

     [:> Group {:x 729 :y 750}
      [:> Rect {:x 1 :width 460 :height 24 :fill "#ffffff" :corner-radius 25}]
      [:> Group {:clip-x 0 :clip-y 0 :clip-width (+ 0.1 (* @progress 4.62)) :clip-height 24}
       [:> Rect {:width 462 :height 24 :fill "#2c9600" :corner-radius 25}]]
      ]]))

(defn course
  []
    (let [ui-screen (re-frame/subscribe [::es/screen])
          scene-id (re-frame/subscribe [::subs/current-scene])
          loaded (re-frame/subscribe [::subs/scene-loading-complete @scene-id])]
      [:> Stage {:width 1344 :height 756 :scale-x 0.7 :scale-y 0.7}
       [:> Layer
        (case @ui-screen
          :play-scene [play-scene @scene-id]
          (if @loaded
            [scene]
            [preloader]))
        ]]))

(defn check-prev
  [prev current props]
  (when (not= @prev current)
    (reset! prev current)
    (reset! props current)))

(defn properties-panel
  [scene-id name]
  (let [prev (r/atom {})
        props (r/atom {})]
    (fn []
      (let [o (re-frame/subscribe [::subs/scene-object scene-id name])]
        (check-prev prev @o props)
        [na/form {}
         [na/form-input {:label "x" :value (:x @props) :on-change #(swap! props assoc :x (-> %2 .-value js/parseInt))}]
         [na/form-input {:label "y" :value (:y @props) :on-change #(swap! props assoc :y (-> %2 .-value js/parseInt))}]
         [na/form-input {:label "rotation" :value (:rotation @props) :on-change #(swap! props assoc :rotation (-> %2 .-value js/parseInt))}]
         [na/form-button {:content "Save" :on-click #(do (update-object scene-id name @props)
                                                         (update-current-scene-object name @props))}]]))))