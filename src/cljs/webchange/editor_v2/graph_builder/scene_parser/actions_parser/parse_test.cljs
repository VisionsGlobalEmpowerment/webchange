(ns webchange.editor-v2.graph-builder.scene-parser.actions-parser.parse-test
  (:require
    [webchange.editor-v2.graph-builder.scene-parser.actions-parser.interface :refer [parse-actions-chain]]
    [webchange.editor-v2.graph-builder.scene-parser.utils.create-graph-node :refer [create-graph-node]]
    [webchange.editor-v2.graph-builder.utils.merge-actions :refer [merge-actions]]))

(defn handler-name?
  [action-data event-name]
  (let [handler (->> action-data event-name)]
    (string? handler)))

(defn get-test-handler-name
  [action-name action-data event-name]
  (let [handler (->> action-data event-name)]
    (if (string? handler)
      (keyword handler)
      (->> (clojure.core/name event-name)
           (str (clojure.core/name action-name) "-")
           (keyword)))))

(defn get-test-action-data
  [{:keys [action-name action-data prev-action path]}]
  (->> (create-graph-node {:data        action-data
                           :path        (or path [action-name])
                           :connections (->> [(when (contains? action-data :success)
                                                {:previous prev-action
                                                 :name     "success"
                                                 :handler  (get-test-handler-name action-name action-data :success)})
                                              (when (contains? action-data :fail)
                                                {:previous prev-action
                                                 :name     "fail"
                                                 :handler  (get-test-handler-name action-name action-data :fail)})]
                                             (remove nil?)
                                             (vec))})
       (assoc {} action-name)))

(defn parse-test-action-chain
  [actions-data {:keys [action-name action-data sequence-path path graph] :as params}]
  (reduce
    (fn [result event-name]
      (if-not (nil? (get action-data event-name))
        (if (handler-name? action-data event-name)
          (let [next-node-name (-> action-data (get event-name) keyword)]
            (merge-actions result (parse-actions-chain actions-data
                                                       {:action-name   next-node-name
                                                        :action-data   (get actions-data next-node-name)
                                                        :parent-action nil
                                                        :next-action   nil
                                                        :prev-action   action-name
                                                        :sequence-path sequence-path
                                                        :graph         result})))
          (let [next-node-name (get-test-handler-name action-name action-data event-name)
                next-node-data (get action-data event-name)
                path (conj (or path [action-name]) event-name)]
            (merge-actions result (parse-actions-chain actions-data
                                                       {:action-name   next-node-name
                                                        :action-data   next-node-data
                                                        :parent-action nil
                                                        :next-action   nil
                                                        :prev-action   action-name
                                                        :sequence-path sequence-path
                                                        :path          path
                                                        :graph         result}))))
        result))
    (->> (get-test-action-data params)
         (merge-actions graph))
    [:success :fail]))

;; test-var-scalar

(defmethod parse-actions-chain "test-var-scalar"
  [actions-data params]
  (parse-test-action-chain actions-data params))

;; test-transitions-collide

(defmethod parse-actions-chain "test-transitions-collide"
  [actions-data params]
  (parse-test-action-chain actions-data params))

;; test-var-list

(defmethod parse-actions-chain "test-var-list"
  [actions-data params]
  (parse-test-action-chain actions-data params))

;; test-var-list

(defmethod parse-actions-chain "test-value"
  [actions-data params]
  (parse-test-action-chain actions-data params))
