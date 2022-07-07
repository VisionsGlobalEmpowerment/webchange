(ns webchange.parent.pages.students.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.parent.pages.students.state :as state]
    [webchange.ui.index :as ui]))

(defn- remove-student-window
  []
  (let [{:keys [done? open? in-progress? student-id]} @(re-frame/subscribe [::state/remove-window-state])
        remove #(re-frame/dispatch [::state/remove-student student-id])
        close-window #(re-frame/dispatch [::state/close-remove-window])
        confirm-removed #(do (re-frame/dispatch [::state/close-remove-window])
                             (re-frame/dispatch [::state/load-students]))]
    [ui/confirm {:open?        open?
                 :loading?     in-progress?
                 :confirm-text (if done? "Ok" "Yes")
                 :cancel-text  "No"
                 :on-confirm   (if done? confirm-removed remove)
                 :on-cancel    (when-not done? close-window)}
     (if-not done?
       [:<>
        [:p "Are you sure you want to delete this student?"]
        [:p "This will delete student progress."]
        [:p "Student will be permanently deleted."]]
       "Student account successfully deleted")]))

(defn- students-list-item
  [{:keys [name level lesson id img finished]
    :or   {img "/images/parent_dashboard/student.svg"}}]
  (let [loading? @(re-frame/subscribe [::state/login-as-student?])
        handle-play-click #(re-frame/dispatch [::state/play-as-student id])
        handle-remove-click #(re-frame/dispatch [::state/open-remove-window id])]
    [:div.students-card
     [:div.students-card--top-side
      [:img {:src img}]
      [ui/button {:title      "Delete student"
                  :class-name "students-card--remove-button"
                  :icon       "trash"
                  :color      "blue-1"
                  :on-click   handle-remove-click}]]
     [:div.students-card--bottom-side
      [:div.students-card--name name]
      [:div.students-card--progress
       [:b "Progress: "]
       (if finished
         "Finished"
         (str "Level " level " - Lesson " lesson))]
      [ui/button {:class-name "students-card--play-button"
                  :on-click   handle-play-click}
       (if-not loading?
         [:span "Play"]
         [ui/circular-progress])]]]))

(defn- students-list
  []
  (let [loading? @(re-frame/subscribe [::state/students-loading])
        students @(re-frame/subscribe [::state/students])]
    [:div.students-list
     (if-not loading?
       (for [{:keys [id] :as student} students]
         ^{:key id}
         [students-list-item student])
       [ui/loading-overlay])]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    [:div#page--students
     [:h1 "Current Students"]
     [students-list]
     [remove-student-window]]))
