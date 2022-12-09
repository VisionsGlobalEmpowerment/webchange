(ns webchange.question.utils
  (:require
    [clojure.set :refer [intersection]]
    [webchange.utils.deep-merge :refer [deep-merge]]))

(defn- error
  [message]
  #?(:clj  (new Exception message)
     :cljs (new js/Error message)))

(defn apply-log
  [message]
  #?(:clj  (clojure.tools.logging/debug message)
     :cljs (js/console.log message)))

(defn log
  [& messages]
  (doseq [message messages]
    (apply-log message)))

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
      (update :assets concat (get param2 :assets []))
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

(defn get-option-tag
  [action params]
  (let [getters {:set-selected       (fn [{:keys [option-value question-id template?]}]
                                       {:pre [(or template? (string? option-value)) (string? question-id)]}
                                       (str "activate-option-" (if template? "%" option-value) "-" question-id))
                 :set-unselected     (fn [{:keys [option-value question-id template?]}]
                                       {:pre [(or template? (string? option-value)) (string? question-id)]}
                                       (str "inactivate-option-" (if template? "%" option-value) "-" question-id))
                 :set-unselected-all (fn [{:keys [question-id]}]
                                       {:pre [(string? question-id)]}
                                       (str "inactivate-options-" question-id))
                 :set-correct        (fn [{:keys [option-value template? question-id]}]
                                       {:pre [(or template? (string? option-value)) (string? question-id)]}
                                       (str "set-correct-option-" (if template? "%" option-value) "-" question-id))
                 :set-incorrect      (fn [{:keys [option-value template? question-id]}]
                                       {:pre [(or template? (string? option-value)) (string? question-id)]}
                                       (str "set-incorrect-option-" (if template? "%" option-value) "-" question-id))}]
    (if (contains? getters action)
      ((get getters action) params)
      (error (str "Tag '" action "' is not defined")))))

(defn task-has-image?
  [{:keys [task-type]}]
  (some #{task-type} ["image" "text-image"]))

(defn task-has-text?
  [{:keys [task-type]}]
  (some #{task-type} ["text" "text-image"]))

(defn sequence-correct-answer?
  [{:keys [answers-number]}]
  (some #{answers-number} ["sequence"]))

(defn has-correct-answer?
  [{:keys [answers-number]}]
  (some #{answers-number} ["one" "many" "sequence"]))

(defn one-correct-answer?
  [{:keys [answers-number]}]
  (or (= answers-number "one")
      (= answers-number "any")))

(defn many-correct-answers?
  [{:keys [answers-number]}]
  (= answers-number "many"))

(defn options-have-voice-over?
  [{:keys [question-type]}]
  (not= question-type "thumbs-up-n-down"))

(defn show-check-button?
  [form-data]
  (or (one-correct-answer? form-data)
      (many-correct-answers? form-data)))

(defn round
  [x]
  (-> x Math/ceil int))
