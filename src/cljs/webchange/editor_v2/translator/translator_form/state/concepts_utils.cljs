(ns webchange.editor-v2.translator.translator-form.state.concepts-utils
  (:require
    [webchange.editor-v2.translator.translator-form.state.audios-utils :as audios-utils]))

(defn- get-concept-scene-actions
  "Get names of actions in 'concept-data' related to 'scene-id'"
  [concept-scheme scene-id]
  (->> (get-in concept-scheme [:scheme :fields])
       (filter (fn [{:keys [type scenes]}]
                 (and (= type "action")
                      (some #(= scene-id %) scenes))))
       (map :name)
       (map keyword)))

(defn- get-concept-actions
  "Get actions data from 'concepts' list by 'actions-names' list"
  [concepts actions-names]
  (->> concepts
       (reduce (fn [result concept]
                 (concat result (-> (:data concept)
                                    (select-keys actions-names)
                                    (vals))))
               [])))

(defn- get-action-audio-data
  [{:keys [type data] :as action-data}]
  (cond
    (or (= type "audio")
        (= type "animation-sequence")) [(audios-utils/get-action-audio-data action-data)]
    (or (= type "parallel")
        (= type "sequence-data")) (->> data
                                       (map get-action-audio-data)
                                       (flatten)
                                       (distinct))
    :else []))

(defn- get-concepts-audio
  [concepts-actions]
  (->> concepts-actions
       (map get-action-audio-data)
       (flatten)
       (map (fn [audio-data] (select-keys audio-data [:url :target])))
       (distinct)))

(defn- fix-assets-data
  [assets-list]
  (->> assets-list
       (map (fn [asset-data]
              (assoc asset-data :type "audio")))))

(defn get-concepts-audio-assets
  [concept-scheme concepts-list scene-id]
  (->> (get-concept-scene-actions concept-scheme scene-id)
       (get-concept-actions concepts-list)
       (get-concepts-audio)
       (fix-assets-data)))
