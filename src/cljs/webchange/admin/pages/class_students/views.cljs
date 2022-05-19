(ns webchange.admin.pages.class-students.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.class-students.state :as state]
    [webchange.admin.components.list.views :as l]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui-framework.components.index :as c]))


(defn- lesson-picker
  []
  (let [current-lesson @(re-frame/subscribe [::state/current-lesson])
        lesson-options @(re-frame/subscribe [::state/lesson-options])
        on-change #(re-frame/dispatch [::state/select-lesson %])]
    [c/select {:value      current-lesson
               :options    lesson-options
               :type "int"
               :on-change  on-change}]))

(defn- level-picker
  []
  (let [current-level @(re-frame/subscribe [::state/current-level])
        level-options @(re-frame/subscribe [::state/level-options])
        on-change #(re-frame/dispatch [::state/select-level %])]
    [c/select {:value      current-level
               :options    level-options
               :type "int"
               :on-change  on-change}]))

(defn- header
  []
  (let [{class-name :name stats :stats} @(re-frame/subscribe [::state/class])
        {course-name :name} @(re-frame/subscribe [::state/course])
        handle-add-click #(re-frame/dispatch [::state/add-student])]
    [page/header {:title   class-name
                  :icon    "school"
                  :actions [c/icon-button {:icon     "add"
                                           :on-click handle-add-click}
                            "Add Student to Class"]}
     [:p (str (:students stats) " Students")]
     [:p course-name]
     [lesson-picker]
     [level-picker]]))

(defn- activities-row
  [activities]
  [:div
   (for [{:keys [id completed? last-played total-time]} activities]
     ^{:key id}
     [:div
      [:p (str completed?)]
      [:p last-played]
      [:p total-time]])])

(defn- list-item
  [{:keys [id name code activities] :as props}]
  [l/list-item (merge props {:on-click #(re-frame/dispatch [::state/open-student id])})
   [:p id]
   [:p code]
   [activities-row activities]])

(defn- content
  []
  (let [students-data @(re-frame/subscribe [::state/students-data])
        lesson-data @(re-frame/subscribe [::state/lesson-data])]
    [page/main-content {:title "Students"}
     [:div
      [:p (:name lesson-data)]
      [:div
       (for [{:keys [id name preview]} (:activities lesson-data)]
         ^{:key id}
         [:p (str name " " preview)])]]
     [l/list {:class-name "students-list"}
      (for [{:keys [id] :as student-data} students-data]
        ^{:key id}
        [list-item student-data])]]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    [page/page {:class-name "page--class-students"}
     [header]
     [content]]))
