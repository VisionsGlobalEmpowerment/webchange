(ns webchange.book-library.layout.icons.index
  (:require
    [webchange.book-library.layout.icons.book :as book]
    [webchange.book-library.layout.icons.home :as home]
    [webchange.book-library.layout.icons.heart :as heart]
    [webchange.book-library.layout.icons.search :as search]))

(def icons {"book"   book/data
            "home"   home/data
            "heart"  heart/data
            "search" search/data})
