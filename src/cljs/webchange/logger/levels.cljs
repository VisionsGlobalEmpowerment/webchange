(ns webchange.logger.levels
  (:require
    [webchange.utils.list :refer [in-list?]]))

(def current-level (atom :trace))

(def levels (atom {:trace 300
                   :log   400
                   :warn  500
                   :error 600}))

(def synonyms {:trace [:group :group-folded :group-end]})

(defn- check-synonyms
  [method]
  (or (some (fn [[base synonyms]]
              (and (in-list? synonyms method)
                   base))
            synonyms)
      method))

(defn- get-method-level
  [method]
  (let [method (check-synonyms method)]
    (if (contains? @levels method)
      (get @levels method)
      (get @levels :log))))

(defn- dev-mode? [] js/goog.DEBUG)

(defn- allowed-level?
  [method]
  (>= (get-method-level method)
      (get-method-level @current-level)))

(defn allowed?
  [method]
  (and (dev-mode?)
       (allowed-level? method)))
