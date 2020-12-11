(ns webchange.editor-v2.course-table.views-row-common
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.course-table.state.selection :as selection-state]
    [webchange.editor-v2.course-table.utils.cell-data :refer [activity->cell-data cell-data->cell-attributes]]
    [webchange.editor-v2.course-table.state.edit :refer [field-editable?]]))

(defn- cell-selected?
  [selection-type selection-data cell-data]
  (and (= selection-type :cell)
       (= selection-data cell-data)))

(defn field-cell
  [{:keys [data field span] :as props}]
  (let [selection @(re-frame/subscribe [::selection-state/selection])
        cell-data (activity->cell-data data field)
        spanned? (some? span)
        selected? (cell-selected? (:type selection) (:data selection) cell-data)
        editable? (field-editable? (:field cell-data))]
    (into [ui/table-cell (cond-> (merge (:cell-props props)
                                        (cell-data->cell-attributes cell-data))
                                 spanned? (assoc :row-span span)
                                 selected? (update :class-name str " selected")
                                 editable? (update :class-name str " editable"))]
          (-> (r/current-component) (r/children)))))
