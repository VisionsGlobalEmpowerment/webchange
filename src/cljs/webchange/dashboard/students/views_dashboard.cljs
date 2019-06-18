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
    [webchange.dashboard.students.students-list.utils :refer [map-students-list filter-students-list map-classes-list]]
    [webchange.dashboard.students.students-list.views :refer [students-list]]
    [webchange.dashboard.students.students-list-filter.views :refer [students-list-filter]]
    [webchange.dashboard.students.views-common :refer [student-modal]]))

(def fab (r/adapt-react-class (aget js/MaterialUI "Fab")))

(defn translate
  [path]
  (get-in {:title "Students"
           :add-student {:text "Add Student"}}
          path))

(defn students-dashboard
  []
  (r/with-let [filter (r/atom {:class-id nil})]
              (fn []
                (let [class-id @(re-frame/subscribe [::classes-subs/current-class-id])
                      classes (map-classes-list @(re-frame/subscribe [::classes-subs/classes-list]))
                      students (->> @(re-frame/subscribe [::students-subs/class-students class-id])
                                    (map-students-list)
                                    (filter-students-list @filter))
                      _ (when class-id (re-frame/dispatch [::students-events/load-students class-id]))]
                  [ui/card {:style {:display        "flex"
                                    :flex-direction "column"
                                    :height         "100%"}}
                   [ui/card-header {:title (translate [:title])
                                    :style {:flex "0 0 auto"}}]
                   [ui/card-content {:style {:display        "flex"
                                             :flex           "1 1 auto"
                                             :flex-direction "column"}}
                    [students-list-filter
                     {:classes classes
                      :style   {:flex "0 0 auto"}}
                     filter]
                    [students-list
                     {:on-edit-click   (fn [{:keys [id]}] (re-frame/dispatch [::students-events/show-edit-student-form id]))
                      :on-remove-click (fn [{:keys [id class-id]}] (re-frame/dispatch [::students-events/delete-student class-id id]))
                      :style           {:flex       "1 1 auto"
                                        :height     "100%"
                                        :overflow-y "auto"
                                        :padding-bottom 60}}
                     students]

                    [fab
                     {:on-click #(re-frame/dispatch [::students-events/show-add-student-form])
                      :color      "primary"
                      :variant    "extended"
                      :style      {:margin   16
                                   :width    150
                                   :height   40
                                   :position "fixed"
                                   :bottom   20
                                   :right    20}
                      :aria-label (translate [:add-student :text])}
                     [ic/add]
                     (translate [:add-student :text])]
                    [student-modal]]]))))
