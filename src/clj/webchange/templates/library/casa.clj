(ns webchange.templates.library.casa
  (:require
    [webchange.templates.core :as core]))

(def m {:id          1
        :name        "casa"
        :description "Some description of casa mechanics and covered skills"
        :options {:characters {:label "Characters"
                               :type "characters"
                               :max 3}
                  :boxes {:label "Number of boxes"
                          :type "lookup"
                          :options [{:name "1" :value 1}
                                    {:name "2" :value 2}
                                    {:name "3" :value 3}]}
                  }})

(def t {:assets        [{:url "/raw/img/casa/background_casa.png", :size 10, :type "image"}
                        {:url "/raw/img/casa/decoration_casa.png", :size 10, :type "image"}
                        {:url "/raw/img/casa/surface_casa.png", :size 10, :type "image"}]
        :objects       {:layered-background {:type       "layered-background"
                                             :background {:src "/raw/img/casa/background_casa.png"}
                                             :decoration {:src "/raw/img/casa/decoration_casa.png"}
                                             :surface    {:src "/raw/img/casa/surface_casa.png"}}
                        :vera
                                            {:type   "animation",
                                             :x      1128,
                                             :y      960,
                                             :width  380,
                                             :height 537,
                                             :scale  {:x 3, :y 3},
                                             :anim   "idle",
                                             :name   "vera",
                                             :skin   "01 Vera_1"
                                             :speed  0.3,
                                             :start  true}}
        :scene-objects [["layered-background"] ["vera"]],
        :actions       {:main-dialog {:type "sequence-data",
                                      :editor-type "dialog",
                                      :concept-var "current-word",
                                      :data [{:type "animation-sequence", :phrase-text "New action", :audio nil}],
                                      :phrase                        "test",
                                      :phrase-description            "test",
                                      :phrase-description-translated  "test"
                                      }}
        :triggers
                       {:start {:on "start", :action "main-dialog"}}})

(def animations {:vera       {:width  380,
                              :height 537,
                              :scale  {:x 1, :y 1},
                              :speed  0.5
                              :meshes true
                              :name "vera"
                              :skin "01 Vera_1"}
                 :senoravaca {:width  351,
                              :height 717,
                              :scale  {:x 1, :y 1}
                              :speed  0.5
                              :meshes true
                              :name "senoravaca"
                              :skin   "vaca"}
                 :mari       {:width  910,
                              :height 601,
                              :scale  {:x 0.5, :y 0.5}
                              :speed  1
                              :meshes true
                              :name "mari"
                              :skin "01 mari"}})

(defn- create-character
  [character]
  (if-let [c (get animations (-> character :skeleton keyword))]
    (merge c
           {:type "animation" :anim "idle" :start true}
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
                (map-indexed (fn [idx c] (merge c (get characters idx))))
                (map (fn [c] [(-> c :name keyword) (create-character c)]))
                (into {}))]
    (update t :objects merge cs)))

(defn- add-boxes
  [t boxes]
  )
(defn f
  [t args]
  (-> t
      (add-characters (:characters args))
      (add-boxes (:boxes args)))

(core/register-template
  (:id m)
  m
  (partial f t))

