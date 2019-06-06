(ns webchange.dashboard.side-menu.views
  (:require
    [cljs-react-material-ui.core :refer [get-mui-theme color]]
    [cljs-react-material-ui.reagent :as ui]
    [webchange.dashboard.classes.views :refer [classes-list-menu]]))

(defn- students-menu
  []
  [ui/list
   [ui/subheader "Students"]
   [ui/list-item {:primary-text "Mark"
                  :inset-children true}]])

(defn side-menu
  []
   [:div.side-menu
    [classes-list-menu]
    [ui/divider]
    [students-menu]])
