(ns webchange.utils.list)

(defn without-item
  [list item]
  "Returns list without passed item."
  {:pre [(sequential? list)]}
  (->> list
       (remove #(= item %))
       (vec)))

(defn insert-at-position
  ([list item position]
   (insert-at-position list item position {}))
  ([list item position {:keys [insert-list?]
                        :or   {insert-list? false}}]
   "Insert into list item at specific position."
   {:pre [(sequential? list) (number? position)]}
   (let [position (if (< position 0)
                    (+ position (count list))
                    position)
         [before after] (split-at position list)]
     (vec (concat before
                  (if insert-list? item [item])
                  after)))))

(defn remove-at-position
  [list position]
  "Remove from list item at specific position."
  {:pre [(sequential? list)
         (number? position) (>= position 0) (< position (count list))]}
  (-> (concat (subvec list 0 position)
              (subvec list (inc position)))
      (vec)))

(defn replace-at-position
  ([list position new-item]
   (replace-at-position list position new-item {}))
  ([list position new-item options]
   "Replace item in list at specific position."
   {:pre [(sequential? list)
          (number? position) (>= position 0) (< position (count list))]}
   (-> (remove-at-position list position)
       (insert-at-position new-item position options))))

(defn move-item
  [list position-from position-to]
  "Move list item from `position-from` to `position-to` position."
  {:pre [(sequential? list)
         (number? position-from) (>= position-from 0) (< position-from (count list))
         (number? position-to) (>= position-to 0) (< position-to (count list))]}
  (let [item (nth list position-from)]
    (-> (remove-at-position list position-from)
        (insert-at-position item position-to)
        (vec))))

(defn find-item-position
  [list predicate]
  "Find item position in list by predicate."
  {:pre [(sequential? list)
         (fn? predicate)]}
  (->> (map-indexed vector list)
       (some (fn [[index item]]
               (and (predicate item)
                    index)))))

(defn remove-by-predicate
  [list predicate]
  "Remove from list item by predicate."
  {:pre [(sequential? list)
         (fn? predicate)]}
  (let [position (find-item-position list predicate)]
    (if (some? position)
      (remove-at-position list position)
      list)))

(defn in-list?
  [list item]
  "Check if item in list."
  {:pre [(sequential? list)]}
  (some #{item} list))
