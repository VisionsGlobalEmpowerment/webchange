# Create Component

Each component should be represented by the self-explanatory folder (e.g. `image/`)
and at least two files (`image/component.cljs` and `image/wrapper.cljs`).

Then the component must be added to 'components' variable in `webchange.interpreter.renderer.scene.components.index` namespace.

## `component.cljs`

A component file must contain three obligatory public variables: `default-props`, `component-type` and `create`.

### Props

Component's properties must be defined in `default-props` variable.

```
(def default-props {:x {}
                    :y {}})
```

**All** props that should be received from activity template must present as keys of `default-props` map.
Values can be an empty object or can contain some specific fields:

- `:default` - describes default value if a prop was not defined;
  ```
  (def default-props {:scale {:default {:x 1 :y 1}}})
  ```
- `:alias` - allows you to rename a prop. For example, if in activity template prop is defined as `:start`
  but you want to have this prop named as ':animation-start?' you can describe prop as the next:

  ```
  (def default-props {:animation-start? {:alias :start}})
  ```

These fields also can be combined:

```
(def default-props {:skin-name {:alias   :skin 
                                :default "default"}})
```

Other properties would not be received even if they are defined in the template
except properties defined in `skip-check-props` variable
of `webchange.interpreter.renderer.scene.components.props-utils` namespace (e.g. `:object-name`, `:parent`, `:type`, etc..).

### Type

During rendering scene renderer decides which component should be rendered by component type.
Component type must be defined in `component-type` variable. Type must be unique among other components.

Example:

```
(def component-type "rectangle")
```

### Create function

Component's main function `create`. Must be public.
It receives properties that was defined in `default-props` variable and must return wrapper of ready component.

Example:

```
(defn create
  "Example function for rounded rectangle creation"
  [{:keys [parent type object-name x y width height border-radius fill]}] ;; Required in `default-props` component properties
  ;; Generally graphic component consists of
  ;; sprite - colored area,
  ;; mask - per-pixel visibility map,
  ;; container - auxiliary component for setting position, rotation, scale..
  (let [mask (doto (Graphics.)                              ;; Create mask as rounded corner rectangle filled with black color
               (.beginFill 0x000000)
               (.drawRoundedRect 0 0 width height border-radius)
               (.endFill 0x000000))
        sprite (doto (Sprite. WHITE)                        ;; Colored sprite is created as white sprite with needed color tint
                 (aset "tint" fill)
                 (aset "width" width)
                 (aset "height" height))
        container (doto (Container.)                        ;; Overall container
                    (utils/set-position {:x x :y y}))]

    (aset sprite "mask" mask)                               ;; Set mask to sprite

    (.addChild container sprite)                            ;; Put mask and sprite to container 
    (.addChild container mask)                              ;; so they can be transitioned simultaneously

    (.addChild parent container)                            ;; Add out brand-new container to parent component

    (wrap type object-name container)                       ;; Function must return created component wrapper
    ))
```

## `wrapper.cljs`

Wrapper is an object that contains fields and methods to interact with a component from actions.

A base wrapper is created by calling the `create-wrapper` function (`webchange.interpreter.renderer.scene.components.wrapper` namespace) with an object as a parameter.
This object must contain the following fields:

- `:name` - component identifier in the scene. Usually should be passed `:object-name` from component's props;
- `:type` - component type. Should be passed `:type` from component props which should be equivalent to `:component-type`;
- `:object` - the top-level [DisplayObject](https://pixijs.download/dev/docs/PIXI.DisplayObject.html) of component (for example, a container, if a rectangle or picture is contained in a container).

In general, the wrapper declaration will look like this:

```
(defn wrap
  [type name object]
  (create-wrapper {:name name
                   :type type
                   :object object}))
```

This declaration makes default methods available for interaction with the component: `:set-position`, `:set-scale`, `:set-rotation`, `:set-visibility`, `:add-filter` and others.
The full list and technical details can be found in the function `add-default-methods` of `webchange.interpreter.renderer.scene.components.wrapper` namespace.

### Custom methods

If you want to add your own method (e.g. for [your custom action](../actions/create-action.md)),
you can define it in the same object that is passed parameters to `create-wrapper`.

The next is an example of adding a changing picture source method of the *image* component:

```
(defn wrap
  [type name container sprite-object]
  (create-wrapper {:name    name
                   :type    type
                   :object  container
                   :set-src (fn [src]
                              (let [resource (resources/get-resource src)]
                                (when (nil? resource)
                                  (throw (js/Error. (str "Resources for '" src "' were not loaded"))))
                                (aset sprite-object "texture" (.-texture resource))))}))
```


## Register Component

When the new component is ready, it must be added to the  `components` variable so the renderer could use it.

Variable could be found in `webchange.interpreter.renderer.scene.components.index` namespace in `src/cljs/webchange/interpreter/renderer/scene/components/index.cljs` folder.

```
(ns webchange.interpreter.renderer.scene.components.index
  (:require
    ;; --- other components ---
    [webchange.interpreter.renderer.scene.components.rectangle.component :as rectangle]
    ;; --- other components ---
    ))

(def components (apply hash-map [;; --- other components ---
                                 rectangle/component-type {:constructor   rectangle/create
                                                           :default-props rectangle/default-props}
                                 ;; --- other components ---
                                 ]))
```
