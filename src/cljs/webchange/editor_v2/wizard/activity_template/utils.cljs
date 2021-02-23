(ns webchange.editor-v2.wizard.activity-template.utils)

(defn- get-key-value
  [key {:keys [form-data metadata]}]
  (cond
    (and (sequential? key)
         (= "metadata" (first key))) (let [[_ & path] key]
                                      (get-in metadata (map keyword path)))
    :else (get form-data (keyword key))))

(defn- check-condition
  [{:keys [key state value]} data]
  (let [key-value (get-key-value key data)]
    (case (keyword state)
      :equal (= key-value value)
      :in (some #{key-value} value)
      :not-in (not (some #{key-value} value))
      true)))

(defn check-conditions
  [conditions form-data metadata]
  (let [data {:form-data form-data
              :metadata  metadata}]
    (if (sequential? conditions)
      (every? (fn [condition] (check-condition condition data)) conditions)
      (check-condition conditions data))))
