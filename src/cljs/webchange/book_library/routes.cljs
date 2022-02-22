(ns webchange.book-library.routes)

(def routes {[""]          :book-library
             ["/favorite"] :book-library-favorite
             ["/search"]   :book-library-search})
