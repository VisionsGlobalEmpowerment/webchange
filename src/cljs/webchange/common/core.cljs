(ns webchange.common.core
  (:require
    [clojure.string :as s]))

(defn format-date [date]
  (let [pad (fn [number] (if (< number 10) (str "0" number) (str number)))
        year (.getUTCFullYear date)
        month (inc (.getUTCMonth date))
        day (.getUTCDate date)]
    (str year "-" (pad month) "-" (pad day))))

(defn format-date-string [date-string]
  (if (not (s/blank? date-string))
    (format-date (js/Date. date-string))))

