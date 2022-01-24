(ns webchange.validation.specs.parent-student
  (:require
    [clojure.spec.alpha :as s]
    [webchange.validation.predicates :as p]))

(s/def ::name (s/and string? p/not-empty?))
(s/def ::age (s/and number?))

(s/def ::parent-student (s/keys :req-un [::name ::age]
                                :opt []))
