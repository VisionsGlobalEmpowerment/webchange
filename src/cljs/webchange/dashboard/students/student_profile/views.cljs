(ns webchange.dashboard.students.student-profile.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
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
  (get-in {:header            "Student Profile"
           :cumulative-scores {:title  "Cumulative Activity Score"
                               :legend {:green  "Student completed the activity & scored 95%+"
                                        :yellow "Completed the activity & scored 80-95%"
                                        :red    "Completed the activity & scored 79% or below"}}
           :activity-times    {:title  "Activity Time Chart"
                               :legend {:green  "Completed within + or - 15% expected time of completion"
                                        :yellow "Completed 15% later than expected time of completion"
                                        :red    "Completed over 15% later than expected time of completion"}}}
          path))

(defn- ->data [levels]
  {:title       "Activity"
   :items-title "Lesson"
   :marks       [80 95]
   :levels      levels})

(defn student-profile
  [{:keys [student profile]}]
  (let [student (map-student student)]
    [content-page
     {:title (translate [:header])
      :current-title (:name student)}
     [personal-data student]
     [student-scores [{:title  (translate [:cumulative-scores :title])
                       :data   (-> profile :scores ->data)
                       :legend (translate [:cumulative-scores :legend])}
                      {:title  (translate [:activity-times :title])
                       :data   (-> profile :times ->data)
                       :legend (translate [:activity-times :legend])}]]]))

(defn student-profile-page
  []
  (let [student @(re-frame/subscribe [::dss/current-student])
        profile @(re-frame/subscribe [::dss/student-profile])
        is-loading? (or
                      @(re-frame/subscribe [::dss/student-loading])
                      @(re-frame/subscribe [::dss/student-profile-loading]))]
    (if is-loading?
      [ui/linear-progress]
      [student-profile
       {:student student
        :profile profile}])))
