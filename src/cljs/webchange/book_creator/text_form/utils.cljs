(ns webchange.book-creator.text-form.utils
  (:require
    [webchange.editor-v2.graph-builder.scene-parser.scene-parser :refer [parse-data]]
    [webchange.utils.flipbook :as utils]))

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

(defn- page-in-stage?
  [scene-data stage-idx page-idx]
  (->> (utils/get-stage-data scene-data stage-idx)
       (:pages-idx)
       (some #{page-idx})))

(defn get-page-data
  [scene-data stage-idx page-idx]
  (when (page-in-stage? scene-data stage-idx page-idx)
    (let [data (page-idx->data scene-data page-idx)]
      (if (some? (:text data))
        (populate-page-text-data data scene-data)
        data))))
