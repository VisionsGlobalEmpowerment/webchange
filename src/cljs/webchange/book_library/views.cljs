(ns webchange.book-library.views
  (:require
    [webchange.book-library.pages.favorite.views :as favorite]
    [webchange.book-library.pages.library.views :as library]
    [webchange.book-library.pages.search.views :as search]))

(def views
  {:book-library          library/page
   :book-library-favorite favorite/page
   :book-library-search   search/page})
