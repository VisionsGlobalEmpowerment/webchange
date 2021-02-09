(ns webchange.editor-v2.layout.flipbook.utils
  (:require
    [webchange.editor-v2.graph-builder.scene-parser.scene-parser :refer [parse-data]]))

(defn- page-idx->data
  [scene-data page-idx flipbook-name]
  (-> scene-data
      (get-in [:objects (keyword flipbook-name) :pages])
      (nth page-idx)))

(defn get-stage-data
  [scene-data stage-idx]
  (when (some? stage-idx)
    (let [{:keys [stages flipbook-name]} (get scene-data :metadata {})
          stage (nth stages stage-idx)
          [left-page-idx right-page-idx] (:pages-idx stage)]
      (cond-> stage
              (some? left-page-idx) (assoc :left-page (page-idx->data scene-data left-page-idx flipbook-name))
              (some? right-page-idx) (assoc :right-page (page-idx->data scene-data right-page-idx flipbook-name))))))

(defn- find-node
  [graph predicate]
  (some (fn [[node-name node-data]]
          (and (predicate node-name node-data)
               [node-name node-data]))
        graph))

(defn- get-phrase-node
  [scene-data page-data]
  (let [action-name (-> page-data (get :action) (keyword))
        graph (parse-data scene-data action-name)
        [text-animation-node] (find-node graph (fn [_ node-data]
                                                 (= (get-in node-data [:data :type]) "text-animation")))]
    (find-node graph (fn [_ {:keys [children]}]
                                         (some #{text-animation-node} children)))))

(defn- populate-page-text-data
  [page-data scene-data]
  (let [text-name (-> page-data (get :text) (keyword))]
    (if (some? text-name)
      (-> page-data
          (assoc :text {:name text-name
                        :data (get-in scene-data [:objects text-name])})
          (assoc :phrase-action-path (-> (get-phrase-node scene-data page-data) (second) (:path))))
      page-data)))

(defn scene-data->objects-list
  [scene-data stage-idx]
  (let [{:keys [left-page right-page]} (get-stage-data scene-data stage-idx)]
    (->> (cond-> []
                 (some? left-page) (conj (populate-page-text-data left-page scene-data))
                 (some? right-page) (conj (populate-page-text-data right-page scene-data)))
         (remove nil?))))
