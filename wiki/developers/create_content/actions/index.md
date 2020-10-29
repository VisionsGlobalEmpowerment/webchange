# Actions

The application interpreter supports a set of actions that should be enough for most cases.

Below are some of them. You can find a complete list of actions and their descriptions in the specified source code file.

If none of the listed below actions fits your intention, you can [create your own action](create-action.md).

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
- [ ] `:animation-props` - 
- [ ] `:animation-sequence` - run audio and character talking animation
- [ ] `:scene` - 
- [ ] `:location` - 
- [ ] `:transition` - object motion animation
- [ ] `:stop-transition` - stop running transition
- [ ] `:move` - 
- [ ] `:placeholder-audio` - 
- [ ] `:test-transitions-collide` - 
- [ ] `:start-activity` - set current activity as started
- [ ] `:stop-activity` - stop activity when the user exits without completing it
- [ ] `:finish-activity` - set current activity as finished
- [ ] `:text-animation` - 
- [ ] `:pick-correct` - 
- [ ] `:pick-wrong` - 
- [ ] `:set-current-concept` - 
- [ ] `:set-interval` - 
- [ ] `:remove-interval` - 
- [ ] `:set-traffic-light` - 

[`webchange.interpreter.variables.events`](/src/cljs/webchange/interpreter/variables/events.cljs)

- [ ] `:dataset-var-provider` - 
- [ ] `:lesson-var-provider` - provides one concept from a lesson set for each call
- [ ] `:vars-var-provider` - provides one concept from variables list for each call
- [ ] `:test-var` - check if a variable is equal to the specific value
- [ ] `:test-var-scalar` - 
- [ ] `:test-var-list` - 
- [ ] `:test-value` - 
- [ ] `:case` - check if a variable is equal to one of the set of values
- [ ] `:counter` - increase or decrease declared counter
- [ ] `:calc` - 
- [ ] `:set-variable` - set variable value
- [ ] `:set-progress` - 
- [ ] `:copy-variable` - 
- [ ] `:clear-vars` - 
- [ ] `:map-value` - 

Specific actions:

- [ ] [`:propagate-objects`](/src/cljs/webchange/interpreter/renderer/scene/components/group/propagate.cljs) - 

---

[← Back to index](../../index.md)
