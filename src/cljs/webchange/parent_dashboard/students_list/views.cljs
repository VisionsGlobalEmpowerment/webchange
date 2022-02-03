(ns webchange.parent-dashboard.students-list.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.parent-dashboard.layout.views :refer [layout]]
    [webchange.parent-dashboard.students-list.state :as state]
    [webchange.parent-dashboard.ui.index :refer [button circular-progress dialog icon-button]]))

(defn- student-card
  [{:keys [name level lesson id img finished]
    :or   {img "/images/parent_dashboard/user_logo.svg"}}]
  (let [handle-play-click #(re-frame/dispatch [::state/play-as-student id])
        handle-delete-click #(re-frame/dispatch [::state/open-confirm-delete-student id])]
    [:div.student-card
     [:div.top-side
      [:img {:src img}]
      [icon-button {:class-name "delete-button"
                    :icon       "remove"
                    :title      "Delete student"
                    :on-click   handle-delete-click}]]
     [:div.bottom-side
      [:div.name name]
      [:div.progress
       [:b "Progress: "]
       (if finished
         "Finished"
         (str "Level " level " - Lesson " lesson))]
      [button {:class-name "play-button"
               :variant    "play-button"
               :on-click   handle-play-click}
       "Play"]]]))

(defn- confirm-delete-window
  []
  (let [open? @(re-frame/subscribe [::state/window-open?])
        loading? @(re-frame/subscribe [::state/loading?])
        handle-confirm #(re-frame/dispatch [::state/delete-student])
        handle-close #(re-frame/dispatch [::state/close-window])]
    [dialog {:open?         open?
             :on-close      handle-close
             :close-button? false
             :content-align "center"
             :width         600
             :actions       (if-not loading?
                              [[button {:on-click handle-confirm
                                        :variant  "outlined"
                                        :color    "default"}
                                "Yes"]
                               [button {:on-click handle-close
                                        :variant  "outlined"
                                        :color    "default"}
                                "No"]]
                              [[circular-progress]])}
     [:p "Are you sure you want to delete this student?"]
     [:p "This will delete student progress."]
     [:p "Student will be permanently deleted."]]))

(defn- students-list
  []
  (re-frame/dispatch [::state/load-students])
  (fn []
    (let [loading? @(re-frame/subscribe [::state/students-loading?])
          students @(re-frame/subscribe [::state/students-list])]
      [:div.students-list
       (if-not loading?
         (for [{:keys [id] :as student} students]
           ^{:key id}
           [student-card student])
         [circular-progress])
       [confirm-delete-window]])))

(defn students-list-page
  []
  (let [handle-add-click #(re-frame/dispatch [::state/open-add-form])]
    [layout {:title   "Current students"
             :actions [[button {:on-click handle-add-click
                                :color    "primary"
                                :variant  "contained"}
                        "+ Add a student"]]}
     [students-list]]))
