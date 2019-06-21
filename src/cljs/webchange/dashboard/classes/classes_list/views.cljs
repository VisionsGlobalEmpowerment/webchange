(ns webchange.dashboard.classes.classes-list.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.dashboard.classes.events :as classes-events]
    [webchange.dashboard.classes.subs :as classes-subs]
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
  [{:keys [on-edit-click on-profile-click on-remove-click on-students-click]}
   {:keys [name] :as class}]
  [ui/table-row {:hover true}
   [ui/table-cell name]
   [ui/table-cell {:align "right"
                   :style {:white-space "nowrap"}}
    [ui/tooltip
     {:title (translate [:actions :students])}
     [ui/icon-button {:on-click #(on-students-click class)} [ic/people]]]
    [ui/tooltip
     {:title (translate [:actions :profile])}
     [ui/icon-button {:on-click #(on-profile-click class)} [ic/data-usage]]]
    [ui/tooltip
     {:title (translate [:actions :edit])}
     [ui/icon-button {:on-click #(on-edit-click class)} [ic/create]]]
    [ui/tooltip
     {:title (translate [:actions :remove])}
     [ui/icon-button {:on-click #(on-remove-click class)} [ic/delete]]]]])

(defn- classes-list
  [props classes]
  [ui/table
   [ui/table-body
    (for [class classes]
      ^{:key (:id class)}
      [classes-list-item props class])]])

(defn classes-list-page
  []
  (r/with-let [_ (re-frame/dispatch [::classes-events/load-classes])]
              (let [classes @(re-frame/subscribe [::classes-subs/classes-list])]
                [content-page
                 {:title (translate [:title])}
                 [:div
                  [classes-list
                   {:on-edit-click     (fn [{:keys [id]}] (re-frame/dispatch [::classes-events/show-edit-class-form id]))
                    :on-remove-click   (fn [{:keys [id]}] (re-frame/dispatch [::classes-events/delete-class id]))
                    :on-profile-click  #(redirect-to :dashboard-class-profile :class-id (:id %))
                    :on-students-click #(redirect-to :dashboard-students :class-id (:id %))}
                   classes]
                  [fab
                   {:on-click   #(re-frame/dispatch [::classes-events/show-add-class-form])
                    :color      "primary"
                    :variant    "extended"
                    :style      (:add-button styles)
                    :aria-label (translate [:add-class :text])}
                   [ic/add]
                   (translate [:add-class :text])]]])))
