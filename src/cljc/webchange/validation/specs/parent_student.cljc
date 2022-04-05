(ns webchange.validation.specs.parent-student
  (:require
    [clojure.spec.alpha :as s]
    [webchange.validation.predicates :as p]))

(s/def ::name (s/and string? p/not-empty?))
(s/def ::course-id number?)
(s/def ::date-of-birth p/date-string?)

(s/def ::parent-student (s/keys :req-un [::name ::course-id ::date-of-birth]
                                :opt []))
