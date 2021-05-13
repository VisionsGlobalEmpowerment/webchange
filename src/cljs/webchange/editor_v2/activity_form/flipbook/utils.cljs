(ns webchange.editor-v2.activity-form.flipbook.utils
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

(defn- page-in-stage?
  [scene-data stage-idx page-idx]
  (->> (utils/get-stage-data scene-data stage-idx)
       (:pages-idx)
       (some #{page-idx})))

(defn get-page-data
  [scene-data stage-idx object-name page-idx]
  (when (page-in-stage? scene-data stage-idx page-idx)
    (let [data (page-idx->data scene-data page-idx)]
      (if (some? object-name)
        (populate-page-objects-data data scene-data object-name)
        data))))
