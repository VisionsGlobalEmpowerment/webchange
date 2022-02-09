(ns webchange.dashboard.classes.class-modal.views
  (:require
   [cljs-react-material-ui.reagent :as ui]
   [re-frame.core :as re-frame]
   [webchange.dashboard.classes.subs :as classes-subs]
   [webchange.dashboard.students.subs :as students-subs]
   [webchange.dashboard.classes.events :as classes-events]))

(defn class-delete-modal
  []
  (let [modal-state @(re-frame/subscribe [::classes-subs/delete-modal-state])
        class-id @(re-frame/subscribe [::classes-subs/current-class-id])
        students @(re-frame/subscribe [::students-subs/class-students class-id])
        is-loading? @(re-frame/subscribe [::students-subs/students-loading class-id])]
    (when modal-state
      [ui/dialog {:open true}
       (if is-loading?
         [ui/linear-progress]
         [:div
          [ui/dialog-title "Are you sure?"]
          [ui/dialog-content
           [ui/dialog-content-text "You are about to delete this class"]
           (when (not-empty students)
             [ui/dialog-content-text "This class contains students. Please, delete students first."])]
          [ui/dialog-actions
           [ui/button {:on-click #(re-frame/dispatch [::classes-events/close-delete-modal])} "Cancel"]
           [ui/button
            {:variant  "contained"
             :color    "primary"
             :disabled (boolean (not-empty students))
             :on-click #(re-frame/dispatch [::classes-events/confirm-delete class-id])}
            "Confirm"]]])])))
