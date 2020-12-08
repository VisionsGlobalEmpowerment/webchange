(ns webchange.editor-v2.course-table.state.edit-utils)

(defn- find-data-idx
  [list field value]
  (some (fn [[idx item-data]]
          (and (= (get item-data field) value)
               [idx item-data]))
        (map-indexed vector list)))

(defn get-activity-path
  [course-data selection-data]
  (let [[level-idx level-data] (find-data-idx (:levels course-data) :level (:level selection-data))
        [lesson-idx _] (find-data-idx (:lessons level-data) :lesson (:lesson selection-data))]
    [:levels level-idx
     :lessons lesson-idx
     :activities (:lesson-idx selection-data)]))
