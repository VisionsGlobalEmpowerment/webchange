(ns webchange.editor-v2.course-table.utils.move-selection-vertically)

(defn- get-item-idx
  [item list]
  (some (fn [[idx list-item]]
          (and (= list-item item) idx))
        (map-indexed vector list)))

(defn- get-table-map
  "Return levels and lessons count info:
  {:levels-ids [2]
   :levels     {2 {:lessons-ids [1 2]
                   :lessons     {1 {:activities-count 9}
                                 2 {:activities-count 10}}}}}"
  [rows]
  (reduce (fn [result {:keys [level lesson]}]
            (-> result
                (update-in [:levels-ids] concat [level])
                (update-in [:levels-ids] distinct)
                (update-in [:levels level :lessons-ids] concat [lesson])
                (update-in [:levels level :lessons-ids] distinct)
                (update-in [:levels level :lessons lesson :activities-count] inc)))
          {}
          rows))

(defn- get-level-position
  [level-id table-map]
  (get-item-idx level-id (get-in table-map [:levels-ids])))

(defn- get-lesson-in-level-position
  [lesson-id level-id table-map]
  (get-item-idx lesson-id (get-in table-map [:levels level-id :lessons-ids])))

(defn- get-activities-count
  [lesson-id level-id table-map]
  (get-in table-map [:levels level-id :lessons lesson-id :activities-count]))

(defn- get-level-lessons
  [level-id table-map]
  (get-in table-map [:levels level-id :lessons-ids]))

(defn- get-levels-count
  [table-map]
  (->> (get-in table-map [:levels-ids])
       (count)))

(defn- get-lessons-count
  [level-id table-map]
  (->> (get-level-lessons level-id table-map)
       (count)))

(defn- level-position->level-id
  [level-position table-map]
  (-> (get-in table-map [:levels-ids])
      (nth level-position)))

(defn- lesson-position->lesson-id
  [lesson-position level-id table-map]
  (-> (get-in table-map [:levels level-id :lessons-ids])
      (nth lesson-position)))

(defn move-selection-up
  [{:keys [selection table-data]}]
  (let [{:keys [level lesson lesson-idx field]} selection
        table-map (get-table-map table-data)
        lesson-position (get-lesson-in-level-position lesson level table-map)
        level-position (get-level-position level table-map)]

    (cond
      ;; Move along :level column
      (and (= field :level)
           (= level-position 0)) (let [first-lessons-id (first (get-level-lessons level table-map))]
                                   (-> selection
                                       (assoc :lesson first-lessons-id)
                                       (assoc :lesson-idx 0)))

      (and (= field :level)
           (> level-position 0)) (let [previous-level-id (level-position->level-id (dec level-position) table-map)
                                       previous-level-first-lessons-id (first (get-level-lessons previous-level-id table-map))]
                                   (-> selection
                                       (assoc :level previous-level-id)
                                       (assoc :lesson previous-level-first-lessons-id)
                                       (assoc :lesson-idx 0)))

      ;; Move along :lesson column
      (and (= field :lesson)
           (> lesson-position 0)) (let [previous-lesson-id (lesson-position->lesson-id (dec lesson-position) level table-map)]
                                    (-> selection
                                        (assoc :lesson previous-lesson-id)
                                        (assoc :lesson-idx 0)))
      (and (= field :lesson)
           (> level-position 0)) (let [previous-level-id (level-position->level-id (dec level-position) table-map)
                                       previous-level-last-lessons-id (last (get-level-lessons previous-level-id table-map))]
                                   (-> selection
                                       (assoc :level previous-level-id)
                                       (assoc :lesson previous-level-last-lessons-id)
                                       (assoc :lesson-idx 0)))
      ;; Move along rest columns
      (> lesson-idx 0) (update selection :lesson-idx dec)
      (> lesson-position 0) (let [previous-lesson-id (lesson-position->lesson-id (dec lesson-position) level table-map)
                                  previous-lesson-activities-count (get-activities-count previous-lesson-id level table-map)]
                              (-> selection
                                  (assoc :lesson previous-lesson-id)
                                  (assoc :lesson-idx (dec previous-lesson-activities-count))))
      (> level-position 0) (let [previous-level-id (level-position->level-id (dec level-position) table-map)
                                 previous-level-last-lessons-id (last (get-level-lessons previous-level-id table-map))
                                 previous-level-last-lesson-activities-count (get-activities-count previous-level-last-lessons-id previous-level-id table-map)]
                             (-> selection
                                 (assoc :level previous-level-id)
                                 (assoc :lesson previous-level-last-lessons-id)
                                 (assoc :lesson-idx (dec previous-level-last-lesson-activities-count))))
      :else selection)))

(defn move-selection-down
  [{:keys [selection table-data]}]
  (let [{:keys [level lesson lesson-idx field]} selection
        table-map (get-table-map table-data)
        lesson-position (get-lesson-in-level-position lesson level table-map)
        level-position (get-level-position level table-map)

        last-activity-position (dec (get-activities-count lesson level table-map))
        last-lessons-position (dec (get-lessons-count level table-map))
        last-levels-position (dec (get-levels-count table-map))]
    (cond
      ;; Move along :level column
      (and (= field :level)
           (= level-position last-levels-position)) (let [first-lessons-id (first (get-level-lessons level table-map))]
                                                      (-> selection
                                                          (assoc :lesson first-lessons-id)
                                                          (assoc :lesson-idx 0)))

      (and (= field :level)
           (< level-position last-levels-position)) (let [next-level-id (level-position->level-id (inc level-position) table-map)
                                                          next-level-first-lesson-id (first (get-level-lessons next-level-id table-map))]
                                                      (-> selection
                                                          (assoc :level next-level-id)
                                                          (assoc :lesson next-level-first-lesson-id)
                                                          (assoc :lesson-idx 0)))

      ;; Move along :lesson column
      (and (= field :lesson)
           (< lesson-position last-lessons-position)) (let [next-lesson-id (lesson-position->lesson-id (inc lesson-position) level table-map)]
                                                        (-> selection
                                                            (assoc :lesson next-lesson-id)
                                                            (assoc :lesson-idx 0)))
      (and (= field :lesson)
           (< level-position last-levels-position)) (let [next-level-id (level-position->level-id (inc level-position) table-map)
                                                          next-level-first-lesson-id (first (get-level-lessons next-level-id table-map))]
                                                      (-> selection
                                                          (assoc :level next-level-id)
                                                          (assoc :lesson next-level-first-lesson-id)
                                                          (assoc :lesson-idx 0)))

      ;; Move along rest columns
      (< lesson-idx last-activity-position) (update selection :lesson-idx inc)
      (< lesson-position last-lessons-position) (let [next-lesson-id (lesson-position->lesson-id (inc lesson-position) level table-map)]
                                                  (-> selection
                                                      (assoc :lesson next-lesson-id)
                                                      (assoc :lesson-idx 0)))
      (< level-position last-levels-position) (let [next-level-id (level-position->level-id (inc level-position) table-map)
                                                    next-level-first-lesson-id (first (get-level-lessons next-level-id table-map))]
                                                (-> selection
                                                    (assoc :level next-level-id)
                                                    (assoc :lesson next-level-first-lesson-id)
                                                    (assoc :lesson-idx 0)))
      :else selection)))
