(ns webchange.validation.specs.course-spec
  (:require
    [clojure.spec.alpha :as s]
    [webchange.validation.predicates :as p]))

(s/def ::id int?)
(s/def ::course-id int?)
(s/def ::courses-id (s/* ::course-id))
(s/def ::class-id int?)
(s/def ::school-id int?)

(s/def ::course (s/keys :req-un [::id ::name ::slug ::lang ::image-src ::status ::type]
                        :opt-un [::metadata]))

(s/def ::courses (s/* ::course))

(s/def ::assign-school-course (s/keys :req-un [::course-id]))
(s/def ::assign-school-courses (s/keys :req-un [::courses-id]))
(s/def ::unassign-school-course (s/keys :req-un [::course-id]))
(s/def ::school-course (s/keys :req-un [::school-id ::course-id]))
(s/def ::school-courses (s/keys :req-un [::school-id ::courses-id]))
(s/def ::create-course (s/keys :req-un [::name ::lang]))
(s/def ::edit-course (s/keys :req-un [::name ::lang]))
(s/def ::duplicate (s/keys :req-un [::lang]))
