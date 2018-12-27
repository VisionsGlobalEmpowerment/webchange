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

(def object-types [{:key :image :value :image :text "Image"}
                   {:key :transparent :value :transparent :text "Transparent"}
                   {:key :group :value :group :text "Group"}
                   {:key :placeholder :value :placeholder :text "Placeholder"}
                   {:key :animation :value :animation :text "Animation"}
                   {:key :text :value :text :text "Text"}
                   {:key :background :value :background :text "Background"}])

(def action-types [{:key :action :value :action :text "Action"}
                   {:key :scene :value :scene :text "Scene"}
                   {:key :state :value :state :text "State"}
                   {:key :animation :value :animation :text "Animation"}
                   {:key :audio :value :audio :text "Audio"}
                   {:key :empty :value :empty :text "Empty"}
                   {:key :sequence :value :sequence :text "Sequence"}
                   {:key :parallel :value :parallel :text "Parallel"}])

(def asset-types [{:key :image :value :image :text "Image"}
                  {:key :audio :value :audio :text "Audio"}
                  {:key :anim-text :value :anim-text :text "Animation Text"}
                  {:key :anim-texture :value :anim-texture :text "Animation Texture"}])

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

(defn update-object-action
  [scene-id name action state]
  (re-frame/dispatch [::events/edit-object-action {:scene-id scene-id :target name :action action :state state}]))

(defn update-object-state
  [scene-id name state data]
  (re-frame/dispatch [::events/edit-object-state {:scene-id scene-id :name name :state state :data data}]))

(defn update-scene-action
  [scene-id action state]
  (re-frame/dispatch [::events/edit-scene-action {:scene-id scene-id :action action :state state}]))

(defn update-asset
  [scene-id id state]
  (re-frame/dispatch [::events/edit-asset {:scene-id scene-id :id id :state state}]))

(defn add-to-scene
  [scene-id name layer]
  (re-frame/dispatch [::events/add-to-scene {:scene-id scene-id :name name :layer layer}])
  (re-frame/dispatch [::events/add-to-current-scene {:name name :layer layer}]))

(defn to-props
  [konva-node]
  {:x (.x konva-node)
   :y (.y konva-node)
   :rotation (.rotation konva-node)
   :scale-x (.scaleX konva-node)
   :scale-y (.scaleY konva-node)})

(defn reset-transform
  []
  (re-frame/dispatch [::events/reset-object])
  (re-frame/dispatch [::events/reset-object-action])
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
    (re-frame/dispatch [::events/register-transform {:transformer transformer :target target}])
    (re-frame/dispatch [::events/select-object scene-id name])
    (.draggable target true)
    (.on target "dragmove" (fn [e] (update-object scene-id name (-> e .-currentTarget to-props))))
    (.on target "transform" (fn [e] (update-object scene-id name (-> e .-currentTarget to-props))))
    (.attachTo transformer target)
    (.add (.getParent target) transformer)
    (-> target .getLayer .draw)))

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

(defn update-group-rect
  [group rect]
  (let [{:keys [width height]} @rect
        group-rect (.getClientRect group #js {:skipTransform true})
        group-width (.-width group-rect)
        group-height (.-height group-rect)
        group-x (.-x group-rect)
        group-y (.-y group-rect)]
    (when (and (= width 0) (= height 0))
      (reset! rect {:x group-x :y group-y :width group-width :height group-height}))))

(defn group
  [scene-id name object]
  (let [g (r/atom {:width 0 :height 0})]
    (fn []
      [:> Group (merge (object-params object) {:ref #(when % (update-group-rect % g))})
       [:> Rect (merge (rect-params scene-id name object) @g)]
       (for [child (:children object)]
        ^{:key (str scene-id child)} [draw-object scene-id child])])))

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
     [anim (-> object
               (assoc :on-mount #(re-frame/dispatch [::ie/register-animation (:name object) %1 %2]))
               (assoc :start false))]
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

(defn action-width
  [action actions]
  (case (-> action :type keyword)
    :sequence (->> (:data action) (map #(get actions (keyword %))) (map #(action-width % actions)) (reduce +))
    :parallel (->> (:data action) (map #(action-width % actions)) (reduce max))
    150))

(defn action-height
  [action actions]
  (case (-> action :type keyword)
    :sequence (->> (:data action) (map #(get actions (keyword %))) (map #(action-height % actions)) (reduce max))
    :parallel (->> (:data action) (map #(action-height % actions)) (reduce +))
    90))

(defn draw-action
  [action actions scene-id action-id x y]
  (let [offset-x (+ x 4)
        offset-y (+ y 4)]
    (case (-> action :type keyword)
      :parallel [:> Group {:x offset-x :y offset-y}
                 [:> Rect {:width (action-width action actions) :height (action-height action actions)
                           :fill "#c5cae9" :stroke "#bdbdbd" :stroke-width 2
                           :on-click #(re-frame/dispatch [::events/select-scene-action scene-id action-id])}]
                 (let [current-y (atom 0)]
                   (for [data-action (:data action)]
                     ^{:key (str @current-y)}
                     [:> Group {:y @current-y}
                      (do
                        (swap! current-y + (action-height data-action actions))
                        [draw-action data-action actions scene-id action-id offset-x offset-y])]))]
      :sequence [:> Group {:x offset-x :y offset-y}
                 [:> Rect {:width (action-width action actions) :height (action-height action actions)
                           :fill "#303f9f" :stroke "#bdbdbd" :stroke-width 2
                           :on-click #(re-frame/dispatch [::events/select-scene-action scene-id action-id])}]
                 (let [current-x (atom 0)]
                   (for [data-action-id (:data action)]
                     ^{:key (str @current-x)}
                     [:> Group {:x @current-x}
                      (do
                        (swap! current-x + (action-width (get actions (keyword data-action-id)) actions))
                        [draw-action (get actions (keyword data-action-id)) actions scene-id data-action-id offset-x offset-y])]))]
      [:> Group {:x offset-x :y offset-y}
       [:> Rect {:width (- 150 offset-x 4) :height (- 90 offset-y 4)
                 :fill "#3f51b5" :stroke "#bdbdbd" :stroke-width 2
                 :on-click #(re-frame/dispatch [::events/select-scene-action scene-id action-id])}]
       [:> Text {:x 10 :y 10 :fill "white" :text (:type action)}]]
      )))

(defn draw-actions
  []
  (let [shown-action (re-frame/subscribe [::es/shown-scene-action])
        scene-id (:scene-id @shown-action)
        action-id (-> @shown-action :action keyword)
        actions (re-frame/subscribe [::subs/scene-actions scene-id])
        action (get @actions action-id)]
    [:> Group {:x 300 :y 300 :draggable true}
     [draw-action action @actions scene-id action-id 0 0]]))

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
            :actions [draw-actions]
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
   [na/form-input {:label "x" :default-value (:x @props) :on-change #(swap! props assoc :x (-> %2 .-value js/parseInt)) :inline? true}]
   [na/form-input {:label "y" :default-value (:y @props) :on-change #(swap! props assoc :y (-> %2 .-value js/parseInt)) :inline? true}]
   [na/form-input {:label "width" :default-value (:width @props) :on-change #(swap! props assoc :width (-> %2 .-value js/parseInt)) :inline? true}]
   [na/form-input {:label "height" :default-value (:height @props) :on-change #(swap! props assoc :height (-> %2 .-value js/parseInt)) :inline? true}]
   [na/form-input {:label "rotation" :default-value (:rotation @props) :on-change #(swap! props assoc :rotation (-> %2 .-value js/parseInt)) :inline? true}]
   [na/form-input {:label "scale x" :default-value (:scale-x @props) :on-change #(swap! props assoc :scale-x (-> %2 .-value js/parseFloat)) :inline? true}]
   [na/form-input {:label "scale y" :default-value (:scale-y @props) :on-change #(swap! props assoc :scale-y (-> %2 .-value js/parseFloat)) :inline? true}]])

(defn properties-panel-transparent
  [props]
  [:div
   [properties-panel-common props]])

(defn properties-panel-image
  [props]
  [:div
   [na/form-input {:label "src" :default-value (:src @props) :on-change #(swap! props assoc :src (-> %2 .-value)) :inline? true}]
   [properties-panel-common props]])

(defn properties-panel-animation
  [props]
  [:div
   [na/form-input {:label "name" :default-value (:name @props) :on-change #(swap! props assoc :name (-> %2 .-value)) :inline? true}]
   [na/form-input {:label "anim" :default-value (:anim @props) :on-change #(swap! props assoc :anim (-> %2 .-value)) :inline? true}]
   [na/form-input {:label "speed" :default-value (:speed @props) :on-change #(swap! props assoc :speed (-> %2 .-value js/parseFloat)) :inline? true}]
   [properties-panel-common props]])

(defn properties-panel-background
  [props]
  [:div
   [na/form-input {:label "src" :default-value (:src @props) :on-change #(swap! props assoc :src (-> %2 .-value)) :inline? true}]])

(declare dispatch-action-panel)

(defn action-properties-panel-action
  [data props scene-id]
  [:div
   [na/form-input {:label "type" :default-value (:type data) :on-change #(swap! props assoc :type (-> %2 .-value)) :inline? true}]
   [na/form-input {:label "id" :default-value (:id data) :on-change #(swap! props assoc :id (-> %2 .-value)) :inline? true}]
   [na/form-group {}
    [na/form-button {:content "Select" :on-click #(re-frame/dispatch [::events/select-scene-action scene-id (:id data)])}]
    [na/form-button {:content "Show" :on-click  #(re-frame/dispatch [::events/show-scene-action scene-id (:id data)])}]]])

(defn action-properties-panel-state
  [data props]
  [:div
   [na/form-input {:label "type" :default-value (:type data) :on-change #(swap! props assoc :type (-> %2 .-value)) :inline? true}]
   [na/form-input {:label "target" :default-value (:target data) :on-change #(swap! props assoc :target (-> %2 .-value)) :inline? true}]
   [na/form-input {:label "id" :default-value (:id data) :on-change #(swap! props assoc :id (-> %2 .-value)) :inline? true}]])

(defn action-properties-panel-animation
  [data props]
  [:div
   [na/form-input {:label "type" :default-value (:type data) :on-change #(swap! props assoc :type (-> %2 .-value)) :inline? true}]
   [na/form-input {:label "target" :default-value (:target data) :on-change #(swap! props assoc :target (-> %2 .-value)) :inline? true}]
   [na/form-input {:label "id" :default-value (:id data) :on-change #(swap! props assoc :id (-> %2 .-value)) :inline? true}]])

(defn action-properties-panel-audio
  [data props]
  [:div
   [na/form-input {:label "type" :default-value (:type data) :on-change #(swap! props assoc :type (-> %2 .-value)) :inline? true}]
   [na/form-input {:label "id" :default-value (:id data) :on-change #(swap! props assoc :id (-> %2 .-value)) :inline? true}]
   [na/form-input {:label "start" :default-value (:start data) :on-change #(swap! props assoc :start (-> %2 .-value js/parseFloat)) :inline? true}]
   [na/form-input {:label "duration" :default-value (:duration data) :on-change #(swap! props assoc :duration (-> %2 .-value js/parseFloat)) :inline? true}]
   [na/form-input {:label "offset" :default-value (:offset data) :on-change #(swap! props assoc :offset (-> %2 .-value js/parseFloat)) :inline? true}]
   [na/form-input {:label "loop" :default-value (:loop data) :on-change #(swap! props assoc :loop (-> %2 .-value (= "true"))) :inline? true}]])

(defn action-properties-panel-scene
  [data props]
  [:div
   [na/form-input {:label "type" :default-value (:type data) :on-change #(swap! props assoc :type (-> %2 .-value)) :inline? true}]
   [na/form-input {:label "scene-id" :default-value (:scene-id data) :on-change #(swap! props assoc :scene-id (-> %2 .-value)) :inline? true}]])

(defn action-properties-panel-empty
  [data props]
  [:div
   [na/form-input {:label "type" :default-value (:type data) :on-change #(swap! props assoc :type (-> %2 .-value)) :inline? true}]
   [na/form-input {:label "duration" :default-value (:duration data) :on-change #(swap! props assoc :duration (-> %2 .-value js/parseInt)) :inline? true}]])

(defn action-properties-panel-sequence
  [data props scene-id]
  (let [edit-mode (r/atom false)
        current-value (atom (clojure.string/join "\n" (:data data)))]
    (swap! props assoc :data (:data data))
    (fn [data props scene-id]
    [:div
     [na/form-input {:label "type" :default-value (:type data) :on-change #(swap! props assoc :type (-> %2 .-value)) :inline? true}]
     (if @edit-mode
       [:div
        [na/text-area {:label "data"
                       :default-value @current-value
                       :on-change #(reset! current-value (-> %2 .-value))}]
        [na/form-button {:content "OK" :on-click #(do
                                                    (swap! props assoc :data (clojure.string/split @current-value "\n"))
                                                    (reset! edit-mode false))}]]
       [:div
        (for [action (:data @props)]
          ^{:key (str scene-id action)}
          [:p [:a {:on-click #(re-frame/dispatch [::events/select-scene-action scene-id action])} (str action)]])
        [na/form-button {:content "Edit" :on-click #(reset! edit-mode true)}]])
     ])))

(defn action-properties-panel-parallel
  [data props scene-id]
  (let [edit-action (r/atom nil)
        current-value (r/atom {})]
    (swap! props assoc :data (:data data))
    (fn [data props scene-id]
      [:div
       [na/form-input {:label "type" :default-value (:type data) :on-change #(swap! props assoc :type (-> %2 .-value)) :inline? true}]
       (if @edit-action
         [:div
          (dispatch-action-panel (get-in @props [:data @edit-action]) current-value scene-id)
          [na/form-button {:content "OK" :on-click #(do
                                                      (swap! props assoc-in [:data @edit-action] @current-value)
                                                      (reset! edit-action nil))}]]
         [:div
          (for [action (->> @props :data (map-indexed (fn [idx itm] [idx itm])))]
            ^{:key (str scene-id (first action))}
            [:div
             [:p [:a {:on-click #(do
                                   (reset! current-value (get-in @props [:data (first action)]))
                                   (reset! edit-action (first action)))} (str (:type (second action)))]]])])
       ])))

(defn action-properties-panel-common
  [data props]
  [:div
   [na/form-input {:label "type" :default-value (:type data) :on-change #(swap! props assoc :type (-> %2 .-value)) :inline? true}]])

(defn dispatch-action-panel
  [action-data props scene-id]
  (case (-> action-data :type keyword)
    :action [action-properties-panel-action action-data props scene-id]
    :scene [action-properties-panel-scene action-data props]
    :state [action-properties-panel-state action-data props]
    :animation [action-properties-panel-animation action-data props]
    :audio [action-properties-panel-audio action-data props]
    :empty [action-properties-panel-empty action-data props]
    :sequence [action-properties-panel-sequence action-data props scene-id]
    :parallel [action-properties-panel-parallel action-data props scene-id]
    [action-properties-panel-common action-data props]))

(defn object-action-properties-panel
  []
  (let [props (r/atom {})]
    (fn []
      (let [action-id (re-frame/subscribe [::es/selected-object-action])
            {:keys [scene-id name action]} @action-id
            o (re-frame/subscribe [::subs/scene-object scene-id name])
            action-data (-> @o :actions (get (keyword action)))]
        ^{:key (str scene-id name action)}
        [na/form {}
         [na/form-input {:label "on" :default-value (:on action-data) :on-change #(swap! props assoc :on (-> %2 .-value)) :inline? true}]
         [dispatch-action-panel action-data props scene-id]

         [na/form-button {:content "Save" :on-click #(update-object-action scene-id name action @props)}]]
        ))))

(defn scene-action-properties-panel
  []
  (let [props (r/atom {})]
    (fn []
      (let [action-id (re-frame/subscribe [::es/selected-scene-action])
            {:keys [scene-id action]} @action-id
            action-data (re-frame/subscribe [::subs/scene-action scene-id action])]
        ^{:key (str scene-id action)}
        [na/form {}
         (dispatch-action-panel @action-data props scene-id)

         [na/form-button {:content "Save" :on-click #(update-scene-action scene-id action @props)}]]
        ))))

(defn add-object-action-panel
  [scene-id name on-save]
  (let [props (r/atom {})]
    (fn [scene-id name on-save]
      [na/form {}
       [sa/Dropdown {:placeholder "Type" :search true :selection true :options action-types :on-change #(swap! props assoc :type (.-value %2))}]
       [na/divider {}]
       [na/form-input {:label "name" :default-value (:scene-name @props) :on-change #(swap! props assoc :scene-name (-> %2 .-value)) :inline? true}]
       [na/form-input {:label "on" :default-value (:on @props) :on-change #(swap! props assoc :on (-> %2 .-value)) :inline? true}]
       [na/divider {}]
       [dispatch-action-panel @props props scene-id]
       [na/divider {}]
       [na/form-button {:content "Add" :on-click #(do (update-object-action scene-id name (:scene-name @props) @props)
                                                      (on-save))}]
       ])))

(defn new-object-action-section
  []
  (let [mode (r/atom nil)]
    (fn []
      (let [{:keys [scene-id name]} @(re-frame/subscribe [::es/selected-object])]
        (if (= @mode :add-action)
          [add-object-action-panel scene-id name #(reset! mode nil)]
          [na/button {:basic? true :content "Add action" :on-click #(reset! mode :add-action)}])))))

(defn actions-panel
  []
  (let [mode (r/atom nil)]
    (fn []
      (let [object (re-frame/subscribe [::es/selected-object])
            {:keys [scene-id name]} @object
            o (re-frame/subscribe [::subs/scene-object scene-id name])]
        [:div
         (for [action (-> @o :actions keys)]
           ^{:key (str scene-id action)}
           [:div
            [:a {:on-click #(re-frame/dispatch [::events/select-object-action scene-id name action])} (str action)]])
         [na/divider {}]
         [new-object-action-section]
         ]))))

(defn dispatch-properties-panel
  [props]
  (case (-> @props :type keyword)
    :image [properties-panel-image props]
    :transparent [properties-panel-transparent props]
    :animation [properties-panel-animation props]
    :background [properties-panel-background props]
    [properties-panel-common props]))

(defn properties-panel
  []
  (let [prev (r/atom {})
        props (r/atom {})
        activeIndex (r/atom -1)]
    (fn []
      (let [object (re-frame/subscribe [::es/selected-object])
            {:keys [scene-id name]} @object
            o (re-frame/subscribe [::subs/scene-object scene-id name])]
        (check-prev prev @o props)
        [sa/Accordion
         [sa/AccordionTitle {:active (= 0 @activeIndex) :on-click #(reset! activeIndex 0)}
          [na/icon {:name "dropdown"}]
          "Properties"]
         [sa/AccordionContent {:active (= 0 @activeIndex)}
          [na/form {}
           [sa/Dropdown {:placeholder "Type" :search true :selection true :options object-types :value (:type @props)
                         :on-change #(swap! props assoc :type (.-value %2))}]
           ^{:key (str scene-id name)} [dispatch-properties-panel props]
           [na/form-button {:content "Save" :on-click #(do (update-object scene-id name @props)
                                                           (update-current-scene-object name @props))}]]]
         [sa/AccordionTitle {:active (= 1 @activeIndex) :on-click #(reset! activeIndex 1)}
          [na/icon {:name "dropdown"}]
          "States"]
         [sa/AccordionContent {:active (= 1 @activeIndex)}
          [sa/ItemGroup {:divided true}
            (for [[state-id state] (:states @o)]
              ^{:key (str scene-id state-id)}
              [sa/Item {}
                [sa/ItemContent {:vertical-align "middle"}
                 (str state-id)
                 [:div {:style {:float "right"}}
                  [na/button {:size "mini" :content "Set default" :on-click #(re-frame/dispatch [::events/set-default-object-state scene-id name state-id])}]
                  [na/button {:size "mini" :content "Edit" :on-click #(re-frame/dispatch [::events/select-object-state scene-id name state-id])}]
                  [na/button {:size "mini" :content "Delete" :on-click #(re-frame/dispatch [::events/delete-object-state scene-id name state-id])}]]
                 ]]
               )]
          [na/divider {}]
          [na/button {:basic? true :content "Add" :on-click #(re-frame/dispatch [::events/select-object-state scene-id name])}]
          ]
         [sa/AccordionTitle {:active (= 2 @activeIndex) :on-click #(reset! activeIndex 2)}
          [na/icon {:name "dropdown"}]
          "Actions"]
         [sa/AccordionContent {:active (= 2 @activeIndex)} [actions-panel]]]
        ))))

(defn object-state-properties-panel
  []
  (let [props (r/atom {})]
    (fn []
      (let [{:keys [scene-id name state]} @(re-frame/subscribe [::es/selected-object-state])
            object (re-frame/subscribe [::subs/scene-object scene-id name])
            state-data (-> @object :states (get (keyword state)))]
        (swap! props #(merge state-data %))
        (swap! props assoc :state-id state)
        [na/form {}
         [na/form-input {:label "id" :default-value state :on-change #(swap! props assoc :state-id (-> %2 .-value)) :inline? true}]
         [sa/Dropdown {:placeholder "Type" :search true :selection true :options object-types :value (:type @props)
                       :on-change #(swap! props assoc :type (.-value %2))}]
         [na/divider {}]
         [dispatch-properties-panel props]

         [na/form-button {:content "Save" :on-click #(update-object-state scene-id name state @props)}]]
        ))))

(defn properties-rail
  []
  (let [object (re-frame/subscribe [::es/selected-object])
        object-action (re-frame/subscribe [::es/selected-object-action])
        object-state (re-frame/subscribe [::es/selected-object-state])
        scene-action (re-frame/subscribe [::es/selected-scene-action])]
    [:div
     (if @object
       [na/segment {}
        [na/header {:as "h4" :floated "left" :content "Object properties"}]
        [na/header {:floated "right" :sub? true}
         [na/icon {:name "close" :on-click remove-transform}]]
        [na/divider {:clearing? true}]

        [properties-panel]])
     (if @object-action
       [na/segment {}
        [na/header {:as "h4" :floated "left" :content "Object action"}]
        [na/header {:floated "right" :sub? true}
         [na/icon {:name "close" :on-click #(re-frame/dispatch [::events/reset-object-action])}]]
        [na/divider {:clearing? true}]

        [object-action-properties-panel]])
     (if @object-state
       [na/segment {}
        [na/header {:as "h4" :floated "left" :content "Object state"}]
        [na/header {:floated "right" :sub? true}
         [na/icon {:name "close" :on-click #(re-frame/dispatch [::events/reset-object-state])}]]
        [na/divider {:clearing? true}]

        ^{:key (str (:scene-id @object-state) (:name @object-state) (:state @object-state))}
        [object-state-properties-panel]])
     (if @scene-action
       [na/segment {}
        [na/header {:as "h4" :floated "left" :content "Scene action"}]
        [na/header {:floated "right" :sub? true}
         [na/icon {:name "close" :on-click #(re-frame/dispatch [::events/reset-scene-action])}]]
        [na/divider {:clearing? true}]

        [scene-action-properties-panel]])]))

(defn add-object-panel
  []
  (let [scene-id (re-frame/subscribe [::subs/current-scene])
        props (r/atom {})]
    (fn []
      [na/segment {}
       [na/header {:as "h4" :floated "left" :content "Object action"}]
       [na/header {:floated "right" :sub? true}
        [na/icon {:name "close" :on-click #(re-frame/dispatch [::events/reset-shown-form])}]]
       [na/divider {:clearing? true}]

       [na/form {}
        [sa/Dropdown {:placeholder "Type" :search true :selection true :options object-types :on-change #(swap! props assoc :type (.-value %2))}]
        [na/divider {}]
        [na/form-input {:label "name" :default-value (:scene-name @props) :on-change #(swap! props assoc :scene-name (-> %2 .-value)) :inline? true}]
        [na/form-input {:label "layer" :default-value (:scene-layer @props) :on-change #(swap! props assoc :scene-layer (-> %2 .-value js/parseInt)) :inline? true}]
        [na/divider {}]
        [dispatch-properties-panel props]
        [na/divider {}]
        [na/form-button {:content "Add" :on-click #(do (update-object @scene-id (:scene-name @props) @props)
                                                       (update-current-scene-object (:scene-name @props) @props)
                                                       (add-to-scene @scene-id (:scene-name @props) (:scene-layer @props))
                                                       (re-frame/dispatch [::events/reset-shown-form]))}]
       ]])))

(defn list-objects-panel
  []
  (let [scene-id (re-frame/subscribe [::subs/current-scene])
        scene (re-frame/subscribe [::subs/scene @scene-id])]
    (fn []
      [na/segment {}
       [na/header {:as "h4" :floated "left" :content "Objects"}]
       [na/header {:floated "right" :sub? true}
        [na/icon {:name "close" :on-click #(re-frame/dispatch [::events/reset-shown-form])}]]
       [na/divider {:clearing? true}]

        [sa/ItemGroup {:divided true}
         (for [[id object] (:objects @scene)]
           ^{:key (str @scene-id id)}
           [sa/Item {}
            [sa/ItemContent {:vertical-align "middle" :content (-> id str)
                             :on-click #(re-frame/dispatch [::events/select-object @scene-id id])}]]
           )]])))

(def page-elements 10)

(defn prev-page-elements
  [page]
  (* page-elements (dec page)))

(defn total-pages
  [elements]
  (Math/ceil (/ elements page-elements)))

(defn asset-properties-panel
  [scene-id id]
  (let [props (r/atom {})]
    (fn [scene-id id]
      (let [asset (re-frame/subscribe [::subs/scene-asset scene-id id])]
        [na/form {}
         [sa/Dropdown {:placeholder "Type" :search true :selection true :options asset-types
                       :default-value (:type @asset) :on-change #(swap! props assoc :type (.-value %2))}]
         [na/divider {}]
         [na/form-input {:label "url" :default-value (:url @asset) :on-change #(swap! props assoc :url (-> %2 .-value)) :inline? true}]
         [na/form-input {:label "size" :default-value (:size @asset) :on-change #(swap! props assoc :size (-> %2 .-value js/parseInt)) :inline? true}]
         [na/divider {}]
         [na/form-button {:content "Save" :on-click #(do (update-asset scene-id id @props)
                                                         (re-frame/dispatch [::events/reset-asset]))}]
         ]))))

(defn edit-asset-section
  []
  (let [{:keys [scene-id id]} @(re-frame/subscribe [::es/selected-asset])]
    (if id
      ^{:key (str scene-id id)} [asset-properties-panel scene-id id]
      [na/button {:basic? true :content "Add asset" :on-click #(re-frame/dispatch [::events/select-new-asset])}])))

(defn list-assets-panel
  []
  (let [scene-id (re-frame/subscribe [::subs/current-scene])
        scene (re-frame/subscribe [::subs/scene @scene-id])
        page (r/atom 1)]
    (fn []
      [na/segment {}
       [na/header {:as "h4" :floated "left" :content "Assets"}]
       [na/header {:floated "right" :sub? true}
        [na/icon {:name "close" :on-click #(re-frame/dispatch [::events/reset-shown-form])}]]
       [na/divider {:clearing? true}]

       [sa/ItemGroup {:divided true}
        (for [[key asset] (->> @scene :assets (map-indexed (fn [idx itm] [idx itm])) (drop (prev-page-elements @page)) (take page-elements))]
          ^{:key (str @scene-id key)}
          [sa/Item {}
           [sa/ItemContent {:on-click #(re-frame/dispatch [::events/select-asset @scene-id key])}
            (str key ". " (:type asset) " : " (:url asset)) ]]
          )]
       [na/divider {:clearing? true}]
       [sa/Pagination {:active-page @page :total-pages (total-pages (->> @scene :assets count))
                       :first-item nil :last-item nil :ellipsis-item nil
                       :on-page-change #(reset! page (.-activePage %2))}]
       [na/divider {}]
       [edit-asset-section]])))

(defn list-scenes-panel
  []
  (let [scenes (re-frame/subscribe [::subs/course-scenes])]
    (fn []
      [na/segment {}
       [na/header {:as "h4" :floated "left" :content "Scenes"}]
       [na/header {:floated "right" :sub? true}
        [na/icon {:name "close" :on-click #(re-frame/dispatch [::events/reset-shown-form])}]]
       [na/divider {:clearing? true}]

       [sa/ItemGroup {:divided true}
        (for [scene-id @scenes]
          ^{:key (str scene-id)}
          [sa/Item {}
           [sa/ItemContent {:as "a" :content scene-id :on-click #(re-frame/dispatch [::ie/set-current-scene scene-id])}]])]])))

(defn shown-form-panel
  []
  (let [show-form (re-frame/subscribe [::es/shown-form])]
    (case @show-form
      :add-object [add-object-panel]
      :list-objects [list-objects-panel]
      :list-assets [list-assets-panel]
      :list-scenes [list-scenes-panel]
      [:div])))

(defn editor []
  (let [scene-id (re-frame/subscribe [::subs/current-scene])]
    [:div {:class-name "ui segment"}
     [:h2 {:class-name "ui dividing header"} "Editor"]
     [:div {:class-name "ui segment"}
      [na/grid {}
       [na/grid-column {:width 12}
        [course]
        [na/divider {:clearing? true}]
        [na/button {:content "Play" :on-click #(re-frame/dispatch [::events/set-screen :play-scene])}]
        [na/button {:content "Editor" :on-click #(do (re-frame/dispatch [::ie/set-current-scene @scene-id])
                                                     (re-frame/dispatch [::events/set-screen :editor]))}]
        [na/button {:content "Actions" :on-click #(re-frame/dispatch [::events/set-screen :actions])}]]
       [na/grid-column {:width 4}
        [na/button {:basic? true :content "Add object" :on-click #(re-frame/dispatch [::events/show-form :add-object])}]
        [na/button {:basic? true :content "List objects" :on-click #(re-frame/dispatch [::events/show-form :list-objects])}]
        [na/button {:basic? true :content "List assets" :on-click #(re-frame/dispatch [::events/show-form :list-assets])}]
        [na/button {:basic? true :content "List scenes" :on-click #(re-frame/dispatch [::events/show-form :list-scenes])}]
        [shown-form-panel]
        [properties-rail]]]

      ]]))