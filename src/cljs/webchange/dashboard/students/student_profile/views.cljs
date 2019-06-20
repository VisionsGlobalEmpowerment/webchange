(ns webchange.dashboard.students.student-profile.views
  (:require
    [cljsjs.material-ui]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.dashboard.students.subs :as dss]
    [webchange.dashboard.score-table.views :refer [score-table]]
    [webchange.dashboard.students.common.map-students :refer [map-student]]
    [webchange.dashboard.students.student-profile.stubs :refer [scores-stub]]
    [webchange.dashboard.students.student-profile.views-personal-data :refer [personal-data]]
    [webchange.dashboard.students.student-profile.views-scores :refer [student-scores]]))

(defn translate
  [path]
  (get-in {:header "Student Profile"}
          path))

(defn student-profile
  []
  (let [student (map-student @(re-frame/subscribe [::dss/current-student]))]
    [ui/grid {:container true :spacing 24 :style {:padding 20}}
     [ui/grid {:item true :xs 12}
      [ui/typography {:variant "h4"}
       (translate [:header])]]
     [ui/grid {:item true :xs 12}
      [ui/paper
       {:style {:padding 20}}
       [personal-data student]]]
     [ui/grid {:item true :xs 12}
      [ui/paper {:style {:padding 20}}
       [student-scores [{:title  "Cumulative Activity Score"
                         :data   (scores-stub)
                         :legend {:green  "Student completed the activity & scored 95%+"
                                  :yellow "Completed the activity & scored 80-95%"
                                  :red    "Completed the activity & scored 79% or below"}}
                        {:title  "Activity Time Chart"
                         :data   (scores-stub)
                         :legend {:green  "Completed within + or - 15% expected time of completion"
                                  :yellow "Completed 15% later than expected time of completion"
                                  :red    "Completed over 15% later than expected time of completion"}}]]]]]))
