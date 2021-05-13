(ns webchange.editor-v2.concepts.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.concepts.fields :refer [dataset-item-control]]
    [webchange.editor-v2.concepts.events :as concepts-events]
    [webchange.editor-v2.concepts.subs :as concepts-subs]
    [webchange.editor-v2.concepts.utils :refer [resource-type?]]
    [webchange.routes :refer [redirect-to]]))

(defn- dataset-item-fields-panel
  [dataset-id data-atom]
  (let [dataset @(re-frame/subscribe [::concepts-subs/dataset dataset-id])]
    [ui/table
     [ui/table-head
      [ui/table-row
       [ui/table-cell {:align "right" :style {:width "20%"}} "Name"]
       [ui/table-cell "Value"]]]
     [ui/table-body
      (doall (for [{:keys [name type template]} (get-in dataset [:scheme :fields])]
               (when (resource-type? type)
                 ^{:key (str name)}
                 [ui/table-row {:key (str name)}
                  [ui/table-cell {:align "right" :style {:width "20%"}} name]
                  [ui/table-cell
                   [dataset-item-control {:type      type
                                          :value     (get-in @data-atom [:data (keyword name)])
                                          :template  template
                                          :on-change #(swap! data-atom assoc-in [:data (keyword name)] %)}]]])))]]))

(defn add-dataset-item-form
  [course-id]
  (let [datasets @(re-frame/subscribe [::concepts-subs/datasets])
        first-dataset-id (-> datasets first :id)
        data (r/atom {:dataset-id first-dataset-id})]
    (if datasets
      (fn []
        (let [loading @(re-frame/subscribe [:loading])
              dataset-id (:dataset-id @data)]
          [ui/card
           [ui/card-content
            [ui/select {:value     dataset-id
                        :on-change #(swap! data assoc :dataset-id (-> % .-target .-value))
                        :variant   "outlined"
                        :style     {:min-width "150px"}}
             (for [dataset datasets]
               ^{:key (:id dataset)}
               [ui/menu-item {:value (:id dataset)} (:name dataset)])]
            [ui/text-field {:label "Name" :full-width true :default-value (:name @data) :on-change #(swap! data assoc :name (-> % .-target .-value))}]
            [ui/divider]
            [dataset-item-fields-panel dataset-id data]]
           [ui/card-actions {:style {:justify-content "flex-end"}}
            (when (:add-dataset-item loading)
              [ui/circular-progress])
            [ui/button {:on-click #(redirect-to :course-editor-v2 :id course-id)}
             "Cancel"]
            [ui/button {:color    "secondary"
                        :variant  "contained"
                        :on-click #(re-frame/dispatch [::concepts-events/add-dataset-item @data])}
             "Add"]]]))
      [ui/circular-progress])))

(defn edit-dataset-item-form
  [course-id item-id]
  (let [item @(re-frame/subscribe [::concepts-subs/dataset-item item-id])
        dataset-id (:dataset-id item)
        data (r/atom {:data (:data item)
                      :name (:name item)
                      :version (:version item)})]
    (if item
      (fn []
        (let [loading @(re-frame/subscribe [:loading])]
          [ui/card
           [ui/card-content
            [ui/text-field {:label "Name" :full-width true :default-value (:name @data) :on-change #(swap! data assoc :name (-> % .-target .-value))}]
            [ui/divider]
            [dataset-item-fields-panel dataset-id data]]
           [ui/card-actions {:style {:justify-content "flex-end"}}
            (when (:edit-dataset-item loading)
              [ui/circular-progress])
            [ui/button {:on-click #(redirect-to :course-editor-v2 :id course-id)}
             "Cancel"]
            [ui/button {:color    "secondary"
                        :variant  "contained"
                        :on-click #(re-frame/dispatch [::concepts-events/edit-dataset-item item-id @data])}
             "Save"]]]))
      [ui/circular-progress])))

(defn delete-dataset-item-modal
  []
  (let [state @(re-frame/subscribe [::concepts-subs/delete-dataset-item-modal-state])]
    [ui/dialog {:open (boolean state) :on-close #(re-frame/dispatch [::concepts-events/close-delete-dataset-item-modal])}
     [ui/dialog-title "Delete concept?"]
     [ui/dialog-content
      [ui/typography {:variant "body1"}
       (str "Are you sure you want to delete concept " (:name state) "?")]]
     [ui/dialog-actions
      [ui/button {:on-click #(re-frame/dispatch [::concepts-events/close-delete-dataset-item-modal])}
       "Cancel"]
      [ui/button {:color    "secondary"
                  :variant  "contained"
                  :on-click #(re-frame/dispatch [::concepts-events/delete-dataset-item (:id state)])}
       "Confirm"]]]))
