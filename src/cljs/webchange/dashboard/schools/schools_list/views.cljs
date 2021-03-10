(ns webchange.dashboard.schools.schools-list.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.dashboard.schools.events :as schools-events]
    [webchange.dashboard.schools.subs :as schools-subs]
    [webchange.dashboard.events :as dashboard-events]
    [webchange.dashboard.common.views :refer [content-page]]))

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
  (get-in {:title     "Schools"
           :actions   {:edit     "Edit"
                       :profile  "Profile"
                       :remove   "Remove"
                       :students "Students"}
           :add-school {:text "Add School"}}
          path))

(defn- schools-list-item
  [{:keys [on-edit-click  on-remove-click on-sync-click]}
   {:keys [name] :as school}]
  [ui/table-row {:hover true}
   [ui/table-cell {} name]
   [ui/table-cell {:align "right"
                   :style {:white-space "nowrap"}}
    [ui/tooltip
     {:title (translate [:actions :edit])}
     [ui/icon-button {:on-click #(on-edit-click school)} [ic/create]]]
    [ui/tooltip
     {:title (translate [:actions :sync])}
     [ui/icon-button {:on-click #(on-sync-click school)} [ic/refresh]]]
    [ui/tooltip
     {:title (translate [:actions :remove])}
     [ui/icon-button {:on-click #(on-remove-click school)} [ic/delete]]]]])

(defn- schools-list
  [props schools]
  [ui/table
   [ui/table-body
    (for [school schools]
      ^{:key (:id school)}
      [schools-list-item props school])]])

(defn schools-list-page
  []
  (let [schools @(re-frame/subscribe [::schools-subs/schools-list])
        is-loading? @(re-frame/subscribe [::schools-subs/schools-loading])]
    (if is-loading?
      [ui/linear-progress]
      [content-page {:title (translate [:title])}
       [:div
        [schools-list
         {:on-edit-click     (fn [{:keys [id]}] (re-frame/dispatch [::schools-events/show-edit-school-form id]))
          :on-remove-click   (fn [{:keys [id]}] (re-frame/dispatch [::dashboard-events/show-delete-school-form id]))
          :on-sync-click     (fn [{:keys [id]}] (re-frame/dispatch [::schools-events/show-sync-school-form id]))}
         schools]
        [fab {:on-click   #(re-frame/dispatch [::schools-events/show-add-school-form])
              :color      "primary"
              :variant    "extended"
              :style      (:add-button styles)
              :aria-label (translate [:add-school :text])}
         [ic/add]
         (translate [:add-school :text])]]
       [ui/button {:on-click #(re-frame/dispatch [::schools-events/software-update])} "Software update"]])))
