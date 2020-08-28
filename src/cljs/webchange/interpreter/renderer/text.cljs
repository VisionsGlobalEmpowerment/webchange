(ns webchange.interpreter.renderer.text
  (:require
    [cljsjs.pixi]
    [re-frame.core :as re-frame]
    [webchange.interpreter.renderer.state.scene :as state]
    [webchange.interpreter.renderer.common-utils :as utils]
    [webchange.interpreter.renderer.text-wrapper :refer [wrap]]))

(def Container (.. js/PIXI -Container))
(def Text (.. js/PIXI -Text))

(def default-params {:x               :x
                     :y               :y
                     :text            :text
                     :align           :align
                     :font-family     :font-family
                     :font-size       :font-size
                     :font-weight     {:name    :font-weight
                                       :default "normal"}
                     :fill            :fill
                     :shadow-color    :shadow-color
                     :shadow-distance :shadow-distance
                     :shadow-blur     :shadow-blur
                     :shadow-opacity  :shadow-opacity})

(def text-params (utils/pick-params default-params [:x
                                                    :y
                                                    :text
                                                    :align
                                                    :font-family
                                                    :font-size
                                                    :font-weight
                                                    :fill
                                                    :shadow-color
                                                    :shadow-distance
                                                    :shadow-blur
                                                    :shadow-opacity]))

(defn- create-object
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

(defn create-text
  [parent {:keys [object-name] :as props}]
  (let [text (create-object (utils/get-specific-params props text-params))
        wrapped-text (wrap object-name text)]
    (.addChild parent text)
    (utils/check-rest-props (str "Text <" (:object-name props) ">")
                            props
                            text-params
                            [:object-name :parent])
    (re-frame/dispatch [::state/register-object wrapped-text])))
