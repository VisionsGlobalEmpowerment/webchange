(ns webchange.templates.library.first-word-book.timeout
  (:require
    [webchange.templates.utils.dialog :as dialog]))

(defn get-action-name
  []
  (str "spread-timeout"))

(defn add-action-data
  [activity-data]
  (let [action-name (-> (get-action-name) (keyword))
        action-data (dialog/default (str "Spread timeout") {:empty-duration 500})]
    (assoc-in activity-data [:actions action-name] action-data)))
