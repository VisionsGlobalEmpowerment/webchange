(ns webchange.admin.pages.class-profile.views
  (:require
    [webchange.admin.widgets.counter.views :refer [counter]]
    [webchange.admin.widgets.profile.views :refer [profile]]))

(defn page
  [props]
  (print props)
  [profile {}
   [:h1 "Class Profile"]
   [counter {:items [{:id     :teachers
                      :value  0
                      :title  "Teachers"
                      :action {:title "Teachers"
                               :icon  "add"}}
                     {:id     :students
                      :value  0
                      :title  "Students"
                      :action {:title "Students"
                               :icon  "add"}}
                     {:id     :events
                      :value  0
                      :title  "Events"
                      :action {:title "Events"
                               :icon  "add"}}]}]])
