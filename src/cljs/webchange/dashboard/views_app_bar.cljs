(ns webchange.dashboard.views-app-bar
  (:require
    [cljsjs.material-ui]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [webchange.dashboard.views-common :refer [get-shift-styles]]))

(defn app-bar
  [{:keys [title on-open-menu drawer-open? drawer-width]}]
  [ui/app-bar
   {:color "default"
    :position "static"
    :style    (get-shift-styles drawer-open? drawer-width)}
   [ui/toolbar
    (when-not drawer-open?
      [ui/icon-button {:color      "inherit"
                       :aria-label "Menu"
                       :on-click   on-open-menu}
       [ic/menu]])
    [ui/typography {:color   "inherit"
                    :variant "h6"}
     title]]])
