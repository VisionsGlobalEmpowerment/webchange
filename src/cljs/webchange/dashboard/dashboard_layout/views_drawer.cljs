(ns webchange.dashboard.dashboard-layout.views-drawer
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [webchange.ui.theme :refer [get-in-theme w-colors]]
    [webchange.ui.with-styles :refer [with-styles use-class]]))

(def styles {:drawer        {:width       300
                             :flex-shrink 0}
             :drawer_paper  {:width 300}
             :drawer_header {:alignItems       "center"
                             :background-color (:primary w-colors)
                             :display          "flex"
                             :height           64
                             :justify-content  "flex-end"
                             :padding          "0 8px"}})

;; ToDo: resolve problem with props camel-casing ({:on-click onClose})

(defn- drawer-styled
  [{:keys [open classes children onClose]}]
  [ui/drawer {:anchor     "left"
              :variant    "persistent"
              :class-name (use-class classes "drawer")
              :classes    {"paper" (use-class classes "drawer_paper")}
              :open       open}
   [ui/app-bar
    {:color    "default"
     :position "static"}
    [ui/toolbar
     [:div {:style {:flex-grow 1}}]
     [ui/icon-button {:on-click onClose}
      [ic/chevron-left]]]]
   [ui/divider]
   children])

(defn drawer
  [props children]
  [(with-styles drawer-styled styles) props children])
