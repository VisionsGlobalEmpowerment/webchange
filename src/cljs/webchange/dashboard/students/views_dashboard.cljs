(ns webchange.dashboard.students.views-dashboard
  (:require
    [cljsjs.material-ui]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.dashboard.classes.subs :as classes-subs]
    [webchange.dashboard.students.events :as students-events]
    [webchange.dashboard.students.subs :as students-subs]
    [webchange.dashboard.students.utils :refer [flatten-student]]
    [webchange.dashboard.students.views-common :refer [student-modal]]))

(defn- add-new-student-dashboard-item []
  [:div.students-dashboard-item.add-new {:on-click #(re-frame/dispatch [::students-events/show-add-student-form])}
   [:div.students-dashboard-item_body "+ New"]])

(defn- students-dashboard-item
  [{{:keys [first-name last-name email]} :user :as student}]
  [:div.students-dashboard-item
   [ui/card
    [ui/card-header {:title    (str first-name " " last-name)
                     :subtitle email
                     :avatar   (r/as-element [ui/avatar (get first-name 0)])}]
    [ui/card-actions {:style {:text-align "right"}}
     [ui/icon-menu {:icon-button-element (r/as-element [ui/icon-button (ic/navigation-more-horiz)])
                    :anchor-origin       {:horizontal "left" :vertical "top"}
                    :target-origin       {:horizontal "left" :vertical "top"}}
      [ui/menu-item {:primary-text "Edit"
                     :on-click     #(re-frame/dispatch [::students-events/show-edit-student-form (:id student)])}]
      [ui/menu-item {:primary-text "Remove"
                     :on-click     #(re-frame/dispatch [::students-events/delete-student (:class-id student) (:id student)])}]]]]])

(defn students-dashboard
  []
  (let [class-id @(re-frame/subscribe [::classes-subs/current-class-id])
        _ (when class-id (re-frame/dispatch [::students-events/load-students class-id]))
        students @(re-frame/subscribe [::students-subs/class-students class-id])]
    [ui/card
     [ui/card-header {:title "Students"}]
     [ui/card-media
      [:div.students-dashboard
       (for [student students]
         ^{:key (:id student)}
         [students-dashboard-item student])
       [add-new-student-dashboard-item]]
      [student-modal]]]))
