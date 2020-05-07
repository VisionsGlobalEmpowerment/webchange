(ns webchange.editor-v2.translator.translator-form.state.scene-utils
  (:require
    [webchange.editor-v2.translator.translator-form.state.utils :refer [get-index
                                                                        remove-by-index]]))

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
