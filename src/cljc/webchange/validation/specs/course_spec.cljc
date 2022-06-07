(ns webchange.validation.specs.course-spec
  (:require
    [clojure.spec.alpha :as s]
    [webchange.validation.predicates :as p]))

(s/def ::id int?)
(s/def ::course-id int?)
(s/def ::class-id int?)
(s/def ::school-id int?)

(s/def ::course (s/keys :req-un [::id ::name ::slug ::lang ::image-src ::status ::type]
                        :opt-un [::metadata]))

(s/def ::courses (s/* ::course))

(s/def ::assign-school-course (s/keys :req-un [::course-id]))
(s/def ::school-course (s/keys :req-un [::school-id ::course-id]))
