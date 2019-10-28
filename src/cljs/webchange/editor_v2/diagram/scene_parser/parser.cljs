(ns webchange.editor-v2.diagram.scene-parser.parser
  (:require
    [webchange.editor-v2.diagram.scene-parser.data-parser.data-parser :refer [parse-data]]
    [webchange.editor-v2.diagram.scene-parser.duplicates-replicator.duplicates-replicator :refer [untangle-reuses]]))

(defn parse-scene
  [scene-data]
  (->> scene-data
       parse-data
       untangle-reuses))
