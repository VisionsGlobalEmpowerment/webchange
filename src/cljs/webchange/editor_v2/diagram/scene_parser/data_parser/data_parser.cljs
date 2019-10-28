(ns webchange.editor-v2.diagram.scene-parser.data-parser.data-parser
  (:require
    [webchange.editor-v2.diagram.scene-parser.data-parser.data-parser-actions :refer [parse-actions]]
    [webchange.editor-v2.diagram.scene-parser.data-parser.data-parser-globals :refer [parse-globals]]
    [webchange.editor-v2.diagram.scene-parser.data-parser.data-parser-objects :refer [parse-objects]]))

(defn parse-data
  [scene-data]
  (let [parsed-objects (parse-objects scene-data)
        parsed-globals (parse-globals scene-data)
        entries (merge parsed-objects parsed-globals)]
    (merge parsed-objects
           parsed-globals
           (parse-actions scene-data entries))))
