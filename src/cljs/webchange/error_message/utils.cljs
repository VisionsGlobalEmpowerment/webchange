(ns webchange.error-message.utils
  (:require
    [camel-snake-kebab.core :refer [->Camel_Snake_Case]]))

(defn get-error-title
  [{:keys [type]}]
  (cond
    (keyword? type) (-> type (clojure.core/name) (->Camel_Snake_Case) (clojure.string/replace "_" " "))
    (string? type) type
    :else nil))

(defn get-error-messages
  [{:keys [message]}]
  (cond
    (map? message) (map (fn [[key value]]
                          (str (clojure.core/name key) ": " value))
                        message)
    (not (sequential? message)) [message]
    :else message))
