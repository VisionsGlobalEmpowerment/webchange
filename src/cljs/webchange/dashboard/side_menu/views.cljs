(ns webchange.dashboard.side-menu.views
  (:require
    [cljsjs.material-ui]
    [cljs-react-material-ui.core :refer [get-mui-theme color]]
    [cljs-react-material-ui.reagent :as ui]
    [webchange.dashboard.classes.views :refer [classes-list-menu]]
    [webchange.dashboard.students.views :refer [students-list-menu]]))

(defn side-menu
  []
   [:div.side-menu
    [classes-list-menu]
    [ui/divider]
    [students-list-menu]])
