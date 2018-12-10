(ns webchange.editor.components
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.subs :as subs]
    [webchange.common.kimage :refer [kimage]]
    [webchange.common.anim :refer [anim]]
    [webchange.common.events :as ce]
    [webchange.interpreter.variables.subs :as vars.subs]
    [webchange.interpreter.core :refer [get-data-as-url]]
    [webchange.interpreter.components :refer [scene with-origin-offset] :rename {scene play-scene}]
    [webchange.interpreter.events :as ie]
    [webchange.editor.events :as events]
    [webchange.editor.subs :as es]
    [konva :refer [Transformer]]
    [react-konva :refer [Stage Layer Group Rect Text Custom]]
    [sodium.core :as na]
    [soda-ash.core :as sa]))

(declare background)
(declare image)
(declare transparent)
(declare group)
(declare placeholder)
(declare animation)
(declare text)

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
  (re-frame/dispatch [::events/edit-object {:scene-id scene-id :target name :state state}]))

(defn update-current-scene-object
  [name state]
  (re-frame/dispatch [::events/edit-current-scene-object {:target name :state state}]))

(defn update-action
  [scene-id name action state]
  (re-frame/dispatch [::events/edit-action {:scene-id scene-id :target name :action action :state state}]))

(defn to-props
  [konva-node]
  {:x (.x konva-node)
   :y (.y konva-node)
   :rotation (.rotation konva-node)
   :scale-x (.scaleX konva-node)
   :scale-y (.scaleY konva-node)})

(defn reset-transform
  []
  (re-frame/dispatch [::events/reset-action])
  (let [state (re-frame/subscribe [::es/transform])
        {:keys [transformer target]} @state]
    (when target (.draggable target false))
    (when transformer (.destroy transformer))))

(defn remove-transform
  []
  (re-frame/dispatch [::events/reset-transform])
  (reset-transform))

(defn transform
  [scene-id name target]
  (let [transformer (Transformer.)]
    (reset-transform)
    (re-frame/dispatch [::events/register-transform {:transformer transformer
                                                     :target target
                                                     :scene-id scene-id
                                                     :name name}])
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
    [(object type) scene-id name @o]))

(defn background
  [scene-id name object]
  [:> Group (object-params object)
   [:> Group {:on-click remove-transform}
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
        (if @loaded
          (case @ui-screen
            :play-scene [play-scene @scene-id]
            [scene]
            )
          [preloader])
        ]]))

(defn check-prev
  [prev current props]
  (when (not= @prev current)
    (reset! prev current)
    (reset! props current)))

(defn properties-panel-common
  [props]
  [:div
   [na/form-input {:label "type" :value (:type @props) :on-change #(swap! props assoc :type (-> %2 .-value)) :inline? true}]
   [na/form-input {:label "x" :value (:x @props) :on-change #(swap! props assoc :x (-> %2 .-value js/parseInt)) :inline? true}]
   [na/form-input {:label "y" :value (:y @props) :on-change #(swap! props assoc :y (-> %2 .-value js/parseInt)) :inline? true}]
   [na/form-input {:label "width" :value (:width @props) :on-change #(swap! props assoc :width (-> %2 .-value js/parseInt)) :inline? true}]
   [na/form-input {:label "height" :value (:height @props) :on-change #(swap! props assoc :height (-> %2 .-value js/parseInt)) :inline? true}]
   [na/form-input {:label "rotation" :value (:rotation @props) :on-change #(swap! props assoc :rotation (-> %2 .-value js/parseInt)) :inline? true}]
   [na/form-input {:label "scale x" :value (:scale-x @props) :on-change #(swap! props assoc :scale-x (-> %2 .-value js/parseFloat)) :inline? true}]
   [na/form-input {:label "scale y" :value (:scale-y @props) :on-change #(swap! props assoc :scale-y (-> %2 .-value js/parseFloat)) :inline? true}]])

(defn properties-panel-transparent
  [props]
  [:div
   [properties-panel-common props]])

(defn properties-panel-image
  [props]
  [:div
   [na/form-input {:label "src" :value (:src @props) :on-change #(swap! props assoc :src (-> %2 .-value)) :inline? true}]
   [properties-panel-common props]])

(defn properties-panel-animation
  [props]
  [:div
   [na/form-input {:label "name" :value (:name @props) :on-change #(swap! props assoc :name (-> %2 .-value)) :inline? true}]
   [na/form-input {:label "anim" :value (:anim @props) :on-change #(swap! props assoc :anim (-> %2 .-value)) :inline? true}]
   [na/form-input {:label "speed" :value (:speed @props) :on-change #(swap! props assoc :speed (-> %2 .-value js/parseFloat)) :inline? true}]
   [properties-panel-common props]])

(defn action-properties-panel-action
  [data props]
  [:div
   [na/form-input {:label "type" :default-value (:type data) :on-change #(swap! props assoc :type (-> %2 .-value)) :inline? true}]
   [na/form-input {:label "id" :default-value (:id data) :on-change #(swap! props assoc :id (-> %2 .-value)) :inline? true}]
   [na/form-input {:label "on" :default-value (:on data) :on-change #(swap! props assoc :on (-> %2 .-value)) :inline? true}]])

(defn action-properties-panel-state
  [data props]
  [:div
   [na/form-input {:label "type" :default-value (:type data) :on-change #(swap! props assoc :type (-> %2 .-value)) :inline? true}]
   [na/form-input {:label "target" :default-value (:target data) :on-change #(swap! props assoc :target (-> %2 .-value)) :inline? true}]
   [na/form-input {:label "id" :default-value (:id data) :on-change #(swap! props assoc :id (-> %2 .-value)) :inline? true}]
   [na/form-input {:label "on" :default-value (:on data) :on-change #(swap! props assoc :on (-> %2 .-value)) :inline? true}]])

(defn action-properties-panel-scene
  [data props]
  [:div
   [na/form-input {:label "type" :default-value (:type data) :on-change #(swap! props assoc :type (-> %2 .-value)) :inline? true}]
   [na/form-input {:label "scene-id" :default-value (:scene-id data) :on-change #(swap! props assoc :scene-id (-> %2 .-value)) :inline? true}]
   [na/form-input {:label "on" :default-value (:on data) :on-change #(swap! props assoc :on (-> %2 .-value)) :inline? true}]])

(defn action-properties-panel-common
  [data props]
  [:div
   [na/form-input {:label "type" :default-value (:type data) :on-change #(swap! props assoc :type (-> %2 .-value)) :inline? true}]
   [na/form-input {:label "on" :default-value (:on data) :on-change #(swap! props assoc :on (-> %2 .-value)) :inline? true}]])


(defn action-properties-panel
  []
  (let [props (r/atom {})]
    (fn []
      (let [action-id (re-frame/subscribe [::es/action])
            {:keys [scene-id name action]} @action-id
            o (re-frame/subscribe [::subs/scene-object scene-id name])
            action-data (-> @o :actions (get (keyword action)))]
        ^{:key (str scene-id name action)}
        [na/form {}
         (case (-> action-data :type keyword)
           :action [action-properties-panel-action action-data props]
           :scene [action-properties-panel-scene action-data props]
           :state [action-properties-panel-state action-data props]
           [action-properties-panel-common action-data props])

         [na/form-button {:content "Save" :on-click #(update-action scene-id name action @props)}]]
        ))))

(defn actions-panel
  []
  (let [transform (re-frame/subscribe [::es/transform])
        {:keys [scene-id name]} @transform
        o (re-frame/subscribe [::subs/scene-object scene-id name])]
    [:div
     (for [action (-> @o :actions keys)]
       [:div
        [:a {:on-click #(re-frame/dispatch [::events/set-current-action scene-id name action])} (str action)]])]))

(defn properties-panel
  []
  (let [prev (r/atom {})
        props (r/atom {})
        activeIndex (r/atom -1)]
    (fn []
      (let [transform (re-frame/subscribe [::es/transform])
            {:keys [scene-id name]} @transform
            o (re-frame/subscribe [::subs/scene-object scene-id name])]
        (check-prev prev @o props)
        [sa/Accordion
         [sa/AccordionTitle {:active (= 0 @activeIndex) :on-click #(reset! activeIndex 0)} "Properties"]
         [sa/AccordionContent {:active (= 0 @activeIndex)}
          [na/form {}
           (case (-> @props :type keyword)
             :image [properties-panel-image props]
             :transparent [properties-panel-transparent props]
             :animation [properties-panel-animation props]
             [properties-panel-common props])

           [na/form-button {:content "Save" :on-click #(do (update-object scene-id name @props)
                                                           (update-current-scene-object name @props))}]]]
         [sa/AccordionTitle {:active (= 1 @activeIndex) :on-click #(reset! activeIndex 1)} "Actions"]
         [sa/AccordionContent {:active (= 1 @activeIndex)} [actions-panel]]]
        ))))

(defn properties-rail
  []
  (let [transform (re-frame/subscribe [::es/transform])
        action (re-frame/subscribe [::es/action])]
    [:div {:class-name "ui right internal rail"}
     (if @transform
       [:div {:class-name "ui segment"}
        [properties-panel]])
     (if @action
       [:div {:class-name "ui segment"}
        [action-properties-panel]])]))

(defn editor []
  (let [scene-id (re-frame/subscribe [::subs/current-scene])]
    [:div {:class-name "ui segment"}
     [:h2 {:class-name "ui dividing header"} "Editor"]
     [:div {:class-name "ui segment"}
      [course]

      [properties-rail]

      [na/button {:content "Play" :on-click #(re-frame/dispatch [::events/set-screen :play-scene])}]
      [na/button {:content "Editor" :on-click #(do (re-frame/dispatch [::events/set-screen :editor])
                                                   (re-frame/dispatch [::ce/execute-remove-flows {:flow-tag (str "scene-" @scene-id)}]))}]]]))