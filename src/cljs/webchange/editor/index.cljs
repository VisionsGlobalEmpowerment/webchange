(ns webchange.editor.index
  (:require
    [clojure.pprint :as p]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.subs :as subs]
    [webchange.common.kimage :refer [kimage]]
    [webchange.common.copybook :refer [copybook]]
    [webchange.common.svg-path :refer [svg-path]]
    [webchange.common.matrix :refer [matrix-objects-list]]
    [webchange.common.anim :refer [anim animations init-spine-player]]
    [webchange.common.events :as ce]
    [webchange.common.core :refer [with-parent-origin
                                   with-origin-offset]]
    [webchange.interpreter.variables.subs :as vars.subs]
    [webchange.interpreter.core :refer [get-data-as-url]]
    [webchange.interpreter.components :refer [scene] :rename {scene play-scene}]
    [webchange.interpreter.events :as ie]
    [webchange.editor.components.data-sets.data-set-item.index :refer [add-dataset-item-form
                                                                       edit-dataset-item-form]]
    [webchange.editor.components.data-sets.index :refer [add-dataset-form
                                                         edit-dataset-form]]
    [webchange.editor.components.main-content-navigation.index :refer [main-content-navigation]]
    [webchange.editor.components.scene-items.index :refer [scene-items]]
    [webchange.editor.common.components :refer [dispatch-properties-panel
                                                update-object
                                                update-object-action]]
    [webchange.editor.enums :refer [object-types]]
    [webchange.editor.events :as events]
    [webchange.editor.subs :as es]
    [webchange.editor.form-elements :as f]
    [webchange.editor.form-elements.wavesurfer.wavesurfer :as ws]
    [konva :refer [Transformer]]
    [react-konva :refer [Custom Group Layer Path Rect Stage Text]]
    [sodium.core :as na]
    [sodium.extensions :as nax]
    [soda-ash.core :as sa :refer [Grid
                                  GridColumn
                                  GridRow]]))

(declare background)
(declare image)
(declare transparent)
(declare group)
(declare placeholder)
(declare animation)
(declare text)
(declare painting-area)
(declare copybook-object)
(declare colors-palette)
(declare carousel-object)
(declare video)
(declare animated-svg-path)
(declare svg-path-object)
(declare matrix-group)

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
    :carousel carousel-object
    :painting-area painting-area
    :animated-svg-path animated-svg-path
    :svg-path svg-path-object
    :copybook copybook-object
    :colors-palette colors-palette
    :video video
    :matrix matrix-group
    (throw (js/Error. (str "Object with type " type " can not be drawn because it is not defined")))))

(defn to-props
  [konva-node]
  {:x        (.x konva-node)
   :y        (.y konva-node)
   :rotation (.rotation konva-node)
   :scale-x  (.scaleX konva-node)
   :scale-y  (.scaleY konva-node)})

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
  (let [template? (:template? object)]
    {:width        (:width object)
     :height       (:height object)
     :stroke       (if template? "orange" "green")
     :stroke-width 4
     :on-click     (if template? #() (fn [e] (transform scene-id name (-> e .-target .getParent))))}))

(defn path-params
  [object]
  {:data         (:path object)
   :stroke       (:stroke object)
   :animation    (:animation object)
   :fill         (:fill object)
   :stroke-width (:stroke-width object)
   :line-cap     (:line-cap object)})

(defn object-params
  [object]
  (-> object with-origin-offset))

(defn draw-object
  ([scene-id name]
   (draw-object scene-id name {}))
  ([scene-id name props]
   (let [o (re-frame/subscribe [::subs/current-scene-object name])
         type (keyword (:type @o))]
     [(object type) scene-id name (merge @o props) draw-object])))

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

(defn matrix-group
  [scene-id name object d]
  [:> Group (object-params object)
   (matrix-objects-list object scene-id d)
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
  (let [params (object-params object)
        animation-name (or (:scene-name object) (:name object))]
    [:> Group params
     [anim (-> object
               (assoc :on-mount #(re-frame/dispatch [::ie/register-animation animation-name %]))
               (assoc :start false))]
     [:> Rect (-> (rect-params scene-id name object)
                  (with-parent-origin object "center-bottom")
                  with-origin-offset)]]))

(defn text
  [scene-id name object]
  (let [object-params (object-params object)]
    [:> Group object-params
     [:> Text (dissoc object-params :x :y)]
     [:> Rect (rect-params scene-id name object)]]))

(defn carousel-object
  [scene-id name object]
  [:> Group (object-params object)
   [kimage (get-data-as-url (:first object))]
   [:> Rect (rect-params scene-id name object)]])

(defn painting-area
  [scene-id name object]
  [:> Group (object-params object)
   [:> Rect (rect-params scene-id name object)]])

(defn copybook-object
  [scene-id name object]
  [:> Group (object-params object)
   [copybook (merge object {:x 0 :y 0})]
   [:> Rect (rect-params scene-id name object)]])

(defn svg-path-object
  [scene-id name object]
  [:> Group (merge (object-params object) {:scale-x 1 :scale-y 1})
   [svg-path (merge object {:x 0 :y 0})]
   [:> Rect (rect-params scene-id name object)]])

(defn animated-svg-path
  [scene-id name object]
  [:> Group (object-params object)
   [:> Rect (rect-params scene-id name object)]
   [:> Path (path-params object)]])

(defn colors-palette
  [scene-id name object]
  [:> Group (object-params object) `[:> Rect (rect-params scene-id name object)]])

(defn video
  [scene-id name object]
  [:> Group (object-params object)
   [:> Rect (rect-params scene-id name object)]
   [:> Text {:x         15
             :y         15
             :font-size 24
             :fill      "green"
             :text      (str "video: " (:src object))}]])

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
    [:> Group {:x offset-x :y offset-y :on-click #(re-frame/dispatch [::events/select-scene-action action-id scene-id])}
     (case (-> action :type keyword)
       :parallel [:> Group {}
                  [:> Rect {:width (action-width action actions) :height (action-height action actions)
                            :fill  "#c5cae9" :stroke "#bdbdbd" :stroke-width 2}]
                  (let [current-y (atom 0)]
                    (for [data-action (:data action)]
                      ^{:key (str @current-y)}
                      [:> Group {:x 0 :y @current-y}
                       (do
                         (swap! current-y + (action-height data-action actions) base-offset)
                         [draw-action data-action actions scene-id action-id 0 0])]))]
       :sequence [:> Group {}
                  [:> Rect {:width (action-width action actions) :height (action-height action actions)
                            :fill  "#303f9f" :stroke "#bdbdbd" :stroke-width 2}]
                  (let [current-x (atom 0)]
                    (for [data-action-id (:data action)]
                      ^{:key (str @current-x)}
                      [:> Group {:x @current-x}
                       (do
                         (swap! current-x + (action-width (get actions (keyword data-action-id)) actions) base-offset)
                         [draw-action (get actions (keyword data-action-id)) actions scene-id data-action-id 0 0])]))]
       :sequence-data [:> Group {}
                       [:> Rect {:width (action-width action actions) :height (action-height action actions)
                                 :fill  "#303f9f" :stroke "#bdbdbd" :stroke-width 2}]
                       (let [current-x (atom 0)]
                         (for [data-action (:data action)]
                           ^{:key (str @current-x)}
                           [:> Group {:x @current-x}
                            (do
                              (swap! current-x + (action-width data-action actions) base-offset)
                              [draw-action data-action actions scene-id action-id 0 0])]))]
       [:> Group {}
        [:> Rect {:width (action-width action actions) :height (action-height action actions)
                  :fill  "#3f51b5" :stroke "#bdbdbd" :stroke-width 2}]
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

(defn draw-triggers
  []
  (r/with-let [scale (r/atom 1)
               scene-id @(re-frame/subscribe [::subs/current-scene])
               triggers @(re-frame/subscribe [::subs/scene-triggers scene-id])
               actions @(re-frame/subscribe [::subs/scene-actions scene-id])]
              [:> Group {:x 50 :y 300 :scale {:x @scale :y @scale} :on-wheel (fn [e]
                                                                               (let [delta (-> e .-evt .-deltaY (/ 1000))]
                                                                                 (-> e .-evt .preventDefault)
                                                                                 (swap! scale + delta)))}
               (let [current-y (atom 0)]
                 (for [[key trigger] triggers]
                   ^{:key (str key)}
                   [:> Group {:x 0 :y @current-y}
                    (let [action-id (-> trigger :action keyword)
                          action (get actions action-id)
                          _ (swap! current-y + (action-height action actions))]
                      [draw-action action actions scene-id action-id 0 0])]))]))

(defn dataset-lessons-panel
  []
  (let [lessons @(re-frame/subscribe [::es/current-dataset-lessons])]
    [na/segment {}
     [na/header {:as "h4"}
      "Lessons"
      [:div {:style {:float "right"}}
       [na/icon {:name     "add" :link? true
                 :on-click #(re-frame/dispatch [::events/show-add-dataset-lesson-form])}]]]
     [na/divider {:clearing? true}]
     [sa/ItemGroup {}
      (for [{name :name id :id} lessons]
        ^{:key name}
        [sa/Item {}
         [sa/ItemContent {}
          [:div {:style {:float "left"}} [:p name]]
          [:div {:style {:float "right"}}
           [na/icon {:name     "edit" :link? true
                     :on-click #(re-frame/dispatch [::events/show-edit-dataset-lesson-form id])}]
           [na/icon {:name     "remove" :link? true
                     :on-click #(re-frame/dispatch [::events/delete-dataset-lesson id])}]]
          ]])]]))

(defn dataset-items-panel
  []
  (let [items @(re-frame/subscribe [::es/current-dataset-items])]
    [na/segment {}
     [na/header {:as "h4"}
      "Items"
      [:div {:style {:float "right"}}
       [na/icon {:name     "add" :link? true
                 :on-click #(re-frame/dispatch [::events/show-add-dataset-item-form])}]]]
     [na/divider {:clearing? true}]
     [sa/ItemGroup {}
      (for [{name :name item-id :id} items]
        ^{:key name}
        [sa/Item {}
         [sa/ItemContent {}
          [:div {:style {:float "left"}} [:p name]]
          [:div {:style {:float "right"}}
           [na/icon {:name     "edit" :link? true
                     :on-click #(re-frame/dispatch [::events/show-edit-dataset-item-form item-id])}]
           [na/icon {:name     "remove" :link? true
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
              [na/checkbox {:label     name
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
            [:div {:style        {:min-height 300}
                   :on-drag-over #(.preventDefault %)
                   :on-drop      (fn [e]
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
             [:div {:style        {:min-height 300}
                    :on-drag-over #(.preventDefault %)
                    :on-drop      (fn [e]
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

(defn scene-source
  [scene-id]
  (r/with-let [scene-data @(re-frame/subscribe [::subs/scene scene-id])
               value-json (r/atom (-> scene-data (dissoc :animations) clj->js (js/JSON.stringify nil 2)))
               value-map (r/atom (-> scene-data (dissoc :animations) p/pprint with-out-str))
               text-area-style {:font-family "monospace"
                                :font-size   12
                                :min-height  750}]
              [na/form {}
               [Grid {:columns 2
                      :divided true}
                [GridRow {}
                 [GridColumn {}
                  [na/text-area {:style text-area-style :default-value @value-json :on-change #(reset! value-json (-> %2 .-value))}]]
                 [GridColumn {}
                  [na/text-area {:style text-area-style :default-value @value-map :on-change #(reset! value-map (-> %2 .-value))}]]
                 ]
                [GridRow {}
                 [GridColumn {}
                  [na/form-button {:content "Save" :on-click #(re-frame/dispatch [::events/save-scene scene-id (-> @value-json js/JSON.parse (js->clj :keywordize-keys true))])}]
                  ]]]]))

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

(defn is-file-drop? [event]
  (->> event
       .-dataTransfer
       .-types
       js->clj
       (some #{"Files"})))

(defn is-asset-drop? [event]
  (->> event
       .-dataTransfer
       .-types
       js->clj
       (some #{"text/plain"})))

(defn get-first-file [event]
  (-> event
      (.-dataTransfer)
      (.-files)
      (.item 0)))

(defn upload-asset-form
  []
  (let [props (r/atom {})]
    (fn []
      (let [scene-id @(re-frame/subscribe [::subs/current-scene])]
        [:div {:on-drag-over #(do (.stopPropagation %) (.preventDefault %))
               :on-drop      (fn [e]
                               (when (is-file-drop? e)
                                 (.stopPropagation e)
                                 (.preventDefault e)
                                 (re-frame/dispatch [::events/upload-asset scene-id (get-first-file e) (select-keys @props [:alias :target])]))
                               )}

         [sa/Segment {:placeholder true :style {:width "100%" :height "500px"}}
          [sa/Header {:icon true}
           [na/icon {:name "file outline"}]
           "Drag & drop your file here..."]
          [na/form-input {:label "Alias: " :on-change #(swap! props assoc :alias (-> %2 .-value)) :inline? true}]
          [na/form-input {:label "Target: " :on-change #(swap! props assoc :target (-> %2 .-value)) :inline? true}]
          ]]))))


(def stage-scale 0.6)

(defn get-drop-data-params [e]
  (-> e
      .-dataTransfer
      (.getData "text/plain")
      js/JSON.parse
      (js->clj :keywordize-keys true)))

(defn get-drop-event-params [e]
  {:offsetX (-> e offsetX (/ stage-scale))
   :offsetY (-> e offsetY (/ stage-scale))})

(defn stage-drop-params [e]
  (let [drop-params (get-drop-data-params e)
        event-params (get-drop-event-params e)]
    (merge event-params drop-params)))

(defn with-stage
  ([component scene-id]
   (with-stage component {} scene-id))
  ([component props scene-id]
   [:div {
          :on-drag-over (fn [e]
                          (.stopPropagation e)
                          (.preventDefault e))
          :on-drop      (fn [e]
                          (cond
                            (is-asset-drop? e) (re-frame/dispatch [::events/add-object-to-scene (stage-drop-params e) scene-id])
                            (is-file-drop? e) (re-frame/dispatch [::events/upload-and-add-asset (get-drop-event-params e) (get-first-file e) scene-id]))
                          (.stopPropagation e)
                          (.preventDefault e)
                          )
          :style        {:display         "flex"
                         :justify-content "center"}}
    [:> Stage (merge {:width 1152 :height 648 :scale-x 0.6 :scale-y 0.6} props)
     [:> Layer component]]]))

(defn main-content
  []
  (let [scene-id (re-frame/subscribe [::subs/current-scene])
        loaded (re-frame/subscribe [::subs/scene-loading-complete @scene-id])
        ui-screen (re-frame/subscribe [::es/current-main-content])]
    ^{:key (str @ui-screen)}
    (if @loaded
      (case @ui-screen
        :play-scene [with-stage [play-scene @scene-id] scene-id]
        :actions (with-stage [draw-actions] {:draggable true} scene-id)
        :triggers (with-stage [draw-triggers] {:draggable true} scene-id)
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
        [with-stage [scene] scene-id]
        )
      (with-stage [preloader] scene-id))
    ))

(defn animation-asset? [asset]
  (let [type (:type asset)]
    (some #(= type %) ["anim-text" "anim-texture"])))

(defn list-scenes-panel
  []
  (let [scenes (re-frame/subscribe [::subs/course-scenes])]
    (fn []
      [na/segment {}
       [na/header {:as "h4"}
        "Scenes"
        [:div {:style {:float "right"}}
         [na/icon {:name     "add" :link? true
                   :on-click #(re-frame/dispatch [::events/set-main-content :add-scene-form])}]]]
       [na/divider {:clearing? true}]
       [sa/ItemGroup {}
        (for [scene-id @scenes]
          ^{:key (str scene-id)}
          [sa/Item {}
           [sa/ItemContent {}
            [:a {:on-click #(re-frame/dispatch [::events/select-current-scene scene-id])} scene-id]
            ]])]])))

(defn list-datasets-panel
  []
  (let [datasets (re-frame/subscribe [::es/course-datasets])]
    (fn []
      [na/segment {}
       [na/header {:as "h4"}
        "Datasets"
        [:div {:style {:float "right"}}
         [na/icon {:name     "add" :link? true
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

(defn editor []
  (let [course-id (re-frame/subscribe [::subs/current-course])
        scene-id (re-frame/subscribe [::subs/current-scene])]

    [:div#editor-container
     [:link {:rel "stylesheet" :href "http://esotericsoftware.com/files/spine-player/3.7/spine-player.css"}]

     [sa/Grid {}
      [sa/GridColumn {:width 2}
       [na/segment {}
        [na/header {}
         [:div {} @course-id
          [:div {:style {:float "right"}}
           [na/icon {:name     "code" :link? true
                     :on-click #(re-frame/dispatch [::events/set-main-content :course-source])}]
           [na/icon {:name     "history" :link? true
                     :on-click #(re-frame/dispatch [::events/open-current-course-versions])}]]]]
        [na/divider {:clearing? true}]
        [list-scenes-panel]
        [list-datasets-panel]]]
      [sa/GridColumn {:width 10}
       [:div {:class-name "ui segment"}
        [na/header {:dividing? true} "Editor"]
        [main-content]
        [na/divider {:clearing? true}]
        [main-content-navigation @scene-id]]]
      [sa/GridColumn {:width 4}
       [scene-items]
       ]]]))
