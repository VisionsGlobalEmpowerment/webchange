(ns webchange.dashboard.students.student-modal.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.ui.theme :refer [w-colors]]
    [webchange.dashboard.students.events :as students-events]
    [webchange.dashboard.students.subs :as students-subs]
    [webchange.utils.map :refer [remove-nil-fields]]))

(defn student-remove-from-class-modal
  []
  (let [modal-state @(re-frame/subscribe [::students-subs/remove-from-class-modal-state])
        is-loading? @(re-frame/subscribe [::students-subs/student-loading])
        {{first-name :first-name last-name :last-name} :user student-id :id class-id :class-id} @(re-frame/subscribe [::students-subs/current-student])]
    (when modal-state
      [ui/dialog {:open true}
       (if is-loading?
         [ui/linear-progress]
         [:div
          [ui/dialog-title "Are you sure?"]
          [ui/dialog-content
           [ui/dialog-content-text (str "You are about to remove " first-name " " last-name " from its class")]]
          [ui/dialog-actions
           [ui/button {:on-click #(re-frame/dispatch [::students-events/close-remove-from-class-modal])} "Cancel"]
           [ui/button
            {:variant  "contained"
             :color    "primary"
             :on-click #(re-frame/dispatch [::students-events/confirm-remove class-id student-id])}
            "Confirm"]]])])))

(defn student-delete-modal
  []
  (let [modal-state @(re-frame/subscribe [::students-subs/delete-modal-state])
        is-loading? @(re-frame/subscribe [::students-subs/student-loading])
        {{first-name :first-name last-name :last-name} :user student-id :id} @(re-frame/subscribe [::students-subs/current-student])]
    (when modal-state
      [ui/dialog {:open true}
       (if is-loading?
         [ui/linear-progress]
         [:div
          [ui/dialog-title "Are you sure?"]
          [ui/dialog-content
           [ui/dialog-content-text (str "You are about to delete " first-name " " last-name)]]
          [ui/dialog-actions
           [ui/button {:on-click #(re-frame/dispatch [::students-events/close-remove-from-class-modal])} "Cancel"]
           [ui/button
            {:variant  "contained"
             :color    "primary"
             :on-click #(re-frame/dispatch [::students-events/confirm-delete student-id])}
            "Confirm"]]])])))

(defn student-complete-modal
  []
  (r/with-let [data (r/atom {})]
    (let [modal-state @(re-frame/subscribe [::students-subs/complete-modal-state])
          is-loading? @(re-frame/subscribe [::students-subs/student-loading])
          current-student @(re-frame/subscribe [::students-subs/current-student])
          {{first-name :first-name
            last-name  :last-name}    :user
           {course-slug :course-slug} :class
           student-id                 :id} current-student]
      (when modal-state
        [ui/dialog {:open true}
         (if is-loading?
           [ui/linear-progress]
           [:div
            [ui/dialog-title "Are you sure?"]
            [ui/dialog-content
             [ui/dialog-content-text (str "You are about to complete progress for " first-name " " last-name)]
             [ui/text-field
              {:label       "Level to complete"
               :type        "text"
               :helper-text "Leave blank to complete all levels"
               :on-change   #(swap! data assoc :level (-> % .-target .-value js/parseInt))}]
             [ui/text-field
              {:label       "Lesson to complete"
               :type        "text"
               :helper-text "Leave blank to complete all lessons"
               :on-change   #(swap! data assoc :lesson (-> % .-target .-value js/parseInt))}]
             [ui/text-field
              {:label       "Activity to complete"
               :type        "text"
               :helper-text "Leave blank to complete all activities"
               :on-change   #(swap! data assoc :activity (-> % .-target .-value js/parseInt))}]
             [ui/checkbox {:label         "Navigation"
                           :variant       "outlined"
                           :default-value false
                           :on-change     #(swap! data assoc :navigation (-> % .-target .-checked))}]]
            [ui/dialog-actions
             [ui/button {:on-click #(re-frame/dispatch [::students-events/close-complete-modal])} "Cancel"]
             [ui/button
              {:variant  "contained"
               :color    "primary"
               :on-click #(re-frame/dispatch [::students-events/confirm-complete student-id course-slug @data])}
              "Confirm"]]])]))))
