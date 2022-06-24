(ns webchange.ui.pages.lists.views
  (:require
    [reagent.core :as r]
    [webchange.ui.index :as ui]
    [webchange.ui.pages.layout :refer [layout panel]]))

(defn page
  []
  [:div#page--lists
   [layout {:title "Lists"}
    [panel {:class-name "lists-panel"}
     [ui/list
      [ui/list-item {:name    "School Name"
                     :stats   [{:counter  68
                                :icon     "students"
                                :text     "Students"
                                :on-click #(print "Handle Students click")}
                               {:counter  7
                                :icon     "teachers"
                                :text     "Teachers"
                                :on-click #(print "Handle Teachers click")}
                               {:counter  12
                                :icon     "classes"
                                :text     "Classes"
                                :on-click #(print "Handle Classes click")}
                               {:counter  4
                                :icon     "courses"
                                :text     "Courses"
                                :on-click #(print "Handle Courses click")}]
                     :actions [{:icon     "edit"
                                :title    "Edit school"
                                :on-click #(print "Handle Edit click")}]}]
      (r/with-let [checked? (r/atom true)
                   handle-change #(swap! checked? not)]
        [ui/list-item {:avatar   nil
                       :name     "Teacher Name"
                       :info     [{:key   "Email"
                                   :value "user@email.com"}
                                  {:key   "Last Login"
                                   :value "user@email.com"}]
                       :controls [ui/switch {:label     (if @checked? "Active" "Inactive")
                                             :checked?  @checked?
                                             :on-change handle-change}]}])]]]])
