(ns webchange.templates.library.flipbook.remove-text
  (:require
    [clojure.tools.logging :as log]))

(defn remove-text
  [activity-data object-name]
  (log/debug ">> remove-text-object")
  (log/debug "object-name" object-name)
  activity-data)
