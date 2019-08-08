(ns webchange.validation.specs.student
  (:require
    [clojure.spec.alpha :as s]
    [webchange.validation.predicates :as p]))

(s/def ::class-id number?)
(s/def ::gender #{1 2})
(s/def ::first-name (s/and string? p/not-empty?))
(s/def ::last-name (s/and string? p/not-empty?))
(s/def ::date-of-birth (s/and string? p/date-string?))
(s/def ::access-code (s/and string? p/not-empty?))

(s/def ::student (s/keys :req-un [::class-id
                                  ::gender
                                  ::first-name
                                  ::last-name
                                  ::date-of-birth
                                  ::access-code]
                         :opt []))
