(ns webchange.config)

(def log-level :debug)

(def debug?
  ^boolean goog.DEBUG)

(def use-cache (not debug?))
