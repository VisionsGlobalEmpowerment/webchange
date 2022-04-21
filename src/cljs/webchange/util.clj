(ns webchange.util)

(defmacro lazy-component [the-sym]
  `(webchange.util/lazy-component* (shadow.lazy/loadable ~the-sym)))