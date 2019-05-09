(ns webchange.editor.components.data-sets.data-set-item.index
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [soda-ash.core :refer [Table
                           TableBody
                           TableCell
                           TableHeader
                           TableHeaderCell
                           TableRow]]
    [sodium.core :as na]
    [webchange.editor.components.data-sets.data-set-item.dataset-item-field :refer [dataset-item-control]]
    [webchange.editor.events :as events]
    [webchange.editor.subs :as es]))

(defn- dataset-item-fields-panel
  [data-atom]
  (let [dataset-id @(re-frame/subscribe [::es/current-dataset-id])
        dataset @(re-frame/subscribe [::es/dataset dataset-id])]
    [Table {:selectable true}
     [TableHeader {}
      [TableRow {}
       [TableHeaderCell {:width 4} "Name"]
       [TableHeaderCell {} "Value"]]
      ]
     [TableBody {}
      (doall (for [{name :name type :type} (get-in dataset [:scheme :fields])]
               ^{:key (str name)}
               [TableRow {}
                [TableCell {} name]
                [TableCell {}
                 [dataset-item-control {:type      type
                                        :value     (get-in @data-atom [:data (keyword name)])
                                        :on-change #(swap! data-atom assoc-in [:data (keyword name)] %)}]]]))]]))

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
