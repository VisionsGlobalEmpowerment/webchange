(ns webchange.common.core
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.subs :as subs]
    [webchange.events :as events]
    [webchange.interpreter.events :as ie]
    [webchange.interpreter.variables.events :as vars.events]
    [webchange.common.events :as ce]
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

(defn with-parent-origin
  ([object parent-params]
   (with-parent-origin object parent-params "center-center"))
  ([object parent-params default-origin]
   (assoc object :origin (if (contains? parent-params :origin)
                           (:origin parent-params)
                           {:type default-origin}))))

