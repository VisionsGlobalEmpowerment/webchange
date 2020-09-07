(ns webchange.interpreter.object-data.object-filters
  (:require
    [webchange.interpreter.object-data.with-transition :refer [with-transition]]))

(defn- with-filter-transition
  [{:keys [transition] :as object}]
  (if transition
    (let [filter-transition (str transition "-filter")]
      (with-transition (assoc object :transition filter-transition)))
    object))

(defn- with-grayscale
  [object-params]
  (if (= (:filter object-params) "grayscale")
    (update-in object-params [:filters] conj {:name "grayscale"})
    object-params))

(defn- with-brighten
  [object-params]
  (if (= (:filter object-params) "brighten")
    (update-in object-params [:filters] conj {:name  "brightness"
                                              :value (:brightness object-params)})
    object-params))

(defn- with-highlight
  [object-params]
  (if (contains? object-params :highlight)
    (update-in object-params [:filters] conj {:name "glow"})
    object-params))

(defn- with-pulsation
  [object-params]
  (if (:eager object-params)
    (update-in object-params [:filters] conj {:name "pulsation"})
    object-params))

(defn- empty-filter [] {:filters []})

(defn with-filter-params
  [params]
  (-> params
      (with-grayscale)
      (with-brighten)
      (with-highlight)
      (with-pulsation)
      (with-filter-transition)))
