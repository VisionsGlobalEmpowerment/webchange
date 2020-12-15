(ns webchange.editor-v2.course-table.utils.move-selection
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.course-table.state.data :as data-state]
    [webchange.editor-v2.course-table.state.selection :as selection-state]
    [webchange.editor-v2.course-table.state.pagination :as pagination-state]
    [webchange.editor-v2.course-table.utils.move-selection-horizontally :refer [move-selection-left move-selection-right]]
    [webchange.editor-v2.course-table.utils.move-selection-vertically :refer [move-selection-up move-selection-down]]))

(defn update-selection
  [{:keys [direction selection] :as params}]
  (case direction
    :left (move-selection-left params)
    :right (move-selection-right params)
    :up (move-selection-up params)
    :down (move-selection-down params)
    selection))

(defn- get-row-idx
  [table-data selection]
  (let [fields [:level :lesson :lesson-idx]]
    (->> table-data
         (some (fn [row]
                 (and (= (select-keys row fields)
                         (select-keys selection fields))
                      row)))
         :idx)))

(defn- update-pagination
  [{:keys [table-data prev-selection new-selection]}]
  (let [rows-skip @(re-frame/subscribe [::pagination-state/skip-rows])
        rows-count @(re-frame/subscribe [::pagination-state/page-rows])

        current-row-idx (get-row-idx table-data new-selection)
        prev-row-idx (get-row-idx table-data prev-selection)

        page-start-idx rows-skip
        page-end-idx (+ rows-skip rows-count)
        total-rows (count table-data)

        padding 2]
    (when (and (> current-row-idx prev-row-idx)
               (> current-row-idx (- page-end-idx padding)))
      (re-frame/dispatch [::pagination-state/shift-skip-rows (-> current-row-idx (- page-end-idx) (+ padding)) total-rows]))

    (when (and (< current-row-idx prev-row-idx)
               (< current-row-idx (+ page-start-idx padding)))
      (re-frame/dispatch [::pagination-state/shift-skip-rows (-> current-row-idx (- page-start-idx) (- padding)) total-rows]))))

(defn move-selection
  [direction columns]
  (let [table-data @(re-frame/subscribe [::data-state/table-data])
        selection @(re-frame/subscribe [::selection-state/selection])
        new-selection (update-selection {:columns    columns
                                         :direction  direction
                                         :selection  (:data selection)
                                         :table-data table-data})]
    (when-not (= new-selection (:data selection))
      (update-pagination {:table-data     table-data
                          :prev-selection (:data selection)
                          :new-selection  new-selection})
      (re-frame/dispatch [::selection-state/set-selection (:type selection) new-selection]))))
