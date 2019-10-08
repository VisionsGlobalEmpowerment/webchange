(ns webchange.editor-v2.views
  (:require
    [webchange.editor-v2.fix-lodash]
    [webchange.editor-v2.views-data :refer [data]]
    [webchange.editor-v2.views-diagram :refer [diagram]]
    [webchange.editor-v2.views-scene :refer [scene]]
    [webchange.ui.theme :refer [with-mui-theme]]))

(defn main-view
  []
  [with-mui-theme "dark"
   [:div.editor-v2 {}
    [:div.top-side
     [data]
     [scene]]
    [diagram]]])
