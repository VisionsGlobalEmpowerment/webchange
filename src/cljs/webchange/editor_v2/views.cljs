(ns webchange.editor-v2.views
  (:require
    [webchange.editor-v2.fix-lodash]
    [webchange.editor-v2.view-diagram :refer [diagram]]))

(defn main-view
  [id]
  [:div.editor-v2 {}
   [:div {}
    "Scene 3"]
   [:div.diagram-container
    [diagram id]]])
