(ns webchange.ui-framework.components.confirm.index
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.button.index :as button-component]
    [webchange.ui-framework.components.dialog.index :as dialog-component]))

(def button button-component/component)
(def dialog dialog-component/component)

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
               [dialog
                {:title    title
                 :on-close handle-cancel
                 :size     "message"
                 :actions  [[button {:on-click handle-cancel
                                     :variant  "outlined"}
                             (or cancel-text "Cancel")]
                            [button {:color    "primary"
                                     :on-click handle-confirm}
                             (or confirm-text "Confirm")]]}
                (when-not (nil? message)
                  [:p message])])]
            (r/children this)))))

(defn with-confirmation
  [{:keys [confirm-text discard-text message on-confirm on-discard title]
    :or   {confirm-text "Confirm"
           discard-text "Cancel"
           message      "Are you sure?"
           on-confirm   #()
           on-discard   #()
           title        "Confirm"}}]
  (let [container (.createElement js/document "div")
        unmount-window (fn []
                         (r/unmount-component-at-node container)
                         (.remove container))
        handle-confirm (fn []
                         (unmount-window)
                         (on-confirm))
        handle-discard (fn []
                         (unmount-window)
                         (on-discard))]
    (.appendChild js/document.body container)
    (r/render
      [dialog
       {:title    title
        :on-close handle-discard
        :size     "message"
        :actions  [[button {:on-click handle-confirm}
                    confirm-text]
                   [button {:variant  "outlined"
                            :on-click handle-discard}
                    discard-text]]}
       (when-not (nil? message)
         message)]
      container)))
