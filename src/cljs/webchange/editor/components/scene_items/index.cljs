(ns webchange.editor.components.scene-items.index
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [soda-ash.core :as sa]
    [sodium.core :as na]
    [webchange.common.anim :refer [animations
                                   init-spine-player]]
    [webchange.editor.action-properties.core :refer [action-types] :as action-properties]
    [webchange.editor.common.components :refer [dispatch-properties-panel
                                                update-current-scene-object
                                                update-object
                                                update-object-action]]
    [webchange.editor.enums :refer [actions-with-selected-actions
                                    asset-types
                                    object-types]]
    [webchange.editor.events :as events]
    [webchange.editor.object-properties-panel.object-properties-panel :refer [properties-panel]]
    [webchange.editor.subs :as es]
    [webchange.subs :as subs]
    ))

(defn add-to-scene
  [scene-id name layer]
  (re-frame/dispatch [::events/add-to-scene {:scene-id scene-id :name name :layer layer}])
  (re-frame/dispatch [::events/add-to-current-scene {:name name :layer layer}]))

(defn add-object-panel
  []
  (let [scene-id @(re-frame/subscribe [::subs/current-scene])
        defaults @(re-frame/subscribe [::es/new-object-defaults])
        props (r/atom (or defaults {}))]
    (fn []
      [na/segment {}
       [na/header {:as "h4" :floated "left" :content "Object action"}]
       [:div {:style {:float "right"}}
        [na/icon {:name "close" :link? true :on-click #(re-frame/dispatch [::events/reset-shown-form])}]]
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

(defn remove-from-scene
  [scene-id object-name]
  (let [prepared-name (name object-name)]
    (re-frame/dispatch [::events/remove-from-scene {:scene-id scene-id :name prepared-name}])
    (re-frame/dispatch [::events/remove-from-current-scene {:name prepared-name}])))

(defn remove-object [scene-id name]
  (remove-from-scene scene-id name)
  (re-frame/dispatch [::events/remove-object {:scene-id scene-id :target name}])
  (re-frame/dispatch [::events/remove-current-scene-object {:target name}]))

(defn list-objects-panel
  []
  (let [scene-id @(re-frame/subscribe [::subs/current-scene])
        scene (re-frame/subscribe [::subs/scene scene-id])]
    (fn []
      [na/segment {}
       [na/header {:as "h4" :floated "left" :content "Objects"}]
       [:div {:style {:float "right"}}
        [na/icon {:name "close" :link? true :on-click #(re-frame/dispatch [::events/reset-shown-form])}]]
       [na/divider {:clearing? true}]

       [sa/ItemGroup {:divided true}
        (for [[id object] (:objects @scene)]
          ^{:key (str scene-id id)}
          [sa/Item {}
           [sa/ItemContent {:vertical-align "middle"
                            :on-click #(re-frame/dispatch [::events/select-object scene-id id])}
            [:div {:style {:float "left"}} (-> id str)]
            [:div {:style {:float "right"}}
             [na/icon {:name "hide" :link? true
                       :on-click #(remove-from-scene scene-id id)}]
             [na/icon {:name "remove" :link? true
                       :on-click #(remove-object scene-id id)}]]
            ]]
          )]

       [na/divider {}]
       [na/button {:basic? true :content "Add object" :on-click #(re-frame/dispatch [::events/show-form :add-object])}]])))

(defn list-actions-panel []
  (let [scene-id @(re-frame/subscribe [::subs/current-scene])
        scene (re-frame/subscribe [::subs/scene scene-id])
        selected (r/atom #{})
        props (r/atom {})
        errors (r/atom {})]
    (fn []
      [na/segment {}
       [na/header {}
        [sa/Menu {:size "mini" :secondary true}
         [sa/MenuItem {:header true} "Actions"]
         [sa/MenuItem {}
          [na/form {:size "mini"}
           [na/form-input {:placeholder "Name" :on-change #(do
                                                             (swap! errors dissoc :name)
                                                             (swap! props assoc :name (-> %2 .-value)))
                           :error? (:name @errors)}]]]
         [sa/MenuItem {:position "right"}
          [sa/Dropdown {:text "New" :options action-types :scrolling true
                        :on-change #(if (:name @props)
                                      (re-frame/dispatch [::events/add-new-scene-action (:name @props) (-> %2 .-value keyword)])
                                      (swap! errors assoc :name true))}]]
         [sa/MenuItem {:position "right"}
          [sa/Dropdown {:text "Selected" :options actions-with-selected-actions
                        :direction "left"
                        :on-change #(if (:name @props)
                                      (re-frame/dispatch [::events/process-selected-actions @selected (:name @props) (-> %2 .-value keyword)])
                                      (swap! errors assoc :name true))}]]
         [sa/MenuItem {:position "right"}
          [na/icon {:name "close" :link? true :on-click #(re-frame/dispatch [::events/reset-shown-form])}]]
         ]
        ]
       [na/divider {:clearing? true}]

       [sa/ItemGroup {:divided true :style {:overflow "auto" :max-height "500px"}}
        (for [[key action] (sort-by first (:actions @scene))]
          ^{:key (str scene-id key)}
          [sa/Item {}
           [sa/ItemImage {:size "mini"}
            [na/checkbox {:on-change #(swap! selected (fn [selected]
                                                        (if (.-checked %2)
                                                          (conj selected key)
                                                          (disj selected key))))}]]
           [sa/ItemContent {:on-click #(re-frame/dispatch [::events/show-scene-action key])}
            [sa/ItemHeader {:as "a"}
             (str key)]

            [sa/ItemDescription {}
             (str "type: " (:type action)) ]
            ]]
          )]])))

(defn update-asset
  [scene-id id state]
  (re-frame/dispatch [::events/edit-asset {:scene-id scene-id :id id :state state}]))

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
         [na/form-input {:label "alias" :default-value (:alias @asset) :on-change #(swap! props assoc :alias (-> %2 .-value)) :inline? true}]
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
       [:div {:style {:float "right"}}
        [na/icon {:name "close" :link? true :on-click #(re-frame/dispatch [::events/reset-shown-form])}]]
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

(defn list-animations-panel
  []
  (let [scene-id @(re-frame/subscribe [::subs/current-scene])]
    (fn []
      [na/segment {}
       [na/header {:as "h4" :floated "left" :content "Animations"}]
       [:div {:style {:float "right"}}
        [na/icon {:name "close" :link? true :on-click #(re-frame/dispatch [::events/reset-shown-form])}]]
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
       [:div {:style {:float "right"}}
        [na/icon {:name "close" :link? true :on-click #(re-frame/dispatch [::events/reset-shown-form])}]]
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

;; Duplicated from webchange.editor.index
(defn reset-transform
  []
  (re-frame/dispatch [::events/reset-object])
  (re-frame/dispatch [::events/reset-object-action])
  (let [state (re-frame/subscribe [::es/transform])
        {:keys [transformer target]} @state]
    (when target (.draggable target false))
    (when transformer (.destroy transformer))))

;; Duplicated from webchange.editor.index
(defn remove-transform
  []
  (re-frame/dispatch [::events/reset-transform])
  (reset-transform))

(defn object-action-properties-panel
  []
  (let [{:keys [scene-id name action]} @(re-frame/subscribe [::es/selected-object-action])
        o (re-frame/subscribe [::subs/scene-object scene-id name])
        action-data (-> @o :actions (get (keyword action)))
        props (r/atom action-data)]
    (fn []
      [na/form {}
       [na/form-input {:label "on" :default-value (:on @props) :on-change #(swap! props assoc :on (-> %2 .-value)) :inline? true}]
       [action-properties/action-properties-panel props]

       [na/form-button {:content "Save" :on-click #(update-object-action scene-id name action @props)}]]
      )))

(defn update-object-state
  [scene-id name state data]
  (re-frame/dispatch [::events/edit-object-state {:scene-id scene-id :name name :state state :data data}]))

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

(defn scene-action-properties-panel
  []
  (let [{:keys [data]} @(re-frame/subscribe [::es/selected-scene-action])
        props (r/atom data)]
    (fn []
      [na/form {}
       [action-properties/action-properties-panel props]
       [na/divider {}]
       [na/form-button {:content "Save" :on-click #(re-frame/dispatch [::events/edit-selected-scene-action @props])}]]
      )))

(defn scene-action-panel
  []
  (let [edit (r/atom nil)]
    (fn []
      (if-let [{:keys [scene-id action path]} @(re-frame/subscribe [::es/selected-scene-action])]
        [na/segment {}
         (if @edit
           [na/header {:as "h4" :floated "left"}
            [na/form-input {:label "Scene action: " :default-value (name action) :on-change #(swap! edit assoc :name (-> %2 .-value)) :inline? true}]]
           [na/header {:as "h4" :floated "left" :content (str "Scene action: " (name action))}])
         [:div {:style {:float "right"}}
          (if @edit
            [na/icon {:name "checkmark" :link? true :on-click #(do
                                                                 (re-frame/dispatch [::events/rename-scene-action action (:name @edit)])
                                                                 (reset! edit nil))}]
            [na/icon {:name "pencil" :link? true :on-click #(reset! edit {})}])
          [na/icon {:name "close" :link? true :on-click #(re-frame/dispatch [::events/reset-scene-action])}]]
         [na/divider {:clearing? true}]

         ^{:key (str scene-id action path)}
         [scene-action-properties-panel]]))))

(defn properties-rail
  []
  (let [object (re-frame/subscribe [::es/selected-object])
        object-state (re-frame/subscribe [::es/selected-object-state])]
    [:div
     (if @object
       [na/segment {}
        [na/header {:as "h4" :floated "left" :content "Object properties"}]
        [:div {:style {:float "right"}}
         [na/icon {:name "close" :link? true :on-click remove-transform}]]
        [na/divider {:clearing? true}]

        [properties-panel]])
     (if-let [{:keys [scene-id name action]} @(re-frame/subscribe [::es/selected-object-action])]
       [na/segment {}
        [na/header {:as "h4" :floated "left" :content "Object action"}]
        [:div {:style {:float "right"}}
         [na/icon {:name "close" :link? true :on-click #(re-frame/dispatch [::events/reset-object-action])}]]
        [na/divider {:clearing? true}]

        ^{:key (str scene-id name action)}
        [object-action-properties-panel]])
     (if @object-state
       [na/segment {}
        [na/header {:as "h4" :floated "left" :content "Object state"}]
        [:div {:style {:float "right"}}
         [na/icon {:name "close" :link? true :on-click #(re-frame/dispatch [::events/reset-object-state])}]]
        [na/divider {:clearing? true}]

        ^{:key (str (:scene-id @object-state) (:name @object-state) (:state @object-state))}
        [object-state-properties-panel]])
     [scene-action-panel]]))

(defn scene-items
  []
  [:div
   [:div
    [na/button {:basic? true :content "Objects" :on-click #(re-frame/dispatch [::events/show-form :list-objects])}]
    [na/button {:basic? true :content "Actions" :on-click #(re-frame/dispatch [::events/show-form :list-actions])}]
    ]
   [:div
    [:label "Templates: "]
    [na/button {:basic? true :content "Assets" :on-click #(re-frame/dispatch [::events/show-form :list-asset-templates])}]
    [na/button {:basic? true :content "Animations" :on-click #(re-frame/dispatch [::events/show-form :list-animation-templates])}]
    #_[na/button {:basic? true :content "Actions" :on-click #(re-frame/dispatch [::events/show-form :list-action-templates])}]]
   [shown-form-panel]
   [properties-rail]
   ])
