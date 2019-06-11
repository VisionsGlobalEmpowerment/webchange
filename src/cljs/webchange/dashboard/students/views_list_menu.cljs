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
    [ui/list-item {:primary-text (str first-name " " last-name)
                   :left-avatar  (r/as-element [ui/avatar (get first-name 0)])}]))

(defn students-list-menu
  []
  (let [class-id @(re-frame/subscribe [::classes-subs/current-class-id])
        _ (when class-id (re-frame/dispatch [::students-events/load-students class-id]))
        students @(re-frame/subscribe [::students-subs/class-students class-id])]
    [ui/list
     [ui/list-item {:primary-text "Students"
                    :disabled     true}]
     (for [student students]
       ^{:key (:id student)}
       [students-list-menu-item student])
     (if class-id
       [ui/list-item {:primary-text "Add"
                      :left-icon    (ic/content-add)
                      :on-click     #(re-frame/dispatch [::students-events/show-add-student-form])}]
       [ui/list-item {:disabled       true
                      :inset-children true} [ui/chip "Select Class"]])]))
