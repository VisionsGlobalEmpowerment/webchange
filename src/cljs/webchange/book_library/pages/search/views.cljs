(ns webchange.book-library.pages.search.views
  (:require
    [webchange.book-library.layout.views :refer [layout]]))

(defn page
  []
  [layout {:title          "Search Book Library"
           :document-title "Search"}
   [:div]])
