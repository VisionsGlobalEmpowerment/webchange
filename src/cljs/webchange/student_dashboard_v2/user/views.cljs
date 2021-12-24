(ns webchange.student-dashboard-v2.user.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.auth.subs :as as]))

(defn- avatar
  []
  [:div.avatar
   [:img {:src "/images/student_dashboard/avatar.png"}]
   [:div.background]])

(defn user
  []
  (let [{:keys [first-name]} @(re-frame/subscribe [::as/user])]
    [:div.user
     [avatar]
     [:div.name first-name]]))
