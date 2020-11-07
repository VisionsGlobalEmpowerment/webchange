(ns webchange.interpreter.object-data.object-filters
  (:require
    [webchange.interpreter.object-data.with-transition :refer [with-transition]]))

(defn- with-filter-transition
  [[{:keys [transition] :as object-params} has-filter]]
  (if (and transition has-filter)
    (let [filter-transition (str transition "-filter")]
      (with-transition (assoc object-params :filter-transition filter-transition)))
    object-params))

(defn- with-grayscale
  [[object-params has-filter]]
  (if (= (:filter object-params) "grayscale")
    [(update-in object-params [:filters] conj {:name "grayscale"}) true]
    [object-params has-filter]))

(defn- with-brighten
  [[object-params has-filter]]
  (if (= (:filter object-params) "brighten")
    [(update-in object-params [:filters] conj {:name  "brightness"
                                              :value (:brightness object-params)}) true]
    [object-params has-filter]))

(defn- with-highlight
  [[object-params has-filter]]
  (if (contains? object-params :highlight)
    [(update-in object-params [:filters] conj {:name "glow"}) true]
    [object-params has-filter]))

(defn- with-pulsation
  [[object-params has-filter]]
  (if (:eager object-params)
    [(update-in object-params [:filters] conj {:name "pulsation"}) true]
    [object-params has-filter]))

(defn- empty-filter [] {:filters []})

(defn with-filter-params
  [params]
  (-> [params false]
      (with-grayscale)
      (with-brighten)
      (with-highlight)
      (with-pulsation)
      (with-filter-transition)))
