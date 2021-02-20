(ns webchange.editor-v2.components.confirm-dialog.views
  (:require
    [cljs-react-material-ui.reagent :as ui]))

(defn confirm-dialog
  [{:keys [open? on-confirm on-reject title description confirm-text reject-text]
    :or   {confirm-text "Confirm"
           reject-text  "Cancel"
           on-confirm   #()
           on-reject    #()}}]
  (let [handle-cancel #(do (on-reject)
                           (reset! open? false))
        handle-confirm #(do (on-confirm)
                            (reset! open? false))]
    [ui/dialog {:open @open?}
     [ui/dialog-title (or title "Confirm your action")]
     (when-not (nil? description)
       [ui/dialog-content
        [ui/typography {:variant "body1"} description]])
     [ui/dialog-actions
      [ui/button {:on-click handle-cancel}
       reject-text]
      [ui/button {:color    "secondary"
                  :variant  "contained"
                  :on-click handle-confirm}
       confirm-text]]]))
