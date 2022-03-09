(ns webchange.interpreter.renderer.scene.components.button.component
  (:require
    [webchange.interpreter.pixi :refer [Container Graphics Sprite WHITE Text]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.scene.components.button.wrapper :refer [wrap]]
    [webchange.interpreter.renderer.scene.filters.filters :refer [apply-filters]]))

(def default-props {:x                   {}
                    :y                   {}
                    :on-click            {:default #()}
                    :text                {:default "Button"}
                    :text-color          {:default 0xffffff}
                    :font-size           {:default 68}
                    :font-family         {:default "Luckiest Guy"}
                    :font-weight         {:default "normal"}
                    :text-shadow         {}
                    :text-shadow-offset  {:default 5}
                    :text-shadow-blur    {:default 5}
                    :text-shadow-opacity {:default 0.8}
                    :background-color    {:default 0xff9000}
                    :border-radius       {:default 50}
                    :padding-top         {:default 8}
                    :padding-bottom      {:default 16}
                    :padding-left        {:default 68}
                    :padding-right       {:default 68}
                    :ref                 {}
                    :filters             {}})

(defn- create-text
  [{:keys [text text-color font-size font-family font-weight padding-left padding-top text-shadow text-shadow-offset text-shadow-blur text-shadow-opacity]}]
  (let [text (doto (Text. text (clj->js {:fontSize   font-size
                                         :fontFamily font-family
                                         :fontWeight font-weight
                                         :fill       text-color}))
               (utils/set-position {:x padding-left
                                    :y padding-top}))]

    (when-not (nil? text-shadow)
      (aset (.-style text) "dropShadow" true)
      (aset (.-style text) "dropShadowColor" text-shadow)
      (aset (.-style text) "dropShadowDistance" text-shadow-offset)
      (aset (.-style text) "dropShadowBlur" text-shadow-blur)
      (aset (.-style text) "dropShadowAlpha" text-shadow-opacity))

    text))

(defn- create-mask
  [{:keys [width height border-radius]}]
  (doto (Graphics.)
    (.beginFill 0x000000)
    (.drawRoundedRect 0 0 width height border-radius)
    (.endFill 0x000000)))

(defn- create-sprite
  [{:keys [width height background-color]}]
  (let [sprite (doto (Sprite. WHITE)
                 (aset "tint" background-color)
                 (aset "width" width)
                 (aset "height" height))]
    sprite))

(defn- create-container
  [{:keys [x y]}]
  (doto (Container.)
    (utils/set-position {:x x :y y})))

(def component-type "button")

(defn create
  "Create `button` component.

  Props params:
  :x - component x-position.
  :y - component y-position.
  :on-click - on click event handler.
  :text - text which will be placed on button. Default Button
  :text-color - color of text which will be placed on button. Default 0xffffff
  :font-family - specify font family. Default: Luckiest Guy.
  :font-size - font size. Default 68.
  :font-weight - sets the weight (or boldness) of the font. Default normal
  :text-shadow - color of text shadow. Default 0xba4f02
  :text-shadow-offset - size of shadow. Default 5
  :text-shadow-blur - control blur of shadow. Default 5
  :text-shadow-opacity - shadow opacity. Default 0.8
  :background-color - button background color. Default 0xff9000
  :border-radius - button border radius. Default 50
  :padding-top - padding top from button margin to text. Default 8
  :padding-bottom - padding top from button margin to text. Default 16
  :padding-left - padding top from button margin to text. Default 68
  :padding-right - padding top from button margin to text. Default 68
  "

  [{:keys [parent on-click padding-left padding-right padding-top padding-bottom object-name type ref filters] :as props}]
  (let [text (create-text props)
        text-size (utils/get-local-bounds text)
        button-size {:width  (+ padding-left (:width text-size) padding-right)
                     :height (+ padding-top (:height text-size) padding-bottom)}
        mask (create-mask (merge button-size props))
        sprite (create-sprite (merge button-size props))
        container (create-container props)
        wrapped-component (wrap type object-name container)]

    (when-not (nil? on-click) (utils/set-handler container "click" on-click))
    (when-not (nil? ref) (ref wrapped-component))

    (aset sprite "mask" mask)
    (.addChild container sprite)
    (.addChild container text)
    (.addChild container mask)
    (.addChild parent container)

    (apply-filters container filters)
    wrapped-component))
