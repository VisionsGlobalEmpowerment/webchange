(ns webchange.validation.specs.activity
  (:require
    [clojure.spec.alpha :as s]
    [webchange.validation.predicates :as p]))

(s/def ::id int?)

(s/def ::name p/not-empty-string?)
(s/def ::preview (s/or :string string?
                       :nil nil?))
(s/def ::about (s/or :string string?
                     :nil nil?))
(s/def ::short-description (s/or :string string?
                                 :nil nil?))
(s/def ::lang (s/or :string string?
                    :nil nil?))
(s/def ::slug (s/or :string string?
                    :nil nil?))
(s/def ::created-at p/not-empty-string?)
(s/def ::updated-at p/not-empty-string?)

(s/def ::activity-info (s/keys :req-un [::id ::name ::preview ::about ::short-description ::created-at ::updated-at ::lang ::slug]))
(s/def ::activities-info (s/* ::activity-info))

(s/def ::metadata (s/keys :opt-un [::about ::short-description]))

(s/def ::edit-activity (s/keys :req-un [::name ::lang]
                               :opt-un [::metadata]))

(s/def :activity-data/assets some?)
(s/def :activity-data/objects some?)
(s/def :activity-data/scene-objects some?)
(s/def :activity-data/actions some?)
(s/def :activity-data/triggers some?)
(s/def :activity-data/metadata some?)
(s/def ::activity-data (s/keys :req-un [:activity-data/assets :activity-data/objects :activity-data/scene-objects :activity-data/actions :activity-data/triggers :activity-data/metadata]))

(s/def ::archive-activity (s/keys :req-un [::archive]))
(s/def ::toggle-visibility (s/keys :req-un [::visible]))
(s/def ::duplicate (s/keys :req-un [::name ::lang]))
(s/def ::template-options map?)
(s/def ::update-template (s/keys :req-un [::update]))
(s/def ::template-action (s/keys :req-un [::common-action? ::action ::data]))
