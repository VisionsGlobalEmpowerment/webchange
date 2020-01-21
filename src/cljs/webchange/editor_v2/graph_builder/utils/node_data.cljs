(ns webchange.editor-v2.graph-builder.utils.node-data)

(defn get-node-type
  [node-data]
  (get-in node-data [:data :type]))

(defn speech-node?
  [node-data]
  (let [node-type (get-node-type node-data)]
    (or (= "audio" node-type)
        (and (= "animation-sequence" node-type)
             (contains? (:data node-data) :audio)))))

(defn concept-action-node?
  [node-data]
  (-> node-data
      (get-in [:data :concept-action])
      (boolean)))

(defn phrase-node?
  [node-data]
  (contains? (:data node-data) :phrase))

(defn object-node?
  [node-data]
  (= :object (:entity node-data)))

(defn trigger-node?
  [node-data]
  (= :trigger (:entity node-data)))
