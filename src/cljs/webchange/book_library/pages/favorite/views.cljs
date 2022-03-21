(ns webchange.book-library.pages.favorite.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.book-library.layout.views :refer [layout]]
    [webchange.i18n.translate :as i18n]))

(defn page
  []
  [layout {:title          @(re-frame/subscribe [::i18n/t [:favorite-books]])
           :document-title @(re-frame/subscribe [::i18n/t [:favorite]])}
   [:div]])
