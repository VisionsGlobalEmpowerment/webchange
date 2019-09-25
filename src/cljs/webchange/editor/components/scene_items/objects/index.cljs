(ns webchange.editor.components.scene-items.objects.index
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [soda-ash.core :as sa]
    [sodium.core :as na]
    [webchange.editor.common.components :refer [dispatch-properties-panel
                                                update-object
                                                update-current-scene-object]]
    [webchange.editor.enums :refer [object-types]]
    [webchange.editor.events :as events]
    [webchange.editor.subs :as es]
    [webchange.subs :as subs]))

;; Add object panel

(defn- add-to-scene
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

;; Objects list

(defn- remove-from-scene
  [scene-id object-name]
  (let [prepared-name (name object-name)]
    (re-frame/dispatch [::events/remove-from-scene {:scene-id scene-id :name prepared-name}])
    (re-frame/dispatch [::events/remove-from-current-scene {:name prepared-name}])))

(defn- remove-object [scene-id name]
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
           (if (:template? object)
             [sa/ItemContent {:vertical-align "middle"}
              [:div {:style {:float "left"}} (-> id str)]
              [:div {:style {:float "right" :margin-right 5}}
               "(template)"]]
             [sa/ItemContent {:vertical-align "middle"
                              :on-click       #(re-frame/dispatch [::events/select-object scene-id id])}
              [:div {:style {:float "left"}} (-> id str)]
              [:div {:style {:float "right"}}
               [na/icon {:name     "hide" :link? true
                         :on-click #(remove-from-scene scene-id id)}]
               [na/icon {:name     "remove" :link? true
                         :on-click #(remove-object scene-id id)}]]
              ])
           ])]
       [na/divider {}]
       [na/button {:basic? true :content "Add object" :on-click #(re-frame/dispatch [::events/show-form :add-object])}]])))
