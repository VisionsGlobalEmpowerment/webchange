(ns webchange.editor-v2.layout.components.common-actions.views
  (:require
    [webchange.editor-v2.layout.components.common-actions.background-music :as bm]
    ))


(defn actions
  []
  [:div
    [bm/background-music]
    [bm/remove-background-music]
   ])
