(ns webchange.editor-v2.course-table.views-edit-menu
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.course-table.state.edit :as edit-state]))

(defn edit-menu
  [{:keys [on-save]}]
  (let [this (r/current-component)
        open? @(re-frame/subscribe [::edit-state/open?])
        title @(re-frame/subscribe [::edit-state/title])
        handle-close #(re-frame/dispatch [::edit-state/close-menu])
        handle-save #(do (handle-close)
                         (on-save))]
    [ui/dialog {:open     open?
                :on-close handle-close}
     [ui/dialog-title title]
     (into [ui/dialog-content]
           (r/children this))
     [ui/dialog-actions
      [ui/button {:color    "primary"
                  :on-click handle-close}
       "Cancel"]
      [ui/button {:color    "secondary"
                  :variant  "contained"
                  :on-click handle-save}
       "Save"]]]))
