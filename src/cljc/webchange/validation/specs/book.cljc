(ns webchange.validation.specs.book
  (:require
    [clojure.spec.alpha :as s]
    [webchange.validation.predicates :as p]))


(s/def ::cover-title p/not-empty-string?)
(s/def ::cover-image p/not-empty?)
(s/def ::cover-layout p/not-empty?)


(s/def ::lang (s/or :string string?
                    :nil nil?))

(s/def ::create-book (s/keys :req-un [::cover-title ::cover-image ::cover-layout
                                      ::lang]
                             :opt-un [::authors ::illustrators]))
