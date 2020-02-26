(ns webchange.editor.components.data-sets.views-fields-list
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [reagent.core :as r]
    [webchange.editor.components.data-sets.views-edit-field-modal :refer [edit-field-modal]]))

(defn- remove-field
  [dataset field-name]
  (assoc dataset :fields (->> (:fields dataset)
                              (filter #(not= (:name %) field-name)))))

(defn- update-field
  [dataset field-name value]
  (-> dataset
      (remove-field field-name)
      (update-in [:fields] conj value)))

(defn- row
  [{:keys [name type scenes on-edit on-remove] :as field}]
  [ui/table-row
   [ui/table-cell name]
   [ui/table-cell type]
   [ui/table-cell
    (for [scene scenes]
      ^{:key scene}
      [ui/chip {:label   scene
                :variant "outlined"
                :style   {:margin 2}}])]
   [ui/table-cell {:align "right"}
    [ui/button
     {:on-click #(on-remove field)}
     "Delete"]
    [ui/button
     {:on-click #(on-edit field)}
     "Edit"]]])

(defn- body
  [{:keys [items on-edit on-remove]}]
  [ui/table-body
   (for [{:keys [name] :as field-data} items]
     ^{:key name}
     [row (merge field-data
                 {:on-edit   on-edit
                  :on-remove on-remove})])])

(defn- header
  []
  [ui/table-head
   [ui/table-row
    [ui/table-cell "Name"]
    [ui/table-cell "Type"]
    [ui/table-cell "Scenes"]
    [ui/table-cell ""]]])

(defn fields-list
  [data-atom]
  (r/with-let [current-field (r/atom nil)]
              (let [handle-remove-click (fn [field] (swap! data-atom remove-field (:name field)))
                    handle-edit-click (fn [field] (reset! current-field field))
                    handle-modal-save (fn [prev-data new-data]
                                        (swap! data-atom update-field (:name prev-data) new-data)
                                        (reset! current-field nil))
                    handle-modal-cancel (fn [] (reset! current-field nil))
                    modal-open? (-> @current-field nil? not)]
                [:div
                 [ui/table
                  [header]
                  [body {:items     (->> @data-atom :fields (sort-by :name))
                         :on-edit   handle-edit-click
                         :on-remove handle-remove-click}]]
                 (when modal-open?
                   [edit-field-modal {:open      modal-open?
                                      :value     @current-field
                                      :on-save   handle-modal-save
                                      :on-cancel handle-modal-cancel}])])))
