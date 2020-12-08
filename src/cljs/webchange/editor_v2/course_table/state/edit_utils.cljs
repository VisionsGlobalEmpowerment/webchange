(ns webchange.editor-v2.course-table.state.edit-utils)

(defn- find-data-idx
  [list field value]
  (some (fn [[idx item-data]]
          (and (= (get item-data field) value)
               [idx item-data]))
        (map-indexed vector list)))

(defn get-lesson-path
  [course-data selection-data]
  (let [[level-idx level-data] (find-data-idx (:levels course-data) :level (:level selection-data))
        [lesson-idx _] (find-data-idx (:lessons level-data) :lesson (:lesson selection-data))]
    [:levels level-idx
     :lessons lesson-idx]))

(defn get-activity-path
  [course-data selection-data]
  (-> (get-lesson-path course-data selection-data)
      (concat [:activities (:lesson-idx selection-data)])))

(defn- get-list-item
  [list field-name field-value]
  (some (fn [item]
          (and (= (get item field-name) field-value)
               item))
        list))

(defn get-level-data
  [course-data {:keys [level]}]
  (-> (:levels course-data)
      (get-list-item :level level)))

(defn get-lesson-data
  [course-data {:keys [lesson] :as selection}]
  (-> (get-level-data course-data selection)
      (:lessons)
      (get-list-item :lesson lesson)))

(defn get-activity-data
  [course-data {:keys [lesson-idx] :as selection}]
  (-> (get-lesson-data course-data selection)
      (:activities)
      (nth lesson-idx)))
