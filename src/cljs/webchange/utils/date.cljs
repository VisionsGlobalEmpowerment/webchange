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

(defn ->time
  [date]
  (let [fill-zero #(if (< % 10) (str "0" %) %)
        m (-> date get-minutes fill-zero)
        h (-> date get-hours fill-zero)]
    (str h ":" m)))

(defn date-str->time
  [date-str]
  " '2022-05-18T11:33:36.316428' -> '18/05/2022' "
  (-> (new-date date-str)
      (->time)))

(defn ms->duration
  [ms]
  (let [date (new-date ms)
        m (get-minutes date)
        s (get-seconds date)
        h (get-hours date)]
    (cond->> ""
             (> s 0) (str s "s")
             (> m 0) (str m "m ")
             (> h 0) (str h "h "))))
