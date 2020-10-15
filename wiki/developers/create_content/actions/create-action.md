# Create Action

In case the existing actions are not enough for your purposes, you can define your own action.

If the action is logically related to your new component, then it is better to create a file the action declaration next to the component file.

In this case, you will need to add to the dependencies `[webchange.common.events :as ce]` and all `reg-simple-executor` function:

`(ce/reg-simple-executor :my-action ::execute-my-action)`, where

- `:my-action` - the type of the action. For registration, you should use a keyword; for the call, a string representation will be used;
- `::execute-my-action` - the name of the re-frame event handler that will be called when the interpreter starts executing your action.

In the basic case, the declaration will look like this:

```
(ns webchange.interpreter.renderer.scene.components.my-component.my-action
  (:require
    [re-frame.core :as re-frame]
    [webchange.common.events :as ce]))

(ce/reg-simple-executor :my-action ::execute-my-action)

(re-frame/reg-event-fx
  ::execute-my-action
  (fn [{:keys [db]} [_ {:keys [param-1 param-2] :as action}]]
    ;; Do something...
    {:dispatch (ce/success-event action)}))
```

Calling the action from an activity code will look like this:

```
:some-action {:type "my-action" :param-1 "value-1" :param-2 "value-2"}
```

If the action is not related logically with any specific component, then you can define it in `webchange.interpreter.events` namespace.
The rest of the process for defining an action will be the same as described above.

---

[← Back to index](../../index.md)
