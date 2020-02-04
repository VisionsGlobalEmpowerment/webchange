(ns webchange.editor-v2.graph-builder.scene-parser.actions-parser.parse-case
  (:require
    [webchange.editor-v2.graph-builder.scene-parser.actions-parser.interface :refer [parse-actions-chain]]
    [webchange.editor-v2.graph-builder.scene-parser.utils.create-graph-node :refer [create-graph-node]]
    [webchange.editor-v2.graph-builder.utils.merge-actions :refer [merge-actions]]))

(defn get-case-options
  [action-name action-data]
  (reduce (fn [result [option-name option-data]]
            (conj result {:option-name  option-name
                          :option-data  option-data
                          :handler-name (->> (clojure.core/name option-name)
                                             (str (clojure.core/name action-name) "-")
                                             (keyword))}))
          []
          (-> action-data :options)))

(defn get-case-action-data
  [{:keys [action-name action-data prev-action]} action-path]
  (let [options (get-case-options action-name action-data)]
    (->> (create-graph-node {:data        action-data
                             :path        action-path
                             :children    (map :handler-name options)
                             :connections (map (fn [{:keys [option-name handler-name]}]
                                                 {:previous prev-action
                                                  :name     (name option-name)
                                                  :handler  handler-name})
                                               options)})
         (assoc {} action-name))))

(defn parse-case-action-chain
  [actions-data {:keys [action-name action-data next-action parent-action sequence-path path] :as params}]
  (let [action-path (or path [action-name])
        options (get-case-options action-name action-data)]
    (reduce
      (fn [result {:keys [option-name option-data handler-name]}]
        (merge-actions result (parse-actions-chain actions-data
                                                   {:action-name   handler-name
                                                    :action-data   option-data
                                                    :parent-action parent-action
                                                    :next-action   next-action
                                                    :prev-action   action-name
                                                    :sequence-path sequence-path
                                                    :path          (concat action-path [:options option-name])})))
      (get-case-action-data params action-path)
      options)))

(defmethod parse-actions-chain "case"
  [actions-data params]
  (parse-case-action-chain actions-data params))
