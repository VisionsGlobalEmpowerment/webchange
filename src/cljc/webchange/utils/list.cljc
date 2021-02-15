(ns webchange.utils.list)

(defn without-item
  [list item]
  "Returns list without passed item."
  {:pre [(sequential? list)]}
  (->> list
       (remove #(= item %))
       (vec)))

(defn insert-at-position
  [list item position]
  "Insert into list item at specific position."
  {:pre [(sequential? list) (number? position)]}
  (let [position (if (< position 0)
                   (+ position (count list))
                   position)
        [before after] (split-at position list)]
    (vec (concat before [item] after))))

(defn remove-at-position
  [list position]
  "Remove from list item at specific position."
  {:pre [(sequential? list)
         (number? position) (>= position 0) (< position (count list))]}
  (-> (concat (subvec list 0 position)
              (subvec list (inc position)))
      (vec)))

(defn move-item
  [list position-from position-to]
  "Move list item from `position-from` to `position-to` position."
  {:pre [(sequential? list)
         (number? position-from) (>= position-from 0) (< position-from (count list))
         (number? position-to) (>= position-to 0) (< position-to (count list))]}
  (let [item (nth list position-from)]
    (-> (remove-at-position list position-from)
        (insert-at-position item position-to))))
