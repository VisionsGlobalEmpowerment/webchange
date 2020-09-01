(ns webchange.interpreter.object-data.object-filters
  (:require
    [webchange.common.core :refer [with-filter-transition]]))

(defn- grayscale-filter
  []
  {:filters [{:name "grayscale"}]})

(defn- brighten-filter
  [{:keys [brightness transition]}]
  (->
    {:filters    [{:name  "brighten"
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
