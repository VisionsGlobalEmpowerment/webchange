(ns webchange.editor-v2.graph-builder.scene-parser.utils.get-action-data
  (:require
    [webchange.editor-v2.graph-builder.scene-parser.utils.create-graph-node :refer [get-sequence-item-name]]))

(defn action-data->action-type
  [action-data]
  (get-in action-data [:type]))

(defn graph-node->action-type
  [graph node-name]
  (action-data->action-type (get-in graph [node-name :data])))

(defn get-action-type
  [graph action-name]
  (action-data->action-type (get-in graph [action-name])))

(defn sequence-action-name?
  [data action-name]
  (= "sequence" (get-action-type data action-name)))

(defn sequence-data-action?
  [action-data]
  (= "sequence-data" (action-data->action-type action-data)))

(defn parallel-action-name?
  [data action-name]
  (= "parallel" (graph-node->action-type data action-name)))

(defn get-parallel-action-children
  [graph action-name]
  (->> (get-in graph [action-name :connections])
       (map :handler)))

(defn get-sequence-action-last-child
  [data action-name]
  (->> (get-in data [action-name :data :data])
       (last)
       (keyword)))

(defn get-sequence-data-action-last-child
  [previous-action-data previous-item-name]
  (->> (get previous-action-data :data)
       (count)
       (dec)
       (get-sequence-item-name previous-item-name)))
