(ns webchange.editor-v2.sandbox.parse-actions)

(defn- props-overlap?
  [data props-filter]
  (every? (fn [[prop-name possible-values]]
            (let [prop-value (get data prop-name)]
              (if (fn? possible-values)
                (possible-values prop-value)
                (some #{prop-value} possible-values))))
          props-filter))

(defn- get-case-action-child-nodes
  [node-name node-data]
  (map (fn [[option-name option-data]]
         [(flatten [node-name option-name]) option-data])
       (-> node-data :options)))

(defn- get-provider-action-child-nodes
  [node-name node-data]
  (->> [:on-end]
       (map (fn [handler-name]
              (let [handler-data (get node-data handler-name)]
                (when (and (some? handler-data)
                           (not (string? handler-data)))
                  [(flatten [node-name handler-name]) handler-data]))))
       (remove nil?)))

(defn- get-sequence-action-child-nodes
  [node-name node-data]
  (map-indexed (fn [idx child-data]
                 [(flatten [node-name idx]) child-data])
               (:data node-data)))

(defn- get-test-action-child-nodes
  [node-name node-data]
  (->> [:success :fail]
       (map (fn [handler-name]
              (let [handler-data (get node-data handler-name)]
                (when (and (some? handler-data)
                           (not (string? handler-data)))
                  [(flatten [node-name handler-name]) handler-data]))))
       (remove nil?)))

(defn- get-child-nodes
  [node-name {:keys [type] :as node-data}]
  (case type
    "parallel" (get-sequence-action-child-nodes node-name node-data)
    "sequence-data" (get-sequence-action-child-nodes node-name node-data)
    "lesson-var-provider" (get-provider-action-child-nodes node-name node-data)
    "vars-var-provider" (get-provider-action-child-nodes node-name node-data)
    "test-var-scalar" (get-test-action-child-nodes node-name node-data)
    "test-transitions-collide" (get-test-action-child-nodes node-name node-data)
    "test-var-list" (get-test-action-child-nodes node-name node-data)
    "test-value" (get-test-action-child-nodes node-name node-data)
    "case" (get-case-action-child-nodes node-name node-data)
    []))

(defn find-all-actions
  [scene-data props-filter]
  (loop [result []
         [current-node & rest-nodes] (:actions scene-data)]
    (if-not (some? current-node)
      result
      (let [[node-name node-data] current-node
            child-nodes (get-child-nodes node-name node-data)]
        (if (props-overlap? node-data props-filter)
          (recur (conj result current-node)
                 (concat rest-nodes child-nodes))
          (recur result
                 (concat rest-nodes child-nodes)))))))
