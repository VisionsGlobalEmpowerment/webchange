(ns webchange.editor.components.data-sets.index
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [soda-ash.core :as sa :refer [Table
                                  TableBody
                                  TableCell
                                  TableHeader
                                  TableHeaderCell
                                  TableRow
                                  Modal
                                  ModalActions
                                  ModalContent
                                  ModalHeader
                                  Button]]
    [sodium.core :as na]
    [webchange.editor.events :as events]
    [webchange.editor.subs :as es]
    [webchange.editor.common.insert-json-modal :refer [insert-json-modal]]))

(def item-field-types [
                       {:key   :number
                        :value :number
                        :text  "Number"}
                       {:key   :string
                        :value :string
                        :text  "String"}
                       {:key   :image
                        :value :image
                        :text  "Image"}
                       {:key   :audio
                        :value :audio
                        :text  "Audio"}
                       {:key   :action
                        :value :action
                        :text  "Action"}
                       ])

(defn- add-dataset-field-panel
  [data-atom]
  (r/with-let [field-data (r/atom {})]
              [na/form-group {}
               [na/form-input {:label     "Name"
                               :on-change #(swap! field-data assoc :name (-> %2 .-value))
                               :inline?   true}]
               [sa/Dropdown {:placeholder "Type"
                             :search      true
                             :selection   true
                             :options     item-field-types
                             :on-change   #(swap! field-data assoc :type (.-value %2))}]
               [insert-json-modal {:text "Template (JSON)"
                                   :on-change #(swap! field-data assoc :template (js->clj %))}]
               [na/form-button {:content  "Add field"
                                :on-click #(swap! data-atom update-in [:fields] conj @field-data)}]]))

(defn remove-field
  [dataset field-name]
  (assoc dataset :fields (->> (:fields dataset)
                              (filter #(not= (:name %) field-name)))))

(defn update-field
  [dataset field-name value]
  (-> dataset
      (remove-field field-name)
      (update-in [:fields] conj value)))

(defn edit-field-modal
  [{:keys [value on-change]}]
  (r/with-let [modal-open (r/atom false)
               props (r/atom value)]
    [Modal {:open    @modal-open
            :trigger (r/as-element [:div
                                    [Button {:basic    true
                                             :on-click #(reset! modal-open true)}
                                     "Edit"]])}
     [ModalHeader {} "Edit"]
     [ModalContent {:scrolling true}
      [na/form-group {}
       [na/form-input {:label     "Name"
                       :value (:name @props)
                       :on-change #(swap! props assoc :name (-> %2 .-value))
                       :inline?   true}]
       [sa/Dropdown {:placeholder "Type"
                     :search      true
                     :selection   true
                     :options     item-field-types
                     :value (:type @props)
                     :on-change   #(swap! props assoc :type (.-value %2))}]
       [insert-json-modal {:text "Template (JSON)"
                           :value (-> @props :template clj->js)
                           :on-change #(swap! props assoc :template (js->clj %))}]]]
     [ModalActions {}
      [Button {:basic    true
               :on-click #(reset! modal-open false)} "Cancel"]
      [Button {:primary  true
               :on-click #(do (on-change @props)
                              (reset! modal-open false))} "Save"]]]))

(defn- dataset-fields-panel
  [data-atom]
  [Table {:selectable true}
   [TableHeader {}
    [TableRow {}
     [TableHeaderCell {:width 4} "Name"]
     [TableHeaderCell {} "Type"]
     [TableHeaderCell]]
    ]
   [TableBody {}
    (for [{:keys [name type] :as field-data} (:fields @data-atom)]
      ^{:key name}
      [TableRow {}
       [TableCell {} name]
       [TableCell {} type]
       [TableCell {}
        [na/button {:floated  "right"
                    :basic?   true
                    :content  "Delete"
                    :on-click #(swap! data-atom remove-field name)}]
        [:div {:style {:float "right"}}
         [edit-field-modal {:value field-data :on-change #(swap! data-atom update-field name %)}]]
        ]])]])

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
          [na/form-button {:content  "Save"
                           :on-click #(re-frame/dispatch [::events/edit-dataset dataset-id @data])}]]]))))
