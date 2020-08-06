(ns webchange.editor-v2.diagram.widget.items-factory.utils)

;; ToDo: move to graph node utils collection

(defn get-node-connections
  [node-name node-data]
  (->> (:connections node-data)
       (map (fn [{:keys [name handler]}]
              [node-name (keyword name) handler]))
       (set)))

(defn get-node-outs
  [node-data]
  (->> (:connections node-data)
       (map :name)
       (map keyword)
       (set)))
