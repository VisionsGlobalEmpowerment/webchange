(ns webchange.dashboard.dashboard-layout.views-side-menu
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [webchange.dashboard.classes.views :refer [classes-menu]]
    [webchange.dashboard.students.views :refer [students-menu]]
    ))

(defn side-menu
  []
   [:div
    [classes-menu]
    [students-menu]])
