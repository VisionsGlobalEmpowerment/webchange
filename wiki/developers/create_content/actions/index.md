# Actions

The application interpreter supports a set of actions that should be enough for most cases.

Below are some of them. You can find a complete list of actions and their descriptions in the specified source code file.

If none of the listed below actions fits your intention, you can [create your own action](create-action.md).

[`webchange.common.events`](/src/cljs/webchange/common/events.cljs)

- [ ] `:action` - run specific action by its name
- [ ] `:sequence` - run a sequence of actions by their names
- [ ] `:sequence-data` - run a sequence of actions by their data
- [ ] `:parallel` - run multiple actions by their data in parallel
- [ ] `:remove-flows` - 
- [ ] `:remove-flow-tag` - 
- [ ] `:callback` - 
- [ ] `:hide-skip` - 

[`webchange.interpreter.events`](/src/cljs/webchange/interpreter/events.cljs)
 
- [ ] `:audio` - run audio file
- [ ] `:play-video` - start "video" component
- [ ] `:path-animation` - 
- [ ] `:state` - 
- [ ] `:set-attribute` - set component attribute (prop)
- [ ] `:add-alias` - 
- [ ] `:empty` - delay in ms
- [ ] `:animation` - 
- [ ] `:add-animation` - add animation to que
- [ ] `:start-animation` - 
- [ ] `:remove-animation` - 
- [ ] `:set-skin` - set character appearance
- [ ] `:set-slot` - set image to the front side of a box
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
