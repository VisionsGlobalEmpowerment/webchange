(ns webchange.templates.library.casa
  (:require
    [webchange.templates.core :as core]))

(def m {:id          1
        :name        "casa"
        :tags        ["Direct Instruction - Animated Instructor"]
        :description "Some description of casa mechanics and covered skills"
        :lesson-sets ["concepts"]
        :fields      [{:name "image-src",
                       :type "image"}]
        :options     {:characters {:label "Characters"
                                   :type  "characters"
                                   :max   3}
                      :boxes      {:label   "Number of boxes"
                                   :type    "lookup"
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
                                             :surface    {:src "/raw/img/casa/surface_casa.png"}}}
        :scene-objects [["layered-background"]],
        :actions       {:dialog-1-before-boxes {:type               "sequence-data",
                                                :editor-type        "dialog",
                                                :concept-var        "current-word",
                                                :data               [{:type "sequence-data"
                                                                      :data [{:type "empty" :duration 0}
                                                                             {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                :phrase             "before-boxes",
                                                :phrase-description "Before boxes appear"}
                        :dialog-2-after-boxes  {:type               "sequence-data",
                                                :editor-type        "dialog",
                                                :concept-var        "current-word",
                                                :data               [{:type "sequence-data"
                                                                      :data [{:type "empty" :duration 0}
                                                                             {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                :phrase             "after-boxes",
                                                :phrase-description "After boxes appear"}
                        :dialog-3-before-open  {:type               "sequence-data",
                                                :editor-type        "dialog",
                                                :concept-var        "current-word",
                                                :data               [{:type "sequence-data"
                                                                      :data [{:type "empty" :duration 0}
                                                                             {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                :phrase             "before-open",
                                                :phrase-description "Before box opens"}
                        :dialog-4-after-open   {:type               "sequence-data",
                                                :editor-type        "dialog",
                                                :concept-var        "current-word",
                                                :data               [{:type "sequence-data"
                                                                      :data [{:type "empty" :duration 0}
                                                                             {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                :phrase             "after-open",
                                                :phrase-description "After box opens"}
                        :dialog-5-after-hide   {:type               "sequence-data",
                                                :editor-type        "dialog",
                                                :concept-var        "current-word",
                                                :data               [{:type "sequence-data"
                                                                      :data [{:type "empty" :duration 0}
                                                                             {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                :phrase             "after-hide",
                                                :phrase-description "After box flies off"}
                        :dialog-6-pick-wrong   {:type               "sequence-data",
                                                :editor-type        "dialog",
                                                :concept-var        "current-word",
                                                :data               [{:type "sequence-data"
                                                                      :data [{:type "empty" :duration 0}
                                                                             {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                :phrase             "pick-wrong",
                                                :phrase-description "Wrong box picket",
                                                :dialog-track       "4 Wrong box"}
                        :intro
                                               {:type "sequence-data",
                                                :data [{:type "empty" :duration 2000}
                                                       {:type "start-activity"}
                                                       {:type "lesson-var-provider", :from "concepts", :provider-id "words-set", :variables ["box0" "box1" "box2"]}
                                                       {:type "action" :id "dialog-1-before-boxes"}
                                                       {:type "parallel",
                                                        :data
                                                              [{:id "visible", :type "state", :target "box0"}
                                                               {:id "visible", :type "state", :target "box1"}
                                                               {:id "visible", :type "state", :target "box2"}]}
                                                       {:type "empty" :duration 500}
                                                       {:type "parallel",
                                                        :data
                                                              [{:id "idle_fly1", :loop true, :type "add-animation", :target "box0"}
                                                               {:id "idle_fly2", :loop true, :type "add-animation", :target "box1"}
                                                               {:id "idle_fly3", :loop true, :type "add-animation", :target "box2"}]}
                                                       {:type "action" :id "dialog-2-after-boxes"}
                                                       {:type "set-variable", :var-name "current-box", :var-value "box0"}]}
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
                       {:start {:on "start", :action "intro"}}
        :metadata      {:tracks [{:title "1 Intro"
                                  :nodes [{:type      "dialog"
                                           :action-id :dialog-1-before-boxes}
                                          {:type "prompt"
                                           :text "Boxes appear"}
                                          {:type      "dialog"
                                           :action-id :dialog-2-after-boxes}]}
                                 {:title "2 Concept"
                                  :nodes [{:type      "dialog"
                                           :action-id :dialog-3-before-open}
                                          {:type "prompt"
                                           :text "Box opens"}
                                          {:type      "dialog"
                                           :action-id :dialog-4-after-open}
                                          {:type "prompt"
                                           :text "Box flies off"}
                                          {:type      "dialog"
                                           :action-id :dialog-5-after-hide}]}]}})

(def boxes-variations
  {1 {:advance-progress  {:type "sequence-data",
                          :data [{:type "action" :id "dialog-after-box0"}
                                 {:type "finish-activity"}]}
      :dialog-after-box0 {:type               "sequence-data",
                          :editor-type        "dialog",
                          :concept-var        "current-word",
                          :data               [{:type "sequence-data"
                                                :data [{:type "empty" :duration 0}
                                                       {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                          :phrase             "after-box0",
                          :phrase-description "After first box"}}
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
                          :data               [{:type "sequence-data"
                                                :data [{:type "empty" :duration 0}
                                                       {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                          :phrase             "after-box0",
                          :phrase-description "After first box"}
      :dialog-after-box1 {:type               "sequence-data",
                          :editor-type        "dialog",
                          :concept-var        "current-word",
                          :data               [{:type "sequence-data"
                                                :data [{:type "empty" :duration 0}
                                                       {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                          :phrase             "after-box1",
                          :phrase-description "After second box"}}
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
                          :data               [{:type "sequence-data"
                                                :data [{:type "empty" :duration 0}
                                                       {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                          :phrase             "after-box0",
                          :phrase-description "After first box"}
      :dialog-after-box1 {:type               "sequence-data",
                          :editor-type        "dialog",
                          :concept-var        "current-word",
                          :data               [{:type "sequence-data"
                                                :data [{:type "empty" :duration 0}
                                                       {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                          :phrase             "after-box1",
                          :phrase-description "After second box"}
      :dialog-after-box2 {:type               "sequence-data",
                          :editor-type        "dialog",
                          :concept-var        "current-word",
                          :data               [{:type "sequence-data"
                                                :data [{:type "empty" :duration 0}
                                                       {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                          :phrase             "after-box2",
                          :phrase-description "After third box"}}})

(def tracks-variations
  {1 {:title "3 Dialog after box"
      :nodes [{:type "prompt"
               :text "1st box"}
              {:type      "dialog"
               :action-id :dialog-after-box0}]}
   2 {:title "3 Dialog after box"
      :nodes [{:type "prompt"
               :text "1st box"}
              {:type      "dialog"
               :action-id :dialog-after-box0}
              {:type "prompt"
               :text "2nd box"}
              {:type      "dialog"
               :action-id :dialog-after-box1}]}
   3 {:title "3 Dialog after box"
      :nodes [{:type "prompt"
               :text "1st box"}
              {:type      "dialog"
               :action-id :dialog-after-box0}
              {:type "prompt"
               :text "2nd box"}
              {:type      "dialog"
               :action-id :dialog-after-box1}
              {:type "prompt"
               :text "3rd box"}
              {:type      "dialog"
               :action-id :dialog-after-box2}]}})

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

(def box {:type       "animation",
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
          :states     {:default {:visible false}, :visible {:visible true}}
          :visible    false,
          :editable?  true})

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
        names (->> cs keys (map name) (map clojure.string/lower-case) (into []))]
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
        (update :actions merge (get boxes-variations boxes))
        (update-in [:metadata :tracks] conj (get tracks-variations boxes)))))

(defn f
  [t args]
  (-> t
      (add-characters (:characters args))
      (add-boxes (:boxes args))))

(core/register-template
  (:id m)
  m
  (partial f t))

