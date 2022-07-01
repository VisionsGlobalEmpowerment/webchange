(ns webchange.ui.pages.headers.views
  (:require
    [webchange.admin.widgets.page.header.views :refer [header]]
    [webchange.ui.index :as ui]
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
                         :on-click #(print "Add")}]}]
     [header {:title    "Class"
              :icon     "classes"
              :stats    [{:icon    "students"
                          :counter 48
                          :label   "Students"}]
              :info     [{:key   "Course Name"
                          :value "English 01"}]
              :controls [[ui/select {:label   "Level"
                                     :options (->> (range 9)
                                                   (map inc)
                                                   (map (fn [i]
                                                          {:text  (str "Level 0" i)
                                                           :value (str "level-0" i)})))}]
                         [ui/select {:label   "Lesson"
                                     :options (->> (range 9)
                                                   (map inc)
                                                   (map (fn [i]
                                                          {:text  (str "Lesson 0" i)
                                                           :value (str "lesson-0" i)})))}]]
              :actions  [{:text     "Add Student"
                          :icon     "plus"
                          :on-click #(print "Add")}]}]]]])
