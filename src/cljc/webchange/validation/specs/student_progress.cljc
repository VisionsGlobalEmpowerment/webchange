(ns webchange.validation.specs.student-progress
  (:require
    [clojure.spec.alpha :as s]))

(s/def ::level (s/and number? #(>= % 1)))
(s/def ::lesson (s/and number? #(>= % 1)))
(s/def ::activity (s/and number? #(>= % 1)))

(s/def ::complete-student-progress (s/keys :req-un [::level ::lesson ::activity]))
