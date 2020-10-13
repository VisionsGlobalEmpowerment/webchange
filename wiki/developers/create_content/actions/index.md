# Actions

The application interpreter supports a set of actions that should be enough for most cases.

Below are some of them. You can find a complete list of actions and their descriptions in the specified source code file.

If none of the listed below actions fits your intention, you can [create your own action](create-action.md).

[`webchange.common.events`](/src/cljs/webchange/common/events.cljs)

- `:action` - run specific action by its name
- `:sequence` - run a sequence of actions by their names
- `:sequence-data` - run a sequence of actions by their data
- `:parallel` - run multiple actions by their data in parallel

[`webchange.interpreter.events`](/src/cljs/webchange/interpreter/events.cljs)

- `:start-activity` - set current activity as started
- `:finish-activity` - set current activity as finished
- `:stop-activity` - stop activity when the user exits without completing it

- `:set-slot` - set image to the front side of a box
- `:animation-sequence` - run audio and character talking animation
- `:transition` - object motion animation
- `:stop-transition` - stop running transition

- `:audio` - run audio file
- `:play-video` - start "video" component
- `:state` - apply component state
- `:set-attribute` - set component attribute (prop)
- `:empty` - delay in ms

- `:set-skin` - set character appearance
- `:animation` - set character animation
- `:add-animation` - add animation to que

[`webchange.interpreter.variables.events`](/src/cljs/webchange/interpreter/variables/events.cljs)

- `:lesson-var-provider` - provides one concept from a lesson set for each call
- `:vars-var-provider` - provides one concept from variables list for each call

- `:set-variable` - set variable value
- `:test-var` - check if a variable is equal to the specific value
- `:case` - check if a variable is equal to one of the set of values
- `:counter` - increase or decrease declared counter

Specific actions:

- [`:propagate-objects`](/src/cljs/webchange/interpreter/renderer/scene/components/group/propagate.cljs)
