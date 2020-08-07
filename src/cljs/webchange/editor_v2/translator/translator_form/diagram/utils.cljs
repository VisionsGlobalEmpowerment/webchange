(ns webchange.editor-v2.translator.translator-form.diagram.utils)

(defn get-graph-changes
  [old-graph new-graph]
  (->> (concat (keys old-graph)
               (keys new-graph))
       (distinct)
       (map (fn [node-name]
              (cond
                (not (contains? new-graph node-name)) {:node node-name
                                                       :type :remove}
                (not (contains? old-graph node-name)) {:node node-name
                                                       :type :add
                                                       :data (get new-graph node-name)}
                (not (= (get old-graph node-name)
                        (get new-graph node-name))) {:node node-name
                                                     :type :update
                                                     :data (get new-graph node-name)}
                :else nil)))
       (filter #(not (nil? %)))))
