(ns webchange.editor-v2.graph-builder.scene-parser.scene-parser-actions
  (:require
    [webchange.editor-v2.graph-builder.utils.merge-actions :refer [merge-actions]]
    [webchange.editor-v2.graph-builder.scene-parser.actions-parser.interface :refer [parse-actions-chain]]
    [webchange.editor-v2.graph-builder.scene-parser.actions-parser.parse-provider]
    [webchange.editor-v2.graph-builder.scene-parser.actions-parser.parse-sequence]
    [webchange.editor-v2.graph-builder.scene-parser.actions-parser.parse-test]
    [webchange.editor-v2.graph-builder.scene-parser.actions-parser.parse-action]
    [webchange.editor-v2.graph-builder.scene-parser.actions-parser.parse-case]
    [webchange.editor-v2.graph-builder.scene-parser.actions-parser.parse-default]))

(defn parse-actions
  [scene-data entries]
  (let [actions-data (:actions scene-data)]
    (reduce
      (fn [result [action-name object-name]]
        (merge-actions result (parse-actions-chain
                                actions-data
                                {:action-name   action-name
                                 :action-data   (get actions-data action-name)
                                 :parent-action nil
                                 :next-action   nil
                                 :prev-action   object-name
                                 :sequence-path []})))
      {}
      entries)))
