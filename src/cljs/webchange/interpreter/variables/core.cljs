(ns webchange.interpreter.variables.core
  (:require
    [clojure.set :refer [union]]))

(def variables (atom {}))
(def providers (atom {}))

(defn get-variable
  [var-name]
  (get @variables var-name))

(defn set-variable!
  [var-name value]
  (swap! variables assoc var-name value))

(defn set-progress
  [db var-name value]
  (assoc-in db [:progress-data :variables (keyword var-name)] value))

(defn set-variables!
  [vars]
  (doall (map (fn [[k v]] (set-variable! k v)) vars)))

(defn- get-processed
  [provider-id]
  (get-in @providers [provider-id :processed]))

(defn- add-processed!
  [provider-id processed]
  (swap! providers update-in [provider-id :processed] union processed))

(defn- unprocessed
  [items provider-id]
  (let [processed (get-processed provider-id)]
    (->> items
         (filter #(->> % :id (contains? processed) not)))))

(defn filter-property-values
  [exclude-property-values items]
  (let [filter-map exclude-property-values
        key (-> filter-map keys first)
        filter-map-value (get filter-map key)
        filter-values (if (sequential? filter-map-value) filter-map-value [filter-map-value])]
    (filter (fn [item]
              (not (some (fn [filter-value] (= filter-value (get item key))) filter-values))) items)))

(defn provide!
  ([items variables provider-id]
   (provide! items variables provider-id {}))
  ([items variables provider-id params]
   (let [new-items (cond->> (unprocessed items provider-id)
                            (:exclude-values params) (remove (into #{} (:exclude-values params)))
                            (:exclude-property-values params) (filter-property-values (:exclude-property-values params))
                            (:limit params) (take (:limit params))
                            (:repeat params) (#(apply concat (repeat (:repeat params) %)))
                            (:shuffled params) shuffle
                            (:unique params) distinct
                            :always (take (count variables)))
         processed (->> new-items (map :id) (into #{}))
         vars (zipmap variables new-items)]
     (set-variables! vars)
     (when provider-id
       (add-processed! provider-id processed)))))

(defn has-next
  ([items provider-id]
   (has-next items provider-id {}))
  ([items provider-id params]
   (let [unprocessed (cond->> (unprocessed items provider-id)
                              (:exclude-values params) (remove (into #{} (:exclude-values params)))
                              (:exclude-property-values params) (filter-property-values (:exclude-property-values params)))]
     (->> unprocessed count (< 0)))))

(defn clear-vars!
  [keep-running]
  (reset! variables {})
  (reset! providers {})
  (when keep-running
    (set-variable! "status" :running)))
