(ns webchange.editor-v2.diagram.graph-builder.scene-parser.parser
  (:require
    [webchange.editor-v2.diagram.graph-builder.scene-parser.data-parser.data-parser :refer [parse-data]]
    [webchange.editor-v2.diagram.graph-builder.scene-parser.duplicates-replicator.duplicates-replicator :refer [untangle-reuses]]))

(defn parse-scene
  ([scene-data]
   (parse-scene scene-data nil))
  ([scene-data start-node-name]
   (-> (if (nil? start-node-name)
         (parse-data scene-data)
         (parse-data scene-data start-node-name))
       (untangle-reuses))))
