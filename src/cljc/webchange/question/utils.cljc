(ns webchange.question.utils
  (:require
    [clojure.set :refer [intersection]]
    [webchange.utils.deep-merge :refer [deep-merge]]))

(defn- error
  [message]
  #?(:clj  (new Exception message)
     :cljs (new js/Error message)))

(defn- check-keys!
  [obj1 obj2]
  (let [keys1 (-> obj1 keys set)
        keys2 (-> obj2 keys set)]
    (when-not (-> (intersection keys1 keys2) (empty?))
      (-> (str "Duplicated keys:" (intersection keys1 keys2)) error throw))))

(defn- merge-data-simple
  [param1 param2]
  (doseq [field [:actions :objects]]
    (check-keys! (get param1 field {}) (get param2 field {})))
  (-> param1
      (update :actions merge (get param2 :actions {}))
      (update :objects merge (get param2 :objects {}))
      (update-in [:track :nodes] concat (get-in param2 [:track :nodes] []))))

(defn merge-data
  [param-1 & params]
  (reduce (fn [result param-n]
            (merge-data-simple result param-n))
          param-1
          params))

(defn get-voice-over-tag
  [{:keys [question-id value] :or {value "task"}}]
  {:activate            (str "activate-voice-over-" value "-" question-id)
   :inactivate          (str "inactivate-voice-over-" value "-" question-id)
   :inactivate-all      (str "inactivate-voice-overs-" question-id)
   :activate-template   (str "activate-voice-over-%-" question-id)
   :inactivate-template (str "inactivate-voice-over-%-" question-id)})
