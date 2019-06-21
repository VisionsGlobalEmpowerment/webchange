(ns webchange.dashboard.students.student-profile.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.dashboard.common.views :refer [content-page score-table]]
    [webchange.dashboard.students.subs :as dss]
    [webchange.dashboard.students.events :as students-events]
    [webchange.dashboard.students.common.map-students :refer [map-student]]
    [webchange.dashboard.students.student-profile.stubs :refer [scores-stub]]
    [webchange.dashboard.students.student-profile.views-personal-data :refer [personal-data]]
    [webchange.dashboard.students.student-profile.views-scores :refer [student-scores]]))

(defn translate
  [path]
  (get-in {:header "Student Profile"
           :cumulative-scores {:title "Cumulative Activity Score"
                               :legend {:green  "Student completed the activity & scored 95%+"
                                        :yellow "Completed the activity & scored 80-95%"
                                        :red    "Completed the activity & scored 79% or below"}}
           :activity-scores {:title "Activity Time Chart"
                             :legend {:green  "Completed within + or - 15% expected time of completion"
                                      :yellow "Completed 15% later than expected time of completion"
                                      :red    "Completed over 15% later than expected time of completion"}}}
          path))

(defn student-profile
  [{:keys [student]}]
  (let [student (map-student student)]
    [content-page
     {:title (translate [:header])}
     [personal-data student]
     [student-scores [{:title  (translate [:cumulative-scores :title])
                       :data   (scores-stub)
                       :legend (translate [:cumulative-scores :legend])}
                      {:title  (translate [:activity-scores :title])
                       :data   (scores-stub)
                       :legend (translate [:activity-scores :legend])}]]]))

(defn student-profile-page
  []
  (let [student-id @(re-frame/subscribe [::dss/current-student-id])
        student @(re-frame/subscribe [::dss/current-student])
        _ (when student-id (re-frame/dispatch [::students-events/load-student student-id]))]
    [student-profile
     {:student student}]))
