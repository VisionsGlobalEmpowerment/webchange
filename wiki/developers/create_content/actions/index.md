# Actions

The application interpreter supports a set of actions that should be enough for most cases.

Below are some of them. You can find a complete list of actions and their descriptions in the specified source code file.

If none of the listed below actions fits your intention, you can [create your own action](create-action.md).

## Common action properties
### Interceptors

In some cases we need to add to action some information from environment of current scene. In that case we have interceptors

 - **:from-params** - this interceptors allow to copy data from params to action before execution.
   `:from-params [{:param-property 'scene-id', :action-property 'var-value'}]`
    - :param-property - params property name which will be used as source
    - :action-property - object property name to save value 
    - :template - if template is set value will be constructed by replacing every "%" sign with value, if not value will be set as is.

 - **:from-var** - this interceptors allow to copy data from variables to action before execution.
   `:from-var [{:var-name "item-1" :action-property "image" :var-property "image-src"}]`
    - :var-name - name of variable which will be used as source
    - :var-property - property of variable if only one property is needed
    - :var-key - if this property exist data will be converted to hash-map with var-key as key and value, as property value
    - :template - if template is set value will be constructed by replacing every "%" sign with value, if not value will be set as is.
    - :to-vector - boolean property, if true value will be added to vector
    - :action-property - property which will be used to place value to action, if it is not set data will be merged to action on root level.
    

[`webchange.common.events`](/src/cljs/webchange/common/events.cljs)

- `:action` - call another action by its name;
- `:sequence` - run a sequence of actions defined by their names;
- `:sequence-data` - run a sequence of actions defined by their data;
- `:parallel` - run multiple actions by their data in parallel;
- `:remove-flows` - terminate execution of actions marked with passed tag;
- `:remove-flow-tag` - remove a tag from an action;
- `:callback` - call external function;
- `:hide-skip` - hide user interface button for actions flow skipping (technical).

[`webchange.interpreter.events`](/src/cljs/webchange/interpreter/events.cljs)
 
- `:audio` - run audio file;
- `:play-video` - play video file;
- `:path-animation` - run svg path animation;
- `:state` - apply component state;
- `:set-attribute` - set component attribute value;
- `:empty` - delay in ms;
- `:animation` - immediately run animation of `animation` component;
- `:add-animation` - add animation to animations que.
- `:start-animation` - start animation of `animation` component;
- `:remove-animation` - remove animations from `animation` component;
- `:set-skin` - set character appearance;
- `:set-slot` - set image to the front side of a box;
- `:animation-props` - set properties of `animation` component. Deprecated. Use `set-attribute` instead;
- `:animation-sequence` - play audio file and speaking animation simultaneously;
- `:scene` - change current scene;
- `:location` - change current scene by location name;
- `:transition` - object motion animation;
- `:stop-transition` - abort running transition;
- `:move` - animation of moving a component along the graph;
- `:placeholder-audio` - play audio with params from variable. Deprecated;
- `:test-transitions-collide` - check if components intersect;
- `:start-activity` - set activity as started;
- `:stop-activity` - stop activity when the user exits without completing it;
- `:finish-activity` - set activity as finished;
- `:text-animation` - play audio file and text chunks animation simultaneously;
- `:pick-correct` - send to back that user has chosen the correct concept;
- `:pick-wrong` - send to back that user has chosen the wrong concept;
- `:set-current-concept` - set current concept;
- `:set-interval` - set a periodically repeating action;
- `:remove-interval` - reset a periodically repeating action

[`webchange.interpreter.variables.events`](/src/cljs/webchange/interpreter/variables/events.cljs)

- `:lesson-var-provider` - provides one concept from a lesson set for each call;
- `:vars-var-provider` - provides one concept from variables list for each call;
- `:test-var` - check if a current object is equal to the specific value;
- `:test-var-scalar`  - compare variable value with test value;
- `:test-var-list`  - compare variables list value with test values list. Variables are compared with values according to their positions in the list;
- `:test-value` - compare two values;
- `:case` - check if a variable is equal to one of the set of values;
- `:counter` - increase or decrease declared counter;
- `:calc` - allow to perform simple calculations;
- `:set-variable` - allow to set value to corresponding variable;
- `:set-progress` - allow to set variables to student progress data;
- `:copy-variable` - allow to copy one variable value to another;
- `:clear-vars` - allow to drop all variables and providers;
- `:map-value` - This method find index of element in 'from' array with 'value', then takes element from to array with the same index and store that information in variable with name `var-name`.

Specific actions:

- [`:propagate-objects`](/src/cljs/webchange/interpreter/renderer/scene/components/group/propagate.cljs) -propagate component with concepts data.

---

[← Back to index](../../index.md)
