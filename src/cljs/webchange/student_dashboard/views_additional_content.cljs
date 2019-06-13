(ns webchange.student-dashboard.views-additional-content
  (:require
    [webchange.student-dashboard.views-life-skill-list :refer [life-skill-list]]
    [webchange.student-dashboard.views-related-content-list :refer [related-content-list]]))

(def additional-content-styles
  {:background-color "#f7f7f7"
   :display          "flex"
   :flex-direction   "column"
   :flex             "0 0 auto"
   :padding          "50px"
   :width            410})

(defn additional-content
  [{:keys [related-content on-related-content-click
           life-skills on-life-skill-click]}]
  [:div {:style additional-content-styles}
   [related-content-list related-content {:on-click on-related-content-click}]
   [life-skill-list life-skills {:on-click on-life-skill-click}]])
