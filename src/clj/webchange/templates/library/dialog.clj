(ns webchange.templates.library.dialog
  (:require
    [webchange.templates.core :as core]))

(def m {:id          4
        :name        "dialog"
        :tags        ["Direct Instruction - Educational Video"]
        :description "Simple dialog"
        :options     {:characters {:label "Characters"
                                   :type  "characters"
                                   :max   3}}})

(def t {:assets        [{:url "/raw/img/casa/background_casa.png", :size 10, :type "image"}
                        {:url "/raw/img/casa/decoration_casa.png", :size 10, :type "image"}
                        {:url "/raw/img/casa/surface_casa.png", :size 10, :type "image"}]
        :objects       {:layered-background {:type       "layered-background"
                                             :background {:src "/raw/img/casa/background_casa.png"}
                                             :decoration {:src "/raw/img/casa/decoration_casa.png"}
                                             :surface    {:src "/raw/img/casa/surface_casa.png"}}}
        :scene-objects [["layered-background"]],
        :actions       {:dialog-1 {:type               "sequence-data",
                                   :editor-type        "dialog",
                                   :concept-var        "current-word",
                                   :data               [{:type "sequence-data"
                                                         :data [{:type "empty" :duration 0}
                                                                {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                   :phrase             "dialog",
                                   :phrase-description "Dialog",
                                   :dialog-track       "1 Dialog"}
                        :intro
                                  {:type "sequence-data",
                                   :data [{:type "empty" :duration 2000}
                                          {:type "start-activity"}
                                          {:type "action" :id "dialog-1"}
                                          {:type "finish-activity"}]}}
        :triggers
                       {:start {:on "start", :action "intro"}}})

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
                              :skin   "01 mari"}})

(defn- create-character
  [character]
  (if-let [c (get animations (-> character :skeleton keyword))]
    (merge c
           {:type "animation" :editable? true :anim "idle" :start true :scene-name (-> character :name)}
           (select-keys character [:x :y]))))

(def character-positions
  [{:x 428
    :y 960}
   {:x 928
    :y 960}
   {:x 1428
    :y 960}])

(defn- add-characters
  [t characters]
  (let [cs (->> characters
                (map-indexed (fn [idx c] (merge c (get character-positions idx))))
                (map (fn [c] [(-> c :name keyword) (create-character c)]))
                (into {}))
        names (->> cs keys (map name) (into []))]
    (-> t
        (update :objects merge cs)
        (update :scene-objects conj names))))

(defn f
  [t args]
  (-> t
      (add-characters (:characters args))))

(core/register-template
  (:id m)
  m
  (partial f t))

