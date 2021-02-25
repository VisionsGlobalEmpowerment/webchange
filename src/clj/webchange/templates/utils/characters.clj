(ns webchange.templates.utils.characters
  (:require [clojure.string :refer [lower-case]]))

(defn create-character
  [character animations]
  (if-let [c (get animations (-> character :skeleton keyword))]
    (merge c
           {:type "animation" :editable? true :anim "idle" :start true :scene-name (-> character :name)}
           (select-keys character [:x :y]))))

(defn add-characters
  [t characters character-positions animations]
  (let [cs (->> characters
                (map-indexed (fn [idx c] (merge c (get character-positions idx))))
                (map (fn [c] [(-> c :name lower-case keyword) (create-character c animations)]))
                (into {}))
        names (->> cs keys (map name) (map lower-case) (into []))]
    (-> t
        (update :objects merge cs)
        (update :scene-objects conj names))))
