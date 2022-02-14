(ns webchange.dashboard.classes.classes-list.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.dashboard.classes.classes-list.state :as state]
    [webchange.dashboard.classes.events :as classes-events]
    [webchange.dashboard.classes.subs :as classes-subs]
    [webchange.dashboard.events :as dashboard-events]
    [webchange.dashboard.common.views :refer [content-page]]
    [webchange.routes :refer [redirect-to]]))

(def fab (r/adapt-react-class (aget js/MaterialUI "Fab")))

(def styles
  {:add-button {:margin   16
                :width    150
                :height   40
                :position "fixed"
                :bottom   20
                :right    20}})


(defn- translate
  [path]
  (get-in {:title     "Classes"
           :actions   {:edit     "Edit"
                       :profile  "Profile"
                       :remove   "Remove"
                       :students "Students"}
           :add-class {:text "Add Class"}}
          path))

(defn- classes-list-item
  [{:keys [id name]}]
  (let [handle-edit-click #(re-frame/dispatch [::state/edit id])
        handle-remove-click #(re-frame/dispatch [::state/remove id])
        handle-profile-click #(re-frame/dispatch [::state/open-profile id])
        handle-students-click #(re-frame/dispatch [::state/open-students id])]
    [ui/table-row {:hover true}
     [ui/table-cell {:on-click handle-profile-click} name]
     [ui/table-cell {:align "right"
                     :style {:white-space "nowrap"}}
      [ui/tooltip
       {:title (translate [:actions :students])}
       [ui/icon-button {:on-click handle-students-click} [ic/people]]]
      [ui/tooltip
       {:title (translate [:actions :profile])}
       [ui/icon-button {:on-click handle-profile-click} [ic/data-usage]]]
      [ui/tooltip
       {:title (translate [:actions :edit])}
       [ui/icon-button {:on-click handle-edit-click} [ic/create]]]
      [ui/tooltip
       {:title (translate [:actions :remove])}
       [ui/icon-button {:on-click handle-remove-click} [ic/delete]]]]]))

(defn- classes-list
  []
  (re-frame/dispatch [::state/init])
  (fn []
    (let [classes @(re-frame/subscribe [::state/classes])]
      [ui/table
       [ui/table-body
        (for [class classes]
          ^{:key (:id class)}
          [classes-list-item class])]])))

(defn classes-list-page
  []
  (let [handle-add-click #(re-frame/dispatch [::state/add])]
    [content-page
     {:title (translate [:title])}
     [:div
      [classes-list]
      [fab
       {:on-click   handle-add-click
        :color      "primary"
        :variant    "extended"
        :style      (:add-button styles)
        :aria-label (translate [:add-class :text])}
       [ic/add]
       (translate [:add-class :text])]]]))
