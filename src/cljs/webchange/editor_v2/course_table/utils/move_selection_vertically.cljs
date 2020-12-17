(ns webchange.editor-v2.course-table.utils.move-selection-vertically)

(defn- get-levels-number
  [table-data]
  (->> table-data
       (map :level-idx)
       (distinct)
       (count)))

(defn- get-lessons-number
  [table-data level-index]
  (->> table-data
       (filter (fn [{:keys [level-idx]}] (= level-idx level-index)))
       (map :lesson-idx)
       (distinct)
       (count)))

(defn- get-activities-number
  [table-data level-index lessons-index]
  (->> table-data
       (filter (fn [{:keys [level-idx lesson-idx]}] (and (= level-idx level-index)
                                                         (= lesson-idx lessons-index))))
       (count)))

(defn move-selection-up
  [{:keys [selection table-data]}]
  (let [{:keys [level-idx lesson-idx activity-idx field]} selection]
    (cond
      ;; Move along :level column
      (and (= field :level-idx)
           (> level-idx 0)) (-> selection
                                (update :level-idx dec)
                                (assoc :lesson-idx 0)
                                (assoc :activity-idx 0))

      ;; Move along :lesson column
      (and (= field :lesson-idx)
           (> lesson-idx 0)) (-> selection
                                 (update :lesson-idx dec)
                                 (assoc :activity-idx 0))

      (and (= field :lesson-idx)
           (= lesson-idx 0)
           (> level-idx 0)) (let [lessons-in-previous-level (get-lessons-number table-data (dec level-idx))]
                              (-> selection
                                  (update :level-idx dec)
                                  (assoc :lesson-idx (dec lessons-in-previous-level))
                                  (assoc :activity-idx 0)))

      ;; Move along rest columns
      (> activity-idx 0) (update selection :activity-idx dec)

      (and (= activity-idx 0)
           (> lesson-idx 0)) (let [activities-in-previous-lesson (get-activities-number table-data level-idx (dec lesson-idx))]
                               (-> selection
                                   (update :lesson-idx dec)
                                   (assoc :activity-idx (dec activities-in-previous-lesson))))

      (and (= activity-idx 0)
           (= lesson-idx 0)
           (> level-idx 0)) (let [lessons-in-previous-level (get-lessons-number table-data (dec level-idx))
                                  activities-in-previous-lesson (get-activities-number table-data (dec level-idx) (dec lessons-in-previous-level))]
                              (-> selection
                                  (update :level-idx dec)
                                  (assoc :lesson-idx (dec lessons-in-previous-level))
                                  (assoc :activity-idx (dec activities-in-previous-lesson))))

      :else selection)))

(defn move-selection-down
  [{:keys [selection table-data]}]
  (let [{:keys [level-idx lesson-idx activity-idx field]} selection
        levels-number (get-levels-number table-data)
        lessons-number (get-lessons-number table-data level-idx)
        activities-number (get-activities-number table-data level-idx lesson-idx)]
    (cond
      ;; Move along :level column
      (and (= field :level-idx)
           (< level-idx (dec levels-number))) (-> selection
                                                  (update :level-idx inc)
                                                  (assoc :lesson-idx 0)
                                                  (assoc :activity-idx 0))

      ;; Move along :lesson column
      (and (= field :lesson-idx)
           (< lesson-idx (dec lessons-number))) (-> selection
                                                    (update :lesson-idx inc)
                                                    (assoc :activity-idx 0))

      (and (= field :lesson-idx)
           (= lesson-idx (dec lessons-number))
           (< level-idx (dec levels-number))) (-> selection
                                                  (update :level-idx inc)
                                                  (assoc :lesson-idx 0)
                                                  (assoc :activity-idx 0))

      ;; Move along rest columns
      (< activity-idx (dec activities-number)) (update selection :activity-idx inc)

      (and (= activity-idx (dec activities-number))
           (< lesson-idx (dec lessons-number))) (-> selection
                                                    (update :lesson-idx inc)
                                                    (assoc :activity-idx 0))

      (and (= activity-idx (dec activities-number))
           (= lesson-idx (dec lessons-number))
           (< level-idx (dec levels-number))) (-> selection
                                                  (update :level-idx inc)
                                                  (assoc :lesson-idx 0)
                                                  (assoc :activity-idx 0))

      :else selection)))
