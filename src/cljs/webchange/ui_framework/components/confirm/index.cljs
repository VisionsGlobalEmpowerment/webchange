(ns webchange.ui-framework.components.confirm.index
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.button.index :as button-component]
    [webchange.ui-framework.components.dialog.index :as dialog-component]))

(def button button-component/component)

(defn component
  [{:keys [title message confirm-text cancel-text on-confirm on-cancel]
    :or   {title "Confirm"}}]
  (r/with-let [confirm-open? (r/atom false)]
    (let [this (r/current-component)
          handle-open #(reset! confirm-open? true)
          handle-close #(reset! confirm-open? false)
          handle-confirm #(do (handle-close)
                              (when-not (nil? on-confirm)
                                (on-confirm)))
          handle-cancel #(do (handle-close)
                             (when-not (nil? on-cancel)
                               (on-cancel)))]
      (into [:div {:on-click handle-open}
             (when @confirm-open?
               [dialog-component/component
                {:title    title
                 :on-close handle-cancel
                 :size     "message"
                 :actions  [:div.confirm-actions
                            [button {:on-click handle-cancel
                                     :variant  "outlined"}
                             (or cancel-text "Cancel")]
                            [button {:color    "primary"
                                     :on-click handle-confirm}
                             (or confirm-text "Confirm")]]}
                (when-not (nil? message)
                  [:p message])])]
            (r/children this)))))
