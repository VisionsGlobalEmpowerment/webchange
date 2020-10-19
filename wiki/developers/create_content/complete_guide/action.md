# Create Action

Now let's add an action to our traffic light.
Let it be turning on green, yellow or red light.

## From action call to wrapper method

###  Add a action call from an activity

Let's go back to the test activity and add the action call:

```
{...
 :actions {:init {:type "sequence-data",
                  :data [{:type   "set-traffic-light"
                          :value  "red"
                          :target "tl"}]}},
 ...}
```

Earlier in the section `:objects` we declared our component as `:tl`, so we also set "tl" in the `:target` of the action.

### Call handling

We have defined the action type as "set-traffic-light". Let's add it to the game interpreter:

`src/cljs/webchange/interpreter/events.cljs`


```
...
(ce/reg-simple-executor :set-traffic-light ::execute-set-traffic-light)
```

```
(re-frame/reg-event-fx
  ::execute-set-traffic-light
  (fn [{:keys [_]} [_ {:keys [target value] :as action}]]
    {:dispatch-n (list [::scene/set-traffic-light (keyword target) value]
                       (ce/success-event action))}))
```

Here we call the `set-traffic-light` event from namespace `scene` (`webchange.interpreter.renderer.state.scene`).

Let's declare it:

```
(re-frame/reg-event-fx
  ::set-traffic-light
  (fn [{:keys [db]} [_ object-name color]]
    (let [wrapper (get-scene-object db object-name)]
      (apply-to-wrapper w/set-traffic-light wrapper color))))
```

For convenience of writing, we use a method call through the wrapper interface.
Add a new method to the interface.

`src/cljs/webchange/interpreter/renderer/scene/components/wrapper_interface.cljs`

```
...
(defn set-traffic-light [wrapper & params] (execute wrapper :set-traffic-light params))
```

And finally, let's go to our component wrapper and add the `set-traffic-light` method we need:

```
(ns webchange.interpreter.renderer.scene.components.traffic-light.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]))

(defn wrap
  [type name container]
  (create-wrapper {:name   name
                   :type   type
                   :object container
                   :set-traffic-light (fn [color]
                                        (print ":set-traffic-light" color))}))
```

## Traffic light change implementation

Let's add a function to create a shining traffic light:

```
(defn- create-light
  [{:keys [src scale mask-y mask-height]}]
  (let [resource (resources/get-resource src)
        sprite (doto (Sprite. (.-texture resource))
                 (utils/set-scale scale)
                 (utils/set-position {:x 10 :y 0}))
        mask (doto (Graphics.)
               (.beginFill 0x000000)
               (.drawRect 0 mask-y 300 mask-height)
               (.endFill 0x000000))
        container (doto (Container.)
                    (utils/set-position {:x 0 :y 0}))]
    (aset sprite "mask" mask)
    (.addChild container sprite)
    (.addChild container mask)

    (utils/set-visibility container false)                  ;; Make invisible by default

    container))
```

Update the component creation function:

- create objects of shining light of each color,
- add them to the container of our component,
- pass them to the wrapper

```
(defn create
  [{:keys [parent type object-name scale] :as props}]
  (let [src "/images/demo/traffic-light.png"
        sprite (create-sprite "/images/demo/traffic-light.png" props)
        mask (create-mask {:width 380 :height 830})
        container (create-container props)

        red-light (create-light {:src         src
                                 :scale       scale
                                 :mask-y      30
                                 :mask-height 240})
        yellow-light (create-light {:src         src
                                    :scale       scale
                                    :mask-y      270
                                    :mask-height 240})
        green-light (create-light {:src         src
                                   :scale       scale
                                   :mask-y      510
                                   :mask-height 240})]

    (aset sprite "mask" mask)
    (.addChild container sprite)
    (.addChild container mask)
    (.addChild parent container)

    (.addChild container red-light)
    (.addChild container yellow-light)
    (.addChild container green-light)

    (wrap type object-name container {:red    red-light
                                      :yellow yellow-light
                                      :green  green-light})))
```

Update the wrapper method:

```
(ns webchange.interpreter.renderer.scene.components.traffic-light.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]))

(defn wrap
  [type name container lights]
  (create-wrapper {:name   name
                   :type   type
                   :object container
                   :set-traffic-light (fn [color]
                                        (doseq [[light-name light-container] lights]
                                          (utils/set-visibility light-container (= (keyword color) light-name))))}))
```

The traffic light component and the turn on the needed light action are now complete.
Now we can proceed to creating an activity template.

---

[← Back to index](../../index.md) | [Guide main page](index.md) | [Create template →](template.md)
