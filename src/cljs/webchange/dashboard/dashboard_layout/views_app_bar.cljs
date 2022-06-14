(ns webchange.dashboard.dashboard-layout.views-app-bar
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [webchange.dashboard.dashboard-layout.views-breadcrumbs :refer [breadcrumbs]]
    [webchange.dashboard.dashboard-layout.utils :refer [get-shift-styles]]
    [webchange.ui-deprecated.theme :refer [get-in-theme]]))

(defn app-bar
  [{:keys [on-open-menu drawer-open? drawer-width]}]
  [ui/app-bar
   {:color    "default"
    :position "static"
    :style    (merge (get-shift-styles drawer-open? drawer-width)
                     {:box-shadow "none"})}
   [ui/toolbar
    {:style {:box-shadow (get-in-theme [:shadows 4])}}
    (when-not drawer-open?
      [ui/icon-button {:color      "inherit"
                       :aria-label "Menu"
                       :on-click   on-open-menu}
       [ic/menu]])
    [breadcrumbs]]])
