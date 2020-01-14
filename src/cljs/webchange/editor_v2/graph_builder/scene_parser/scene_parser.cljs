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

(defn get-scene-entries
  [scene-data]
  (let [objects (parse-objects scene-data)
        globals (parse-globals scene-data)
        entries (get-chain-entries (merge objects globals))]
    {:objects objects
     :globals globals
     :entries entries}))

(defn parse-whole-scene
  [scene-data]
  (let [{:keys [entries objects globals]} (get-scene-entries scene-data)]
    (merge objects
           globals
           (parse-actions scene-data entries))))

(defn parse-sub-graph
  [scene-data start-node-name]
  (let [{:keys [entries objects globals]} (get-scene-entries scene-data)
         roots (merge objects globals)]
    (if (contains? roots start-node-name)
      (let [entry (some (fn [entry]
                          (and (= (second entry) start-node-name)
                               entry))
                        entries)]
        (merge (select-keys roots [start-node-name])
               (parse-actions scene-data [entry])))
      (parse-actions scene-data [[start-node-name nil]]))))

(defn parse-data
  ([scene-data]
   (parse-data scene-data nil))
  ([scene-data start-node-name]
   (if-not (nil? start-node-name)
     (parse-sub-graph scene-data start-node-name)
     (parse-whole-scene scene-data))))
