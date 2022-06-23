(ns webchange.ui.pages.headers.views
  (:require
    [webchange.admin.widgets.page.header.views :refer [header]]
    [webchange.ui.pages.layout :refer [layout]]))

(defn page
  []
  [:div#page--headers
   [layout {:title "Headers"}
    [:div.headers-list
     [header {:title    "Schools"
              :icon     "school"
              :on-close #(print "Close")}]
     [header {:title   "Schools"
              :icon    "school"
              :actions [{:text     "Add"
                         :icon     "plus"
                         :on-click #(print "Add")}]}]
     [header {:title   "Schools"
              :icon    "school"
              :actions [{:text     "Archive"
                         :icon     "archive"
                         :on-click #(print "Archive")}
                        {:text     "Add"
                         :icon     "plus"
                         :on-click #(print "Add")}]}]
     [header {:title   "Schools"
              :icon    "school"
              :stats   [{:icon    "teachers"
                         :counter 18
                         :label   "Teachers"}
                        {:icon    "classes"
                         :counter 12
                         :label   "Classes"}]
              :actions [{:text     "Add"
                         :icon     "plus"
                         :on-click #(print "Add")}]}]]]])
