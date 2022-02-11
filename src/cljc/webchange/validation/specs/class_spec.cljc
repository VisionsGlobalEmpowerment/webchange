(ns webchange.validation.specs.class-spec
  (:require
    [clojure.spec.alpha :as s]
    [webchange.validation.predicates :as p]))

(s/def ::name p/not-empty-string?)
(s/def ::course-id number?)

(s/def ::class (s/keys :req-un [::name
                                ::course-id]
                       :opt []))
