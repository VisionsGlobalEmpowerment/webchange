(ns webchange.book-library.pages.search.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.book-library.layout.views :refer [layout]]
    [webchange.i18n.translate :as i18n]))

(defn page
  []
  [layout {:title          @(re-frame/subscribe [::i18n/t [:search-book-library]])
           :document-title @(re-frame/subscribe [::i18n/t [:search]])}
   [:div]])
