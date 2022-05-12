(ns webchange.validation.specs.student
  (:require
    [clojure.spec.alpha :as s]
    [webchange.validation.predicates :as p]))

(s/def ::class-id number?)
(s/def ::first-name (s/and string? p/not-empty?))
(s/def ::last-name (s/or :empty p/not-defined?
                         :valid string?))
(s/def ::gender (s/or :empty p/not-defined?
                      :valid #{1 2}))
(s/def ::date-of-birth (s/or :empty p/not-defined?
                             :valid (s/and string? p/date-string?)))
(s/def ::access-code (s/and string? p/not-empty?))

(s/def ::student (s/keys :req-un [::class-id
                                  ::first-name
                                  ::access-code]
                         :opt-un [::last-name
                                  ::gender
                                  ::date-of-birth]))

(s/def ::create-student (s/keys :req-un [::first-name
                                         ::access-code]
                                :opt-un [::class-id
                                         ::last-name
                                         ::gender
                                         ::date-of-birth]))
