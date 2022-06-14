(ns webchange.error-message.views
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.error-message.state :as state]
    [webchange.error-message.utils :refer [get-error-title get-error-messages]]
    [webchange.ui-deprecated.theme :refer [get-in-theme]]))

(defn- get-styles
  []
  {:error-message      {:background-color (get-in-theme [:palette :error :dark])}
   :error-message-type {:font-weight "bold"}
   :error-message-text {}
   :close-button-icon  {:color     "#323232"
                        :font-size "16px"}})

(defn- error-message-text
  [{:keys [error]}]
  (let [title (get-error-title error)
        messages (get-error-messages error)
        styles (get-styles)]
    [:span
     (when (some? title)
       [:div {:style (:error-message-type styles)}
        title])
     (when (some? messages)
       (for [[idx message] (map-indexed vector messages)]
         ^{:key idx}
         [:div {:style (:error-message-text styles)}
          message]))]))

(defn- close-button
  [{:keys [on-click]}]
  (let [styles (get-styles)]
    [ui/icon-button {:on-click on-click}
     [ic/close {:style (:close-button-icon styles)}]]))

(defn error-message
  []
  (let [error @(re-frame/subscribe [::state/error-message])
        open? (some? error)
        handle-close #(re-frame/dispatch [::state/reset])
        styles (get-styles)]
    [ui/snackbar {:anchor-origin      {:vertical   "top"
                                       :horizontal "right"}
                  :open               open?
                  :auto-hide-duration 20000
                  :on-close           handle-close}
     [ui/snackbar-content {:style   (:error-message styles)
                           :message (r/as-element [error-message-text {:error error}])
                           :action  (r/as-element [close-button {:on-click handle-close}])}]]))
