(ns webchange.editor-v2.translator.translator-form.audio-assets.utils)

(defn get-action-audio-data
  [{:keys [type audio target data]}]
  (cond
    (= type "animation-sequence") [{:url    audio
                                    :target target}]
    (or (= type "parallel")
        (= type "sequence-data")) (->> data
                                       (map get-action-audio-data)
                                       (flatten)
                                       (distinct))
    :else []))

(defn get-concept-actions
  "Get actions data from 'concepts' list by 'actions-names' list"
  [concepts actions-names]
  (->> concepts
       (reduce (fn [result concept]
                 (concat result (-> (:data concept)
                                    (select-keys actions-names)
                                    (vals))))
               [])))

(defn get-concept-scene-actions
  "Get names of actions in 'concept-data' related to 'scene-id'"
  [concept-data scene-id]
  (->> (get-in concept-data [:scheme :fields])
       (filter (fn [{:keys [type scenes]}]
                 (and (= type "action")
                      (some #(= scene-id %) scenes))))
       (map :name)
       (map keyword)))
