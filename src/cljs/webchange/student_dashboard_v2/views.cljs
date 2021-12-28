(ns webchange.student-dashboard-v2.views
  (:require
    [webchange.student-dashboard-v2.header.views :refer [header]]
    [webchange.student-dashboard-v2.timeline.views :refer [timeline]]
    [webchange.student-dashboard-v2.toolbar.views :refer [toolbar]]
    [webchange.student-dashboard-v2.user.views :refer [user]]))

(defn- logo
  []
  [:div.logo
   [:img {:src "/images/student_dashboard/tabschool_logo.svg"}]])

(defn student-dashboard
  []
  [:div.student-dashboard
   [header
    [user]
    [logo]
    [toolbar]]
   [:div.content
    [timeline]]])
