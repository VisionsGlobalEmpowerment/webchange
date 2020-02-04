(ns webchange.editor-v2.graph-builder.scene-parser.actions-parser.parse-default
  (:require
    [webchange.editor-v2.graph-builder.scene-parser.actions-parser.interface :refer [parse-actions-chain]]
    [webchange.editor-v2.graph-builder.scene-parser.utils.create-graph-node :refer [create-graph-node]]))

(defn get-action-data-default
  [{:keys [action-name action-data parent-action next-action prev-action path]}]
  (let [prev-actions (if (sequential? prev-action) prev-action [prev-action])
        next-actions (if (sequential? next-action) next-action [next-action])]
    (->> (create-graph-node {:data        action-data
                             :path        (or path [action-name])
                             :connections (reduce (fn [result prev-action]
                                                    (concat result (map (fn [next-action]
                                                                          {:previous prev-action
                                                                           :handler  next-action
                                                                           :sequence parent-action})
                                                                        next-actions)))
                                                  []
                                                  prev-actions)})
         (assoc {} action-name))))

(defmethod parse-actions-chain :default
  [_ params]
  (get-action-data-default params))
