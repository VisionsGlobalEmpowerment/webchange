(ns webchange.book-library.components.error-message.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.i18n.translate :as i18n]))

(defn error-message
  []
  [:div.error-message
   @(re-frame/subscribe [::i18n/t [:error-general]])])
