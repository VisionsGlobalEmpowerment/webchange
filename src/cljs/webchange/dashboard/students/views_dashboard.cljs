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
    [webchange.dashboard.students.dashboard-students-list.utils :refer [map-students-list]]
    [webchange.dashboard.students.dashboard-students-list.views :refer [students-list]]
    [webchange.dashboard.students.views-common :refer [student-modal]]))

(defn- add-new-student-dashboard-item []
  [:div.students-dashboard-item.add-new
   {:on-click #(re-frame/dispatch [::students-events/show-add-student-form])}
   [:div.students-dashboard-item_body "+ New"]])

(defn students-dashboard
  []
  (let [class-id @(re-frame/subscribe [::classes-subs/current-class-id])
        _ (when class-id (re-frame/dispatch [::students-events/load-students class-id]))
        students (map-students-list @(re-frame/subscribe [::students-subs/class-students class-id]))
        ]
    [ui/card
     [ui/card-header {:title "Students"}]
     [ui/card-content
      [students-list
       {:on-edit-click   (fn [{:keys [id]}] (re-frame/dispatch [::students-events/show-edit-student-form id]))
        :on-remove-click (fn [{:keys [id class-id]}] (re-frame/dispatch [::students-events/delete-student class-id id]))}
       students]
      [student-modal]]]))
