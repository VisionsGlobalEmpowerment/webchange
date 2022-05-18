(ns webchange.admin.pages.student-profile.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.student-profile.state :as state]
    [webchange.admin.components.list.views :as l]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui-framework.components.index :as c]))

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
  (let [{student-name :name} @(re-frame/subscribe [::state/student])
        {:keys [started-at last-login activity-progress cumulative-time]} @(re-frame/subscribe [::state/course-stats])]
    [page/header {:title   student-name
                  :icon    "school"}
     [:p (str "Program start" started-at)]
     [:p (str "Latest login at" last-login)]
     [:p (str "Activities completed" activity-progress)]
     [:p (str "Total played time" cumulative-time)]]))

(defn- activities-row
  [activities]
  [:div
   (for [{:keys [id name completed? last-played total-time]} activities]
     ^{:key id}
     [:div
      [:p (str "Name: " name)]
      [:p (str "Completed? " completed?)]
      [:p last-played]
      [:p total-time]])])

(defn- list-item
  [{:keys [activities] :as props}]
  [l/list-item props
   [activities-row activities]])

(defn- content
  []
  (let [{class-name :name} @(re-frame/subscribe [::state/class])
        {course-name :name} @(re-frame/subscribe [::state/course])
        lessons-data @(re-frame/subscribe [::state/lessons-data])]
    [page/main-content {:title "Student Profile"}
     [:div
      [:p class-name]
      [:p course-name]
      [level-picker]]
     [l/list {:class-name "lessons"}
      (for [{:keys [id] :as lesson-data} lessons-data]
        ^{:key id}
        [list-item lesson-data])]]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    [page/page {:class-name "page--student-profile"}
     [header]
     [content]]))
