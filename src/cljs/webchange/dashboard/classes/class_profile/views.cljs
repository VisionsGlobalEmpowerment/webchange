(ns webchange.dashboard.classes.class-profile.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.dashboard.common.views :refer [content-page content-page-section score-table]]
    [webchange.dashboard.classes.subs :as classes-subs]
    [webchange.dashboard.classes.class-profile.stubs :refer [scores-stub profile-stub]]
    [webchange.dashboard.classes.class-profile.views-profile-table :refer [profile-table]]
    [webchange.dashboard.students.common.map-students :refer [map-students-list]]
    [webchange.dashboard.students.events :as students-events]
    [webchange.dashboard.students.subs :as students-subs]))

(defn- translate
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

(defn- class-profile
  [{:keys [students]}]
  (let [assessment-data (scores-stub students)
        assessment-levels [80 95]
        course-name "Vera La Vaquita"]
    [content-page
     {:title (translate [:header])}
     [content-page-section
      {:title (translate [:course :header])}
      course-name]
     [content-page-section
      {:title (translate [:class :header])}
      [profile-table]]
     #_[content-page-section
      {:title (translate [:assessment :header])}
      [score-table
       {:title       (translate [:assessment :table-title])
        :items-title (translate [:assessment :table-items-title])
        :levels      assessment-levels
        :legend      (translate [:assessment :legend])}
       assessment-data]
      ]]))

(defn class-profile-page
  []
  (let [class-id @(re-frame/subscribe [::classes-subs/current-class-id])
        students @(re-frame/subscribe [::students-subs/class-students class-id])
        _ (when class-id (re-frame/dispatch [::students-events/load-students class-id]))
        is-loading? @(re-frame/subscribe [::students-subs/students-loading class-id])]
    (if is-loading?
      [ui/linear-progress]
      [class-profile
       {:students (map-students-list students)}])))
