(ns webchange.templates.utils.dialog)

(defn default
  [phrase]
  {:type               "sequence-data",
   :editor-type        "dialog",
   :data               [{:type "sequence-data"
                         :data [{:type "empty" :duration 0}
                                {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
   :phrase             phrase,
   :phrase-description phrase})

(defn create-and-place-before
  [dialog {:keys [old-action-name new-action-name unique-suffix]}]
  [(keyword (str "dialog-" unique-suffix))
   {(keyword old-action-name)               {:type "sequence-data",
                                             :data [{:type "action" :id (keyword (str "dialog-" unique-suffix))}
                                                    {:type "action" :id new-action-name}],
                                             }
    (keyword (str "dialog-" unique-suffix)) (default dialog)}
   []
   ])

(defn create-simple
  [key description unique-suffix available-activities]
  (let [name (clojure.string/replace key "-" " ")]
    {(keyword (str key "-" unique-suffix)) (cond-> {:type               "sequence-data",
                                                   :editor-type        "dialog",
                                                   :data               [{:type "sequence-data"
                                                                         :data [{:type "empty" :duration 0}
                                                                                {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                   :phrase             name,
                                                   :phrase-description (str name " " description)}
                                                  available-activities (assoc :available-activities available-activities))}))
