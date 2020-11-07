(ns webchange.editor-v2.graph-builder.scene-parser.actions-parser.parse-action
  (:require
    [webchange.editor-v2.graph-builder.scene-parser.actions-parser.interface :refer [parse-actions-chain]]
    [webchange.editor-v2.graph-builder.scene-parser.utils.create-graph-node :refer [create-graph-node]]
    [webchange.editor-v2.graph-builder.utils.merge-actions :refer [merge-actions]]))

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

(defn get-action-data-action
  [{:keys [action-name action-data parent-action prev-action next-action path]}]
  (let [prev-actions (if (sequential? prev-action) prev-action [prev-action])
        next-actions (or (get-referred-actions action-data) [next-action])]
    (->> (create-graph-node {:data        action-data
                             :path        (or path [action-name])
                             :children    (get-referred-actions action-data)
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

(defn- already-parsed?
  [action-name action-data graph]
  (and (contains? graph action-name)
       (clojure.set/superset? (get-in graph [action-name :connections])
                              (get action-data :connections))))

(defmethod parse-actions-chain "action"
  [actions-data {:keys [action-data action-name parent-action next-action sequence-path graph] :as params}]
  (let [referred-actions (get-referred-actions action-data)
        action-node (get-action-data-action params)]
    (if-not (already-parsed? action-name (get action-node action-name) graph)
      (reduce
        (fn [result referred-action]
          (merge-actions result (parse-actions-chain actions-data
                                                     {:action-name   referred-action
                                                      :action-data   (get actions-data referred-action)
                                                      :parent-action parent-action
                                                      :next-action   next-action
                                                      :prev-action   action-name
                                                      :sequence-path sequence-path
                                                      :graph         result})))
        (->> (get-action-data-action params)
             (merge-actions graph))
        referred-actions)
      {})))
