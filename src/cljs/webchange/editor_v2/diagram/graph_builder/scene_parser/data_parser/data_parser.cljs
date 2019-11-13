(ns webchange.editor-v2.diagram.graph-builder.scene-parser.data-parser.data-parser
  (:require
    [webchange.editor-v2.diagram.graph-builder.scene-parser.data-parser.data-parser-actions :refer [parse-actions]]
    [webchange.editor-v2.diagram.graph-builder.scene-parser.data-parser.data-parser-globals :refer [parse-globals]]
    [webchange.editor-v2.diagram.graph-builder.scene-parser.data-parser.data-parser-objects :refer [parse-objects]]))

(defn get-chain-entries
  [objects-data]
  (reduce
    (fn [result [object-name object-data]]
      (concat result (reduce
                       (fn [result [_ connection-data]]
                         (concat result (reduce
                                          (fn [result [_ handlers]]
                                            (concat result (map
                                                             (fn [handler] [handler object-name])
                                                             handlers)))
                                          []
                                          (:handlers connection-data))))
                       []
                       (:connections object-data))))
    []
    objects-data))

(defn parse-data
  ([scene-data]
   (let [parsed-objects (parse-objects scene-data)
         parsed-globals (parse-globals scene-data)
         entries (get-chain-entries (merge parsed-objects parsed-globals))]
     (merge parsed-objects
            parsed-globals
            (parse-actions scene-data entries))))
  ([scene-data start-node-name]
   (let [entries [[start-node-name nil]]]
     (parse-actions scene-data entries))))
