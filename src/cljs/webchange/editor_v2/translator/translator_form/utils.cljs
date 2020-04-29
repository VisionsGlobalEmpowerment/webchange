(ns webchange.editor-v2.translator.translator-form.utils
  (:require
    [webchange.editor-v2.graph-builder.graph :refer [get-diagram-graph]]
    [webchange.editor-v2.graph-builder.utils.node-children :refer [get-children]]
    [webchange.editor-v2.graph-builder.utils.root-nodes :refer [add-root-node
                                                                get-root-nodes
                                                                remove-root-node]]))

(defn trim-text
  [text]
  (when-not (nil? text)
    (-> text
        (.split "\n")
        (.map (fn [string] (.trim string)))
        (.join "\n"))))

(defn get-graph
  [{:keys [scene-data start-node concept]}]
  (let [start-node-name (or (:origin-name start-node)
                            (keyword (:name start-node)))
        params {:start-node   start-node-name
                :concept-data {:current-concept concept}}
        diagram-mode :translation]
    (when-not (nil? start-node-name)
      (get-diagram-graph scene-data diagram-mode params))))

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
  [path action-id node-action-data data-store]
  (let [action-name (first path)
        edited-action-data (get-in data-store [[action-name action-id] :data])
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
     :data (update-with-current-data (-> selected-node-data :path) id data data-store)}))

(defn node-data->phrase-data
  [action-data]
  (select-keys (:data action-data)
               [:phrase-text
                :phrase-text-translated
                :target]))

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
       (let [phrase-data (-> node-data node-data->phrase-data)]
         [(if-not (= node-name :root)
            (conj result phrase-data)
            result)
          used-map])
       (get-children node-name node-data prev-node-name)))))

(defn get-dialog-data
  [phrase-node graph]
  (let [phrase-node? (-> (:data phrase-node)
                         (contains? :phrase-text))]
    (if phrase-node?
      [(-> phrase-node node-data->phrase-data)]
      (->> graph
           (get-root-nodes)
           (add-root-node graph)
           (get-dialog-data-dfs)))))
