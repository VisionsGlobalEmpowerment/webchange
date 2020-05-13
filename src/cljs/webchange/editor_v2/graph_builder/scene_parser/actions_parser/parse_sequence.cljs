(ns webchange.editor-v2.graph-builder.scene-parser.actions-parser.parse-sequence
  (:require
    [clojure.set :refer [superset?]]
    [webchange.editor-v2.graph-builder.utils.node-data :refer [action-with-many-possible-ids?
                                                               case-node-data?
                                                               get-action-possible-ids
                                                               parallel-node-data?
                                                               provider-node-data?]]
    [webchange.editor-v2.graph-builder.scene-parser.actions-parser.interface :refer [parse-actions-chain]]
    [webchange.editor-v2.graph-builder.scene-parser.utils.create-graph-node :refer [create-graph-node
                                                                                    get-sequence-item-name]]
    [webchange.editor-v2.graph-builder.utils.merge-actions :refer [merge-actions]]))

(defn next-to-index
  [list index]
  (if-not (or (= -1 index)
              (= index (dec (count list))))
    (nth list (inc index))
    nil))

(defn get-last-children
  ([graph node-name]
   (get-last-children graph node-name []))
  ([graph node-name path]
   (if (some #{node-name} path)
     node-name
     (let [children (get-in graph [node-name :children])]
       (if (< 0 (count children))
         (let [node-data (get graph node-name)]
           (cond
             (provider-node-data? node-data) node-name
             (case-node-data? node-data) (->> children
                                              (map (fn [child]
                                                     (get-last-children graph child (conj path node-name))))
                                              (flatten))
             (parallel-node-data? node-data) children
             (action-with-many-possible-ids? node-data) (get-action-possible-ids node-data)
             :else (let [last-child (last children)]
                     (get-last-children graph last-child (conj path node-name)))))
         node-name)))))

;; parallel

(defn get-parallel-action-data-children
  [action-name action-data]
  (->> (:data action-data)
       (map-indexed
         (fn [index child-action-data]
           [(keyword (str (name action-name) "-" index))
            child-action-data
            index]))
       (vec)))

(defn get-action-data-parallel
  [{:keys [action-name action-data prev-action path]}]
  (let [children (->> (get-parallel-action-data-children action-name action-data)
                      (map first))]
    (->> (create-graph-node {:data        action-data
                             :path        (or path [action-name])
                             :children    children
                             :connections (map (fn [child]
                                                 {:previous prev-action
                                                  :handler  child
                                                  :sequence action-name})
                                               children)})
         (assoc {} action-name))))

(defmethod parse-actions-chain "parallel"
  [actions-data {:keys [action-name action-data next-action sequence-path parent-action path graph] :as params}]
  (let [parsed-action (get-action-data-parallel params)
        next-actions (get-parallel-action-data-children action-name action-data)
        path (or path [action-name])]
    (reduce
      (fn [result [next-action-name next-action-data index]]
        (merge-actions result (parse-actions-chain actions-data
                                                   {:action-name   next-action-name
                                                    :action-data   next-action-data
                                                    :parent-action parent-action
                                                    :next-action   next-action
                                                    :prev-action   action-name
                                                    :path          (conj path index)
                                                    :sequence-path (conj sequence-path action-name)
                                                    :graph         result})))
      parsed-action
      next-actions)))

;; sequence

(defn get-action-data-sequence
  [{:keys [action-name action-data prev-action path sequence-path next-action parent-action]}]
  (let [first-item (->> action-data :data first keyword)
        connections [{:previous prev-action
                      :handler  first-item
                      :sequence action-name}]
        cycled? (some #{action-name} sequence-path)
        return-immediately? (:return-immediately action-data)
        connections (if (and return-immediately? (not cycled?))
                      (conj connections {:previous prev-action
                                         :handler  next-action
                                         :sequence parent-action})
                      connections)]
    (->> (create-graph-node {:data        action-data
                             :path        (or path [action-name])
                             :children    (->> action-data :data (map keyword))
                             :connections connections})
         (assoc {} action-name))))

(defmethod parse-actions-chain "sequence"
  [actions-data {:keys [action-name action-data prev-action next-action parent-action sequence-path graph] :as params}]
  (let [parsed-action (get-action-data-sequence (assoc params
                                                  :prev-action
                                                  (get-last-children actions-data prev-action)))
        sequence-data (->> action-data :data (map keyword))
        next-actions (map-indexed (fn [index item] [index item]) sequence-data)
        cycled? (some #{action-name} sequence-path)]
    (if-not cycled?
      (->> next-actions
           (reduce
             (fn [[result prev-action-name] [index sequence-item-name]]
               (let [sequence-item-data (get actions-data sequence-item-name)
                     next-item-name (or (next-to-index sequence-data index) next-action)
                     last-item? (= index (dec (count next-actions)))
                     prev-action (if (nil? prev-action-name)
                                   action-name
                                   (get-last-children result prev-action-name))]
                 [(merge-actions result (parse-actions-chain actions-data
                                                             {:action-name   sequence-item-name
                                                              :action-data   sequence-item-data
                                                              :parent-action (if last-item? parent-action action-name)
                                                              :next-action   next-item-name
                                                              :prev-action   prev-action
                                                              :sequence-path (conj sequence-path action-name)
                                                              :graph         result}))
                  sequence-item-name]))
             [(merge-actions graph parsed-action) nil])
           (first))
      parsed-action)))

;; sequence-data

(defn get-action-data-sequence-data
  [{:keys [action-name action-data prev-action path]}]
  (let [path (or path [action-name])
        child-actions (->> (:data action-data)
                           (map-indexed
                             (fn [index child-action-data]
                               [(-> (clojure.core/name action-name) (str "-" index) (keyword))
                                child-action-data]))
                           (vec))]
    (->> (create-graph-node {:data        action-data
                             :path        path
                             :children    (map first child-actions)
                             :connections [{:previous prev-action
                                            :handler  (->> child-actions first first)
                                            :sequence action-name}]})
         (assoc {} action-name))))

(defmethod parse-actions-chain "sequence-data"
  [actions-data {:keys [action-name action-data next-action parent-action sequence-path path graph] :as params}]
  (let [parsed-action (get-action-data-sequence-data params)
        sequence-data (->> action-data :data)
        next-actions (map-indexed (fn [index item] [index item]) sequence-data)]
    (->> next-actions
         (reduce
           (fn [[result prev-action-name] [index sequence-item-data]]
             (let [sequence-item-name (get-sequence-item-name action-name index)
                   next-item-name (if (next-to-index sequence-data index)
                                    (get-sequence-item-name action-name (inc index))
                                    next-action)
                   prev-action (if (nil? prev-action-name)
                                 action-name
                                 (get-last-children result prev-action-name))
                   last-item? (= index (dec (count next-actions)))
                   path (conj (or path [action-name]) index)]
               [(merge-actions result (parse-actions-chain actions-data
                                                           {:action-name   sequence-item-name
                                                            :action-data   sequence-item-data
                                                            :parent-action (if last-item? parent-action action-name)
                                                            :next-action   next-item-name
                                                            :prev-action   prev-action
                                                            :sequence-path (conj sequence-path action-name)
                                                            :path          path
                                                            :graph         result}))
                sequence-item-name]))
           [(merge-actions graph parsed-action) nil])
         (first))))
