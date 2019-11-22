(ns webchange.editor-v2.translator.translator-form.utils
  (:require
    [webchange.editor-v2.diagram.graph-builder.graph :refer [get-diagram-graph]]))

(defn get-graph
  [scene-data action-name concepts-scheme]
  (when-not (nil? action-name)
    (get-diagram-graph scene-data :translation {:start-node action-name
                                                :concepts   (get-in concepts-scheme [:scheme :fields])})))

(defn get-used-concept-actions
  [graph]
  (->> graph
       (filter (fn [[_ action-data]] (get-in action-data [:data :concept-action])))
       (map (fn [[action-name _]] action-name))))

(defn- get-scene-audios
  [scene-data]
  (->> (:assets scene-data)
       (filter (fn [{:keys [type]}] (= type "audio")))
       (map (fn [{:keys [url]}] url))))

(defn- get-concepts-audios
  [concepts used-concept-actions]
  (->> concepts
       (reduce
         (fn [result {:keys [data]}]
           (let [concept-actions (select-keys data used-concept-actions)
                 concept-audios (map (fn [[_ {:keys [audio]}]] audio) concept-actions)]
             (concat result concept-audios)))
         [])
       (distinct))
  )

(defn get-audios
  [scene-data concepts used-concept-actions]
  (->> (concat (get-scene-audios scene-data)
               (get-concepts-audios concepts used-concept-actions))
       (distinct)))
