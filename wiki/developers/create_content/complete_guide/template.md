# Create Template

Создадим файл темплэта src/clj/webchange/templates/library/traffic_light.clj

У него будет следующее состояние

```
(ns webchange.templates.library.traffic-light
  (:require
    [webchange.templates.core :as core]))

(def metadata {:id          11
               :name        "Traffic light"
               :description "Teaches children how to use traffic lights"
               :lesson-sets []
               :options     {:teacher {:label "Teacher"
                                       :type  "characters"
                                       :max   1}}})

(def template {:assets        [{:url "/raw/img/map/background.jpg", :size 10, :type "image"}
                               {:url "/images/demo/traffic-light.png", :size 10, :type "image"}],
               :objects       {:background {:type "background", :brightness -0.15, :filter "brighten", :src "/raw/img/map/background.jpg"}
                               :tl         {:type "traffic-light"
                                            :x    300
                                            :y    100}},
               :scene-objects [["background"] ["tl"]],
               :actions       {:start-activity     {:type "start-activity", :id "traffic-light"},
                               :stop-activity      {:type "stop-activity", :id "traffic-light"},
                               :finish-activity    {:type "finish-activity", :id "traffic-light"},
                               :start-scene        {:type "sequence-data",
                                                    :data [{:type "action"
                                                            :id   "start-activity"}
                                                           {:type   "set-traffic-light"
                                                            :value  "red"
                                                            :target "tl"}
                                                           {:type "action"
                                                            :id   "red-light-state"}
                                                           {:type     "empty"
                                                            :duration 500}
                                                           {:type   "set-traffic-light"
                                                            :value  "yellow"
                                                            :target "tl"}
                                                           {:type "action"
                                                            :id   "yellow-light-state"}
                                                           {:type     "empty"
                                                            :duration 500}
                                                           {:type   "set-traffic-light"
                                                            :value  "green"
                                                            :target "tl"}
                                                           {:type "action"
                                                            :id   "green-light-state"}
                                                           {:type   "set-traffic-light"
                                                            :value  "none"
                                                            :target "tl"}
                                                           {:type "action"
                                                            :id   "finish-activity"}]}
                               :red-light-state    {:type               "sequence-data",
                                                    :editor-type        "dialog",
                                                    :data               [{:type "sequence-data"
                                                                          :data [{:type "empty" :duration 0}
                                                                                 {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                    :phrase             "Red light",
                                                    :phrase-description "Explain the red traffic light"}
                               :yellow-light-state {:type               "sequence-data",
                                                    :editor-type        "dialog",
                                                    :data               [{:type "sequence-data"
                                                                          :data [{:type "empty" :duration 0}
                                                                                 {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                    :phrase             "Yellow light",
                                                    :phrase-description "Explain the yellow traffic light"}
                               :green-light-state  {:type               "sequence-data",
                                                    :editor-type        "dialog",
                                                    :data               [{:type "sequence-data"
                                                                          :data [{:type "empty" :duration 0}
                                                                                 {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                    :phrase             "Green light",
                                                    :phrase-description "Explain the green traffic light"}},
               :triggers      {:back  {:on "back", :action "stop-activity"},
                               :start {:on "start", :action "start-scene"}},
               :metadata      {:autostart true}})

(def available-teachers {:vera       {:type      "animation",
                                      :x         1200,
                                      :y         800,
                                      :width     380,
                                      :height    537,
                                      :scale     {:x 3, :y 3},
                                      :anim      "idle",
                                      :name      "vera",
                                      :skin      "01 Vera_1",
                                      :start     true,
                                      :editable? true}
                         :senoravaca {:type      "animation",
                                      :x         1200,
                                      :y         800,
                                      :width     351,
                                      :height    717,
                                      :scale     {:x 3, :y 3},
                                      :anim      "idle",
                                      :name      "senoravaca",
                                      :skin      "vaca",
                                      :start     true,
                                      :editable? true}
                         :mari       {:type       "animation",
                                      :x          1200,
                                      :y          500,
                                      :width      473,
                                      :height     511,
                                      :scene-name "mari",
                                      :transition "mari",
                                      :anim       "idle",
                                      :loop       true,
                                      :name       "mari",
                                      :scale-x    0.5,
                                      :scale-y    0.5,
                                      :speed      1,
                                      :start      true,
                                      :editable?  true}})

(defn- add-teacher
  [template teachers]
  (let [teachers-names (->> teachers
                            (map :name)
                            (map clojure.string/lower-case)
                            (into []))
        teachers-data (->> teachers
                           (map (fn [{:keys [name skeleton]}]
                                  [(keyword name) (get available-teachers (keyword skeleton))]))
                           (into {}))]
    (-> template
        (update :objects merge teachers-data)
        (update :scene-objects conj teachers-names))))

(defn- create
  [template args]
  (add-teacher template (:teacher args)))

(core/register-template
  (:id metadata)
  metadata
  (partial create template))

```

Файл resources/courses/spanish/scenes/map.edn вернем в исходное состояние.



Добавляем наш темплэйт в библиотеку

src/clj/webchange/templates/library.clj

ёёё
(ns webchange.templates.library
  (:require
    ...
    [webchange.templates.library.book]
    [webchange.templates.library.traffic-light]))
ёёё

Дальше можно переходить к созданию активити.

---

[← Back to index](../../index.md) | [Guide main page](index.md) | [Create activity →](activity.md)
