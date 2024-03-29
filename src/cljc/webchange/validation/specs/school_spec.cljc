(ns webchange.validation.specs.school-spec
  (:require
    [clojure.spec.alpha :as s]
    [webchange.validation.predicates :as p]))

(s/def ::id int?)
(s/def ::name p/not-empty-string?)
(s/def ::location p/not-empty-string?)
(s/def ::about p/not-empty-string?)

(s/def :existing-school/location (s/or :string string?
                                       :nil nil?))
(s/def :existing-school/about (s/or :string string?
                                    :nil nil?))

(s/def ::create-school (s/keys :req-un [::name ::location]
                               :opt-un [::about]))
(s/def ::edit-school (s/keys :req-un [::name ::location]
                             :opt-un [::about]))
(s/def ::new-school (s/keys :req-un [::id ::name ::location]
                            :opt-un [::stats ::archived ::about]))
(s/def ::school (s/keys :req-un [::id ::name :existing-school/location :existing-school/about]
                        :opt-un [::stats ::archived]))
(s/def ::schools (s/* ::school))
