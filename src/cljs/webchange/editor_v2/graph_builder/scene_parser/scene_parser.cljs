(ns webchange.editor-v2.graph-builder.scene-parser.scene-parser
  (:require
    [webchange.editor-v2.graph-builder.scene-parser.scene-parser-actions :refer [parse-actions]]
    [webchange.editor-v2.graph-builder.scene-parser.scene-parser-globals :refer [parse-globals]]
    [webchange.editor-v2.graph-builder.scene-parser.scene-parser-objects :refer [parse-objects]]))

(defn get-chain-entries
  [objects-data]
  (reduce
    (fn [result [object-name object-data]]
      (concat result (map
                       (fn [{:keys [handler]}]
                         [handler object-name])
                       (:connections object-data))))
    []
    objects-data))

(defn parse-whole-scene
  [scene-data]
  (let [parsed-objects (parse-objects scene-data)
        parsed-globals (parse-globals scene-data)
        entries (get-chain-entries (merge parsed-objects parsed-globals))]
    (merge parsed-objects
           parsed-globals
           (parse-actions scene-data entries))))

(defn parse-sub-graph
  [scene-data start-node-name]
  (let [entries [[start-node-name nil]]]
    (parse-actions scene-data entries)))

(defn parse-data
  ([scene-data]
   (parse-data scene-data nil))
  ([scene-data start-node-name]
   (if-not (nil? start-node-name)
     (parse-sub-graph scene-data start-node-name)
     (parse-whole-scene scene-data))))
