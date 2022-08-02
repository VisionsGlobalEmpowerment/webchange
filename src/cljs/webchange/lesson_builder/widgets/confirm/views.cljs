(ns webchange.lesson-builder.widgets.confirm.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.widgets.confirm.state :as state]
    [webchange.ui.index :as ui]))

(defn- confirm-window
  [props]
  (let [{:keys [open? message title]} @(re-frame/subscribe [::state/confirm-window])
        handle-confirm #(re-frame/dispatch [::state/handle-confirm-window])
        handle-cancel #(re-frame/dispatch [::state/close-confirm-window])]
    [ui/confirm (merge props
                       (cond-> {:open?      open?
                                :on-confirm handle-confirm
                                :on-cancel  handle-cancel}
                               (and (some? title)
                                    (some? message)) (assoc :title title)))
     (if (some? message)
       message
       [:h1 title])]))

(defn- message-window
  [props]
  (let [{:keys [open? message title]} @(re-frame/subscribe [::state/message-window])
        handle-confirm #(re-frame/dispatch [::state/close-message-window])]
    [ui/confirm (merge props
                       (cond-> {:open?        open?
                                :confirm-text "Ok"
                                :on-confirm   handle-confirm}
                               (some? title) (assoc :title title)))
     message]))

(defn block-confirm
  []
  (let [props {:class-name-content "widget--confirm-content"
               :class-name-overlay "widget--confirm-overlay"}]
    [:<>
     [confirm-window props]
     [message-window props]]))
