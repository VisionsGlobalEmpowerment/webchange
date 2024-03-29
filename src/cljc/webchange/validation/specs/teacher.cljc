(ns webchange.validation.specs.teacher
  (:require
    [clojure.spec.alpha :as s]
    [webchange.validation.predicates :as p]))

(def email-regex #"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,63}$")

(def status? #{"active", "inactive"})
(def type? #{"admin", "teacher"})

(s/def ::first-name p/not-empty-string?)
(s/def ::last-name p/not-empty-string?)
(s/def ::email (s/and string? #(re-matches email-regex %)))
(s/def ::status status?)
(s/def ::type type?)
(s/def ::id number?)

(s/def ::create-teacher (s/keys :req-un [::first-name ::last-name ::email ::password ::type]
                                :opt-un []))
(s/def ::edit-teacher (s/keys :req-un [::first-name ::last-name ::type]
                              :opt-un [::password]))

(s/def ::edit-teacher-status (s/keys :req-un [::status]))
(s/def ::transfer-teacher (s/keys :req-un [::email]))
