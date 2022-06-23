(ns webchange.ui.components.form.form-action.views
  (:require
    [webchange.ui.components.button.views :refer [button]]
    [webchange.ui.components.input-label.views :refer [input-label]]))

(defn form-action
  [{:keys [icon label on-click text value]
    :or   {text "Action"}}]
  (let [handle-button-click #(when (fn? on-click)
                               (on-click value))]
    [button {:shape      "rounded"
             :class-name "bbs--form-action"
             :color      "transparent"
             :icon       icon
             :text-align "left"
             :on-click   handle-button-click}
     [input-label label]]))