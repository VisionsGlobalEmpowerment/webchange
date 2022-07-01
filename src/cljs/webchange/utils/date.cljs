(ns webchange.utils.date)

(defn new-date
  [date-str]
  (js/Date. date-str))

(defn ->locale-date-string
  [date]
  (.toLocaleDateString date))

(defn date-str->locale-date
  [date-str]
  " '2022-05-18T11:33:36.316428' -> '18/05/2022' "
  (-> (new-date date-str)
      (->locale-date-string)))

(defn get-seconds
  [date]
  (.getSeconds date))

(defn get-minutes
  [date]
  (.getMinutes date))

(defn ms->time
  [ms]
  (let [date (new-date ms)
        m (get-minutes date)
        s (get-seconds date)]
    (cond->> (str s "s")
             (> m 0) (str m "m : "))))
