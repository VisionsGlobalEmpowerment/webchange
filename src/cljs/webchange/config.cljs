(ns webchange.config)

(def debug?
  ^boolean goog.DEBUG)

(def use-cache (or true (not debug?)))
