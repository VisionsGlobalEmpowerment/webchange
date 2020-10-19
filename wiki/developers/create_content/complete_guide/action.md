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

И наконец перейдем во враппер нашего компонента и добавим нужный нам метод set-traffic-light

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

---

[← Back to index](../../index.md) | [Guide main page](index.md) | [Create template →](template.md)
