(ns webchange.dashboard.classes.remove-form.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.dashboard.classes.remove-form.state :as state]))

(defn- loading-form
  []
  [ui/dialog-content
   "Loading class info.."
   [ui/linear-progress]])

(defn- confirm-form
  []
  (let [has-students? @(re-frame/subscribe [::state/has-students?])]
    [ui/dialog-content
     [ui/dialog-content-text "You are about to delete this class"]
     (when has-students?
       [ui/dialog-content-text
        "This class contains students. "
        [:b "Please, delete students first."]])]))

(defn class-delete-modal
  []
  (let [open? @(re-frame/subscribe [::state/window-open?])
        loading? @(re-frame/subscribe [::state/loading?])
        confirm-button-enabled? @(re-frame/subscribe [::state/confirm-button-enabled?])

        handle-cancel-click #(re-frame/dispatch [::state/cancel])
        handle-remove-click #(re-frame/dispatch [::state/remove-class])]
    [ui/dialog {:open open?}
     [ui/dialog-title "Remove class"]
     (if loading?
       [loading-form]
       [confirm-form])
     [ui/dialog-actions
      [ui/button {:on-click handle-cancel-click}
       "Cancel"]
      [ui/button
       {:variant  "contained"
        :color    "primary"
        :disabled (not confirm-button-enabled?)
        :on-click handle-remove-click}
       "Confirm"]]]))
