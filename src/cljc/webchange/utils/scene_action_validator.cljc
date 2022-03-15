(ns webchange.utils.scene-action-validator)

(defn- talk-animation?
  [value]
  (-> #{value}
      (some ["talk"])
      (boolean)))

(defn- animation-sequence-data-item?
  [{:keys [start end anim]}]
  (and (number? start)
       (number? end)
       (talk-animation? anim)))

(defn- animation-sequence-data?
  [value]
  (and (sequential? value)
       (every? animation-sequence-data-item? value)))

(defn- character-target?
  [value]
  (string? value))

(defn- audio?
  [value]
  (or (nil? value)
      (string? value)))

(defn- phrase?
  [value]
  (string? value))

(defn- time-value?
  [value]
  (number? value))

(defn- undefined-or-valid?
  [action-data field-name valid?]
  (or (-> action-data (contains? field-name) (not))
      (-> action-data (get field-name) (valid?))))

(def validators
  {"animation-sequence" {:type               {:valid?        (fn [{:keys [type]}]
                                                               (= type "animation-sequence"))
                                              :default-value "animation-sequence"}
                         :target             {:valid?        #(undefined-or-valid? % :target character-target?)
                                              :default-value ::none}
                         :phrase-text        {:valid?        #(-> % :phrase-text phrase?)
                                              :default-value nil}
                         :phrase-placeholder {:valid?        #(undefined-or-valid? % :phrase-placeholder phrase?)
                                              :default-value nil}
                         :audio              {:valid?        #(-> % :audio audio?)
                                              :default-value nil}
                         :start              {:valid?        #(undefined-or-valid? % :start time-value?)
                                              :default-value ::none}
                         :end                {:valid?        #(undefined-or-valid? % :end time-value?)
                                              :default-value ::none}
                         :duration           {:valid?        #(undefined-or-valid? % :duration time-value?)
                                              :default-value ::none}
                         :data               {:valid?        #(undefined-or-valid? % :data animation-sequence-data?)
                                              :default-value []}}})

(defn validate-action
  [action-type action-data]
  (if-let [validator (get validators action-type)]
    (let [fields-validation (->> validator
                                 (map (fn [[field {:keys [valid?]}]]
                                        [field (valid? action-data)])))
          fields-valid? (->> fields-validation (map second) (every? true?))
          invalid-fields (->> fields-validation
                              (filter (fn [[_ valid?]] (not valid?)))
                              (map first))]
      {:valid?   fields-valid?
       :messages [(str "Invalid fields: " (clojure.string/join ", " invalid-fields))]})
    {:valid? true}))

(defn fix-action
  [action-type action-data]
  (if-let [validator (get validators action-type)]
    (->> validator
         (map (fn [[field-name {:keys [default-value valid?]}]]
                (let [field-value (get action-data field-name)]
                  [field-name (if (valid? action-data)
                                (if (contains? action-data field-name)
                                  field-value
                                  ::none)
                                default-value)])))
         (filter (fn [[_ value]]
                   (not= value ::none)))
         (into {}))
    action-data))
