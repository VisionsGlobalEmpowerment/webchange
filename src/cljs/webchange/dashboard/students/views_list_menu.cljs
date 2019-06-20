(ns webchange.dashboard.students.views-list-menu
  (:require
    [cljsjs.material-ui]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.dashboard.classes.subs :as classes-subs]
    [webchange.dashboard.students.events :as students-events]
    [webchange.dashboard.students.subs :as students-subs]))

(defn- students-list-menu-item
  [{:keys [user]}]
  (let [{:keys [first-name last-name]} user]
    [ui/list-item
     [ui/list-item-avatar
      [ui/avatar
       (get first-name 0)]]
     [ui/list-item-text
      {:inset   true
       :primary (str first-name " " last-name)}]]))

(defn students-list-menu
  []
  (let [class-id @(re-frame/subscribe [::classes-subs/current-class-id])
        _ (when class-id (re-frame/dispatch [::students-events/load-students class-id]))
        students @(re-frame/subscribe [::students-subs/class-students class-id])]
    [ui/list
     [ui/list-item
      "Students"]
     (for [student students]
       ^{:key (:id student)}
       [students-list-menu-item student])
     (if class-id
       [ui/list-item
        {:button   true
         :on-click #(re-frame/dispatch [::students-events/show-add-student-form])}
        [ui/list-item-icon [ic/add]]
        [ui/list-item-text
         {:inset   true
          :primary "Add"}]]
       [ui/list-item
        [ui/chip
         {:label "Select Class"}]])]))
