(ns webchange.editor-v2.creation-progress.translation-progress.overall-progress
  (:require
    [webchange.editor-v2.creation-progress.translation-progress.concepts-fulfillness :refer [check-dataset-items]]
    [webchange.editor-v2.creation-progress.translation-progress.dialogs-fulfillness :refer [check-dialogs]]))

(defn get-progress
  [scene-data dataset-items dataset-scheme]
  (let [overall-data [(check-dataset-items scene-data dataset-items dataset-scheme)
                      (check-dialogs scene-data)]]
    {:progress     (/ (->> overall-data
                           (map :progress)
                           (apply +))
                      (count overall-data))
     :overall-data overall-data}))
