(ns webchange.editor-v2.graph-builder.utils.node-data)

(defn get-node-type
  [node-data]
  (get-in node-data [:data :type]))

(defn concept-action-node?
  [node-data]
  (-> node-data
      (get-in [:data :concept-action])
      (boolean)))
