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
                          :on-click #(print "Add")}]}]
     [header {:title      "User Name"
              :icon       "accounts"
              :icon-color "green-2"
              :on-close   #(print "Close")
              :info       [{:key   "Account Created"
                            :value "04/12/2022"}
                           {:key   "Last Login"
                            :value "04/27/2022"}]}]

     [header {:avatar ""
              :title  "Account Holder"
              :info   [{:key   "Program Start Date"
                        :value "02/23/2022"
                        :icon  "school"}
                       {:key   "Last Login Date"
                        :value "02/27/2022"
                        :icon  "students"}
                       {:key        "Activities Completed"
                        :value      "5"
                        :icon       "games"
                        :icon-color "blue-2"}
                       {:key        "Total Played Time"
                        :value      "1h 23m 34s"
                        :icon       "play"
                        :icon-color "blue-2"}]}]]]])
