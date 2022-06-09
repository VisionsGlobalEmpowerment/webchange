(ns webchange.validation.specs.activity
  (:require
    [clojure.spec.alpha :as s]
    [webchange.validation.predicates :as p]))

(s/def ::about string?)

(s/def ::activity-info (s/keys :req-un [::about]))
