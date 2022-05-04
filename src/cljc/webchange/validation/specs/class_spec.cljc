(ns webchange.validation.specs.class-spec
  (:require
    [clojure.spec.alpha :as s]
    [webchange.validation.predicates :as p]))

(s/def ::id number?)
(s/def ::name p/not-empty-string?)
(s/def ::course-id number?)

(s/def ::create-class (s/keys :req-un [::name
                                       ::course-id]
                              :opt-un []))

(s/def ::class (s/keys :req-un [::name
                                ::course-id]
                       :opt-un []))
