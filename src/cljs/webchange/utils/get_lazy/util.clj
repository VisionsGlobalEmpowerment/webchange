(ns webchange.utils.get-lazy.util)

(defmacro lazy-component [the-sym]
  `(webchange.utils.get-lazy.util/lazy-component* (shadow.lazy/loadable ~the-sym)))
