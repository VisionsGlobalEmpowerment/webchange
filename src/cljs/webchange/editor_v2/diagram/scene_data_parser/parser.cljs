(ns webchange.editor-v2.diagram.scene-data-parser.parser
  (:require
    [webchange.editor-v2.diagram.scene-data-parser.actions-parser :refer [parse-actions]]
    [webchange.editor-v2.diagram.scene-data-parser.globals-parser :refer [parse-globals]]
    [webchange.editor-v2.diagram.scene-data-parser.objects-parser :refer [parse-objects]]))

(defn parse-scene-data
  [scene-data]
  (merge (parse-actions scene-data)
         (parse-globals scene-data)
         (parse-objects scene-data)))

;   :box3                   {:name   "box3"
;                            :entity :object
;                            :outs   {:click [:click-on-box3]}
;                            :ins    {:scene "Change scene"}}