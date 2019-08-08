(ns webchange.validation.phrases
  (:require
    [phrase.alpha :refer [defphraser]]
    [webchange.validation.predicates :as p]))

(defphraser :default
            [_ problem]
            (println "Unhandled validation predicate" (:pred problem))
            "Invalid value")

(defphraser #(contains? % ::gender)
            [_ _]
            "Required field")

(defphraser p/date-string?
            [_ _]
            "Date must have format \"dd-mm-yyyy\" ")

(defphraser p/not-empty?
            [_ _]
            "Required field")

(defphraser string?
            [_ _]
            "Must be a string")

(defphraser number?
            [_ _]
            "Must be a number")
