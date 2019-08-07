(ns webchange.dashboard.classes.class-modal.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.dashboard.classes.subs :as dcs]
    [webchange.dashboard.classes.events :as dce]))

(defn- class-form-inputs
  [props]
  [ui/text-field
   {:label         "Class Name"
    :default-value (:name @props)
    :on-change     #(swap! props assoc :name (->> % .-target .-value))}])

(defn class-modal
  []
  (let [current-class @(re-frame/subscribe [::dcs/current-class])
        class-data (r/atom current-class)
        class-modal-state @(re-frame/subscribe [::dcs/class-modal-state])
        handle-save (if (= :edit class-modal-state)
                      (fn [class-data] (re-frame/dispatch [::dce/edit-class (:id class-data) class-data]))
                      (fn [class-data] (re-frame/dispatch [::dce/add-class class-data])))
        handle-close #(re-frame/dispatch [::dce/close-class-modal])
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
