(ns webchange.templates.utils.characters
  (:require [clojure.string :refer [lower-case]]
            [camel-snake-kebab.core :refer [->kebab-case]]))

(def animations {:vera       {:width  380,
                              :height 537,
                              :scale  {:x 1, :y 1},
                              :speed  0.5
                              :meshes true
                              :name   "vera"
                              :skin   "01 Vera_1"}
                 :senoravaca {:width  351,
                              :height 717,
                              :scale  {:x 1, :y 1}
                              :speed  0.5
                              :meshes true
                              :name   "senoravaca"
                              :skin   "vaca"}
                 :mari       {:width  910,
                              :height 601,
                              :scale  {:x 0.5, :y 0.5}
                              :speed  1
                              :meshes true
                              :name   "mari"
                              :skin   "01 mari"}
                 :teacher    {:width  630,
                              :height 1308,
                              :scale  {:x 0.5, :y 0.5}
                              :speed  1
                              :meshes true
                              :name   "teacher"
                              :skin   "default"}
                 :student    {:width  654,
                              :height 1022,
                              :scale  {:x 0.5, :y 0.5}
                              :speed  1
                              :meshes true
                              :name   "student"
                              :skin   "default"}
                 :guide      {:width  591,
                              :height 591,
                              :scale  {:x 0.5, :y 0.5}
                              :speed  1
                              :meshes true
                              :name   "guide"
                              :skin   "default"}
                 :child      {:width      342,
                              :height     691,
                              :scale      {:x 0.5 :y 0.5}
                              :speed      1
                              :meshes     true
                              :name       "child"
                              :skin-names {:body "BODY/ChildTon-01"
                                           :clothes "CLOTHES/Girl-01-Clothes-01"
                                           :head "HEAD/Head-Girl-01-Ton-01"}}})

(def character-positions
  [{:x 176 :y 960}
   {:x 543 :y 960}
   {:x 918 :y 960}
   {:x 1294 :y 960}
   {:x 1664 :y 960}])

(defn- create-character
  [character animations]
  (if-let [c (get animations (-> character :skeleton keyword))]
    (merge c
           {:type       "animation"
            :editable?  {:select true :drag true :show-in-tree? true}
            :anim       "idle"
            :start      true
            :scene-name (-> character :name)}
           (select-keys character (cond-> [:x :y]
                                          (-> character :skin some?) (conj :skin))))))

(defn add-characters
  [t characters]
  (let [cs (->> characters
                (map (fn [c] (assoc c :name (->kebab-case (:name c)))))
                (map-indexed (fn [idx c] (merge c (get character-positions idx))))
                (map (fn [c] [(-> c :name lower-case keyword) (create-character c animations)]))
                (into {}))
        names (->> cs keys (map name) (map lower-case) (into []))]
    (-> t
        (update :objects merge cs)
        (update :scene-objects conj names))))
