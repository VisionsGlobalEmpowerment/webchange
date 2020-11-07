(ns webchange.editor-v2.graph-builder.scene-parser.actions-parser.interface)

(defmulti parse-actions-chain
          (fn [_ {:keys [action-data]}]
            (:type action-data)))
