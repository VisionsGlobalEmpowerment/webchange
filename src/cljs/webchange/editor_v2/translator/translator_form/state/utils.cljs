(ns webchange.editor-v2.translator.translator-form.state.utils)

(defn get-index
  [list key value]
  (->> list
       (map-indexed vector)
       (some (fn [[index item]]
               (and (= (get item key) value)
                    index)))))

(defn remove-by-index
  [list index]
  (vec (concat (subvec list 0 index)
               (subvec list (inc index)))))

(defn insert-by-index
  [list index item]
  (vec (concat (subvec list 0 index)
               [item]
               (subvec list index))))
