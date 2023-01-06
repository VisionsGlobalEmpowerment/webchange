(ns webchange.common.date-time
  (:require
    [clojure.tools.logging :as log]
    [java-time :as jt]))

(defn date-time2iso-str [date]
  (try
    (jt/format "yyyy-MM-dd'T'HH:mm:ss.SSSSSS" date)
    (catch Exception e
      (log/warn e))))

(defn date2str [date]
  (jt/format "yyyy-MM-dd" date))

(defn str2date [date]
  (jt/local-date "yyyy-MM-dd"  date))

(defn iso-str2date-time [date]
  (try
    (jt/local-date-time "yyyy-MM-dd'T'HH:mm:ss.SSSSSS" date)
    (catch Exception e
      (log/warn e))))

(comment
  (->> "2022-10-06T19:29:57.775990"
       (jt/local-date-time "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
       (jt/format "yyyy-MM-dd'T'HH:mm:ss.SSSSSS"))
  
  (iso-str2date-time "2022-10-06T19:29:57.775990"))
