(ns webchange.interpreter.renderer.scene.components.text.component
  (:require
    [webchange.interpreter.renderer.pixi :refer [Container Text]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.scene.components.text.wrapper :refer [wrap]]))

(def default-props {:x               {}
                    :y               {}
                    :text            {}
                    :align           {}
                    :font-family     {}
                    :font-size       {}
                    :fill            {}
                    :shadow-color    {}
                    :shadow-distance {}
                    :shadow-blur     {}
                    :shadow-opacity  {}
                    :font-weight     {:default "normal"}})

(defn- create-text
  [{:keys [x y text align font-family font-size font-weight fill shadow-color shadow-distance shadow-blur shadow-opacity]}]
  (let [text (doto (Text. text (clj->js {:fontSize   font-size
                                         :fontFamily font-family
                                         :fontWeight font-weight
                                         :fill       fill}))
               (utils/set-position {:x x
                                    :y y}))]
    (when-not (nil? shadow-color)
      (aset (.-style text) "dropShadow" true)
      (aset (.-style text) "dropShadowColor" shadow-color)
      (aset (.-style text) "dropShadowDistance" shadow-distance)
      (aset (.-style text) "dropShadowBlur" shadow-blur)
      (aset (.-style text) "dropShadowAlpha" shadow-opacity))

    (case align
      "left" (aset (.-anchor text) "x" 0)
      "center" (aset (.-anchor text) "x" 0.5)
      "right" (aset (.-anchor text) "x" 1))
    text))

(def component-type "text")

(defn create
  [parent {:keys [type object-name] :as props}]
  (let [text (create-text props)
        wrapped-text (wrap type object-name text)]
    (.addChild parent text)
    wrapped-text))
