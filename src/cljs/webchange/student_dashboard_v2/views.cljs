(ns webchange.student-dashboard-v2.views
  (:require
    [webchange.student-dashboard-v2.timeline.views :refer [timeline]]
    [webchange.student-dashboard-v2.toolbar.views :refer [toolbar]]
    [webchange.student-dashboard-v2.user.views :refer [user]]))

(defn- logo
  []
  [:div.logo
   [:img {:src "/images/student_dashboard/tabschool_logo.png"}]])

(defn student-dashboard
  []
  [:div.student-dashboard
   [:div.header
    [user]
    [logo]
    [toolbar]]
   [:div.content
    [timeline]]])
