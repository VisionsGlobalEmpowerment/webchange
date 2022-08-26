(ns webchange.student-dashboard.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.student-dashboard.state :as state]
    [webchange.student-dashboard.header.views :refer [header]]
    [webchange.student-dashboard.timeline.views :refer [timeline]]
    [webchange.student-dashboard.toolbar.views :refer [toolbar]]
    [webchange.student-dashboard.user.views :refer [user]]))

(defn- logo
  []
  [:div.header-logo
   [:img {:src "/images/student_dashboard/tabschool_logo.svg"}]])

(defn student-dashboard
  [{:keys [id]}]
  (re-frame/dispatch [::state/init {:course-id id}])
  (fn []
    [:div {:class-name "student-dashboard"}
     [header {:class-name "student-dashboard--header"}
      [user]
      [logo]
      [toolbar]]
     [:div {:class-name "student-dashboard--content"}
      [timeline]]]))

(def views
  {:student-course-dashboard student-dashboard})
