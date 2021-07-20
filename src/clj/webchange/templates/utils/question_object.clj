(ns webchange.templates.utils.question-object
  (:require
    [clojure.tools.logging :as log]
    [webchange.templates.utils.question-object.multiple-choice-image :as multiple-choice-image]))

(defn create
  [{:keys [question-type question question-screenshot answers img] :as args}
   {:keys [suffix action-name object-name next-action-name]}]
  (log/debug "create-question-object")
  (log/debug "args" args)
  {:action-name action-name
   :object-name object-name
   :actions     {}
   :objects     (multiple-choice-image/create {:object-name object-name} args)})
