(ns webchange.dashboard.students.student-profile.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [webchange.dashboard.common.views :refer [content-page score-table]]
    [webchange.dashboard.classes.subs :as classes-subs]
    [webchange.dashboard.students.subs :as dss]
    [webchange.dashboard.students.events :as students-events]
    [webchange.dashboard.students.common.map-students :refer [map-student]]
    [webchange.dashboard.students.student-profile.stubs :refer [scores-stub]]
    [webchange.dashboard.students.student-profile.views-personal-data :refer [personal-data]]
    [webchange.dashboard.students.student-profile.views-scores :refer [student-scores]]
    [webchange.dashboard.students.student-modal.views :as student-modal-views]
    [webchange.routes :refer [redirect-to]]))

(defn translate
  [path]
  (get-in {:header            "Student Profile"
           :actions           {:edit   "Edit"
                               :remove "Remove"
                               :class  "Class"
                               :complete "Complete"}
           :cumulative-scores {:title  "Cumulative Activity Score"
                               :legend {:gray   "Activity was started but not finished"
                                        :green  "Student completed the activity & scored 95%+"
                                        :yellow "Completed the activity & scored 80-95%"
                                        :red    "Completed the activity & scored 79% or below"}}
           :activity-times    {:title  "Activity Time Chart"
                               :legend {:gray   "Activity was started but not finished"
                                        :green  "Completed within + or - 15% expected time of completion"
                                        :yellow "Completed 15% later than expected time of completion"
                                        :red    "Completed over 15% later than expected time of completion"}}}
          path))

(defn- ->data [levels]
  {:title       "Activity"
   :items-title "Lesson"
   :marks       [80 95]
   :levels      levels})

(defn- actions
  [{:keys [student on-edit-click on-remove-click on-class-click on-complete-click]}]
  [:div
   [ui/tooltip
    {:title (translate [:actions :class])}
    [ui/icon-button {:on-click #(on-class-click student)} [ic/assignment]]]
   [ui/tooltip
    {:title (translate [:actions :edit])}
    [ui/icon-button {:on-click #(on-edit-click student)} [ic/create]]]
   [ui/tooltip
    {:title (translate [:actions :remove])}
    [ui/icon-button {:on-click #(on-remove-click student)} [ic/delete]]]
   [ui/tooltip
    {:title (translate [:actions :complete])}
    [ui/icon-button {:on-click #(on-complete-click student)} [ic/check]]]])

(defn student-profile
  [{:keys [student profile class-id]}]
  (let [student (map-student student)]
    [content-page
     {:title         (translate [:header])
      :current-title (:name student)
      :actions       (actions {:student         student
                               :on-edit-click   (fn [{:keys [id]}] (re-frame/dispatch [::students-events/show-edit-student-form id]))
                               :on-remove-click (fn [{:keys [id]}] (re-frame/dispatch [::students-events/show-remove-from-class-form id]))
                               :on-class-click  (fn [] (redirect-to :dashboard-class-profile :class-id class-id))
                               :on-complete-click (fn [{:keys [id]}] (re-frame/dispatch [::students-events/show-complete-form id]))})}
     [personal-data student]
     [student-scores [{:title  (translate [:cumulative-scores :title])
                       :data   (-> profile :scores ->data)
                       :legend (translate [:cumulative-scores :legend])}
                      {:title  (translate [:activity-times :title])
                       :data   (-> profile :times ->data)
                       :legend (translate [:activity-times :legend])}]]
     [student-modal-views/student-complete-modal]]))

(defn student-profile-page
  []
  (let [student @(re-frame/subscribe [::dss/current-student])
        profile @(re-frame/subscribe [::dss/student-profile])
        class-id @(re-frame/subscribe [::classes-subs/current-class-id])
        is-loading? (or @(re-frame/subscribe [::dss/student-loading])
                        @(re-frame/subscribe [::dss/student-profile-loading]))
        student-removed? (and (nil? student) (not is-loading?))]
    (cond
      student-removed? (do (redirect-to :dashboard-students :class-id class-id)
                           [:span "Redirect.."])
      is-loading? [ui/linear-progress]
      :else [student-profile
             {:student  student
              :profile  profile
              :class-id class-id}])))
