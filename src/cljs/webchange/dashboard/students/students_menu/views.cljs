(ns webchange.dashboard.students.students-menu.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [webchange.dashboard.classes.subs :as classes-subs]
    [webchange.dashboard.students.common.map-students :refer [map-students-list]]
    [webchange.dashboard.students.events :as students-events]
    [webchange.dashboard.students.subs :as students-subs]
    [webchange.routes :refer [redirect-to]]))

(defn- translate
  [path]
  (get-in {:title      "Students"
           :add-button "Add Student"}
          path))

(defn- students-menu-item
  [{:keys [on-click]}
   {:keys [name] :as class}]
  [ui/menu-item
   {:on-click #(on-click class)}
   name])

(defn students-menu
  []
  (let [class-id @(re-frame/subscribe [::classes-subs/current-class-id])
        students (->> @(re-frame/subscribe [::students-subs/class-students class-id])
                      map-students-list)
        _ (when class-id (re-frame/dispatch [::students-events/load-students class-id]))]
    [ui/expansion-panel
     {:disabled (->> class-id boolean not)}
     [ui/expansion-panel-summary
      [ui/typography {:variant "h6"}
       (translate [:title])]]
     [ui/expansion-panel-details
      [ui/menu-list
       {:style {:padding 0
                :width   "100%"}}
       (for [student students]
         ^{:key (:id student)}
         [students-menu-item
          {:on-click #(redirect-to :dashboard-student-profile :class-id class-id :student-id (:id %))}
          student])
       ]]
     [ui/expansion-panel-actions
      [ui/button
       {:color    "secondary"
        :on-click #(re-frame/dispatch [::students-events/show-add-student-form])}
       [ic/add]
       (translate [:add-button])]]]))
