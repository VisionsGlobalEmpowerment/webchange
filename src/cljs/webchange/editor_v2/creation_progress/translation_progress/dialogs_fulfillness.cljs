(ns webchange.editor-v2.creation-progress.translation-progress.dialogs-fulfillness
  (:require
    [webchange.editor-v2.creation-progress.translation-progress.validate-action :refer [validate-dialog-action]]
    [webchange.editor-v2.sandbox.parse-actions :refer [find-all-actions]]))

(defn check-dialogs
  [scene-data]
  (let [dialog-actions (find-all-actions scene-data {:phrase #(some? %)})
        warnings (->> dialog-actions
                      (map (fn [[action-name action-data]]
                             (let [complete? (validate-dialog-action action-data)]
                               (when-not complete?
                                 [{:id    action-name
                                   :label (:phrase-description action-data)}]))))
                      (remove nil?))
        items-count (count dialog-actions)
        completed-items-count (- items-count (count warnings))]
    {:entity   :dialogs
     :warnings warnings
     :progress (/ completed-items-count items-count)}))
