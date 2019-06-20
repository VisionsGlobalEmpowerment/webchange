(ns webchange.dashboard.views-drawer
  (:require
    [cljsjs.material-ui]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [webchange.ui.with-styles :refer [with-styles use-class]]))

(def styles {:drawer        {:width       300
                             :flex-shrink 0}
             :drawer_paper  {:width 300}
             :drawer_header {:display        "flex"
                             :alignItems     "center"
                             :padding        "0 8px"
                             :height         64
                             :justifyContent "flex-end"
                             }})

;; ToDo: resolve problem with props camel-casing ({:on-click onClose})

(defn drawer-styled
  [{:keys [open classes children onClose]}]
  [ui/drawer {:anchor     "left"
              :variant    "persistent"
              :class-name (use-class classes "drawer")
              :classes    {"paper" (use-class classes "drawer_paper")}
              :open       open}
   [:div {:class-name (use-class classes "drawer_header")}
    [ui/icon-button {:on-click onClose}
     [ic/chevron-left]]]
   [ui/divider]
   children])

(defn drawer
  [props children]
  [(with-styles drawer-styled styles) props children])
