(ns webchange.editor-v2.components.confirm.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [reagent.core :as r]))

(defn with-confirmation
  [{:keys [title message confirm-text cancel-text on-confirm on-cancel]}]
  (r/with-let [confirm-open? (r/atom false)]
              (let [this (r/current-component)
                    handle-open #(reset! confirm-open? true)
                    handle-close #(reset! confirm-open? false)
                    handle-confirm #(do (.stopPropagation %)
                                        (handle-close)
                                        (when-not (nil? on-confirm)
                                          (on-confirm)))
                    handle-cancel #(do (.stopPropagation %)
                                       (handle-close)
                                       (when-not (nil? on-cancel)
                                         (on-cancel)))]
                (into [:div {:on-click handle-open}
                       [ui/dialog {:open     @confirm-open?
                                   :on-close handle-close}
                        (when-not (nil? title)
                          [ui/dialog-title title])
                        (when-not (nil? message)
                          [ui/dialog-content
                           [ui/typography {:variant "body1"}
                            message]])
                        [ui/dialog-actions
                         [ui/button {:on-click handle-cancel}
                          (or cancel-text "Cancel")]
                         [ui/button {:color    "secondary"
                                     :variant  "contained"
                                     :on-click handle-confirm}
                          (or confirm-text "Confirm")]]]]
                      (r/children this)))))
