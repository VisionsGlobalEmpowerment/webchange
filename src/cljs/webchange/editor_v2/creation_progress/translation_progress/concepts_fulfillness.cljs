(ns webchange.editor-v2.creation-progress.translation-progress.concepts-fulfillness
  (:require
    [webchange.editor-v2.concepts.views :refer [resource-type?]]
    [webchange.editor-v2.sandbox.parse-actions :refer [find-all-actions]]))

(defn- empty-field?
  [field-name field-data scheme]
  (let [template (get-in scheme [field-name :template])]
    (or (nil? field-data)
        (= field-data template))))

(defn- get-field-type-group
  [field-name scheme]
  (let [field-type (get-in scheme [field-name :type])]
    (cond
      (= field-type "action") :action
      (resource-type? field-type) :resource
      :else :etc)))

(def concept-related-action-props
  {:from-var (fn [property-value]
               (and (sequential? property-value)
                    (some (fn [item]
                            (and (contains? item :var-name)
                                 (contains? item :var-property))) property-value)))})

(defn- get-used-fields
  [scene-data]
  (->> (find-all-actions scene-data concept-related-action-props)
       (map second)
       (map :from-var)
       (map first)
       (map :var-property)
       (map keyword)))

(defn check-dataset-items
  [scene-data dataset-items dataset-scheme]
  (let [scheme-fields (->> (get-in dataset-scheme [:scheme :fields])
                           (map (fn [{:keys [name] :as field}] [(keyword name) field]))
                           (into {}))
        field-names (get-used-fields scene-data)
        warnings (->> dataset-items
                      (map (fn [dataset-item]
                             [(select-keys dataset-item [:id :name :dataset-id])
                              (->> field-names
                                   (map (fn [field-name]
                                          (let [field-data (get-in dataset-item [:data field-name])
                                                field-type (get-field-type-group field-name scheme-fields)]
                                            (when (empty-field? field-name field-data scheme-fields)
                                              (let [dialog-actions (find-all-actions scene-data {:from-var (fn [property-value]
                                                                                                             (and (sequential? property-value)
                                                                                                                  (some (fn [item]
                                                                                                                          (= (:var-property item) (clojure.core/name field-name)))
                                                                                                                        property-value)))})
                                                    dialog-action-path (-> dialog-actions first first)
                                                    dialog-action-name (if (coll? dialog-action-path) (first dialog-action-path) dialog-action-path)
                                                    dialog-action-data (get-in scene-data [:actions dialog-action-name])]
                                                {:name   field-name
                                                 :type   field-type
                                                 :action {:id    dialog-action-name
                                                          :title (:phrase-description dialog-action-data)}})))))
                                   (filter #(not (= (:type %) :etc)))
                                   (remove nil?))]))
                      (remove (fn [[_ warnings]]
                                (empty? warnings))))
        items-count (count dataset-items)
        completed-items-count (- items-count (count warnings))]
    {:entity   :concepts
     :warnings warnings
     :progress (/ completed-items-count items-count)}))
