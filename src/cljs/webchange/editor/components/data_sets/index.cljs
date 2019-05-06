(ns webchange.editor.components.data-sets.index
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [soda-ash.core :as sa]
    [sodium.core :as na]
    [webchange.editor.events :as events]
    [webchange.editor.subs :as es]))

(def item-field-types [{:key :number :value :number :text "Number"}
                       {:key :string :value :string :text "String"}
                       {:key :image :value :image :text "Image"}
                       {:key :audio :value :audio :text "Audio"}])

(defn- add-dataset-field-panel
  [data-atom]
  (r/with-let [field-data (r/atom {})]
              [na/form-group {}
               [na/form-input {:label     "name"
                               :on-change #(swap! field-data assoc :name (-> %2 .-value))
                               :inline?   true}]
               [sa/Dropdown {:placeholder "Type"
                             :search      true
                             :selection   true
                             :options     item-field-types
                             :on-change   #(swap! field-data assoc :type (.-value %2))}]
               [na/form-button {:content  "Add field"
                                :on-click #(swap! data-atom update-in [:fields] conj @field-data)}]]))

(defn remove-field
  [dataset field-name]
  (assoc dataset :fields (->> (:fields dataset)
                              (filter #(not= (:name %) field-name)))))

(defn- dataset-fields-panel
  [data-atom]
  [sa/ItemGroup {}
   (for [field (:fields @data-atom)]
     ^{:key (:name field)}
     [sa/Item {}
      [sa/ItemContent {}
       [:p (str (:name field) " " (:type field))
        [na/button {:floated  "right"
                    :basic?   true
                    :content  "Delete"
                    :on-click #(swap! data-atom remove-field (:name field))}]]]])])

(defn add-dataset-form
  []
  (let [data (r/atom {})]
    (fn []
      (let [loading @(re-frame/subscribe [:loading])]
        [na/segment {:loading? (when (:add-dataset loading))}
         [na/header {:as      "h4"
                     :content "Add dataset"}]
         [na/divider {:clearing? true}]
         [na/form {}
          [na/form-input {:label         "name"
                          :default-value (:name @data)
                          :on-change     #(swap! data assoc :name (-> %2 .-value))
                          :inline?       true}]
          [na/divider {}]
          [dataset-fields-panel data]
          [na/divider {}]
          [add-dataset-field-panel data]
          [na/divider {}]
          [na/form-button {:content  "Add"
                           :on-click #(re-frame/dispatch [::events/add-dataset @data])}]]]))))

(defn edit-dataset-form
  []
  (let [dataset-id @(re-frame/subscribe [::es/current-dataset-id])
        {scheme :scheme} @(re-frame/subscribe [::es/dataset dataset-id])
        data (r/atom {:fields (:fields scheme)})]
    (fn []
      (let [loading @(re-frame/subscribe [:loading])]
        [na/segment {:loading? (when (:edit-dataset loading))}
         [na/header {:as      "h4"
                     :content "Edit dataset"}]
         [na/divider {:clearing? true}]
         [na/form {}
          [dataset-fields-panel data]
          [na/divider {}]
          [add-dataset-field-panel data]
          [na/divider {}]
          [na/form-button {:content  "Edit"
                           :on-click #(re-frame/dispatch [::events/edit-dataset dataset-id @data])}]]]))))