(ns webchange.config)

(def log-level :debug)

(def debug?
  ^boolean goog.DEBUG)

(def use-cache (not debug?))

(defn api-url
  [& parts]
  (let [api-url (if debug? "//localhost:3000/api" "/api")]
    (->> (concat [api-url] parts)
         (apply str))))
