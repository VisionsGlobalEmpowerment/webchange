# Common component properties
- :scene-name - used in actions when you need to specify object, and in common case should be like object name. Must be unique in scene.
- :transition - used in transitions when you need to specify object, and in common case should be like object name. Must be unique in scene.
- :states - map of maps with object properties. Each map determines list of properties which will be set to object.
    > :states     {:hidden {:visible false}, :visible {:visible true}, :init-position {:x 655, :y 212}}}

    you could switch states, using "state" action. See example bellow:
    > {:id "hidden", :type "state", :target "box2"} 
- :actions - map of actions which may be triggered on event of corresponding object. 

# Existing Components

All currently implemented components can be found in the [folder `/components/`](/src/cljs/webchange/interpreter/renderer/scene/components) of the scene renderer.

- [`background`](/src/cljs/webchange/interpreter/renderer/scene/components/background/component.cljs) - simple background image,
- [`layered-background`](/src/cljs/webchange/interpreter/renderer/scene/components/layered_background/component.cljs) - background consisting of three layers: `:background`, `:decoration` and `:surface`,
- [`carousel`](/src/cljs/webchange/interpreter/renderer/scene/components/carousel/component.cljs) - moving from right to left background consisting of three tiles: `:first`, `:next` and `:last`,
- [`animation`](/src/cljs/webchange/interpreter/renderer/scene/components/animation/component.cljs) - character animation,
- [`image`](/src/cljs/webchange/interpreter/renderer/scene/components/image/component.cljs) - static image,
- [`text`](/src/cljs/webchange/interpreter/renderer/scene/components/text/component.cljs) - static or animated text,
- [`button`](/src/cljs/webchange/interpreter/renderer/scene/components/button/component.cljs) - clickable button with text,
- [`group`](/src/cljs/webchange/interpreter/renderer/scene/components/group/component.cljs) - group several components into one object,
- [`transparent`](/src/cljs/webchange/interpreter/renderer/scene/components/transparent/component.cljs) - transparent placeholder,
- [`progress`](/src/cljs/webchange/interpreter/renderer/scene/components/progress/component.cljs) - progress bar,
- [`slider`](/src/cljs/webchange/interpreter/renderer/scene/components/slider/component.cljs) - horizontal slider input,
- [`rectangle`](/src/cljs/webchange/interpreter/renderer/scene/components/rectangle/component.cljs) - rectangle with rounded borders.
- [`video`](/src/cljs/webchange/interpreter/renderer/scene/components/video/component.cljs) - playing video component,
- [`svg-path`](/src/cljs/webchange/interpreter/renderer/scene/components/svg_path/component.cljs) - renders svg,
- [`animated-svg-path`](/src/cljs/webchange/interpreter/renderer/scene/components/animated_svg_path/component.cljs) - renders svg animation,
- [`painting-area`](/src/cljs/webchange/interpreter/renderer/scene/components/painting_area/component.cljs) - lets user to paint,
- [`colors-palette`](/src/cljs/webchange/interpreter/renderer/scene/components/colors_palette/component.cljs) - set color to painting tool.

---

[← Back to index](../../index.md)
