(ns webchange.book-library.pages.library.views
  (:require
    [webchange.book-library.layout.views :refer [layout]]))

(defn page
  []
  [layout {:title "Book Library"}
   [:div]])
