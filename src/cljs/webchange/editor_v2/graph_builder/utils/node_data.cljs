(ns webchange.editor-v2.graph-builder.utils.node-data)

(defn get-node-type
  [node-data]
  (get-in node-data [:data :type]))

(defn parallel-node-data?
  [node-data]
  (= "parallel" (get-node-type node-data)))

(defn case-node-data?
  [node-data]
  (= "case" (get-node-type node-data)))

(defn provider-node-data?
  [node-data]
  (= "lesson-var-provider" (get-node-type node-data)))

(defn action-node-data?
  [node-data]
  (= "action" (get-node-type node-data)))

(defn test-node-data?
  [node-data]
  (some #{(get-node-type node-data)} ["test-var-scalar"
                                      "test-transitions-collide"
                                      "test-var-list"
                                      "test-value"]))

(defn- get-action-id-from-var
  [node-data]
  (some
    (fn [{:keys [action-property] :as from-var}]
      (and (= action-property "id")
           from-var))
    (get-in node-data [:data :from-var])))

(defn action-with-many-possible-ids?
  [node-data]
  (when (action-node-data? node-data)
    (let [from-var-id (get-action-id-from-var node-data)]
      (and (-> from-var-id nil? not)
           (contains? from-var-id :possible-values)))))

(defn get-action-possible-ids
  [node-data]
  (->> node-data get-action-id-from-var :possible-values (map keyword)))

(defn speech-node?
  [node-data]
  (let [node-type (get-node-type node-data)]
    (or (= "audio" node-type)
        (and (= "animation-sequence" node-type)
             (contains? (:data node-data) :audio))
        (and (= "text-animation" node-type)
             (contains? (:data node-data) :audio)))))

(defn concept-action-node?
  [node-data]
  (-> node-data
      (get-in [:data :concept-action])
      (boolean)))

(defn phrase-node?
  [node-data]
  (contains? (:data node-data) :phrase))

(defn object-node?
  [node-data]
  (= :object (:entity node-data)))

(defn trigger-node?
  [node-data]
  (= :trigger (:entity node-data)))
