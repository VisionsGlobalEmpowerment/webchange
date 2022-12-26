(ns webchange.validation.specs.assets
  (:require
    [clojure.spec.alpha :as s]
    [webchange.validation.predicates :as p]))

(s/def ::save-subtitles (s/keys :req-un [::filename ::result]))
