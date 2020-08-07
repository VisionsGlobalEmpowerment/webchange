(ns webchange.editor-v2.translator.translator-form.diagram.items-factory.utils)

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
