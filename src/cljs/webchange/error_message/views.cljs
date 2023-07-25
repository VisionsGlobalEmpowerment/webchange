(ns webchange.error-message.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.error-message.state :as state]
    [webchange.error-message.utils :refer [get-error-title get-error-messages]]
    [webchange.ui.index :as ui]))

(defn- error-message-text
  [{:keys [error]}]
  (let [title (get-error-title error)
        messages (get-error-messages error)]
    [:span
     (when (some? title)
       [:div.error-message--title 
        title])
     (when (some? messages)
       (for [[idx message] (map-indexed vector messages)]
         ^{:key idx}
         [:div.error-message--message
          message]))]))

(defn error-message
  []
  (let [error @(re-frame/subscribe [::state/error-message])
        open? (some? error)
        handle-close #(re-frame/dispatch [::state/reset])]
    (when open?
      (re-frame/dispatch [::state/schedule-reset])
      [ui/dialog {:title    "Error"
                  :class-name "error-message--dialog"
                  :on-close handle-close}
       [error-message-text {:error error}]])))
