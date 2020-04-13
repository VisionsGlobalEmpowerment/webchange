(ns webchange.dashboard.schools.school-modal.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.dashboard.schools.subs :as schools-subs]
    [webchange.dashboard.students.subs :as students-subs]
    [webchange.dashboard.schools.events :as schools-events]))

(defn- school-form-inputs
  [props]
  [ui/text-field
   {:label         "School Name"
    :default-value (:name @props)
    :on-change     #(swap! props assoc :name (->> % .-target .-value))}])

(defn school-modal
  []
  (let [current-school @(re-frame/subscribe [::schools-subs/current-school])
        school-data (r/atom current-school)
        school-modal-state @(re-frame/subscribe [::schools-subs/school-modal-state])
        handle-save (if (= :edit school-modal-state)
                      (fn [school-data] (re-frame/dispatch [::schools-events/edit-school (:id school-data) school-data]))
                      (fn [school-data] (re-frame/dispatch [::schools-events/add-school school-data])))
        handle-close #(re-frame/dispatch [::schools-events/close-school-modal])
        loading @(re-frame/subscribe [:loading])]
    [ui/dialog
     {:open     (boolean school-modal-state)
      :on-close handle-close}
     [:form
      [ui/dialog-title
       (case school-modal-state
         :add "Add New School"
         :edit "Edit School"
         "")]
      [ui/dialog-content
       (if (:class loading)
         [ui/circular-progress
          {:size  80
           :color "secondary"}]
         [school-form-inputs school-data])]
      [ui/dialog-actions
       [ui/button
        {:on-click handle-close}
        "Cancel"]
       [ui/button
        {:type     "submit"
         :variant  "contained"
         :color    "primary"
         :on-click #(do (.preventDefault %)
                        (handle-save @school-data)
                        (handle-close))}
        "Save"]]]]))

(defn school-delete-modal
  []
  (let [modal-state @(re-frame/subscribe [::schools-subs/delete-modal-state])
        school-id @(re-frame/subscribe [::schools-subs/current-school-id])
        is-loading? false]
    (when modal-state
      [ui/dialog {:open true}
       (if is-loading?
         [ui/linear-progress]
         [:div
          [ui/dialog-title "Are you sure?"]
          [ui/dialog-content
           [ui/dialog-content-text "You are about to delete this school"]
          [ui/dialog-actions
           [ui/button {:on-click #(re-frame/dispatch [::schools-events/close-delete-modal])} "Cancel"]
           [ui/button
            {:variant  "contained"
             :color    "primary"
             :on-click #(re-frame/dispatch [::schools-events/confirm-delete school-id])}
            "Confirm"]]]])
       ])))
