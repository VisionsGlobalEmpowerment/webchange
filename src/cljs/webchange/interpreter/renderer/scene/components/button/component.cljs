(ns webchange.interpreter.renderer.scene.components.button.component
  (:require
    [webchange.interpreter.renderer.pixi :refer [Container Graphics Sprite WHITE Text]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.scene.components.button.wrapper :refer [wrap]]))

(def default-props {:x                   {}
                    :y                   {}
                    :on-click            {:default #()}
                    :text                {:default "Button"}
                    :text-color          {:default 0xffffff}
                    :font-size           {:default 68}
                    :font-family         {:default "Luckiest Guy"}
                    :font-weight         {:default "normal"}
                    :text-shadow         {:default 0xba4f02}
                    :text-shadow-offset  {:default 5}
                    :text-shadow-blur    {:default 5}
                    :text-shadow-opacity {:default 0.8}
                    :background-color    {:default 0xff9000}
                    :border-radius       {:default 50}
                    :padding-top         {:default 8}
                    :padding-bottom      {:default 16}
                    :padding-left        {:default 68}
                    :padding-right       {:default 68}})

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
  [{:keys [parent on-click padding-left padding-right padding-top padding-bottom object-name type] :as props}]
  (let [text (create-text props)
        text-size (utils/get-size text)
        button-size {:width  (+ padding-left (:width text-size) padding-right)
                     :height (+ padding-top (:height text-size) padding-bottom)}
        mask (create-mask (merge button-size props))
        sprite (create-sprite (merge button-size props))
        container (create-container props)]

    (when-not (nil? on-click) (utils/set-handler container "click" on-click))

    (aset sprite "mask" mask)
    (.addChild container sprite)
    (.addChild container text)
    (.addChild container mask)
    (.addChild parent container)

    (wrap type object-name container)))
