;; Object properties panel

(ns webchange.editor.object-properties-panel.object-properties-panel
  (:require
    [webchange.editor.common :refer [dispatch-properties-panel
                                     object-types
                                     update-current-scene-object
                                     update-object
                                     update-object-action]]
    [webchange.editor.action-properties.core :refer [action-types] :as action-properties]
    [webchange.editor.events :as events]
    [webchange.editor.subs :as es]
    [webchange.subs :as subs]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [soda-ash.core :as sa]
    [sodium.core :as na]
    [sodium.extensions :as nax]))

(defn- add-object-action-panel
  [scene-id name on-save]
  (let [props (r/atom {})]
    (fn [scene-id name on-save]
      [na/form {}
       [sa/Dropdown {:placeholder "Type" :search true :selection true :options action-types :on-change #(swap! props assoc :type (.-value %2))}]
       [na/divider {}]
       [na/form-input {:label "name" :default-value (:scene-name @props) :on-change #(swap! props assoc :scene-name (-> %2 .-value)) :inline? true}]
       [na/form-input {:label "on" :default-value (:on @props) :on-change #(swap! props assoc :on (-> %2 .-value)) :inline? true}]
       [na/divider {}]
       [action-properties/action-properties-panel props]
       [na/divider {}]
       [na/form-button {:content "Add" :on-click #(do (update-object-action scene-id name (:scene-name @props) @props)
                                                      (on-save))}]
       ])))

(defn- new-object-action-section
  []
  (let [mode (r/atom nil)]
    (fn []
      (let [{:keys [scene-id name]} @(re-frame/subscribe [::es/selected-object])]
        (if (= @mode :add-action)
          [add-object-action-panel scene-id name #(reset! mode nil)]
          [na/button {:basic? true :content "Add action" :on-click #(reset! mode :add-action)}])))))

(defn- actions-panel
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

(defn- check-prev
  [prev current props]
  (when (not= @prev current)
    (reset! prev current)
    (reset! props current)))

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

         ;; Properties

         [sa/AccordionTitle {:active   (= 0 @activeIndex)
                             :on-click #(reset! activeIndex 0)}
          [na/icon {:name "dropdown"}]
          "Properties"]
         [sa/AccordionContent {:active (= 0 @activeIndex)}
          [na/form {}
           [sa/Dropdown {:placeholder "Type"
                         :search      true
                         :selection   true
                         :options     object-types
                         :value       (:type @props)
                         :on-change   #(swap! props assoc :type (.-value %2))}]
           ^{:key (str scene-id name)} [dispatch-properties-panel props]
           [na/form-button {:content  "Save"
                            :on-click #(do (update-object scene-id name @props)
                                           (update-current-scene-object name @props))}]]]

         ;; States

         [sa/AccordionTitle {:active   (= 1 @activeIndex)
                             :on-click #(reset! activeIndex 1)}
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
                [na/button {:size     "mini"
                            :content  "Set default"
                            :on-click #(re-frame/dispatch [::events/set-default-object-state scene-id name state-id])}]
                [na/button {:size     "mini"
                            :content  "Edit"
                            :on-click #(re-frame/dispatch [::events/select-object-state scene-id name state-id])}]
                [na/button {:size     "mini"
                            :content  "Delete"
                            :on-click #(re-frame/dispatch [::events/delete-object-state scene-id name state-id])}]]]])]
          [na/divider {}]
          [na/button {:basic? true :content "Add" :on-click #(re-frame/dispatch [::events/select-object-state scene-id name])}]]

         ;; Actions

         [sa/AccordionTitle {:active   (= 2 @activeIndex)
                             :on-click #(reset! activeIndex 2)}
          [na/icon {:name "dropdown"}]
          "Actions"]
         [sa/AccordionContent {:active (= 2 @activeIndex)} [actions-panel]]]))))