(ns webchange.editor-v2.graph-builder.scene-parser.utils.get-action-data)

(defn get-action-type
  [graph action-name]
  (get-in graph [action-name :data :type]))

(defn sequence-action-name?
  [data action-name]
  (= "sequence" (get-action-type data action-name)))

(defn parallel-action-name?
  [data action-name]
  (= "parallel" (get-action-type data action-name)))

(defn get-parallel-action-children
  [graph action-name]
  (->> (get-in graph [action-name :connections])
       (map :handler)))

(defn get-sequence-action-last-child
  [data action-name]
  (->> (get-in data [action-name :data :data])
       (last)
       (keyword)))
