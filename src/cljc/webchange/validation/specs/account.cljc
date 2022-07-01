(ns webchange.validation.specs.account
  (:require
    [clojure.spec.alpha :as s]
    [webchange.validation.predicates :as p]))

(def email-regex #"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,63}$")
(def type? #{"admin", "live", "teacher"})

(s/def ::first-name p/not-empty-string?)
(s/def ::last-name p/not-empty-string?)
(s/def ::email (s/and string? #(re-matches email-regex %)))
(s/def ::password p/not-empty-string?)

(s/def ::id int?)
(s/def ::type type?)
(s/def ::active boolean?)


(s/def ::create-account (s/keys :req-un [::first-name ::last-name ::email ::password ::type]
                                :opt-un []))
(s/def ::edit-account (s/keys :req-un [::first-name ::last-name ::email ::type]
                              :opt-un []))
(s/def ::change-password (s/keys :req-un [::password]))
(s/def ::set-status (s/keys :req-un [::active]))
