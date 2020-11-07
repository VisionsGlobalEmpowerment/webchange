# Create Template

Let's create a template file `src/clj/webchange/templates/library/traffic_light.clj`.

Declare an object with template metadata.
Template ID must be unique among other templates (one more than the maximum one already available).

When creating an activity from the template, we will let the user choose a character who will act as a teacher.
To do this, add the `:options` field to the metadata.
It will describe the parameter `:teacher` with type `"characters"`.
Only one character can be selected:

```
(def metadata {:id          11
               :name        "Traffic light"
               :description "Teaches children how to use traffic lights"
               :lesson-sets []
               :options     {:teacher {:label "Teacher"
                                       :type  "characters"
                                       :max   1}}})
```

Then we will declare the template of the activity itself.

In `:assets`, you need to enter the addresses of the images for the background and traffic light so that the loader will download them.
In the `:objects` field, there will be characteristics of the background and traffic light. The light source will have the "traffic-light" type declared by us.
`:scene-objects` contains lists of object names to be rendered.
Field `:metadata {:autostart true}` means that the scene will run as soon as it is loaded.
In `:triggers` we describe the global event handlers: start the `start-scene` action when the scene is ready to start,
`stop-activity` - when the player leaves the activity.

The `:actions` contains the actions themselves that will be executed by the interpreter.
The main events develop due to the sequential call of the next actions:

Calling the action we created to switch the color of the traffic light:
```
{:type   "set-traffic-light"
 :value  "red"
 :target "tl"}
```

Action call `:red-light-state` which is declared separately:
```
{:type "action"
 :id   "red-light-state"}
```

Dialog action template that will be editable from the translator window:
```
:red-light-state {:type               "sequence-data",
                  :editor-type        "dialog",
                  :data               [{:type "sequence-data"
                                        :data [{:type "empty" :duration 0}
                                               {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                  :phrase             "Red light",
                  :phrase-description "Explain the red traffic light"}
```

Half second pause:
```
{:type     "empty"
 :duration 500} 
```

Similar actions for yellow and green color.


The next is declarations of functions for creating a template and registering the template in the library.
More details can be found [page about creating templates](../templates/create-template.md).

Below is full template content:

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

If you edited the file [`map.edn`](/resources/courses/spanish/scenes/map.edn), revert it to its original state.

Next, add our template to the [`library`](/src/clj/webchange/templates/library.clj):

```
(ns webchange.templates.library
  (:require
    ...
    [webchange.templates.library.book]
    [webchange.templates.library.traffic-light]))
```

Now you can proceed to creating an activity.

---

[← Back to index](../../index.md) | [Guide main page](index.md) | [Create activity →](activity.md)
