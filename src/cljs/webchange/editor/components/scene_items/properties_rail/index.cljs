(ns webchange.editor.components.scene-items.properties-rail.index
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [soda-ash.core :as sa]
    [sodium.core :as na]
    [webchange.editor.common.actions.action-form :refer [action-form]]
    [webchange.editor.common.components :refer [dispatch-properties-panel]]
    [webchange.editor.components.scene-items.properties-rail.object-properties-panel :refer [properties-panel]]
    [webchange.editor.enums :refer [object-types]]
    [webchange.editor.events :as events]
    [webchange.editor.subs :as es]
    [webchange.subs :as subs]
    [webchange.editor.common.actions.subs :as actions.subs]
    [webchange.editor.common.actions.events :as actions.events]))

;; Todo: Duplicated from webchange.editor.index. Remove
(defn reset-transform
  []
  (re-frame/dispatch [::events/reset-object])
  (re-frame/dispatch [::events/reset-object-action])
  (let [state (re-frame/subscribe [::es/transform])
        {:keys [transformer target]} @state]
    (when target (.draggable target false))
    (when transformer (.destroy transformer))))

;; Todo: Duplicated from webchange.editor.index. Remove
(defn remove-transform
  []
  (re-frame/dispatch [::events/reset-transform])
  (reset-transform))

(defn- update-object-state
  [scene-id name state data]
  (re-frame/dispatch [::events/edit-object-state {:scene-id scene-id :name name :state state :data data}]))

(defn- object-state-properties-panel
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

(defn- scene-action-properties-panel
  []
  (r/with-let [scene-id @(re-frame/subscribe [::subs/current-scene])
               form-data @(re-frame/subscribe [::actions.subs/form-data])
               props (r/atom form-data)
               params {:scene-id scene-id}]
              [na/form {}
               [action-form props params]
               [na/divider {}]
               [na/form-button {:content  "Save"
                                :on-click #(do (re-frame/dispatch [::actions.events/edit-selected-action @props])
                                               (re-frame/dispatch [::events/edit-selected-scene-action]))}]]))

(defn- scene-action-properties-panel-with-key []
  (let [hash @(re-frame/subscribe [::actions.subs/form-data-hash])]
    ^{:key hash}
    [scene-action-properties-panel]))

(defn- scene-action-panel
  []
  (let [edit (r/atom nil)]
    (fn []
      (if-let [{:keys [scene-id action]} @(re-frame/subscribe [::es/selected-scene-action])]
        [na/segment {}
         (if @edit
           [na/header {:as "h4" :floated "left"}
            [na/form-input {:label "Scene action: " :default-value (name action) :on-change #(swap! edit assoc :name (-> %2 .-value)) :inline? true}]]
           [na/header {:as "h4" :floated "left" :content (str "Scene action: " (name action))}])
         [:div {:style {:float "right"}}
          (if @edit
            [na/icon {:name "checkmark" :link? true :on-click #(do
                                                                 (re-frame/dispatch [::events/rename-scene-action action (:name @edit) scene-id])
                                                                 (reset! edit nil))}]
            [na/icon {:name "pencil" :link? true :on-click #(reset! edit {})}])
          [na/icon {:name "close" :link? true :on-click #(re-frame/dispatch [::events/reset-scene-action])}]]
         [na/divider {:clearing? true}]

         [scene-action-properties-panel-with-key]]))))

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
        [properties-panel]])
     (if @object-state
       [na/segment {}
        [na/header {:as "h4" :floated "left" :content "Object state"}]
        [:div {:style {:float "right"}}
         [na/icon {:name "close" :link? true :on-click #(re-frame/dispatch [::events/reset-object-state])}]]
        [na/divider {:clearing? true}]

        ^{:key (str (:scene-id @object-state) (:name @object-state) (:state @object-state))}
        [object-state-properties-panel]])
     [scene-action-panel]]))
