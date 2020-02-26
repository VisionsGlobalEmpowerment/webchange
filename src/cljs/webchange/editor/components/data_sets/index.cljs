(ns webchange.editor.components.data-sets.index
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [sodium.core :as na]
    [webchange.editor.events :as events]
    [webchange.editor.subs :as es]
    [webchange.editor.common.insert-json-modal :refer [insert-json-modal]]
    [webchange.editor.components.data-sets.views-add-field-form :refer [add-field-form]]
    [webchange.editor.components.data-sets.views-fields-list :refer [fields-list]]))

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
          [fields-list data]
          [na/divider {}]
          [add-field-form data]
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
          [fields-list data]
          [na/divider {}]
          [add-field-form data]
          [na/divider {}]
          [na/form-button {:content  "Save"
                           :on-click #(re-frame/dispatch [::events/edit-dataset dataset-id @data])}]]]))))
