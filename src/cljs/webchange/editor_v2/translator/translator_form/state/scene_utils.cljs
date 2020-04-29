(ns webchange.editor-v2.translator.translator-form.state.scene-utils)

(defn- get-index
  [list key value]
  (->> list
       (map-indexed vector)
       (some (fn [[index item]]
               (and (= (get item key) value)
                    index)))))

(defn- remove-by-index
  [list index]
  (vec (concat (subvec list 0 index)
               (subvec list (inc index)))))

(defn add-if-not-exist
  [list key value data]
  (let [index (get-index list key value)]
    (if (nil? index)
      (conj list data)
      list)))

(defn remove-by-key
  [list key value]
  (->> (get-index list key value)
       (remove-by-index list)))

(defn update-by-key
  [list key value data-patch]
  (let [index (get-index list key value)]
    (update-in list [index] merge data-patch)))
