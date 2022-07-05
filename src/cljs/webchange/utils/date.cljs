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
  (.getUTCSeconds date))

(defn get-minutes
  [date]
  (.getUTCMinutes date))

(defn get-hours
  [date]
  (.getUTCHours date))

(defn ms->time
  [ms]
  (let [date (new-date ms)
        m (get-minutes date)
        s (get-seconds date)
        h (get-hours date)]
    (cond->> ""
             (> s 0) (str s "s")
             (> m 0) (str m "m ")
             (> h 0) (str h "h "))))
