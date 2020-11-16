(ns webchange.editor-v2.creation-progress.views
  (:require
    [webchange.editor-v2.creation-progress.views-full-info :refer [full-info]]
    [webchange.editor-v2.creation-progress.views-short-info :refer [short-info]]))

(defn progress-panel
  []
  [:div
   [full-info]
   [short-info]])
