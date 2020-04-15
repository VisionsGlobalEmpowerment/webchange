(ns webchange.common.date-time
  (:require
    [java-time :as jt]
  ))

(defn date-time2iso-str [date]
  (jt/format "yyyy-MM-dd'T'HH:mm:ss.SSS" date))

(defn date2str [date]
  (jt/format "yyyy-MM-dd" date))

(defn str2date [date]
  (jt/local-date "yyyy-MM-dd"  date))

(defn iso-str2date-time [date]
  (jt/local-date-time "yyyy-MM-dd'T'HH:mm:ss.SSS" date))