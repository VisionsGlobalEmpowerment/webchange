(ns webchange.lesson-builder.stage-actions-spec
  (:require
    [clojure.spec.alpha :as s]))

(s/def ::action-data map?)
(s/def ::action-path sequential?)
(s/def ::action-tag string?)
(s/def ::action-target string?)

(s/def :audio-region/duration number?)
(s/def :audio-region/end number?)
(s/def :audio-region/start number?)
(s/def ::audio-region (s/keys :req-un [:audio-region/duration :audio-region/end :audio-region/start]
                              :opt-un [::data]))

(s/def :background-single-data/type #(= "background"))
(s/def :background-single-data/src string?)
(s/def ::background-single-data (s/keys :req-un [:background-single-data/type :background-single-data/src]))
(s/def :background-layered-layer/src string?)
(s/def ::background-layered-layer (s/keys :req-un [:background-layered-layer/src]))
(s/def :background-layered-data/background (s/or :empty nil? :defined ::background-layered-layer))
(s/def :background-layered-data/surface (s/or :empty nil? :defined ::background-layered-layer))
(s/def :background-layered-data/decoration (s/or :empty nil? :defined ::background-layered-layer))
(s/def :background-layered-data/type #(= "layered-background"))
(s/def ::background-layered-data (s/keys :req-un [:background-layered-data/type]
                                         :opt-un [:background-layered-data/background
                                                  :background-layered-data/surface
                                                  :background-layered-data/decoration]))
(s/def ::background-data (s/or :single ::background-single-data
                               :layered ::background-layered-data))

(s/def ::data map?)
(s/def ::number number?)
(s/def ::object-name (s/or :string string? :keyword keyword?))
(s/def ::position number?)
(s/def ::text string?)
(s/def ::url string?)
(s/def ::chunks sequential?)
