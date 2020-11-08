(ns webchange.templates.library.mall_main
    (:require
      [webchange.templates.core :as core]))

(def metadata {:id 17
               :name        "Shopping Mall"
               :description "Teaches children concepts in a shopping environment"
               :tags        ["Direct Instruction - Animated Instructor"]
               :lesson-sets ["concepts"]
               :fields      [{:name "image-src",
                              :type "image"}]
               :options {:characters {:label "Teacher"
                                      :type  "characters"
                                      :max   1}
                         :boxes      {:label   "Number of boxes"
                                      :type    "lookup"
                                      :options [{:name "1" :value 1}
                                                {:name "2" :value 2}
                                                {:name "3" :value 3}]}
                         }})

(def template {:assets        [{:url "/raw/img/mall/MallBackground.png",  :size 10, :type "image"}
                               {:url "/raw/img/mall/StoreBackground.png", :size 10, :type "image"}],
               :objects       {:background {:type "background", :src "raw/img/mall/MallBackground.png"}}
               :scene-objects [["background"]],
               :actions       {:dialog-1-before-boxes {:type               "sequence-data",
                                                       :editor-type        "dialog",
                                                       :concept-var        "current-word",
                                                       :data               [{:type "sequence-data"
                                                                             :data [{:type "empty" :duration 0}
                                                                                    {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                       :phrase             "before-boxes",
                                                       :phrase-description "Before any boxes appear",
                                                       :dialog-track       "1 Intro"}
                               :dialog-2-after-boxes  {:type               "sequence-data",
                                                       :editor-type        "dialog",
                                                       :concept-var        "current-word",
                                                       :data               [{:type "sequence-data",
                                                                             :data [{:type "empty" :duration 0}
                                                                                    {:type "animation-sequence", :phrase-text "New action", :audio nil}]}]}
                               :dialog-3-pick-wrong   {:type                "sequence-data",
                                                       :editor-type         "dialog",
                                                       :concept-var         "current-word",
                                                       :data                [{:type "sequence-data"
                                                                              :data [{:type "empty" :duration 0}
                                                                                     {:type "animation-sequence", :phrase-text "New action", :audio nil}]}]
                                                       :phrase              "pick-wrong"
                                                       :phrase-description  "Wrong box picked"
                                                       :dialog-track        "4 Wrong box"}
                               :dialog-4-pick-correct {:type                "sequence-data",
                                                       :editor-type         "dialog",
                                                       :concept-var         "current-word",
                                                       :data                [{:type "sequence-data"
                                                                              :data [{:type "empty" :duration 0}
                                                                                     {:type "animation-sequence", :phrase-text "New action", :audio nil}]}]
                                                       :phrase              "pick-correct"
                                                       :phrase-description  "Correct box picked"
                                                       :dialog-track        "5 Correct box"}
                               :click-on-box          {:type        "test-value"
                                                       :from-params [{:action-property "value1" :param-property "target"}]
                                                       :from-var    [{:action-property "value2" :var-name "current-box"}]
                                                       :success     "dialog-4-pick-correct"
                                                       :fail        "dialog-3-pick-wrong"}
                               :intro                 {:type                "sequence-data"
                                                       :data                [{:type "empty" :duration 2000}
                                                                             {:type "start-activity"}
                                                                             {:type "lesson-var-provider", :from "concepts", :provider-id "words-set", :variables ["box0" "box1" "box2" "box3"]}
                                                                             {:type "action" :id "dialog-1-before-boxes"}
                                                                             {:type "parallel",
                                                                              :data [{:id "visible", :type "state", :target "box0"}
                                                                                     {:id "visible", :type "state", :target "box1"}
                                                                                     {:id "visible", :type "state", :target "box2"}
                                                                                     {:id "visible", :type "state", :target "box3"}]}]}}
               :triggers      {:start {:on "start", :action "intro"}}})

;;---- vv TAKEN FROM casa.clj vv ----;;
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
                          :data               [{:type "sequence-data"
                                                :data [{:type "empty" :duration 0}
                                                       {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                          :phrase             "after-box0",
                          :phrase-description "After first box",
                          :dialog-track       "3 Dialog after box"}
      :dialog-after-box1 {:type               "sequence-data",
                          :editor-type        "dialog",
                          :concept-var        "current-word",
                          :data               [{:type "sequence-data"
                                                :data [{:type "empty" :duration 0}
                                                       {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
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
                          :data               [{:type "sequence-data"
                                                :data [{:type "empty" :duration 0}
                                                       {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                          :phrase             "after-box0",
                          :phrase-description "After first box",
                          :dialog-track       "3"}
      :dialog-after-box1 {:type               "sequence-data",
                          :editor-type        "dialog",
                          :concept-var        "current-word",
                          :data               [{:type "sequence-data"
                                                :data [{:type "empty" :duration 0}
                                                       {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                          :phrase             "after-box1",
                          :phrase-description "After second box",
                          :dialog-track       "3"}
      :dialog-after-box2 {:type               "sequence-data",
                          :editor-type        "dialog",
                          :concept-var        "current-word",
                          :data               [{:type "sequence-data"
                                                :data [{:type "empty" :duration 0}
                                                       {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                          :phrase             "after-box2",
                          :phrase-description "After third box",
                          :dialog-track       "3"}}})

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
;;---- ^^ TAKEN FROM casa.clj ^^ ----;;

(defn- create
  [template args]
  (-> template
      ;;(add-teacher template (:teacher args))
      (add-characters (:characters args))
      (add-boxes (:boxes args))))

(core/register-template
 (:id metadata)
 metadata
 (partial create template))