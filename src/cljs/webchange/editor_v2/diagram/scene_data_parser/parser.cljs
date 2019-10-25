(ns webchange.editor-v2.diagram.scene-data-parser.parser
  (:require
    [webchange.editor-v2.diagram.scene-data-parser.duplicates-processor :refer [remove-duplicates]]
    [webchange.editor-v2.diagram.scene-data-parser.parser-actions :refer [parse-actions]]
    [webchange.editor-v2.diagram.scene-data-parser.parser-globals :refer [parse-globals]]
    [webchange.editor-v2.diagram.scene-data-parser.parser-objects :refer [parse-objects]]))

(defn parse-scene-data
  [scene-data]
  (let [parsed-objects (parse-objects scene-data)
        parsed-globals (parse-globals scene-data)
        entries (merge parsed-objects parsed-globals)
        parsed-actions (parse-actions scene-data entries)]
    (->> (merge parsed-objects
                parsed-globals
                parsed-actions)
         (remove-duplicates entries))))
