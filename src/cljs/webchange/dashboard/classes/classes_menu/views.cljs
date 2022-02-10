(ns webchange.dashboard.classes.classes-menu.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [webchange.dashboard.classes.classes-menu.state :as state]))

(defn- translate
  [path]
  (get-in {:title      "Classes"
           :add-button "Add Class"}
          path))

(defn- classes-menu-item
  [{:keys [id name]}]
  (let [handle-click #(re-frame/dispatch [::state/open-class-profile id])]
    [ui/menu-item
     {:on-click handle-click}
     name]))

(defn classes-menu
  []
  (re-frame/dispatch [::state/init])
  (fn []
    (let [classes @(re-frame/subscribe [::state/classes])
          handle-add-click #(re-frame/dispatch [::state/add-class])]
      [ui/expansion-panel {:default-expanded true}
       [ui/expansion-panel-summary
        [ui/typography {:variant "h6"}
         (translate [:title])]]
       [ui/expansion-panel-details
        [ui/menu-list
         {:style {:padding 0
                  :width   "100%"}}
         (for [{:keys [id] :as class} classes]
           ^{:key id}
           [classes-menu-item class])]]
       [ui/expansion-panel-actions
        [ui/button
         {:color    "secondary"
          :on-click handle-add-click}
         [ic/add]
         (translate [:add-button])]]])))
