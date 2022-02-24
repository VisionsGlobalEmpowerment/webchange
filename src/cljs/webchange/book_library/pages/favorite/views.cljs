(ns webchange.book-library.pages.favorite.views
  (:require
    [webchange.book-library.layout.views :refer [layout]]))

(defn page
  []
  [layout {:title          "My Favorite books"
           :document-title "Favorite"}
   [:div]])
