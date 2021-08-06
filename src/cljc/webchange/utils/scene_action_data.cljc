(ns webchange.utils.scene-action-data
  (:require
    [clojure.string :as string]))

(defn get-action-type
  [action-data]
  (get action-data :type))

(defn text-animation-action?
  [action-data]
  (-> (get-action-type action-data)
      (= "text-animation")))

(defn dialog-action?
  [action-data]
  (contains? action-data :phrase))

(defn fix-available-effect
  [available-effect]
  (if (string? available-effect)
    {:name   (string/replace available-effect "-" " ")
     :action available-effect}
    available-effect))

(defn get-available-effects
  [action-data]
  (->> (get action-data :available-activities [])
       (map fix-available-effect)))
