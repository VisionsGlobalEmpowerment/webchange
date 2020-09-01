(ns webchange.templates.library.cycling
  (:require
    [webchange.templates.core :as core]))

(def m {:id          2
        :name        "cycling"
        :description "Some description of cycling mechanics and covered skills"
        :options     {:characters {:label "Characters"
                                   :type  "characters"
                                   :max   3}
                      :boxes      {:label   "Number of boxes"
                                   :type    "lookup"
                                   :options [{:name "1" :value 1}
                                             {:name "2" :value 2}
                                             {:name "3" :value 3}]}
                      }})

(def t {:assets        [{:url "/raw/img/stadium/cycling/cycle_race_bg_01.jpg", :size 10, :type "image"}
                        {:url "/raw/img/stadium/cycling/cycle_race_bg_02.jpg", :size 10, :type "image"}
                        {:url "/raw/img/stadium/cycling/cycle_race_bg_03.jpg", :size 10, :type "image"}]
        :objects       {:layered-background {:type  "carousel" :x 0 :y 0 :width 1920 :height 1080,
                                             :first "/raw/img/stadium/cycling/cycle_race_bg_01.jpg"
                                             :last  "/raw/img/stadium/cycling/cycle_race_bg_03.jpg"
                                             :next  "/raw/img/stadium/cycling/cycle_race_bg_02.jpg"}}
        :scene-objects [["background"]],
        :actions       {:dialog-1-before-boxes {:type               "sequence-data",
                                                :editor-type        "dialog",
                                                :concept-var        "current-word",
                                                :data               [{:type "animation-sequence", :phrase-text "New action", :audio nil}],
                                                :phrase             "before-boxes",
                                                :phrase-description "Before boxes appear",
                                                :dialog-track       "1 Intro"}
                        :dialog-2-after-boxes  {:type               "sequence-data",
                                                :editor-type        "dialog",
                                                :concept-var        "current-word",
                                                :data               [{:type "animation-sequence", :phrase-text "New action", :audio nil}],
                                                :phrase             "after-boxes",
                                                :phrase-description "After boxes appear",
                                                :dialog-track       "1 Intro"}
                        :dialog-3-before-open  {:type               "sequence-data",
                                                :editor-type        "dialog",
                                                :concept-var        "current-word",
                                                :data               [{:type "animation-sequence", :phrase-text "New action", :audio nil}],
                                                :phrase             "before-open",
                                                :phrase-description "Before box opens",
                                                :dialog-track       "2 Concept"}
                        :dialog-4-after-open   {:type               "sequence-data",
                                                :editor-type        "dialog",
                                                :concept-var        "current-word",
                                                :data               [{:type "animation-sequence", :phrase-text "New action", :audio nil}],
                                                :phrase             "after-open",
                                                :phrase-description "After box opens",
                                                :dialog-track       "2 Concept"}
                        :dialog-5-after-hide   {:type               "sequence-data",
                                                :editor-type        "dialog",
                                                :concept-var        "current-word",
                                                :data               [{:type "animation-sequence", :phrase-text "New action", :audio nil}],
                                                :phrase             "after-hide",
                                                :phrase-description "After box flies off",
                                                :dialog-track       "2 Concept"}
                        :dialog-6-pick-wrong   {:type               "sequence-data",
                                                :editor-type        "dialog",
                                                :concept-var        "current-word",
                                                :data               [{:type "animation-sequence", :phrase-text "New action", :audio nil}],
                                                :phrase             "pick-wrong",
                                                :phrase-description "Wrong box picket",
                                                :dialog-track       "4 Wrong box"}
                        :intro
                                               {:type "sequence-data",
                                                :data
                                                      [{:type "start-activity", :id "cycling"}
                                                       {:type "parallel",
                                                        :data
                                                              [{:type "set-variable", :var-name "slot1", :var-value "box1"}
                                                               {:type "set-variable", :var-name "slot2", :var-value "box2"}
                                                               {:type "set-variable", :var-name "slot3", :var-value "box3"}]}
                                                       {:type "set-variable", :var-name "current-line", :var-value "box2"},
                                                       {:type        "lesson-var-provider",
                                                        :from        "concepts",
                                                        :limit       3,
                                                        :provider-id "words-set",
                                                        :repeat      4,
                                                        :shuffled    true,
                                                        :variables   ["item-1" "item-2" "item-3" "item-4" "item-5" "item-6" "item-7" "item-8" "item-9" "item-10"]}
                                                       {:type "action" :id "dialog-1-before-start"}
                                                       "renew-current-concept"
                                                       "voice-high-var"]}
                        :click-on-box          {:type        "test-value"
                                                :from-params [{:action-property "value1" :param-property "target"}]
                                                :from-var    [{:action-property "value2" :var-name "current-box"}]
                                                :success     "pick-correct"
                                                :fail        "dialog-6-pick-wrong"}
                        :pick-correct          {:type       "sequence-data",
                                                :data       [{:type "action" :id "dialog-3-before-open"}
                                                             {:type        "copy-variable" :var-name "current-word"
                                                              :from-params [{:action-property "from" :param-property "target"}]}
                                                             {:type "parallel",
                                                              :data
                                                                    [{:id          "wood", :loop false, :type "animation"
                                                                      :from-params [{:action-property "target" :param-property "target"}]}
                                                                     {:type        "set-slot",
                                                                      :from-var    [{:var-name "current-word", :var-property "image-src", :action-property "image"}],
                                                                      :slot-name   "box1",
                                                                      :attachment  {:x 40, :scale-x 4, :scale-y 4}
                                                                      :from-params [{:action-property "target" :param-property "target"}]}
                                                                     {:id          "idle_fly1" :loop true :type "add-animation"
                                                                      :from-params [{:action-property "target" :param-property "target"}]}]}
                                                             {:type "action" :id "dialog-4-after-open"}
                                                             {:type "parallel"
                                                              :data [{:id          "jump", :type "animation"
                                                                      :from-params [{:action-property "target" :param-property "target"}]}
                                                                     {:to          {:y -100, :duration 2},
                                                                      :type        "transition",
                                                                      :from-params [{:action-property "transition-id" :param-property "target"}]}]}
                                                             {:type        "state" :id "default"
                                                              :from-params [{:action-property "target" :param-property "target"}]}
                                                             {:type "action" :id "dialog-5-after-hide"}
                                                             {:type "action" :id "advance-progress"}],
                                                :unique-tag "box"}}
        :triggers
                       {:start {:on "start", :action "intro"}}})

(def boxes-variations
  {1 {:advance-progress  {:type "sequence-data",
                          :data [{:type "action" :id "dialog-after-box0"}
                                 {:type "finish-activity"}]}
      :dialog-after-box0 {:type               "sequence-data",
                          :editor-type        "dialog",
                          :concept-var        "current-word",
                          :data               [{:type "animation-sequence", :phrase-text "New action", :audio nil}],
                          :phrase             "after-box0",
                          :phrase-description "After first box",
                          :dialog-track       "3 Dialog after box"}}
   2 {:advance-progress  {:type     "case"
                          :options  {"box0" {:type "sequence-data",
                                             :data [{:type "set-variable", :var-name "current-box", :var-value "box1"}
                                                    {:type "action" :id "dialog-after-box0"}]}
                                     "box1" {:type "sequence-data",
                                             :data [{:type "action" :id "dialog-after-box1"}
                                                    {:type "finish-activity"}]}}
                          :from-var [{:action-property "value" :var-name "current-box"}]}
      :dialog-after-box0 {:type               "sequence-data",
                          :editor-type        "dialog",
                          :concept-var        "current-word",
                          :data               [{:type "animation-sequence", :phrase-text "New action", :audio nil}],
                          :phrase             "after-box0",
                          :phrase-description "After first box",
                          :dialog-track       "3 Dialog after box"}
      :dialog-after-box1 {:type               "sequence-data",
                          :editor-type        "dialog",
                          :concept-var        "current-word",
                          :data               [{:type "animation-sequence", :phrase-text "New action", :audio nil}],
                          :phrase             "after-box1",
                          :phrase-description "After second box",
                          :dialog-track       "3 Dialog after box"}}
   3 {:advance-progress  {:type     "case"
                          :options  {"box0" {:type "sequence-data",
                                             :data [{:type "set-variable", :var-name "current-box", :var-value "box1"}
                                                    {:type "action" :id "dialog-after-box0"}]}
                                     "box1" {:type "sequence-data",
                                             :data [{:type "set-variable", :var-name "current-box", :var-value "box2"}
                                                    {:type "action" :id "dialog-after-box1"}]}
                                     "box2" {:type "sequence-data",
                                             :data [{:type "action" :id "dialog-after-box2"}
                                                    {:type "finish-activity"}]}}
                          :from-var [{:action-property "value" :var-name "current-box"}]}
      :dialog-after-box0 {:type               "sequence-data",
                          :editor-type        "dialog",
                          :concept-var        "current-word",
                          :data               [{:type "animation-sequence", :phrase-text "New action", :audio nil}],
                          :phrase             "after-box0",
                          :phrase-description "After first box",
                          :dialog-track       "3"}
      :dialog-after-box1 {:type               "sequence-data",
                          :editor-type        "dialog",
                          :concept-var        "current-word",
                          :data               [{:type "animation-sequence", :phrase-text "New action", :audio nil}],
                          :phrase             "after-box1",
                          :phrase-description "After second box",
                          :dialog-track       "3"}
      :dialog-after-box2 {:type               "sequence-data",
                          :editor-type        "dialog",
                          :concept-var        "current-word",
                          :data               [{:type "animation-sequence", :phrase-text "New action", :audio nil}],
                          :phrase             "after-box2",
                          :phrase-description "After third box",
                          :dialog-track       "3"}}})

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

(def box {:type       "transparent",
          :width      772,
          :height     1032,
          :scale      {:x 0.3, :y 0.3},
          :origin     {:type "center-center"},
          :scene-name nil,
          :transition nil,
          :actions    {:click {:id "click-on-box", :on "click", :type "action" :params {:target nil}}},
          :speed      0.3
          :anim       "come"
          :loop       false
          :name       "boxes"
          :skin       "qwestion"
          :start      true
          :states
                      {:default {:type "transparent"},
                       :visible {:type "animation"}}})

(defn- create-character
  [character]
  (if-let [c (get animations (-> character :skeleton keyword))]
    (merge c
           {:type "animation" :anim "idle" :start true :scene-name (-> character :name)}
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

(def box-positions
  [{:x 505
    :y 375}
   {:x 955
    :y 375}
   {:x 1405
    :y 375}])

(defn- create-box
  [idx]
  (let [name (str "box" idx)
        pos (get box-positions idx)]
    (-> box
        (assoc :scene-name name :transition name)
        (assoc-in [:actions :click :params :target] name)
        (merge pos))))

(defn- add-boxes
  [t boxes]
  (let [bs (->> (range boxes)
                (map create-box)
                (map (juxt :scene-name identity))
                (map (fn [[k v]] [(keyword k) v]))
                (into {}))
        names (->> bs keys (map name) (into []))]
    (-> t
        (update :objects merge bs)
        (update :scene-objects conj names)
        (update :actions merge (get boxes-variations boxes)))))

(defn f
  [t args]
  (-> t
      (add-characters (:characters args))
      (add-boxes (:boxes args))))

(core/register-template
  (:id m)
  m
  (partial f t))

