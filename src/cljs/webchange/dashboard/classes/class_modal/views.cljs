(ns webchange.dashboard.classes.class-modal.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.dashboard.classes.subs :as classes-subs]
    [webchange.dashboard.students.subs :as students-subs]
    [webchange.dashboard.classes.events :as classes-events]))

(defn- class-form-inputs
  [props]
  [ui/text-field
   {:label         "Class Name"
    :default-value (:name @props)
    :on-change     #(swap! props assoc :name (->> % .-target .-value))}])

(defn class-modal
  []
  (let [current-class @(re-frame/subscribe [::classes-subs/current-class])
        class-data (r/atom current-class)
        class-modal-state @(re-frame/subscribe [::classes-subs/class-modal-state])
        handle-save (if (= :edit class-modal-state)
                      (fn [class-data] (re-frame/dispatch [::classes-events/edit-class (:id class-data) class-data]))
                      (fn [class-data] (re-frame/dispatch [::classes-events/add-class class-data])))
        handle-close #(re-frame/dispatch [::classes-events/close-class-modal])
        loading @(re-frame/subscribe [:loading])]
    [ui/dialog
     {:open     (boolean class-modal-state)
      :on-close handle-close}
     [:form
      [ui/dialog-title
       (case class-modal-state
         :add "Add New Class"
         :edit "Edit Class"
         "")]
      [ui/dialog-content
       (if (:class loading)
         [ui/circular-progress
          {:size  80
           :color "secondary"}]
         [class-form-inputs class-data])]
      [ui/dialog-actions
       [ui/button
        {:on-click handle-close}
        "Cancel"]
       [ui/button
        {:type     "submit"
         :variant  "contained"
         :color    "primary"
         :on-click #(do (handle-save @class-data)
                        (handle-close))}
        "Save"]]]]))

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
            "Confirm"]]])
       ])))
