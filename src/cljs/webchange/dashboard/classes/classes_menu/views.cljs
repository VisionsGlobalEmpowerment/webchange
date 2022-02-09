(ns webchange.dashboard.classes.classes-menu.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [webchange.dashboard.classes.classes-menu.state :as state]
    [webchange.dashboard.classes.events :as classes-events]
    [webchange.routes :refer [redirect-to]]))

(defn- translate
  [path]
  (get-in {:title      "Classes"
           :add-button "Add Class"}
          path))

(defn- classes-menu-item
  [{:keys [on-click]}
   {:keys [name] :as class}]
  [ui/menu-item
   {:on-click #(on-click class)}
   name])

(defn classes-menu
  []
  (re-frame/dispatch [::state/init])
  (fn []
    (let [classes @(re-frame/subscribe [::state/classes])]
    [ui/expansion-panel {:default-expanded true}
     [ui/expansion-panel-summary
      [ui/typography {:variant "h6"}
       (translate [:title])]]
     [ui/expansion-panel-details
      [ui/menu-list
       {:style {:padding 0
                :width   "100%"}}
       (for [class classes]
         ^{:key (:id class)}
         [classes-menu-item
          {:on-click #(redirect-to :dashboard-class-profile :class-id (:id %))}
          class])]]
     [ui/expansion-panel-actions
      [ui/button
       {:color    "secondary"
        :on-click #(re-frame/dispatch [::classes-events/show-add-class-form])}
       [ic/add]
       (translate [:add-button])]]])))
