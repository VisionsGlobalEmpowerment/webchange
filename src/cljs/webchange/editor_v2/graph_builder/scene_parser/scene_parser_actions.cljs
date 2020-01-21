(ns webchange.editor-v2.graph-builder.scene-parser.scene-parser-actions
  (:require
    [webchange.editor-v2.graph-builder.scene-parser.utils.create-graph-node :refer [create-graph-node
                                                                                    get-sequence-item-name]]
    [webchange.editor-v2.graph-builder.scene-parser.utils.get-action-data :refer [get-parallel-action-children
                                                                                  get-sequence-action-last-child
                                                                                  get-sequence-data-action-last-child
                                                                                  sequence-action-name?
                                                                                  sequence-data-action?
                                                                                  parallel-action-name?]]
    [webchange.editor-v2.graph-builder.utils.merge-actions :refer [merge-actions]]))

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

;; ---

(defmulti get-action-data
          (fn [{:keys [action-data]}]
            (:type action-data)))

(defmulti parse-actions-chain
          (fn [_ {:keys [action-data]}]
            (:type action-data)))

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

(defmethod get-action-data "parallel"
  [{:keys [action-name action-data prev-action path]}]
  (->> (create-graph-node {:data        action-data
                           :path        (or path [action-name])
                           :connections (->> (get-parallel-action-data-children action-name action-data)
                                             (map first)
                                             (map (fn [child]
                                                    {:previous prev-action
                                                     :handler  child
                                                     :sequence action-name})))})
       (assoc {} action-name)))

(defmethod parse-actions-chain "parallel"
  [actions-data {:keys [action-name action-data next-action sequence-path parent-action path] :as params}]
  (let [parsed-action (get-action-data params)
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
                                                    :sequence-path (conj sequence-path action-name)})))
      parsed-action
      next-actions)))

;; sequence

(defmethod get-action-data "sequence"
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
                             :connections connections})
         (assoc {} action-name))))

(defmethod parse-actions-chain "sequence"
  [actions-data {:keys [action-name action-data next-action parent-action sequence-path] :as params}]
  (let [parsed-action (get-action-data params)
        sequence-data (->> action-data :data (map keyword))
        next-actions (map-indexed (fn [index item] [index item]) sequence-data)
        cycled? (some #{action-name} sequence-path)]
    (if-not cycled?
      (reduce
        (fn [result [index sequence-item-name]]
          (let [sequence-item-data (get actions-data sequence-item-name)
                next-item-name (or (next-to-index sequence-data index) next-action)
                previous-item-name (or (prev-to-index sequence-data index) action-name)
                prev-action (cond
                              (and (sequence-action-name? actions-data previous-item-name)
                                   (not (= previous-item-name action-name))) (get-sequence-action-last-child result previous-item-name)
                              (parallel-action-name? result previous-item-name) (get-parallel-action-children result previous-item-name)
                              :else previous-item-name)
                last-item? (= index (dec (count next-actions)))]
            (merge-actions result (parse-actions-chain actions-data
                                                       {:action-name   sequence-item-name
                                                        :action-data   sequence-item-data
                                                        :parent-action (if last-item? parent-action action-name)
                                                        :next-action   next-item-name
                                                        :prev-action   prev-action
                                                        :sequence-path (conj sequence-path action-name)}))))
        parsed-action
        next-actions)
      parsed-action)))

;; sequence-data

(defmethod get-action-data "sequence-data"
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
                             :connections [{:previous prev-action
                                            :handler  (->> child-actions first first)
                                            :sequence action-name}]})
         (assoc {} action-name))))

(defmethod parse-actions-chain "sequence-data"
  [actions-data {:keys [action-name action-data next-action parent-action sequence-path path] :as params}]
  (let [parsed-action (get-action-data params)
        sequence-data (->> action-data :data)
        next-actions (map-indexed (fn [index item] [index item]) sequence-data)]
    (reduce
      (fn [result [index sequence-item-data]]
        (let [sequence-item-name (get-sequence-item-name action-name index)
              next-item-name (if (next-to-index sequence-data index)
                               (get-sequence-item-name action-name (inc index))
                               next-action)
              previous-item-name (if (prev-to-index sequence-data index)
                                   (get-sequence-item-name action-name (dec index))
                                   action-name)
              previous-action-data (if (> index 0)
                                     (second (nth next-actions (dec index)))
                                     nil)
              prev-action (cond
                            (and (sequence-data-action? previous-action-data)
                                 (not (= previous-item-name action-name))) (get-sequence-data-action-last-child previous-action-data previous-item-name)
                            (parallel-action-name? result previous-item-name) (get-parallel-action-children result previous-item-name)
                            :else previous-item-name)
              last-item? (= index (dec (count next-actions)))
              path (conj (or path [action-name]) index)]
          (merge-actions result (parse-actions-chain actions-data
                                                     {:action-name   sequence-item-name
                                                      :action-data   sequence-item-data
                                                      :parent-action (if last-item? parent-action action-name)
                                                      :next-action   next-item-name
                                                      :prev-action   prev-action
                                                      :sequence-path (conj sequence-path action-name)
                                                      :path          path}))))
      parsed-action
      next-actions)))

;; test

(defn handler-name?
  [action-data event-name]
  (let [handler (->> action-data event-name)]
    (string? handler)))

(defn get-test-handler-name
  [action-name action-data event-name]
  (let [handler (->> action-data event-name)]
    (if (string? handler)
      (keyword handler)
      (->> (clojure.core/name event-name)
           (str (clojure.core/name action-name) "-")
           (keyword)))))

(defn get-test-action-data
  [{:keys [action-name action-data prev-action path]}]
  (->> (create-graph-node {:data        action-data
                           :path        (or path [action-name])
                           :connections [{:previous prev-action
                                          :name     "success"
                                          :handler  (get-test-handler-name action-name action-data :success)}
                                         {:previous prev-action
                                          :name     "fail"
                                          :handler  (get-test-handler-name action-name action-data :fail)}]})
       (assoc {} action-name)))

(defn parse-test-action-chain
  [actions-data {:keys [action-name action-data sequence-path path] :as params}]
  (reduce
    (fn [result event-name]
      (if-not (nil? (get action-data event-name))
        (if (handler-name? action-data event-name)
          (let [next-node-name (-> action-data (get event-name) keyword)]
            (merge-actions result (parse-actions-chain actions-data
                                                       {:action-name   next-node-name
                                                        :action-data   (get actions-data next-node-name)
                                                        :parent-action nil
                                                        :next-action   nil
                                                        :prev-action   action-name
                                                        :sequence-path sequence-path})))

          (let [next-node-name (get-test-handler-name action-name action-data event-name)
                next-node-data (get action-data event-name)
                path (conj (or path [action-name]) event-name)]
            (merge-actions result (parse-actions-chain actions-data
                                                       {:action-name   next-node-name
                                                        :action-data   next-node-data
                                                        :parent-action nil
                                                        :next-action   nil
                                                        :prev-action   action-name
                                                        :sequence-path sequence-path
                                                        :path          path}))))
        result)
      )
    (get-action-data params)
    [:success :fail]))

;; test-var-scalar

(defmethod get-action-data "test-var-scalar"
  [params]
  (get-test-action-data params))

(defmethod parse-actions-chain "test-var-scalar"
  [actions-data params]
  (parse-test-action-chain actions-data params))

;; test-transitions-collide

(defmethod get-action-data "test-transitions-collide"
  [params]
  (get-test-action-data params))

(defmethod parse-actions-chain "test-transitions-collide"
  [actions-data params]
  (parse-test-action-chain actions-data params))

;; action

(defn get-possible-values
  [link-data]
  (let [possible-values (:possible-values link-data)]
    (if-not (nil? possible-values)
      (->> possible-values
           (map (fn [value] (cond
                              (number? value) (str value)
                              (keyword? value) (clojure.core/name value)
                              :else value))))
      (do (.warn js/console ":possible-values are not defined")
          []))))

(defn get-template
  [action-data]
  (some
    (fn [from-key]
      (some
        (fn [from-data]
          (and (and (string? (:template from-data))
                    (= "id" (:action-property from-data)))
               from-data))
        (get action-data from-key)))
    [:from-var :from-params]))

(defn template->values
  [template]
  (let [possible-values (get-possible-values template)
        template (:template template)]
    (->> possible-values
         (map (fn [value] (clojure.string/replace template #"%" value)))
         (map keyword))))

(defn link->values
  [link]
  (let [possible-values (get-possible-values link)]
    (->> possible-values
         (map keyword))))

(defn get-action-link-from-var
  [action-data]
  (some
    (fn [from-key]
      (some
        (fn [from-data]
          (and (= "id" (:action-property from-data))
               from-data))
        (get action-data from-key)))
    [:from-var :from-params]))

(defn get-referred-actions
  [action-data]
  (let [template (get-template action-data)
        link-from-var (get-action-link-from-var action-data)]
    (cond
      (-> action-data :id string?) [(-> action-data :id keyword)]
      (-> template nil? not) (template->values template)
      (-> link-from-var nil? not) (link->values link-from-var)
      :else nil)))

;; ToDo: write test when action doesn't have 'referred-actions' but has passed 'next-action' action;
;; e.g. ':concept-intro' sequence in 'home' scene

(defmethod get-action-data "action"
  [{:keys [action-name action-data parent-action prev-action next-action path]}]
  (let [prev-actions (if (sequential? prev-action) prev-action [prev-action])
        next-actions (or (get-referred-actions action-data) [next-action])]
    (->> (create-graph-node {:data        action-data
                             :path        (or path [action-name])
                             :connections (reduce
                                            (fn [result prev-action]
                                              (concat result (map
                                                               (fn [next-action] {:previous prev-action
                                                                                  :handler  next-action
                                                                                  :sequence parent-action})
                                                               next-actions)))
                                            []
                                            prev-actions)})
         (assoc {} action-name))))

(defmethod parse-actions-chain "action"
  [actions-data {:keys [action-data action-name parent-action next-action sequence-path] :as params}]
  (let [referred-actions (get-referred-actions action-data)]
    (reduce
      (fn [result referred-action]
        (merge-actions result (parse-actions-chain actions-data
                                                   {:action-name   referred-action
                                                    :action-data   (get actions-data referred-action)
                                                    :parent-action parent-action
                                                    :next-action   next-action
                                                    :prev-action   action-name
                                                    :sequence-path sequence-path})))
      (get-action-data params)
      referred-actions)))

;; default

(defmethod get-action-data :default
  [{:keys [action-name action-data parent-action next-action prev-action path]}]
  (let [prev-actions (if (sequential? prev-action) prev-action [prev-action])
        next-actions (if (sequential? next-action) next-action [next-action])]
    (->> (create-graph-node {:data        action-data
                             :path        (or path [action-name])
                             :connections (reduce (fn [result prev-action]
                                                    (concat result (map (fn [next-action]
                                                                          {:previous prev-action
                                                                           :handler  next-action
                                                                           :sequence parent-action})
                                                                        next-actions)))
                                                  []
                                                  prev-actions)})
         (assoc {} action-name))))

(defmethod parse-actions-chain :default
  [_ params]
  (get-action-data params))

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
                                 :prev-action   object-name
                                 :sequence-path []})))
      {}
      entries)))
