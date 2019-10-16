(ns webchange.editor-v2.diagram.scene-data-parser.actions-parser)

(println "Updated!")

(defn add-action
  [actions action-name action-data]
  (assoc actions action-name action-data))

(def not-nil? (complement nil?))

(defn add-next-property
  [next data]
  (assoc data :next (cond
                      (sequential? next) (vec next)
                      (not-nil? next) [next]
                      :else [])))

(defn add-parent-property
  [parent data]
  (if-not (nil? parent)
    (assoc data :parent parent)
    data))

(defmulti parse-action
          (fn [_ action-data _ _]
            (:type action-data)))

(defmethod parse-action "test-var-scalar"
  [action-name action-data parent-action next-action]
  (let [success (->> action-data :success keyword)
        fail (->> action-data :fail keyword)]
    (->> {:type (:type action-data)
          :data action-data}
         (add-next-property [success fail])
         (add-parent-property parent-action)
         (assoc {} action-name))))

(defmethod parse-action "sequence"
  [action-name action-data parent-action next-action]
  (let [first-item (->> action-data :data first keyword)]
    (->> {:type (:type action-data)
          :data action-data}
         (add-next-property first-item)
         (add-parent-property parent-action)
         (assoc {} action-name))))

(defmethod parse-action "animation"
  [action-name action-data parent-action next-action]
  (->> {:type   (:type action-data)
        :data   action-data}
       (add-next-property next-action)
       (add-parent-property parent-action)
       (assoc {} action-name)))

(defmethod parse-action "sequence-data"
  [action-name action-data parent-action next-action]
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
                                    next-action)))
      (->> {:type   (:type action-data)
            :data   action-data}
           (add-next-property (->> child-actions first first))
           (add-parent-property parent-action)
           (assoc {} action-name))
      child-actions)))

(defmethod parse-action "parallel"
  [action-name action-data parent-action next-action]
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
                                    next-action)))
      (->> {:type   (:type action-data)
            :data   action-data}
           (add-next-property (->> child-actions (map first)))
           (add-parent-property parent-action)
           (assoc {} action-name))
      child-actions)))

(defmethod parse-action :default
  [action-name action-data parent-action next-action]
  (->> {:type   (:type action-data)
        :data   action-data}
       (add-next-property next-action)
       (add-parent-property parent-action)
       (assoc {} action-name)))

(defn parse-actions
  [scene-data]
  ;(loop [actions-data (:actions scene-data)
  ;       parsing-que (->> scene-data :actions keys)
  ;       result {}]
  ;  (if-not (= 0 (count parsing-que))
  ;    (let [[current-action-name & rest-que] parsing-que
  ;          current-action-data (get actions-data current-action-name)
  ;          [next-action-names parsed-action-data] (parse-action)]
  ;
  ;      )
  ;    result))
  )

;(defn parse-actions
;  [scene-data]
;  (loop [data scene-data
;         parsing-que (->> scene-data :actions keys)
;         result {}]
;    (if-not (= 0 (count parsing-que))
;      (let [[parsing-que result] (reduce
;                                   (fn [[next-que result] action-name]
;                                     (let [action-data (get data action-name)
;                                           _ (println (str action-name " -> " action-data))
;                                           [child-actions prepared-data] (parse-action action-data)]
;                                       [(concat next-que child-actions)
;                                        (add-action result action-name prepared-data)]))
;                                   [[] result]
;                                   ())]
;        (recur data parsing-que result))
;      result)
;    )
;(->> (:actions scene-data)
;     (seq)
;     (reduce
;       (fn [result [action-name action-data]]
;         (assoc result action-name {:name   (name action-name)
;                                    :entity :action
;                                    :type   (:type action-data)
;                                    :outs   (get-action-outs action-data)}))
;       {}))
;)
