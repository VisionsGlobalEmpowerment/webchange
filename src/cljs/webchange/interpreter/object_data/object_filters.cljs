(ns webchange.interpreter.object-data.object-filters
  (:require
    [webchange.interpreter.object-data.with-transition :refer [with-transition]]))

(defn- with-filter-transition
  [{:keys [transition] :as object}]
  (if transition
    (let [filter-transition (str transition "-filter")]
      (with-transition (assoc object :transition filter-transition)))
    object))

(defn- grayscale-filter
  []
  {:filters [{:name "grayscale"}]})

(defn- brighten-filter
  [{:keys [brightness transition]}]
  (->
    {:filters    [{:name  "brightness"
                   :value brightness}]
     :transition transition}
    with-filter-transition))

(defn- with-highlight
  [image-params object-params]
  (if (contains? object-params :highlight)
    (update-in image-params [:filters] conj {:name "glow"})
    image-params))

(defn- with-pulsation
  [image-params object-params]
  (if (:eager object-params)
    (update-in image-params [:filters] conj {:name "pulsation"})
    image-params))

(defn- empty-filter [] {:filters []})

(defn with-filter-params
  [{:keys [filter] :as params}]
  (-> (case filter
        "grayscale" (grayscale-filter)
        "brighten" (brighten-filter params)
        (empty-filter))
      (with-highlight params)
      (with-pulsation params)))
