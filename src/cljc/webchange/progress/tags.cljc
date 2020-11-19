(ns webchange.progress.tags
  (:require [clojure.set :as cset])
  )

(def age-above-or-equal-4 "age-above-or-equal-4")
(def age-less-4 "age-less-4")

(def beginner "beginner")
(def intermediate "intermediate")
(def advanced "advanced")

(def age-tags '( age-above-or-equal-4 age-less-4))
(def learning-level-tags '( beginner intermediate advanced))

(defn remove-tags [tags tags-to-remove]
  (remove #(some #{%} tags-to-remove) tags))

(defn has-one-from [tags target]
  (not= (count (cset/intersection (set (map #(keyword %) tags)) (set (map #(keyword %) target)))) 0))
