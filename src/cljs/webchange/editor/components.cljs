(ns webchange.editor.components
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.subs :as subs]
    [webchange.common.kimage :refer [kimage]]
    [webchange.common.anim :refer [anim animations init-spine-player]]
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
    [sodium.extensions :as nax]
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
                   {:key :sequence-data :value :sequence-data :text "Sequence Data"}
                   {:key :parallel :value :parallel :text "Parallel"}

                   {:key :add-alias :value :add-alias :text "Add alias"}
                   {:key :add-animation :value :add-animation :text "Add animation"}
                   {:key :start-animation :value :start-animation :text "Start animation"}
                   {:key :remove-animation :value :remove-animation :text "Remove animation"}
                   {:key :set-skin :value :set-skin :text "Set skin"}
                   {:key :animation-props :value :animation-props :text "Animation props"}
                   {:key :animation-sequence :value :animation-sequence :text "Animation sequence"}
                   {:key :transition :value :transition :text "Transition"}
                   {:key :placeholder-audio :value :placeholder-audio :text "Placeholder audio"}
                   {:key :test-transitions-collide :value :test-transitions-collide :text "Test transitions collide"}

                   {:key :dataset-var-provider :value :dataset-var-provider :text "Dataset var provider"}
                   {:key :lesson-var-provider :value :lesson-var-provider :text "Lesson var provider"}
                   {:key :vars-var-provider :value :vars-var-provider :text "Vars var provider"}
                   {:key :test-var :value :test-var :text "Test var"}
                   {:key :test-var-scalar :value :test-var-scalar :text "Test var scalar"}
                   {:key :test-var-list :value :test-var-list :text "Test var list"}
                   {:key :test-value :value :test-value :text "Test value"}
                   {:key :case :value :case :text "Case"}
                   {:key :counter :value :counter :text "Counter"}
                   {:key :set-variable :value :set-variable :text "set variable"}
                   {:key :set-progress :value :set-progress :text "Set progress"}
                   {:key :copy-variable :value :copy-variable :text "Copy variable"}])

(def asset-types [{:key :image :value :image :text "Image"}
                  {:key :audio :value :audio :text "Audio"}
                  {:key :anim-text :value :anim-text :text "Animation Text"}
                  {:key :anim-texture :value :anim-texture :text "Animation Texture"}])

(def item-field-types [{:key :number :value :number :text "Number"}
                       {:key :string :value :string :text "String"}
                       {:key :image :value :image :text "Image"}
                       {:key :audio :value :audio :text "Audio"}])

(def actions-with-selected-actions [{:key :to-sequence :value :to-sequence :text "Combine to sequence"}
                                    {:key :to-parallel :value :to-parallel :text "Combine to parallel"}
                                    {:key :to-sequence-data :value :to-sequence-data :text "Convert to sequence"}
                                    {:key :to-parallel-data :value :to-parallel-data :text "Convert to parallel"}])

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

(defn update-asset
  [scene-id id state]
  (re-frame/dispatch [::events/edit-asset {:scene-id scene-id :id id :state state}]))

(defn add-to-scene
  [scene-id name layer]
  (re-frame/dispatch [::events/add-to-scene {:scene-id scene-id :name name :layer layer}])
  (re-frame/dispatch [::events/add-to-current-scene {:name name :layer layer}]))

(defn remove-from-scene
  [scene-id object-name]
  (let [prepared-name (name object-name)]
    (re-frame/dispatch [::events/remove-from-scene {:scene-id scene-id :name prepared-name}])
    (re-frame/dispatch [::events/remove-from-current-scene {:name prepared-name}])))

(defn remove-object [scene-id name]
  (remove-from-scene scene-id name)
  (re-frame/dispatch [::events/remove-object {:scene-id scene-id :target name}])
  (re-frame/dispatch [::events/remove-current-scene-object {:target name}]))

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
  [:> Group (object-params (dissoc object :x :y))
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
  (let [params (object-params object)
        animation-name (or (:scene-name object) (:name object))]
    [:> Group params
     [anim (-> object
               (assoc :on-mount #(re-frame/dispatch [::ie/register-animation animation-name %]))
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

(def base-offset 18)

(defn action-width
  [action actions]
  (case (-> action :type keyword)
    :sequence (->> (:data action) (map #(get actions (keyword %))) (map #(action-width % actions)) (map #(+ % base-offset)) (reduce + base-offset))
    :sequence-data (->> (:data action) (map #(action-width % actions)) (map #(+ % base-offset)) (reduce + base-offset))
    :parallel (->> (:data action) (map #(action-width % actions)) (reduce max) (+ base-offset base-offset))
    (+ base-offset 150 base-offset)))

(defn action-height
  [action actions]
  (case (-> action :type keyword)
    :sequence (->> (:data action) (map #(get actions (keyword %))) (map #(action-height % actions)) (reduce max) (+ base-offset base-offset))
    :sequence-data (->> (:data action) (map #(action-height % actions)) (reduce max) (+ base-offset base-offset))
    :parallel (->> (:data action) (map #(action-height % actions)) (map #(+ % base-offset)) (reduce + base-offset))
    (+ base-offset 90 base-offset)))

(defn draw-action
  [action actions scene-id action-id x y]
  (let [offset-x (+ x base-offset)
        offset-y (+ y base-offset)]
    [:> Group {:x offset-x :y offset-y :on-click #(re-frame/dispatch [::events/select-scene-action action-id])}
      (case (-> action :type keyword)
        :parallel [:> Group {}
                   [:> Rect {:width (action-width action actions) :height (action-height action actions)
                             :fill "#c5cae9" :stroke "#bdbdbd" :stroke-width 2}]
                   (let [current-y (atom 0)]
                     (for [data-action (:data action)]
                       ^{:key (str @current-y)}
                       [:> Group {:x 0 :y @current-y}
                        (do
                          (swap! current-y + (action-height data-action actions) base-offset)
                          [draw-action data-action actions scene-id action-id 0 0])]))]
        :sequence [:> Group {}
                   [:> Rect {:width (action-width action actions) :height (action-height action actions)
                             :fill "#303f9f" :stroke "#bdbdbd" :stroke-width 2}]
                   (let [current-x (atom 0)]
                     (for [data-action-id (:data action)]
                       ^{:key (str @current-x)}
                       [:> Group {:x @current-x}
                        (do
                          (swap! current-x + (action-width (get actions (keyword data-action-id)) actions) base-offset)
                          [draw-action (get actions (keyword data-action-id)) actions scene-id data-action-id 0 0])]))]
        :sequence-data [:> Group {}
                   [:> Rect {:width (action-width action actions) :height (action-height action actions)
                             :fill "#303f9f" :stroke "#bdbdbd" :stroke-width 2}]
                   (let [current-x (atom 0)]
                     (for [data-action (:data action)]
                       ^{:key (str @current-x)}
                       [:> Group {:x @current-x}
                        (do
                          (swap! current-x + (action-width data-action actions) base-offset)
                          [draw-action data-action actions scene-id action-id 0 0])]))]
        [:> Group {}
         [:> Rect {:width (action-width action actions) :height (action-height action actions)
                   :fill "#3f51b5" :stroke "#bdbdbd" :stroke-width 2}]
         [:> Text {:x 10 :y 10 :fill "white" :text (:type action)}]]
        )
  ]))

(defn draw-actions
  []
  (let [scale (r/atom 1)]
    (fn []
      (let [shown-action (re-frame/subscribe [::es/shown-scene-action])
            scene-id (:scene-id @shown-action)
            action-id (-> @shown-action :action keyword)
            actions (re-frame/subscribe [::subs/scene-actions scene-id])
            action (get @actions action-id)]
        [:> Group {:x 50 :y 300 :scale {:x @scale :y @scale} :on-wheel (fn [e]
                                                                         (let [delta (-> e .-evt .-deltaY (/ 1000))]
                                                                           (-> e .-evt .preventDefault)
                                                                           (swap! scale + delta)))}
         [draw-action action @actions scene-id action-id 0 0]]))))

(defn dataset-lessons-panel
  []
  (let [lessons @(re-frame/subscribe [::es/current-dataset-lessons])]
    [na/segment {}
     [na/header {:as "h4"}
      "Lessons"
      [:div {:style {:float "right"}}
       [na/icon {:name "add" :link? true
                 :on-click #(re-frame/dispatch [::events/show-add-dataset-lesson-form])}]]]
     [na/divider {:clearing? true}]
     [sa/ItemGroup {}
      (for [{name :name id :id} lessons]
        ^{:key name}
        [sa/Item {}
         [sa/ItemContent {}
          [:div {:style {:float "left"}} [:p name]]
          [:div {:style {:float "right"}}
           [na/icon {:name "edit" :link? true
                     :on-click #(re-frame/dispatch [::events/show-edit-dataset-lesson-form id])}]
           [na/icon {:name "remove" :link? true
                     :on-click #(re-frame/dispatch [::events/delete-dataset-lesson id])}]]
          ]])]]))

(defn dataset-items-panel
  []
  (let [items @(re-frame/subscribe [::es/current-dataset-items])]
    [na/segment {}
     [na/header {:as "h4"}
      "Items"
      [:div {:style {:float "right"}}
       [na/icon {:name "add" :link? true
                 :on-click #(re-frame/dispatch [::events/show-add-dataset-item-form])}]]]
     [na/divider {:clearing? true}]
     [sa/ItemGroup {}
      (for [{name :name item-id :id} items]
        ^{:key name}
        [sa/Item {}
         [sa/ItemContent {}
          [:div {:style {:float "left"}} [:p name]]
          [:div {:style {:float "right"}}
           [na/icon {:name "edit" :link? true
                     :on-click #(re-frame/dispatch [::events/show-edit-dataset-item-form item-id])}]
           [na/icon {:name "remove" :link? true
                     :on-click #(re-frame/dispatch [::events/delete-dataset-item item-id])}]]

          ]])]]))

(defn dataset-info
  []
  [na/segment-group {:horizontal? true}
   [dataset-items-panel]
   [dataset-lessons-panel]
   ])

(defn toggle-item-in-lesson
  [items toggle id]
  (let [items-v (into [] items)]
    (if toggle
      (conj items-v {:id id})
      (filter #(not= id (:id %)) items-v))))

(defn add-dataset-lesson-form
  []
  (let [data (r/atom {:items []})]
    (fn []
      (let [items @(re-frame/subscribe [::es/current-dataset-items])
            loading @(re-frame/subscribe [:loading])]
        [na/segment {:loading? (when (:add-dataset-lesson loading))}
         [na/header {:as "h4" :content "Add dataset lesson"}]
         [na/divider {:clearing? true}]
         [na/form {}
          [na/form-input {:label "name" :default-value (:name @data) :on-change #(swap! data assoc :name (-> %2 .-value)) :inline? true}]
          [na/divider {}]
          (for [{name :name id :id} items]
            ^{:key name}
            [sa/Item {}
             [sa/ItemContent {}
              [na/checkbox {:label name
                            :on-change #(swap! data update-in [:data :items] toggle-item-in-lesson (.-checked %2) id)}]
              ]])
          [na/divider {}]
          [na/form-button {:content "Add" :on-click #(re-frame/dispatch [::events/add-dataset-lesson @data])}]
          ]]))))

(defn offsetX [e]
  (let [rect (-> e .-target .getBoundingClientRect)]
    (- (.-clientX e) (.-left rect))))

(defn offsetY [e]
  (let [rect (-> e .-target .getBoundingClientRect)]
    (- (.-clientY e) (.-top rect))))

(defn edit-dataset-lesson-form
  []
  (let [lesson-set-id @(re-frame/subscribe [::es/current-dataset-lesson-id])
        {{dataset-lesson :items} :data} @(re-frame/subscribe [::es/dataset-lesson lesson-set-id])
        data (r/atom {:data {:items (into [] dataset-lesson)}})]
    (fn []
      (let [items @(re-frame/subscribe [::es/current-dataset-items])
            loading @(re-frame/subscribe [:loading])
            lesson-items (-> @data :data :items)]
        [na/segment {:loading? (when (:edit-dataset-lesson loading))}
         [na/header {:as "h4" :content "Edit dataset lesson"}]
         [na/divider {:clearing? true}]
         [na/grid {:columns 2}
          [na/divider {:vertical? true} "add"]
          [na/grid-row {}
           [na/grid-column {}
            [:div {:style {:min-height 300}
                   :on-drag-over #(.preventDefault %)
                   :on-drop (fn [e]
                              (swap! data update-in [:data :items] toggle-item-in-lesson false (-> e
                                                                                                   (.-dataTransfer)
                                                                                                   (.getData "text/plain")
                                                                                                   (js/parseInt))))}
              (for [{name :name id :id} items
                    :when (not-any? #(= id (:id %)) lesson-items)]
                ^{:key name}
                [:div {:draggable true :on-drag-start #(-> (.-dataTransfer %) (.setData "text/plain" id))}
                 [sa/Item {:content name}]])]]
           [na/grid-column {}
            [na/segment {:dropzone "true"}
             [:div {:style {:min-height 300}
                    :on-drag-over #(.preventDefault %)
                    :on-drop (fn [e]
                               (swap! data update-in [:data :items] toggle-item-in-lesson true (-> e
                                                                                                   (.-dataTransfer)
                                                                                                   (.getData "text/plain")
                                                                                                   (js/parseInt))))}
              (for [{id :id} lesson-items
                    :let [name (-> (filter #(= id (:id %)) items) first :name)]]
                ^{:key name}
                [:div {:draggable true :on-drag-start #(-> (.-dataTransfer %) (.setData "text/plain" id))}
                 [sa/Item {:content name}]])
              ]]]
           ]]

          [na/divider {}]
          [na/form-button {:content "Save" :on-click #(re-frame/dispatch [::events/edit-dataset-lesson lesson-set-id @data])}]
          ]))))

(defn dataset-item-fields-panel
  [data-atom]
  (let [dataset-id @(re-frame/subscribe [::es/current-dataset-id])
        dataset @(re-frame/subscribe [::es/dataset dataset-id])]
    [sa/ItemGroup {}
     (for [{name :name type :type} (get-in dataset [:scheme :fields])]
       ^{:key name}
       [sa/Item {}
        [sa/ItemContent {}
         [na/form-input {:label name :inline? true
                         :default-value (get-in @data-atom [:data (keyword name)])
                         :on-change #(swap! data-atom assoc-in [:data (keyword name)] (-> %2 .-value))}]
         ]])]))

(defn add-dataset-item-form
  []
  (let [data (r/atom {})]
    (fn []
      (let [loading @(re-frame/subscribe [:loading])]
        [na/segment {:loading? (when (:add-dataset-item loading))}
         [na/header {:as "h4" :content "Add dataset item"}]
         [na/divider {:clearing? true}]
         [na/form {}
          [na/form-input {:label "name" :default-value (:name @data) :on-change #(swap! data assoc :name (-> %2 .-value)) :inline? true}]
          [na/divider {}]
          [dataset-item-fields-panel data]
          [na/divider {}]
          [na/form-button {:content "Add" :on-click #(re-frame/dispatch [::events/add-dataset-item @data])}]
          ]]))))

(defn edit-dataset-item-form
  []
  (let [item-id @(re-frame/subscribe [::es/current-dataset-item-id])
        item @(re-frame/subscribe [::es/dataset-item item-id])
        data (r/atom {:data (:data item)})]
    (fn []
      (let [loading @(re-frame/subscribe [:loading])]
        [na/segment {:loading? (when (:edit-dataset-item loading))}
         [na/header {:as "h4" :content "Edit dataset item"}]
         [na/divider {:clearing? true}]
         [na/form {}
          [dataset-item-fields-panel data]
          [na/divider {}]
          [na/form-button {:content "Save" :on-click #(re-frame/dispatch [::events/edit-dataset-item item-id @data])}]
          ]]))))

(defn remove-field
  [dataset field-name]
  (assoc dataset :fields (->> (:fields dataset)
                              (filter #(not= (:name %) field-name)))))
(defn dataset-fields-panel
  [data-atom]
  [sa/ItemGroup {}
  (for [field (:fields @data-atom)]
    ^{:key (:name field)}
    [sa/Item {}
     [sa/ItemContent {}
      [:p (str (:name field) " " (:type field))
       [na/button {:floated "right" :basic? true :content "Delete" :on-click #(swap! data-atom remove-field (:name field))}]
       ]
      ]])])

(defn add-dataset-field-panel
  [data-atom]
  (r/with-let [field-data (r/atom {})]
              [na/form-group {}
               [na/form-input {:label "name" :on-change #(swap! field-data assoc :name (-> %2 .-value)) :inline? true}]
               [sa/Dropdown {:placeholder "Type" :search true :selection true :options item-field-types :on-change #(swap! field-data assoc :type (.-value %2))}]
               [na/form-button {:content "Add field" :on-click #(swap! data-atom update-in [:fields] conj @field-data)}]]))

(defn add-dataset-form
  []
  (let [data (r/atom {})]
    (fn []
      (let [loading @(re-frame/subscribe [:loading])]
        [na/segment {:loading? (when (:add-dataset loading))}
         [na/header {:as "h4" :content "Add dataset"}]
         [na/divider {:clearing? true}]
         [na/form {}
          [na/form-input {:label "name" :default-value (:name @data) :on-change #(swap! data assoc :name (-> %2 .-value)) :inline? true}]
          [na/divider {}]
          [dataset-fields-panel data]
          [na/divider {}]
          [add-dataset-field-panel data]
          [na/divider {}]
          [na/form-button {:content "Add" :on-click #(re-frame/dispatch [::events/add-dataset @data])}]
          ]]))))

(defn edit-dataset-form
  []
  (let [dataset-id @(re-frame/subscribe [::es/current-dataset-id])
        {scheme :scheme} @(re-frame/subscribe [::es/dataset dataset-id])
        data (r/atom {:fields (:fields scheme)})]
    (fn []
      (let [loading @(re-frame/subscribe [:loading])]
        [na/segment {:loading? (when (:edit-dataset loading))}
         [na/header {:as "h4" :content "Edit dataset"}]
         [na/divider {:clearing? true}]
         [na/form {}
          [dataset-fields-panel data]
          [na/divider {}]
          [add-dataset-field-panel data]
          [na/divider {}]
          [na/form-button {:content "Edit" :on-click #(re-frame/dispatch [::events/edit-dataset dataset-id @data])}]
          ]]))))

(defn scene-source
  [scene-id]
  (r/with-let [scene-data @(re-frame/subscribe [::subs/scene scene-id])
               value (r/atom (-> scene-data (dissoc :animations) clj->js (js/JSON.stringify nil 2)))]
            [na/form {}
             [na/text-area {:style {:min-height 750} :default-value @value :on-change #(reset! value (-> %2 .-value))}]
             [na/form-button {:content "Save" :on-click #(re-frame/dispatch [::events/save-scene scene-id (-> @value js/JSON.parse (js->clj :keywordize-keys true))])}]
             ]))

(defn course-source
  []
  (r/with-let [course-id @(re-frame/subscribe [::subs/current-course])
               data @(re-frame/subscribe [::subs/course-data])
               value (r/atom (-> data (dissoc :animations) clj->js (js/JSON.stringify nil 2)))]
              [na/form {}
               [na/text-area {:style {:min-height 750} :default-value @value :on-change #(reset! value (-> %2 .-value))}]
               [na/form-button {:content "Save" :on-click #(re-frame/dispatch [::events/save-course course-id (-> @value js/JSON.parse (js->clj :keywordize-keys true))])}]
               ]))

(defn course-versions
  []
  (let [loading @(re-frame/subscribe [:loading])
        course-versions @(re-frame/subscribe [::es/course-versions])]
    [na/segment {:loading? (when (:course-versions loading))}
     [na/header {:as "h4" :content "Course versions"}]
     [na/divider {:clearing? true}]
     [sa/ItemGroup {}
      (for [version course-versions]
        [sa/Item {}
         [sa/ItemContent {}
          [:p (str (:created-at version) " " (:owner-name version))
           [na/button {:floated "right" :basic? true :content "Restore" :on-click #(re-frame/dispatch [::events/restore-course-version (:id version)])}]
           ]
          ]])]]))

(defn scene-versions
  []
  (let [loading @(re-frame/subscribe [:loading])
        scene-versions @(re-frame/subscribe [::es/scene-versions])]
    [na/segment {:loading? (when (:scene-versions loading))}
     [na/header {:as "h4" :content "Scene versions"}]
     [na/divider {:clearing? true}]
     [sa/ItemGroup {}
      (for [version scene-versions]
        [sa/Item {}
         [sa/ItemContent {}
          [:p (str (:created-at version) " " (:owner-name version))
           [na/button {:floated "right" :basic? true :content "Restore" :on-click #(re-frame/dispatch [::events/restore-scene-version (:id version)])}]
           ]
          ]])]]))

(defn add-scene-form []
  (let [data (r/atom {})]
    (fn []
      (let [loading @(re-frame/subscribe [:loading])]
        [na/segment {:loading? (when (:add-scene loading))}
         [na/header {:as "h4" :content "Add scene"}]
         [na/divider {:clearing? true}]
         [na/form {}
          [na/form-input {:label "name" :default-value (:name @data) :on-change #(swap! data assoc :name (-> %2 .-value)) :inline? true}]
          [na/divider {}]
          [na/form-button {:content "Add" :on-click #(re-frame/dispatch [::events/create-scene (:name @data)])}]
          ]]))))

(defn get-first-file [event]
  (-> event
      (.-dataTransfer)
      (.-files)
      (.item 0)))

(defn upload-asset-form
  []
  (let [scene-id @(re-frame/subscribe [::subs/current-scene])]
    [:div {:on-drag-over #(do (.stopPropagation %) (.preventDefault %))
           :on-drop (fn [e]
                      (.stopPropagation e)
                      (.preventDefault e)
                      (re-frame/dispatch [::events/upload-asset scene-id (get-first-file e)])
                      )}
     [na/segment {:style {:width "100%" :height "500px"}}
      [na/header {:icon true}
       [na/icon {:name "file outline"}]]
      "Drag & drop your file here..."
      ]]))

(defn stage-drop-params [e]
  (let [drop-params (-> e
                        .-dataTransfer
                        (.getData "text/plain")
                        js/JSON.parse
                        (js->clj :keywordize-keys true))
        event-params {:offsetX (offsetX e)
                      :offsetY (offsetY e)}]
    (merge event-params drop-params)))

(defn with-stage
  ([component]
   (with-stage component {}))
  ([component props]
    [:div {:on-drag-over #(.preventDefault %)
           :on-drop (fn [e]
                      (re-frame/dispatch [::events/add-object-to-current-scene (stage-drop-params e)]))}
     [:> Stage (merge {:width 1152 :height 648 :scale-x 0.6 :scale-y 0.6} props)
      [:> Layer component]]]))

(defn main-content
  []
    (let [scene-id (re-frame/subscribe [::subs/current-scene])
          loaded (re-frame/subscribe [::subs/scene-loading-complete @scene-id])
          ui-screen (re-frame/subscribe [::es/current-main-content])]
      (if @loaded
        (case @ui-screen
          :play-scene (with-stage [play-scene @scene-id])
          :actions (with-stage [draw-actions] {:draggable true})
          :scene-source [scene-source @scene-id]
          :course-source [course-source]
          :scene-versions [scene-versions]
          :course-versions [course-versions]
          :add-scene-form [add-scene-form]
          :add-dataset-form [add-dataset-form]
          :edit-dataset-form [edit-dataset-form]
          :dataset-info [dataset-info]
          :add-dataset-item-form [add-dataset-item-form]
          :edit-dataset-item-form [edit-dataset-item-form]
          :add-dataset-lesson-form [add-dataset-lesson-form]
          :edit-dataset-lesson-form [edit-dataset-lesson-form]
          :upload-asset-form [upload-asset-form]
          (with-stage [scene])
          )
        (with-stage [preloader]))
      ))

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
  (let [scene-id @(re-frame/subscribe [::subs/current-scene])
        scene @(re-frame/subscribe [::subs/scene scene-id])]
    [:div
     [sa/Dropdown {:placeholder "src" :search true :selection true :on-change #(swap! props assoc :src (.-value %2))
                   :default-value (:src @props) :options (na/dropdown-list (:assets scene) :url :url) }]
     [properties-panel-common props]]))

(defn get-animation [name]
  (let [key (keyword name)]
    (get animations key)))

(defn properties-panel-animation
  [props]
  [:div
   [:div
     [:label "name"]
     [sa/Dropdown {:placeholder "name" :search true :selection true :on-change #(swap! props assoc :name (.-value %2))
                   :default-value (:name @props) :options (na/dropdown-list (keys animations) name name) }]]
   [:div
     [:label "anim"]
     [sa/Dropdown {:placeholder "anim" :search true :selection true :on-change #(swap! props assoc :anim (.-value %2))
                   :default-value (:anim @props) :options (na/dropdown-list (-> @props :name get-animation :animations) identity identity) }]]
   [:div
     [:label "skin"]
     [sa/Dropdown {:placeholder "skin" :search true :selection true :on-change #(swap! props assoc :skin (.-value %2))
                   :default-value (:skin @props) :options (na/dropdown-list (-> @props :name get-animation :skins) identity identity) }]]
   [na/form-input {:label "speed" :default-value (:speed @props) :on-change #(swap! props assoc :speed (-> %2 .-value js/parseFloat)) :inline? true}]
   [na/checkbox {:label "start" :default-checked? (:start @props) :on-change #(swap! props assoc :start (.-checked %2))}]
   [properties-panel-common props]])

(defn properties-panel-background
  [props]
  (let [scene-id @(re-frame/subscribe [::subs/current-scene])
        scene @(re-frame/subscribe [::subs/scene scene-id])]
    [:div
     [sa/Dropdown {:placeholder "src" :search true :selection true :on-change #(swap! props assoc :src (.-value %2))
                   :default-value (:src @props) :options (na/dropdown-list (:assets scene) :url :url) }]]))

(declare dispatch-action-panel)

(defn action-properties-panel-action
  [props]
  [:div
   [na/form-input {:label "id" :default-value (:id @props) :on-change #(swap! props assoc :id (-> %2 .-value)) :inline? true}]])

(defn action-properties-panel-state
  [props]
  [:div
   [na/form-input {:label "target" :default-value (:target @props) :on-change #(swap! props assoc :target (-> %2 .-value)) :inline? true}]
   [na/form-input {:label "id" :default-value (:id @props) :on-change #(swap! props assoc :id (-> %2 .-value)) :inline? true}]])

(defn action-properties-panel-animation
  [props]
  [:div
   [na/form-input {:label "target" :default-value (:target @props) :on-change #(swap! props assoc :target (-> %2 .-value)) :inline? true}]
   [na/form-input {:label "id" :default-value (:id @props) :on-change #(swap! props assoc :id (-> %2 .-value)) :inline? true}]])

(defn action-properties-panel-audio
  [props]
  [:div
   [na/form-input {:label "id" :default-value (:id @props) :on-change #(swap! props assoc :id (-> %2 .-value)) :inline? true}]
   [na/form-input {:label "start" :default-value (:start @props) :on-change #(swap! props assoc :start (-> %2 .-value js/parseFloat)) :inline? true}]
   [na/form-input {:label "duration" :default-value (:duration @props) :on-change #(swap! props assoc :duration (-> %2 .-value js/parseFloat)) :inline? true}]
   [na/form-input {:label "offset" :default-value (:offset @props) :on-change #(swap! props assoc :offset (-> %2 .-value js/parseFloat)) :inline? true}]
   [na/form-input {:label "loop" :default-value (:loop @props) :on-change #(swap! props assoc :loop (-> %2 .-value (= "true"))) :inline? true}]])

(defn action-properties-panel-scene
  [props]
  [:div
   [na/form-input {:label "scene-id" :default-value (:scene-id @props) :on-change #(swap! props assoc :scene-id (-> %2 .-value)) :inline? true}]])

(defn action-properties-panel-empty
  [props]
  [:div
   [na/form-input {:label "duration" :default-value (:duration @props) :on-change #(swap! props assoc :duration (-> %2 .-value js/parseInt)) :inline? true}]])

(defn action-properties-panel-sequence
  [props]
  (let [edit-mode (r/atom false)
        current-value (atom (clojure.string/join "\n" (:data @props)))]
    (fn [props]
      [:div
       [na/form-input {:label "type" :default-value (:type @props) :on-change #(swap! props assoc :type (-> %2 .-value)) :inline? true}]
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
            ^{:key (str action)}
            [:p [:a {:on-click #(re-frame/dispatch [::events/select-scene-action action])} (str action)]])
          [na/form-button {:content "Edit" :on-click #(reset! edit-mode true)}]])
       ])))

(defn action-properties-panel-data [props]
  (let [data @(re-frame/subscribe [::es/selected-scene-action-data])]
    (swap! props assoc :data data)
    [sa/ItemGroup {}
     (for [[index action-name] (map-indexed (fn [idx itm] [idx itm]) data)]
       ^{:key (str index action-name)}
       [sa/Item {}
        [sa/ItemContent {}
         [:a {:on-click #(re-frame/dispatch [::events/select-scene-action-path index])} (str (:type action-name))]
         [:div {:style {:float "right"}}
          [na/icon {:name "arrow up" :link? true
                    :on-click #(re-frame/dispatch [::events/selected-action-order-up index])}]
          [na/icon {:name "arrow down" :link? true
                    :on-click #(re-frame/dispatch [::events/selected-action-order-down index])}]
          [na/icon {:name "level up alternate" :link? true
                    :on-click #(re-frame/dispatch [::events/selected-action-add-above index])}]
          [na/icon {:name "level down alternate" :link? true
                    :on-click #(re-frame/dispatch [::events/selected-action-add-below index])}]
          [na/icon {:name "remove" :link? true
                    :on-click #(re-frame/dispatch [::events/selected-action-remove index])}]]
         ]])]))

(defn action-properties-panel-parallel [props]
  [:div
   [action-properties-panel-data props]
   ])

(defn action-properties-panel-sequence-data [props]
  [:div
   [action-properties-panel-data props]
   ])

(defn action-properties-panel-common
  [props]
  [:div
   [sa/Dropdown {:placeholder "Type" :search true :selection true :options action-types
                 :default-value (:type @props) :on-change #(swap! props assoc :type (.-value %2))}]
   [na/form-input {:label "Description" :default-value (:description @props) :on-change #(swap! props assoc :description (-> %2 .-value)) :inline? true}]
   [na/divider {}]])

(defn dispatch-action-panel
  [props]
  [:div
   [action-properties-panel-common props]
   (case (-> @props :type keyword)
     :action [action-properties-panel-action props]
     :scene [action-properties-panel-scene props]
     :state [action-properties-panel-state props]
     :animation [action-properties-panel-animation props]
     :audio [action-properties-panel-audio props]
     :empty [action-properties-panel-empty props]
     :sequence [action-properties-panel-sequence props]
     :parallel [action-properties-panel-parallel props]
     :sequence-data [action-properties-panel-sequence-data props]
     nil)])

(defn object-action-properties-panel
  []
  (let [{:keys [scene-id name action]} @(re-frame/subscribe [::es/selected-object-action])
        o (re-frame/subscribe [::subs/scene-object scene-id name])
        action-data (-> @o :actions (get (keyword action)))
        props (r/atom action-data)]
    (fn []
      [na/form {}
       [na/form-input {:label "on" :default-value (:on @props) :on-change #(swap! props assoc :on (-> %2 .-value)) :inline? true}]
       [dispatch-action-panel props]

       [na/form-button {:content "Save" :on-click #(update-object-action scene-id name action @props)}]]
      )))

(defn scene-action-properties-panel
  []
  (let [{:keys [data]} @(re-frame/subscribe [::es/selected-scene-action])
        props (r/atom data)]
    (fn []
      [na/form {}
       [dispatch-action-panel props]
       [na/form-button {:content "Save" :on-click #(re-frame/dispatch [::events/edit-selected-scene-action @props])}]]
      )))

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
       [dispatch-action-panel props]
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
  (fn []
    (let [{:keys [scene-id name]} @(re-frame/subscribe [::es/selected-object])
          o (re-frame/subscribe [::subs/scene-object scene-id name])]
      [:div
       (for [action (-> @o :actions keys)]
         ^{:key (str scene-id action)}
         [:div
          [:a {:on-click #(re-frame/dispatch [::events/select-object-action scene-id name action])} (str action)]])
       [na/divider {}]
       [new-object-action-section]
       ])))

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
        object-state (re-frame/subscribe [::es/selected-object-state])]
    [:div
     (if @object
       [na/segment {}
        [na/header {:as "h4" :floated "left" :content "Object properties"}]
        [na/header {:floated "right" :sub? true}
         [na/icon {:name "close" :on-click remove-transform}]]
        [na/divider {:clearing? true}]

        [properties-panel]])
     (if-let [{:keys [scene-id name action]} @(re-frame/subscribe [::es/selected-object-action])]
       [na/segment {}
        [na/header {:as "h4" :floated "left" :content "Object action"}]
        [na/header {:floated "right" :sub? true}
         [na/icon {:name "close" :on-click #(re-frame/dispatch [::events/reset-object-action])}]]
        [na/divider {:clearing? true}]

        ^{:key (str scene-id name action)}
        [object-action-properties-panel]])
     (if @object-state
       [na/segment {}
        [na/header {:as "h4" :floated "left" :content "Object state"}]
        [na/header {:floated "right" :sub? true}
         [na/icon {:name "close" :on-click #(re-frame/dispatch [::events/reset-object-state])}]]
        [na/divider {:clearing? true}]

        ^{:key (str (:scene-id @object-state) (:name @object-state) (:state @object-state))}
        [object-state-properties-panel]])
     (if-let [{:keys [scene-id action path]} @(re-frame/subscribe [::es/selected-scene-action])]
       [na/segment {}
        [na/header {:as "h4" :floated "left" :content "Scene action"}]
        [na/header {:floated "right" :sub? true}
         [na/icon {:name "close" :on-click #(re-frame/dispatch [::events/reset-scene-action])}]]
        [na/divider {:clearing? true}]

        ^{:key (str scene-id action path)}
        [scene-action-properties-panel]])]))

(defn add-object-panel
  []
  (let [scene-id @(re-frame/subscribe [::subs/current-scene])
        defaults @(re-frame/subscribe [::es/new-object-defaults])
        props (r/atom (or defaults {}))]
    (fn []
      [na/segment {}
       [na/header {:as "h4" :floated "left" :content "Object action"}]
       [na/header {:floated "right" :sub? true}
        [na/icon {:name "close" :on-click #(re-frame/dispatch [::events/reset-shown-form])}]]
       [na/divider {:clearing? true}]

       [na/form {}
        [sa/Dropdown {:placeholder "Type" :search true :selection true :options object-types
                      :default-value (:type @props) :on-change #(swap! props assoc :type (.-value %2))}]
        [na/divider {}]
        [na/form-input {:label "name" :default-value (:scene-name @props) :on-change #(swap! props assoc :scene-name (-> %2 .-value)) :inline? true}]
        [na/form-input {:label "layer" :default-value (:scene-layer @props) :on-change #(swap! props assoc :scene-layer (-> %2 .-value js/parseInt)) :inline? true}]
        [na/divider {}]
        [dispatch-properties-panel props]
        [na/divider {}]
        [na/form-button {:content "Add" :on-click #(do (update-object scene-id (:scene-name @props) @props)
                                                       (update-current-scene-object (:scene-name @props) @props)
                                                       (add-to-scene scene-id (:scene-name @props) (:scene-layer @props))
                                                       (re-frame/dispatch [::events/reset-shown-form]))}]
       ]])))

(defn list-objects-panel
  []
  (let [scene-id @(re-frame/subscribe [::subs/current-scene])
        scene (re-frame/subscribe [::subs/scene scene-id])]
    (fn []
      [na/segment {}
       [na/header {:as "h4" :floated "left" :content "Objects"}]
       [na/header {:floated "right" :sub? true}
        [na/icon {:name "close" :on-click #(re-frame/dispatch [::events/reset-shown-form])}]]
       [na/divider {:clearing? true}]

        [sa/ItemGroup {:divided true}
         (for [[id object] (:objects @scene)]
           ^{:key (str scene-id id)}
           [sa/Item {}
            [sa/ItemContent {:vertical-align "middle"
                             :on-click #(re-frame/dispatch [::events/select-object scene-id id])}
             [:div {:style {:float "left"}} (-> id str)]
             [:div {:style {:float "right"}}
              [na/icon {:name "eye slash outline" :link? true
                        :on-click #(remove-from-scene scene-id id)}]
              [na/icon {:name "remove" :link? true
                        :on-click #(remove-object scene-id id)}]]
             ]]
           )]

       [na/divider {}]
       [na/button {:basic? true :content "Add object" :on-click #(re-frame/dispatch [::events/show-form :add-object])}]])))

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
      [:div
       [na/button {:basic? true :content "Add existing asset" :on-click #(re-frame/dispatch [::events/select-new-asset])}]
       [na/button {:basic? true :content "Upload asset" :on-click #(re-frame/dispatch [::events/show-upload-asset-form])}]])))

(defn animation-asset? [asset]
  (let [type (:type asset)]
    (some #(= type %) ["anim-text" "anim-texture"])))

(defn list-assets-panel
  []
  (let [scene-id @(re-frame/subscribe [::subs/current-scene])
        scene (re-frame/subscribe [::subs/scene scene-id])]
    (fn []
      [na/segment {}
       [na/header {:as "h4" :floated "left" :content "Assets"}]
       [na/header {:floated "right" :sub? true}
        [na/icon {:name "close" :on-click #(re-frame/dispatch [::events/reset-shown-form])}]]
       [na/divider {:clearing? true}]

       [sa/ItemGroup {:divided true :style {:overflow "auto" :max-height "500px"}}
        (for [[key asset] (->> @scene :assets (filter (complement animation-asset?)) (map-indexed (fn [idx itm] [idx itm])))]
          ^{:key (str scene-id key)}
          [sa/Item {:draggable true :on-drag-start #(-> (.-dataTransfer %) (.setData "text/plain" (-> {:type "asset" :id key}
                                                                                                      clj->js
                                                                                                      js/JSON.stringify)))}
           (cond
             (= "image" (:type asset)) [sa/ItemImage {:size "tiny" :src (:url asset)}]
             (= "audio" (:type asset)) [sa/ItemImage {:size "tiny"} [na/icon {:name "music" :size "huge"}]]
             :else [sa/ItemImage {:size "tiny"} [na/icon {:name "file" :size "huge"}]])

           [sa/ItemContent {:on-click #(re-frame/dispatch [::events/select-asset scene-id key])}
            [sa/ItemHeader {:as "a"}
             (str (:type asset))]

            [sa/ItemDescription {}
             (str (:url asset)) ]]]
          )]
       [na/divider {:clearing? true}]

       [edit-asset-section]])))

(defn list-actions-panel []
  (let [scene-id @(re-frame/subscribe [::subs/current-scene])
        scene (re-frame/subscribe [::subs/scene scene-id])
        selected (r/atom #{})
        props (r/atom {})]
    (fn []
      [na/segment {}
       [na/header {}
        [sa/Menu {:size "mini" :secondary true}
         [sa/MenuItem {:header true} "Actions"]
         [sa/MenuItem {}
          [na/form-input {:placeholder "name" :on-change #(swap! props assoc :name (-> %2 .-value))}]]
         [sa/MenuItem {:position "right"}
          [sa/Dropdown {:text "Selected" :options actions-with-selected-actions
                        :on-change #(re-frame/dispatch [::events/process-selected-actions @selected (:name @props) (-> %2 .-value keyword)])}]]
         [sa/MenuItem {:position "right"}
          [na/icon {:name "close" :on-click #(re-frame/dispatch [::events/reset-shown-form])}]]
         ]
        ]
       [na/divider {:clearing? true}]

       [sa/ItemGroup {:divided true :style {:overflow "auto" :max-height "500px"}}
        (for [[key action] (:actions @scene)]
          ^{:key (str scene-id key)}
          [sa/Item {}
           [sa/ItemImage {:size "mini"}
            [na/checkbox {:on-change #(swap! selected (fn [selected]
                                                        (if (.-checked %2)
                                                          (conj selected key)
                                                          (disj selected key))))}]
            ]

           [sa/ItemContent {:on-click #(re-frame/dispatch [::events/show-scene-action key])}
            [sa/ItemHeader {:as "a"}
             (str key)]

            [sa/ItemDescription {}
             (str "type: " (:type action)) ]
               ]]
          )]])))

(defn list-animations-panel
  []
  (let [scene-id @(re-frame/subscribe [::subs/current-scene])]
    (fn []
      [na/segment {}
       [na/header {:as "h4" :floated "left" :content "Animations"}]
       [na/header {:floated "right" :sub? true}
        [na/icon {:name "close" :on-click #(re-frame/dispatch [::events/reset-shown-form])}]]
       [na/divider {:clearing? true}]

       [sa/ItemGroup {:divided true :style {:overflow "auto" :max-height "500px"}}
        (for [[animation data] animations]
          ^{:key animation}
          [sa/Item {:draggable true :on-drag-start #(-> (.-dataTransfer %) (.setData "text/plain" (-> {:type "animation" :id (name animation)}
                                                                                                      clj->js
                                                                                                      js/JSON.stringify)))}

           [sa/ItemContent {}
            [sa/ItemHeader {:as "a"}
             (str animation)]

            [sa/ItemDescription {}
             [:div {:style {:width "100px" :height "100px"} :ref #(when % (init-spine-player % (name animation)))}] ]]]
          )]])))

(defn list-action-templates-panel
  []
  (let [scene-id @(re-frame/subscribe [::subs/current-scene])]
    (fn []
      [na/segment {}
       [na/header {:as "h4" :floated "left" :content "Action templates"}]
       [na/header {:floated "right" :sub? true}
        [na/icon {:name "close" :on-click #(re-frame/dispatch [::events/reset-shown-form])}]]
       [na/divider {:clearing? true}]

       [sa/ItemGroup {:divided true :style {:overflow "auto" :max-height "500px"}}
        (for [action-type (map #(-> % :value name) action-types)]
          ^{:key action-type}
          [sa/Item {:draggable true :on-drag-start #(-> (.-dataTransfer %) (.setData "text/plain" (-> {:type "action" :id (name action-type)}
                                                                                                      clj->js
                                                                                                      js/JSON.stringify)))}

           [sa/ItemContent {}
            [sa/ItemHeader {:as "a"}
             (str action-type)]

            [sa/ItemDescription {}
              ]]]
          )]])))

(defn list-scenes-panel
  []
  (let [scenes (re-frame/subscribe [::subs/course-scenes])]
    (fn []
      [na/segment {}
       [na/header {:as "h4"}
        "Scenes"
        [:div {:style {:float "right"}}
         [na/icon {:name "add" :link? true
                   :on-click #(re-frame/dispatch [::events/set-main-content :add-scene-form])}]]]
       [na/divider {:clearing? true}]
       [sa/ItemGroup {}
        (for [scene-id @scenes]
          ^{:key (str scene-id)}
          [sa/Item {}
           [sa/ItemContent {}
            [:a {:on-click #(re-frame/dispatch [::ie/set-current-scene scene-id])} scene-id]
            ]])]])))

(defn list-datasets-panel
  []
  (let [datasets (re-frame/subscribe [::es/course-datasets])]
    (fn []
      [na/segment {}
       [na/header {:as "h4"}
        "Datasets"
        [:div {:style {:float "right"}}
         [na/icon {:name "add" :link? true
                   :on-click #(re-frame/dispatch [::events/set-main-content :add-dataset-form])}]]]
       [na/divider {:clearing? true}]
       [sa/ItemGroup {}
        (for [{id :id name :name} @datasets]
          ^{:key (str id)}
          [sa/Item {}
           [sa/ItemContent {}
            [:a {:on-click #(re-frame/dispatch [::events/show-dataset id])} name]
            [:div {:style {:float "right"}}
             [na/icon {:name     "edit" :link? true
                       :on-click #(re-frame/dispatch [::events/show-edit-dataset-form id])}]]
            ]])]])))

(defn shown-form-panel
  []
  (let [show-form (re-frame/subscribe [::es/shown-form])]
    (case @show-form
      :add-object [add-object-panel]
      :list-objects [list-objects-panel]
      :list-actions [list-actions-panel]
      :list-asset-templates [list-assets-panel]
      :list-animation-templates [list-animations-panel]
      :list-action-templates [list-action-templates-panel]
      [:div])))

(defn editor []
  (let [course-id (re-frame/subscribe [::subs/current-course])
        scene-id (re-frame/subscribe [::subs/current-scene])]

    [:div
     [:link {:rel "stylesheet" :href "http://esotericsoftware.com/files/spine-player/3.7/spine-player.css"}]

      [sa/SidebarPushable {}
       [sa/Sidebar {:visible true}
        [na/segment {}
         [na/header {}
          [:div {} @course-id
           [:div {:style {:float "right"}}
            [na/icon {:name "code" :link? true
                      :on-click #(re-frame/dispatch [::events/set-main-content :course-source])}]
            [na/icon {:name "history" :link? true
                      :on-click #(re-frame/dispatch [::events/open-current-course-versions])}]]]]
         [na/divider {:clearing? true}]
         [list-scenes-panel]
         [list-datasets-panel]]]
      [sa/SidebarPusher {}
       [:div {:class-name "ui segment"}
        [na/header {:dividing? true} "Editor"]
        [na/grid {}
         [na/grid-column {:width 10}
          [main-content]
          [na/divider {:clearing? true}]
          [na/button {:content "Play" :on-click #(re-frame/dispatch [::events/set-main-content :play-scene])}]
          [na/button {:content "Editor" :on-click #(do (re-frame/dispatch [::ie/set-current-scene @scene-id])
                                                       (re-frame/dispatch [::events/set-main-content :editor]))}]
          [na/button {:content "Actions" :on-click #(re-frame/dispatch [::events/set-main-content :actions])}]
          [na/button {:content "Source" :on-click #(re-frame/dispatch [::events/set-main-content :scene-source])}]
          [na/button {:content "Versions" :on-click #(re-frame/dispatch [::events/open-current-scene-versions])}]]
         [na/grid-column {:width 4}
          [:div
           [na/button {:basic? true :content "Objects" :on-click #(re-frame/dispatch [::events/show-form :list-objects])}]
           [na/button {:basic? true :content "Actions" :on-click #(re-frame/dispatch [::events/show-form :list-actions])}]
           ]
          [:div
           [:label "Templates: "]
           [na/button {:basic? true :content "Assets" :on-click #(re-frame/dispatch [::events/show-form :list-asset-templates])}]
           [na/button {:basic? true :content "Animations" :on-click #(re-frame/dispatch [::events/show-form :list-animation-templates])}]
           [na/button {:basic? true :content "Actions" :on-click #(re-frame/dispatch [::events/show-form :list-action-templates])}]]
          [shown-form-panel]
          [properties-rail]]]

      ]]]]))