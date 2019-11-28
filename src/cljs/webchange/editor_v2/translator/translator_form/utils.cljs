(ns webchange.editor-v2.translator.translator-form.utils
  (:require
    [webchange.editor-v2.diagram.graph-builder.graph :refer [get-diagram-graph]]))

(defn get-graph
  [scene-data action-name concepts-scheme]
  (when-not (nil? action-name)
    (get-diagram-graph scene-data :translation {:start-node action-name
                                                :concepts   (get-in concepts-scheme [:scheme :fields])})))

(defn get-used-concept-actions
  [graph]
  (->> graph
       (filter (fn [[_ action-data]] (get-in action-data [:data :concept-action])))
       (map (fn [[action-name _]] action-name))))

(defn- get-scene-asset-audios
  [scene-data]
  (->> (:assets scene-data)
       (filter (fn [{:keys [type]}] (= type "audio")))
       (map (fn [{:keys [alias url]}]
              {:alias alias
               :key   url
               :url   url}))))

(defn- get-scene-external-audios
  [scene-data]
  (->> (:audio scene-data)
       (map (fn [[key url]]
              {:alias nil
               :key   (name key)
               :url   url}))))

(defn- get-concepts-audios
  ([concepts]
   (get-concepts-audios concepts nil))
  ([concepts used-concept-actions]
   (->> concepts
        (reduce
          (fn [result {:keys [data]}]
            (let [concept-actions (if-not (nil? used-concept-actions)
                                    (select-keys data used-concept-actions)
                                    data)
                  concept-audios (map (fn [[_ {:keys [id audio]}]]
                                        (let [url (or id audio)]
                                          {:alias nil
                                           :key   url
                                           :url   url})) concept-actions)]
              (concat result concept-audios)))
          []))))

(defn update-alias
  [map url alias]
  (if-not (nil? alias)
    (assoc-in map [url :alias] alias)
    map))

(defn update-key
  [map url key]
  (if-not (= key url)
    (assoc-in map [url :key] key)
    map))

(defn audios-distinct
  [audios]
  (->> audios
       (reduce
         (fn [result {:keys [alias key url] :as audio}]
           (if-not (contains? result url)
             (assoc result url audio)
             (-> result
                 (update-alias url alias)
                 (update-key url key))))
         {})
       (vals)
       (vec)))

(defn get-audios
  [scene-data concepts used-concept-actions]
  (->> (concat (get-scene-asset-audios scene-data)
               (get-scene-external-audios scene-data)
               (get-concepts-audios concepts used-concept-actions))
       (audios-distinct)))

(defn audios->assets
  [audios]
  (map (fn [url] {:type "audio"
                  :size 1
                  :url  url}) audios))

(defn get-current-action-data
  [selected-node-data current-concept-data data-store]
  (let [action-name (-> selected-node-data :name keyword)
        concept-action? (get-in selected-node-data [:data :concept-action])
        [id name type data] (if concept-action?
                              (let [concept-action-name (-> selected-node-data :name keyword)]
                                [(:id current-concept-data)
                                 concept-action-name
                                 :concept
                                 (get-in current-concept-data [:data concept-action-name])])
                              [nil action-name :scene (:data selected-node-data)])
        edited-action-data (get-in data-store [action-name :data])]
    {:id   id
     :name name
     :type type
     :data (merge data edited-action-data)}))
