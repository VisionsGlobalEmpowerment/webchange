(ns webchange.lesson-builder.tools.stage-actions-spec
  (:require
    [clojure.spec.alpha :as s]))

;; ::change-background

(s/def :single-background-data/type #(= "background"))
(s/def :single-background-data/src string?)
(s/def ::single-background-data (s/keys :req-un [:single-background-data/type :single-background-data/src]))

(s/def :layered-background-layer/src string?)
(s/def ::layered-background-layer (s/keys :req-un [:layered-background-layer/src]))

(s/def :layered-background-data/background (s/or :empty nil? :defined ::layered-background-layer))
(s/def :layered-background-data/surface (s/or :empty nil? :defined ::layered-background-layer))
(s/def :layered-background-data/decoration (s/or :empty nil? :defined ::layered-background-layer))

(s/def :layered-background-data/type #(= "layered-background"))
(s/def ::layered-background-data (s/keys :req-un [:layered-background-data/type
                                                  :layered-background-data/background
                                                  :layered-background-data/surface
                                                  :layered-background-data/decoration]))

(s/def ::background-data (s/or :single ::single-background-data
                               :layered ::layered-background-data))
