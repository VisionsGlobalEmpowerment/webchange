(ns webchange.dashboard.classes.class-profile.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.dashboard.students.subs :as students-subs]
    [webchange.dashboard.students.common.map-students :refer [map-students-list]]
    [webchange.dashboard.students.events :as students-events]
    [webchange.dashboard.classes.subs :as classes-subs]
    [webchange.dashboard.classes.class-profile.stubs :refer [scores-stub profile-stub]]
    [webchange.dashboard.classes.class-profile.views-profile-table :refer [profile-table]]
    [webchange.dashboard.common.dashboard-page :refer [dashboard-page dashboard-page-block]]
    [webchange.dashboard.score-table.views :refer [score-table]]))

(defn translate
  [path]
  (get-in {:header     "Class Profile"
           :course     {:header "Course"}
           :class      {:header "Class profile"}
           :assessment {:header            "Assessment / Test"
                        :table-title       "Assessment #"
                        :table-items-title "Stud Name"
                        :legend            {:green  "Scored 95+"
                                            :yellow "Scored between 80-95%"
                                            :red    "Scored 79% or below"}}}
          path))

(defn class-profile
  []
  (let [class-id @(re-frame/subscribe [::classes-subs/current-class-id])
        students (->> @(re-frame/subscribe [::students-subs/class-students class-id])
                      (map-students-list))
        profile-data (profile-stub students)
        assessment-data (scores-stub students)
        assessment-levels [80 95]
        course-name "Vera La Vaquita"
        _ (when class-id (re-frame/dispatch [::students-events/load-students class-id]))]
    [dashboard-page
     {:title (translate [:header])}
     [dashboard-page-block
      {:title (translate [:course :header])}
      course-name]
     [dashboard-page-block
      {:title (translate [:class :header])}
      [profile-table
       {:columns (:columns profile-data)}
       (:data profile-data)]]
     [dashboard-page-block
      {:title (translate [:assessment :header])}
      [score-table
       {:title       (translate [:assessment :table-title])
        :items-title (translate [:assessment :table-items-title])
        :levels      assessment-levels
        :legend      (translate [:assessment :legend])}
       assessment-data]]]))
