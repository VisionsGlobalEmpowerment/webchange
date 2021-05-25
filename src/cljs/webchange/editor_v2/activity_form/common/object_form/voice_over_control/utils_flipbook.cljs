(ns webchange.editor-v2.activity-form.common.object-form.voice-over-control.utils-flipbook
  (:require
    [webchange.editor-v2.graph-builder.scene-parser.scene-parser :refer [parse-data]]
    [webchange.state.state :as state]
    [webchange.state.state-flipbook :as state-flipbook]
    [webchange.utils.flipbook :as utils]))

;; text-name->page-index

(defn- get-all-children
  [{:keys [objects] :as scene-data} object-name]
  (let [{:keys [type children]} (get objects object-name)]
    (if (= type "group")
      (->> (map keyword children)
           (reduce (fn [all-children child]
                     (concat all-children [child] (get-all-children scene-data child)))
                   []))
      [])))

(defn- has-child
  [scene-data parent-name child-name]
  (let [children (get-all-children scene-data parent-name)]
    (some #{child-name} children)))

(defn- text-name->page-index
  [object-name scene-data]
  (->> (utils/get-pages-data scene-data)
       (map-indexed vector)
       (some (fn [[idx {:keys [object]}]]
               (and (has-child scene-data (keyword object) object-name)
                    idx)))))

;; get-page-data

(defn- page-in-stage?
  [scene-data stage-idx page-idx]
  (->> (utils/get-stage-data scene-data stage-idx)
       (:pages-idx)
       (some #{page-idx})))

(defn- page-idx->data
  [scene-data page-idx]
  (-> (utils/get-pages-data scene-data)
      (nth page-idx)))

(defn- find-node
  [graph predicate]
  (some (fn [[node-name node-data]]
          (and (predicate node-name node-data)
               [node-name node-data]))
        graph))

(defn- get-phrase-node
  [scene-data page-data text-name]
  (let [action-name (-> page-data (get :action) (keyword))
        graph (parse-data scene-data action-name)
        [text-animation-node] (find-node graph (fn [_ node-data]
                                                 (let [{:keys [type target]} (get-in node-data [:data])]
                                                   (and (= type "text-animation")
                                                        (= target (clojure.core/name text-name))))))]
    (find-node graph (fn [_ {:keys [children]}]
                       (some #{text-animation-node} children)))))

(defn- populate-page-objects-data
  [page-data scene-data object-name]
  (let [object-data (get-in scene-data [:objects object-name])
        object-type (:type object-data)
        object-info {:name object-name
                     :data object-data}]
    (cond-> (merge page-data
                   {:phrase-action-path (-> (get-phrase-node scene-data page-data object-name) (second) (:path))})
            (= object-type "text") (-> (assoc :text object-info)
                                       (dissoc :image))
            (= object-type "image") (-> (assoc :image object-info)
                                        (dissoc :text)))))

(defn get-page-data
  [scene-data stage-idx object-name page-idx]
  (when (page-in-stage? scene-data stage-idx page-idx)
    (let [data (page-idx->data scene-data page-idx)]
      (if (some? object-name)
        (populate-page-objects-data data scene-data object-name)
        data))))

;; ---

(defn get-actions-data
  [db selected-object-name]
  (let [current-stage-idx (state-flipbook/get-current-stage-idx db)
        scene-data (state/scene-data db)]
    (when (some? selected-object-name)
      (let [page-data (->> (text-name->page-index selected-object-name scene-data)
                           (get-page-data scene-data current-stage-idx selected-object-name))]
        {:dialog-action-name (-> page-data :action keyword)
         :phrase-action-path (-> page-data :phrase-action-path)}))))
