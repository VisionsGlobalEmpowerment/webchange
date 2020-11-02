# Actions

The application interpreter supports a set of actions that should be enough for most cases.

Below are some of them. You can find a complete list of actions and their descriptions in the specified source code file.

If none of the listed below actions fits your intention, you can [create your own action](create-action.md).

## Common action properties
### Interceptors
In some cases we need to add to action some information from environment of current scene. In that case we have interceptors

 - :from-params - this interceptors allow to copy data from params to action before execution.
   `:from-params [{:param-property 'scene-id', :action-property 'var-value'}]`
    - :param-property - params property name which will be used as source
    - :action-property - object property name to save value 
    - :template - if template is set value will be constructed by replacing every "%" sign with value, if not value will be set as is.

 - :from-var - this interceptors allow to copy data from variables to action before execution.
   `:from-var [{:var-name "item-1" :action-property "image" :var-property "image-src"}]`
    - :var-name - name of variable which will be used as source
    - :var-property - property of variable if only one property is needed
    - :var-key - if this property exist data will be converted to hash-map with var-key as key and value, as property value
    - :template - if template is set value will be constructed by replacing every "%" sign with value, if not value will be set as is.
    - :to-vector - boolean property, if true value will be added to vector
    - :action-property - property which will be used to place value to action, if it is not set data will be merged to action on root level.
    
        


[`webchange.common.events`](/src/cljs/webchange/common/events.cljs)

- [x] `:action` - call another action by its name;
- [x] `:sequence` - run a sequence of actions defined by their names;
- [x] `:sequence-data` - run a sequence of actions defined by their data;
- [x] `:parallel` - run multiple actions by their data in parallel;
- [x] `:remove-flows` - terminate execution of actions marked with passed tag;
- [x] `:remove-flow-tag` - remove a tag from an action;
- [x] `:callback` - call external function;
- [x] `:hide-skip` - hide user interface button for actions flow skipping (technical).

[`webchange.interpreter.events`](/src/cljs/webchange/interpreter/events.cljs)
 
- [x] `:audio` - run audio file;
- [x] `:play-video` - play video file;
- [x] `:path-animation` - run svg path animation;
- [x] `:state` - apply component state;
- [x] `:set-attribute` - set component attribute value;
- [x] `:add-alias` - add state alias to component;
- [x] `:empty` - delay in ms;
- [x] `:animation` - immediately run animation of `animation` component;
- [x] `:add-animation` - add animation to animations que.
- [x] `:start-animation` - start animation of `animation` component;
- [x] `:remove-animation` - remove animations from `animation` component;
- [x] `:set-skin` - set character appearance;
- [x] `:set-slot` - set image to the front side of a box;
- [x] `:animation-props` - set properties of `animation` component. Deprecated. Use `set-attribute` instead;
- [x] `:animation-sequence` - play audio file and speaking animation simultaneously;
- [x] `:scene` - change current scene;
- [x] `:location` - change current scene by location name;
- [x] `:transition` - object motion animation;
- [x] `:stop-transition` - abort running transition;
- [x] `:move` - animation of moving a component along the graph;
- [x] `:placeholder-audio` - play audio with params from variable. Deprecated;
- [x] `:test-transitions-collide` - check if components intersect;
- [x] `:start-activity` - set activity as started;
- [x] `:stop-activity` - stop activity when the user exits without completing it;
- [x] `:finish-activity` - set activity as finished;
- [x] `:text-animation` - play audio file and text chunks animation simultaneously;
- [x] `:pick-correct` - send to back that user has chosen the correct concept;
- [x] `:pick-wrong` - send to back that user has chosen the wrong concept;
- [x] `:set-current-concept` - set current concept;
- [x] `:set-interval` - set a periodically repeating action;
- [x] `:remove-interval` - reset a periodically repeating action

[`webchange.interpreter.variables.events`](/src/cljs/webchange/interpreter/variables/events.cljs)

- [x] `:lesson-var-provider` - provides one concept from a lesson set for each call
- [x] `:vars-var-provider` - provides one concept from variables list for each call
- [x] `:test-var` - check if a current object is equal to the specific value
- [x] `:test-var-scalar`  - compare variable value with test value. 
- [x] `:test-var-list`  - compare variables list value with test values list. Variables are compared with values according to their positions in the list.
- [x] `:test-value` - compare two values. 
- [x] `:case` - check if a variable is equal to one of the set of values
- [x] `:counter` - increase or decrease declared counter
- [x] `:calc` - allow to perform simple calculations.
- [x] `:set-variable` - allow to set value to corresponding variable.
- [x] `:set-progress` - allow to set variables to student progress data. 
- [x] `:copy-variable` - allow to copy one variable value to another.
- [x] `:clear-vars` - allow to drop all variables and providers.
- [x] `:map-value` - This method find index of element in 'from' array with 'value', then takes element from to array with the same index and store that information in variable with name `var-name`.

Specific actions:

- [ ] [`:propagate-objects`](/src/cljs/webchange/interpreter/renderer/scene/components/group/propagate.cljs) - 

---

[← Back to index](../../index.md)
