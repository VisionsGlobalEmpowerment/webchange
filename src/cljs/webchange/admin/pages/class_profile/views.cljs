(ns webchange.admin.pages.class-profile.views
  (:require
    [webchange.admin.widgets.counter.views :refer [counter]]
    [webchange.admin.widgets.no-data.views :refer [no-data]]
    [webchange.admin.widgets.profile.views :as profile]))

(defn- class-counter
  []
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
                              :icon  "add"}}]}])

(defn page
  [props]
  [profile/page
   [profile/main-content {:title "Class Profile"}
    [class-counter]
    [profile/block {:title "Statistics"}
     [no-data]]
    [profile/block {:title "Class Info"}
     [no-data]]]
   [profile/side-bar
    [no-data]]])
