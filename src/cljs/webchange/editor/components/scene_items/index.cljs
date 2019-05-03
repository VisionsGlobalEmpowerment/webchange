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
                                                update-object-action]]
    [webchange.editor.components.scene-items.actions.index :refer [list-actions-panel]]
    [webchange.editor.components.scene-items.animations.index :refer [list-animations-panel]]
    [webchange.editor.components.scene-items.assets.index :refer [list-assets-panel]]
    [webchange.editor.components.scene-items.objects.index :refer [add-object-panel
                                                                   list-objects-panel]]
    [webchange.editor.enums :refer [actions-with-selected-actions
                                    asset-types
                                    object-types]]
    [webchange.editor.events :as events]
    [webchange.editor.object-properties-panel.object-properties-panel :refer [properties-panel]]
    [webchange.editor.subs :as es]
    [webchange.subs :as subs]
    ))

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
