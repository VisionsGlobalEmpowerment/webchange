(ns webchange.editor-v2.translator.translator-form.utils
  (:require
    [webchange.editor-v2.graph-builder.graph :refer [get-diagram-graph]]
    [webchange.editor-v2.graph-builder.utils.node-children :refer [get-children]]
    [webchange.editor-v2.graph-builder.utils.root-nodes :refer [add-root-node
                                                                get-root-nodes
                                                                remove-root-node]]))

(defn get-graph
  [scene-data action-name concept-data]
  (when-not (nil? action-name)
    (get-diagram-graph scene-data :translation {:start-node   action-name
                                                :concept-data concept-data})))

(defn- get-scene-asset-audios
  [scene-data]
  (->> (:assets scene-data)
       (filter (fn [{:keys [type]}] (= type "audio")))
       (map (fn [{:keys [alias url date target]}]
              (let [data {:alias alias
                          :key   url
                          :url   url
                          :date  date}]
                (if-not (nil? target)
                  (assoc data :target target)
                  data))))))

(defn- get-scene-external-audios
  [scene-data]
  (->> (:audio scene-data)
       (map (fn [[key url]]
              {:alias nil
               :key   (name key)
               :url   url}))))

(defn- get-concepts-audios
  [graph]
  (reduce
    (fn [result [_ action-data]]
      (if (get-in action-data [:data :concept-action])
        (let [{:keys [id audio target]} (:data action-data)
              url (or id audio)]
          (let [data {:alias  nil
                      :key    url
                      :url    url}]
            (conj result (if-not (nil? target)
                           (assoc data :target target)
                           data))))
        result))
    []
    graph))

(defn filter-no-url
  [audios]
  (filter
    (fn [{:keys [key]}]
      (-> key nil? not))
    audios))

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
  [scene-data graph]
  (->> (concat (get-scene-asset-audios scene-data)
               (get-scene-external-audios scene-data)
               (get-concepts-audios graph))
       (filter-no-url)
       (audios-distinct)
       (sort-by :date >)
       (into [])))

(defn audios->assets
  [audios]
  (map (fn [url] {:type "audio"
                  :size 1
                  :url  url}) audios))

(defn get-scene-action-data
  [selected-node-data]
  (let [action-name (-> selected-node-data :name keyword)
        action-data (:data selected-node-data)]
    [:scene nil action-name action-data]))

(defn get-concept-action-data
  [selected-node-data current-concept-data]
  (let [action-id (:id current-concept-data)
        action-name (-> selected-node-data :name keyword)
        action-data (:data selected-node-data)]
    [:concept action-id action-name action-data]))

(defn- update-with-current-data
  [path node-action-data data-store]
  (let [action-name (first path)
        edited-action-data (get-in data-store [action-name :data])
        single-action? (not (some #{(:type edited-action-data)} ["parallel" "sequence-data"]))
        path-without-action-name (-> path rest vec)
        path-prefix (if single-action? [] [:data])
        inner-path (vec (concat path-prefix path-without-action-name))
        edited-node-action-data (get-in edited-action-data inner-path)]
    (merge node-action-data edited-node-action-data)))

(defn get-current-action-data
  [selected-node-data current-concept-data data-store]
  (let [concept-action? (get-in selected-node-data [:data :concept-action])
        [type id name data] (if concept-action?
                              (get-concept-action-data selected-node-data current-concept-data)
                              (get-scene-action-data selected-node-data))]
    {:id   id
     :name name
     :type type
     :data (update-with-current-data (-> selected-node-data :path) data data-store)}))

(defn action-data->phrase-data
  [action-data]
  (select-keys action-data [:phrase-text :target]))

(defn- get-dialog-data-dfs
  ([graph]
   (first (get-dialog-data-dfs graph [:root :root] {} [])))
  ([graph [prev-node-name node-name] used-map result]
   (let [node-data (get graph node-name)]
     (reduce
       (fn [[result used-map] {:keys [handler]}]
         (if-not (contains? used-map handler)
           (get-dialog-data-dfs graph [node-name handler] (assoc used-map handler true) result)
           [result used-map]))
       (let [phrase-data (action-data->phrase-data (:data node-data))]
         [(if-not (= node-name :root)
            (conj result phrase-data)
            result)
          used-map])
       (get-children node-name node-data prev-node-name)))))

(defn get-dialog-data
  [phrase-node graph]
  (let [action-data (:data phrase-node)]
    (if (contains? action-data :phrase-text)
      [(select-keys action-data [:phrase-text :target])]
      (->> graph
           (get-root-nodes)
           (add-root-node graph)
           (get-dialog-data-dfs)))))
