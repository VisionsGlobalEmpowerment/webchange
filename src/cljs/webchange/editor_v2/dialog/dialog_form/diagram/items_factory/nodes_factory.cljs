(ns webchange.editor-v2.dialog.dialog-form.diagram.items-factory.nodes-factory)

(defn- clean-path [path]
  (vec (filter (fn [key] (not= key :data)) path)))

(defn get-node-data
  [{:keys [concept-node? current-concept scene-data action-path node-path]}]
  (if concept-node?
    (let [action-data (-> (get-in current-concept (concat [:data] action-path))
                          (assoc :concept-action true))
          node-data (get-in scene-data (concat [:actions] node-path))]
      {:data             action-data
       :entity           :action
       :path             (clean-path action-path)
       :action-node-data {:data node-data
                          :path (clean-path node-path)}})
    (let [action-data (get-in scene-data (concat [:actions] action-path))]
      {:data action-data
       :path (clean-path action-path)})))
