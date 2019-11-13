(ns webchange.editor-v2.diagram.graph-builder.scene-parser.data-parser.data-parser-actions
  (:require
    [clojure.set :refer [intersection]]))

(def not-nil? (complement nil?))

(defn next-to-index
  [list index]
  (if-not (or (= -1 index)
              (= index (dec (count list))))
    (nth list (inc index))
    nil))

(defn prev-to-index
  [list index]
  (if-not (or (= -1 index)
              (= 0 index))
    (nth list (dec index))
    nil))

(defn seq-intersection
  [seq-1 seq-2]
  (vec (intersection (set seq-1) (set seq-2))))

(defn sequence-name?
  [data action-name]
  (= "sequence" (get-in data [action-name :type])))

(defn parallel-name?
  [data action-name]
  (= "parallel" (get-in data [action-name :type])))

(defn get-last-sequence-item
  [data action-name]
  (->> (get-in data [action-name :data])
       (last)
       (keyword)))

(defn add-connection-property
  [prev connection-data parent data]
  (let [default-next-action-event :next
        next-data (cond
                    (map? connection-data) connection-data
                    (sequential? connection-data) (assoc {} default-next-action-event connection-data)
                    (not-nil? connection-data) (assoc {} default-next-action-event [connection-data])
                    :else {})
        add-parent-property #(if-not (nil? %1) (assoc %2 :parent %1) %2)
        previous-action (if (nil? prev) :root prev)
        previous-actions (if (sequential? previous-action) previous-action [previous-action])
        connection-data (->> next-data
                             (assoc {} :handlers)
                             (add-parent-property parent))]
    (->> previous-actions
         (reduce
           (fn [result previous-action]
             (assoc result previous-action connection-data))
           {})
         (assoc data :connections))))

(defn merge-handlers
  [handlers-1 handlers-2]
  (reduce
    (fn [result event-name]
      (let [event-1-handlers (get handlers-1 event-name)
            event-2-handlers (get handlers-2 event-name)]
        (assoc result event-name (->> [event-1-handlers
                                       event-2-handlers]
                                      (apply concat)
                                      (distinct)))))
    {}
    (->> [(keys handlers-1)
          (keys handlers-2)]
         (apply concat)
         (distinct))))

(defn merge-connections
  [connections-1 connections-2]
  (reduce
    (fn [result connection-name]
      (let [connections-1-data (get connections-1 connection-name)
            connections-2-data (get connections-2 connection-name)]
        (assoc result connection-name (merge connections-1-data
                                             connections-2-data
                                             {:handlers (merge-handlers (:handlers connections-1-data)
                                                                        (:handlers connections-2-data))}))))
    {}
    (->> [(keys connections-1)
          (keys connections-2)]
         (apply concat)
         (distinct))))

(defn merge-actions
  [map-1 map-2]
  (let [actions-to-merge (seq-intersection (keys map-1) (keys map-2))
        actions-merged (reduce
                         (fn [result action-name]
                           (let [action-1-data (get map-1 action-name)
                                 action-2-data (get map-2 action-name)]
                             (assoc result action-name (merge action-1-data
                                                              action-2-data
                                                              {:connections (merge-connections (:connections action-1-data)
                                                                                               (:connections action-2-data))}))))
                         {}
                         actions-to-merge)]
    (merge map-1 map-2 actions-merged)))

;; ---

(defmulti get-action-data
          (fn [{:keys [action-data]}]
            (:type action-data)))

(defmulti parse-actions-chain
          (fn [_ {:keys [action-data]}]
            (:type action-data)))

;; parallel

(defmethod get-action-data "parallel"
  [{:keys [action-name action-data parent-action next-action prev-action]}]
  (let [child-actions (->> (:data action-data)
                           (map-indexed
                             (fn [index child-action-data]
                               [(keyword (str (name action-name) "-" index))
                                child-action-data]))
                           (vec))]
    (reduce
      (fn [result [child-action-name child-action-data]]
        (merge result (get-action-data {:action-name   child-action-name
                                        :action-data   child-action-data
                                        :parent-action action-name
                                        :next-action   next-action
                                        :prev-action   action-name})))
      (->> {:type (:type action-data)
            :data action-data}
           (add-connection-property prev-action (->> child-actions (map first)) parent-action)
           (assoc {} action-name))
      child-actions)))

(defmethod parse-actions-chain "parallel"
  [actions-data {:keys [action-name next-action] :as params}]
  (let [parsed-action (get-action-data params)
        next-actions (->> parsed-action keys (filter #(not (= % action-name))))]
    (reduce
      (fn [result next-action-name]
        (merge-actions result (parse-actions-chain actions-data
                                                   {:action-name   next-action-name
                                                    :action-data   (->> next-action-name (get parsed-action) :data)
                                                    :parent-action action-name
                                                    :next-action   next-action
                                                    :prev-action   action-name})))
      parsed-action
      next-actions)))

;; sequence

(defn get-parallel-children
  [actions-data action-name]
  (-> (get actions-data action-name)
      (:connections)
      (seq)
      (first)
      (last)
      (:handlers)
      (seq)
      (first)
      (last)))

(defmethod get-action-data "sequence"
  [{:keys [action-name action-data parent-action prev-action]}]
  (let [first-item (->> action-data :data first keyword)]
    (->> {:type (:type action-data)
          :data action-data}
         (add-connection-property prev-action first-item parent-action)
         (assoc {} action-name))))

(defmethod parse-actions-chain "sequence"
  [actions-data {:keys [action-name action-data next-action] :as params}]
  (let [parsed-action (get-action-data params)
        sequence-data (->> action-data :data (map keyword))
        next-actions (map-indexed (fn [index item] [index item]) sequence-data)]
    (reduce
      (fn [result [index sequence-item-name]]
        (let [sequence-item-data (get actions-data sequence-item-name)
              next-item-name (or (next-to-index sequence-data index) next-action)
              previous-item-name (or (prev-to-index sequence-data index) action-name)
              prev-action (cond
                            (and (sequence-name? actions-data previous-item-name)
                                 (not (= previous-item-name action-name))) (get-last-sequence-item actions-data previous-item-name)
                            (parallel-name? result previous-item-name) (get-parallel-children result previous-item-name)
                            :else previous-item-name)]
          (merge-actions result (parse-actions-chain actions-data
                                                     {:action-name   sequence-item-name
                                                      :action-data   sequence-item-data
                                                      :parent-action action-name
                                                      :next-action   next-item-name
                                                      :prev-action   prev-action}))))
      parsed-action
      next-actions)))

;; sequence-data

(defmethod get-action-data "sequence-data"
  [{:keys [action-name action-data parent-action next-action prev-action]}]
  (let [child-actions (->> (:data action-data)
                           (map-indexed
                             (fn [index child-action-data]
                               [(keyword (str (name action-name) "-" index))
                                child-action-data]))
                           (vec))]
    (reduce
      (fn [result [index child-action]]
        (let [[child-action-name child-action-data] child-action
              [next-child-action-name _] (next-to-index child-actions index)]
          (merge
            result
            (get-action-data {:action-name   child-action-name
                              :action-data   child-action-data
                              :parent-action action-name
                              :next-action   (or next-child-action-name next-action)
                              :prev-action   (or (first (prev-to-index child-actions index)) action-name)}))))
      (->> {:type (:type action-data)
            :data action-data}
           (add-connection-property prev-action (->> child-actions first first) parent-action)
           (assoc {} action-name))
      (map-indexed (fn [index item] [index item]) child-actions))))

;; test-var-scalar

(defmethod get-action-data "test-var-scalar"
  [{:keys [action-name action-data parent-action prev-action]}]
  (let [success (->> action-data :success keyword)
        fail (->> action-data :fail keyword)]
    (->> {:type (:type action-data)
          :data action-data}
         (add-connection-property prev-action {:success [success]
                                               :fail    [fail]} parent-action)
         (assoc {} action-name))))

(defmethod parse-actions-chain "test-var-scalar"
  [actions-data {:keys [action-name action-data] :as params}]
  (let [next-nodes [(->> action-data :success keyword)
                    (->> action-data :fail keyword)]]
    (reduce
      (fn [result next-node-name]
        (merge-actions result (parse-actions-chain actions-data
                                                   {:action-name   next-node-name
                                                    :action-data   (get actions-data next-node-name)
                                                    :parent-action nil
                                                    :next-action   nil
                                                    :prev-action   action-name})))
      (get-action-data params)
      next-nodes)))

;; default

(defn verified-type?
  [action-type]
  (some #(= action-type %) ["add-animation"
                            "animation-sequence"
                            "audio"
                            "empty"
                            "parallel"
                            "remove-interval"
                            "set-attribute"
                            "set-variable"
                            "sequence"
                            "sequence-data"
                            "test-var-scalar"]))

(defmethod get-action-data :default
  [{:keys [action-name action-data parent-action next-action prev-action]}]
  (let [action-type (:type action-data)]
    (->> {:type action-type
          :data action-data}
         (add-connection-property prev-action next-action parent-action)
         (assoc {} action-name))))

(defmethod parse-actions-chain :default
  [_ {:keys [action-data] :as params}]
  (let [action-type (:type action-data)]
    (when-not (verified-type? action-type)
      (.warn js/console (str "Parse action: action type '" action-type "' is not verified")))
    (get-action-data params)))

;; ---

(defn parse-actions
  [scene-data entries]
  (let [actions-data (:actions scene-data)]
    (reduce
      (fn [result [action-name object-name]]
        (merge-actions result (parse-actions-chain
                                actions-data
                                {:action-name   action-name
                                 :action-data   (get actions-data action-name)
                                 :parent-action nil
                                 :next-action   nil
                                 :prev-action   object-name})))
      {}
      entries)))
