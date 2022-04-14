(ns webchange.utils.list)

(defn without-item
  "Returns list without passed item."
  [list item]
  {:pre [(sequential? list)]}
  (->> list
       (remove #(= item %))
       (vec)))

(defn insert-at-position
  "Insert into list item at specific position."
  ([list item position]
   (insert-at-position list item position {}))
  ([list item position {:keys [insert-list?]
                        :or   {insert-list? false}}]
   {:pre [(sequential? list) (number? position)]}
   (let [position (if (< position 0)
                    (+ position (count list))
                    position)
         [before after] (split-at position list)]
     (vec (concat before
                  (if insert-list? item [item])
                  after)))))

(defn remove-at-position
  "Remove from list item at specific position."
  [list position]
  {:pre [(sequential? list)
         (number? position) (>= position 0) (< position (count list))]}
  (-> (concat (subvec list 0 position)
              (subvec list (inc position)))
      (vec)))

(defn replace-at-position
  "Replace item in list at specific position."
  ([list new-item position]
   (replace-at-position list new-item position {}))
  ([list new-item position options]
   {:pre [(sequential? list)
          (number? position) (>= position 0) (< position (count list))]}
   (-> (remove-at-position list position)
       (insert-at-position new-item position options))))

(defn move-item
  "Move list item from `position-from` to `position-to` position."
  [list position-from position-to]
  {:pre [(sequential? list)
         (number? position-from) (>= position-from 0) (< position-from (count list))
         (number? position-to) (>= position-to 0) (< position-to (count list))]}
  (let [item (nth list position-from)]
    (-> (remove-at-position list position-from)
        (insert-at-position item position-to)
        (vec))))

(defn find-item-position
  "Find item position in list by predicate."
  [list predicate]
  {:pre [(sequential? list)
         (fn? predicate)]}
  (->> (map-indexed vector list)
       (some (fn [[index item]]
               (and (predicate item)
                    index)))))

(defn remove-by-predicate
  "Remove from list item by predicate."
  [list predicate]
  {:pre [(sequential? list)
         (fn? predicate)]}
  (let [position (find-item-position list predicate)]
    (if (some? position)
      (remove-at-position list position)
      list)))

(defn remove-by-value
  [list value]
  (remove #(= % value) list))

(defn in-list?
  "Check if item in list."
  [list item]
  {:pre [(sequential? list)]}
  (some #{item} list))

(defn distinct-by-key
  [key list]
  (->> list
       (reduce (fn [{:keys [result used-values]} item]
                 (let [value (get item key)]
                   (if (contains? used-values value)
                     {:result      result
                      :used-values used-values}
                     {:result      (conj result item)
                      :used-values (assoc used-values value true)})))
               {:result      []
                :used-values {}})
       (:result)))

(defn update-nth
  [list idx & args]
  (->> (concat [list idx] args)
       (apply update)))

(defn update-first
  [list & args]
  (->> (concat [list 0] args)
       (apply update-nth)))

(defn update-last
  [list & args]
  (let [last-idx (-> list count dec)]
    (->> (concat [list last-idx] args)
         (apply update-nth))))

(defn reverse-operand
  [f args]
  (let [operand (last args)
        params (butlast args)]
    (apply f (concat [operand] params))))

(defn update-first->>
  [& args]
  (reverse-operand update-first args))

(defn update-last->>
  [& args]
  (reverse-operand update-last args))

(defn sort-by-getters
  [[getter & other-getters] list]
  (let [v #(case %
             true 0
             false 1
             %)
        g #(-> % getter v)]
    (if-not (empty? other-getters)
      (->> (sort-by g list)
           (group-by g)
           (map second)
           (map #(sort-by-getters other-getters %))
           (apply concat))
      (sort-by g list))))
