(ns webchange.student-dashboard.views
  (:require
    [webchange.student-dashboard.header.views :refer [header]]
    [webchange.student-dashboard.timeline.views :refer [timeline]]
    [webchange.student-dashboard.toolbar.views :refer [toolbar]]
    [webchange.student-dashboard.user.views :refer [user]]))

(defn- logo
  []
  [:div.header-logo
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
