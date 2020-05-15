(ns webchange.editor-v2.graph-builder.scene-parser.actions-parser.parse-provider
  (:require
    [webchange.editor-v2.graph-builder.scene-parser.actions-parser.interface :refer [parse-actions-chain]]
    [webchange.editor-v2.graph-builder.scene-parser.utils.create-graph-node :refer [create-graph-node]]
    [webchange.editor-v2.graph-builder.utils.merge-actions :refer [merge-actions]]))

(defn- make-sequential
  [next-actions]
  (if (sequential? next-actions) next-actions [next-actions]))

(defn- get-specific-handlers
  [scene-data action-name action-data action-path]
  [(let [on-end (:on-end action-data)]
     (if (string? on-end)
       (let [name (keyword on-end)]
         {:handler-name name
          :handler-data (get scene-data name)
          :event-name   "on-end"})
       (let [name (-> action-name clojure.core/name (str "-on-end") keyword)]
         {:handler-name name
          :handler-data on-end
          :handler-path (conj action-path :on-end)
          :event-name   "on-end"})))])

(defn parse-provider-action-chain
  [scene-data {:keys [action-name action-data parent-action next-action prev-action path graph]}]
  (let [action-path (or path [action-name])
        specific-handlers (get-specific-handlers scene-data action-name action-data action-path)
        prev-actions (make-sequential prev-action)
        next-actions (make-sequential next-action)
        connections (reduce (fn [result prev-action]
                              (concat result
                                      (map (fn [next-action]
                                             {:previous prev-action
                                              :handler  next-action
                                              :sequence parent-action})
                                           next-actions)
                                      (map (fn [{:keys [handler-name event-name]}]
                                             {:previous prev-action
                                              :handler  handler-name
                                              :name     event-name})
                                           specific-handlers)))
                            []
                            prev-actions)]
    (reduce
      (fn [result {:keys [handler-name handler-data handler-path]}]
        (merge-actions result (parse-actions-chain scene-data
                                                   {:action-name handler-name
                                                    :action-data handler-data
                                                    :prev-action action-name
                                                    :path        handler-path
                                                    :graph       result})))
      (->> (create-graph-node {:data        action-data
                               :path        action-path
                               :children    (map :handler-name specific-handlers)
                               :connections connections})
           (assoc {} action-name)
           (merge-actions graph))
      specific-handlers)))

(defmethod parse-actions-chain "lesson-var-provider"
  [actions-data params]
  (parse-provider-action-chain actions-data params))
