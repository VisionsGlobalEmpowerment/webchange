(ns webchange.editor-v2.components.confirm-dialog.views
  (:require
    [cljs-react-material-ui.reagent :as ui]))

(defn confirm-dialog
  [{:keys [open? on-confirm on-cancel title description cancel-text save-text]}]
  (let [call-safely #(when-not (nil? %) (%))
        handle-cancel #(do (call-safely on-cancel)
                           (reset! open? false))
        handle-confirm #(do (call-safely on-confirm)
                            (reset! open? false))]
    [ui/dialog {:open @open?}
     [ui/dialog-title (or title "Confirm your action")]
     (when-not (nil? description)
       [ui/dialog-content
        [ui/typography {:variant "body1"} description]])
     [ui/dialog-actions
      [ui/button {:color    "primary"
                  :on-click handle-cancel}
       (or cancel-text "Cancel")]
      [ui/button {:color    "secondary"
                  :variant  "contained"
                  :on-click handle-confirm}
       (or save-text "Confirm")]]]))
