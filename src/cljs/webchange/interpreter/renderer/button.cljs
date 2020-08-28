(ns webchange.interpreter.renderer.button
  (:require
    [cljsjs.pixi]
    [webchange.interpreter.renderer.common-utils :as utils]))

(def Container (.. js/PIXI -Container))
(def Graphics (.. js/PIXI -Graphics))
(def Sprite (.. js/PIXI -Sprite))
(def WHITE (.. js/PIXI -Texture -WHITE))
(def Text (.. js/PIXI -Text))

(def default-params {:x                   :x
                     :y                   :y
                     :on-click            {:name    :on-click
                                           :default #()}
                     :text                {:name    :text
                                           :default "Button"}
                     :text-color          {:name    :text-color
                                           :default 0xffffff}
                     :font-size           {:name    :font-size
                                           :default 68}
                     :font-family         {:name    :font-family
                                           :default "Luckiest Guy"}
                     :font-weight         {:name    :font-weight
                                           :default "normal"}
                     :text-shadow         {:name    :text-shadow
                                           :default 0xba4f02}
                     :text-shadow-offset  {:name    :text-shadow-offset
                                           :default 5}
                     :text-shadow-blur    {:name    :text-shadow-blur
                                           :default 5}
                     :text-shadow-opacity {:name    :text-shadow-opacity
                                           :default 0.8}
                     :background-color    {:name    :background-color
                                           :default 0xff9000}
                     :border-radius       {:name    :border-radius
                                           :default 50}
                     :padding-top         {:name    :padding-top
                                           :default 8}
                     :padding-bottom      {:name    :padding-bottom
                                           :default 16}
                     :padding-left        {:name    :padding-left
                                           :default 68}
                     :padding-right       {:name    :padding-right
                                           :default 68}})

(def text-params (utils/pick-params default-params [:text :text-color :font-size :font-family :font-weight :padding-left :padding-top :text-shadow :text-shadow-offset :text-shadow-blur :text-shadow-opacity]))
(def mask-params (utils/pick-params default-params [:border-radius]))
(def sprite-params (utils/pick-params default-params [:background-color :padding-top :padding-bottom :padding-left :padding-right]))
(def container-params (utils/pick-params default-params [:x :y :on-click]))

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

(defn- get-props
  [current-props props-names default-props]
  (->> props-names
       (utils/pick-params default-props)
       (utils/get-specific-params current-props)))

(defn create-button
  [parent {:keys [on-click] :as props}]
  (let [text (create-text (utils/get-specific-params props text-params))
        text-size (utils/get-size text)
        button-size (let [{:keys [padding-left
                                  padding-right
                                  padding-top
                                  padding-bottom]} (get-props props [:padding-left
                                                                     :padding-right
                                                                     :padding-top
                                                                     :padding-bottom] default-params)]
                      {:width  (+ padding-left (:width text-size) padding-right)
                       :height (+ padding-top (:height text-size) padding-bottom)})
        mask (create-mask (merge button-size
                                 (utils/get-specific-params props mask-params)))
        sprite (create-sprite (merge button-size
                                     (utils/get-specific-params props sprite-params)))
        container (create-container (utils/get-specific-params props container-params))]

    (when-not (nil? on-click) (utils/set-handler container "click" on-click))

    (aset sprite "mask" mask)
    (.addChild container sprite)
    (.addChild container text)
    (.addChild container mask)
    (.addChild parent container)

    (utils/check-rest-props (str "Button <" (:object-name props) ">")
                            props
                            text-params
                            sprite-params
                            mask-params
                            container-params
                            [:object-name :parent :on-click])))
