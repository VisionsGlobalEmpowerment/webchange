(ns webchange.editor-v2.diagram.scene-data-parser.actions-parser
  (:require
    [clojure.set :refer [intersection]]))

(def not-nil? (complement nil?))

(defn last-index?
  [list index]
  (and
    (= index (dec (count list)))
    (not (= -1 index))))

(defn last?
  [list item]
  (let [index (.indexOf list item)]
    (last-index? list index)))

(defn next-to-index
  [list index]
  (if-not (or (= -1 index)
              (last-index? list index))
    (nth list (inc index))
    nil))

(defn prev-to-index
  [list index]
  (if-not (or (= -1 index)
              (= 0 index))
    (nth list (dec index))
    nil))

(defn next-to
  [list item]
  (let [index (.indexOf list item)]
    (next-to-index list index)))


(defn add-connection-property
  [prev next parent data]
  (let [next-data (cond
                    (sequential? next) (vec next)
                    (not-nil? next) [next]
                    :else [])
        add-parent-property #(if-not (nil? %1) (assoc %2 :parent %1) %2)]
    (->> next-data
         (assoc {} :next)
         (add-parent-property parent)
         (assoc {} prev)
         (assoc data :connections))))

(defn seq-intersection
  [seq-1 seq-2]
  (vec (intersection (set seq-1) (set seq-2))))

(defn merge-actions
  [map-1 map-2]
  (let [actions-to-merge (seq-intersection (keys map-1) (keys map-2))
        actions-merged (reduce
                         (fn [result action-name]
                           (let [action-1-data (get map-1 action-name)
                                 action-2-data (get map-2 action-name)]
                             (assoc result action-name (merge action-1-data
                                                              action-2-data
                                                              {:connections (merge (:connections action-1-data)
                                                                                   (:connections action-2-data))})))
                           )
                         {}
                         actions-to-merge)]
    (merge map-1 map-2 actions-merged)))

(defmulti parse-action
          (fn [_ action-data _ _ _]
            (:type action-data)))

(defmethod parse-action "test-var-scalar"
  [action-name action-data parent-action next-action prev-action]
  (let [success (->> action-data :success keyword)
        fail (->> action-data :fail keyword)]
    (->> {:type (:type action-data)
          :data action-data}
         (add-connection-property prev-action [success fail] parent-action)
         (assoc {} action-name))))

(defmethod parse-action "sequence"
  [action-name action-data parent-action next-action prev-action]
  (let [first-item (->> action-data :data first keyword)]
    (->> {:type (:type action-data)
          :data action-data}
         (add-connection-property prev-action first-item parent-action)
         (assoc {} action-name))))

(defmethod parse-action "sequence-data"
  [action-name action-data parent-action next-action prev-action]
  (let [child-actions (->> (:data action-data)
                           (map-indexed
                             (fn [index child-action-data]
                               [(keyword (str (name action-name) "-" index))
                                child-action-data]))
                           (vec))]
    (reduce
      (fn [result [index child-action]]
        (let [[child-action-name child-action-data] child-action
              [next-child-action-name _] (next-to child-actions child-action)]
          (merge
            result
            (parse-action child-action-name
                          child-action-data
                          action-name
                          (or next-child-action-name next-action)
                          (or (first (prev-to-index child-actions index)) action-name)))))
      (->> {:type (:type action-data)
            :data action-data}
           (add-connection-property prev-action (->> child-actions first first) parent-action)
           (assoc {} action-name))
      (map-indexed (fn [index item] [index item]) child-actions))))

(defmethod parse-action "parallel"
  [action-name action-data parent-action next-action prev-action]
  (let [child-actions (->> (:data action-data)
                           (map-indexed
                             (fn [index child-action-data]
                               [(keyword (str (name action-name) "-" index))
                                child-action-data]))
                           (vec))]
    (reduce
      (fn [result [child-action-name child-action-data]]
        (merge result (parse-action child-action-name
                                    child-action-data
                                    action-name
                                    next-action
                                    action-name)))
      (->> {:type (:type action-data)
            :data action-data}
           (add-connection-property prev-action (->> child-actions (map first)) parent-action)
           (assoc {} action-name))
      child-actions)))

(defmethod parse-action :default
  [action-name action-data parent-action next-action prev-action]
  (->> {:type (:type action-data)
        :data action-data}
       (add-connection-property prev-action next-action parent-action)
       (assoc {} action-name)))

;; ---

(defmulti parse-actions-chain
          (fn [_ _ action-data _ _ _]
            (:type action-data)))

(defmethod parse-actions-chain "sequence"
  [actions-data node-name node-data parent-action next-action prev-action]
  (let [parsed-action (parse-action node-name node-data parent-action next-action prev-action)
        sequence-data (->> node-data :data (map keyword))]
    (reduce
      (fn [result [index next-node-name]]
        (let [next-node-data (get actions-data next-node-name)]
          (merge-actions result (parse-actions-chain actions-data
                                                     next-node-name
                                                     next-node-data
                                                     node-name
                                                     (or (next-to-index sequence-data index) next-action)
                                                     (or (prev-to-index sequence-data index) node-name))))
        )
      parsed-action
      (map-indexed (fn [index item] [index item]) sequence-data))))

(defmethod parse-actions-chain "parallel"
  [actions-data node-name node-data parent-action next-action prev-action]
  (let [parsed-action (parse-action node-name node-data parent-action next-action prev-action)
        next-actions (->> parsed-action keys (filter #(not (= % node-name))))]
    (reduce
      (fn [result next-action-name]
        (merge-actions result (parse-actions-chain actions-data
                                                   next-action-name
                                                   (->> next-action-name (get parsed-action) :data)
                                                   node-name
                                                   next-action
                                                   node-name)))
      parsed-action
      next-actions)
    ))

(defmethod parse-actions-chain "test-var-scalar"
  [actions-data node-name node-data parent-action next-action prev-action]
  (let [next-nodes [(->> node-data :success keyword)
                    (->> node-data :fail keyword)]]
    (reduce
      (fn [result next-node-name]
        (merge-actions result (parse-actions-chain actions-data
                                                   next-node-name
                                                   (get actions-data next-node-name)
                                                   nil nil node-name)))
      (parse-action node-name node-data parent-action next-action prev-action)
      next-nodes)))

(defmethod parse-actions-chain :default
  [_ node-name node-data parent-action next-action prev-action]
  (parse-action node-name node-data parent-action next-action prev-action))

(defn parse-actions
  [scene-data]
  (let [actions-data (:actions scene-data)
        start-node-name :click-on-box1
        start-node-data (get actions-data start-node-name)]
    (parse-actions-chain actions-data start-node-name start-node-data nil nil :box1)))


