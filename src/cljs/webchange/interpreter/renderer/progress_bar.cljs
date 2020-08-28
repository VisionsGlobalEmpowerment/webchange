(ns webchange.interpreter.renderer.progress-bar
  (:require
    [cljsjs.pixi]
    [re-frame.core :as re-frame]
    [webchange.interpreter.renderer.state.scene :as state]
    [webchange.interpreter.renderer.common-utils :as utils]
    [webchange.interpreter.renderer.filters :refer [apply-outline-filter]]
    [webchange.interpreter.renderer.progress-bar-wrapper :refer [wrap]]))

(def Container (.. js/PIXI -Container))
(def Graphics (.. js/PIXI -Graphics))
(def Sprite (.. js/PIXI -Sprite))
(def WHITE (.. js/PIXI -Texture -WHITE))

(def default-params {:x                :x
                     :y                :y
                     :width            :width
                     :height           {:name    :height
                                        :default 25}
                     :value            {:name    :value
                                        :default 0.2}
                     :background-color {:name    :background-color
                                        :default 0xffffff}
                     :foreground-color {:name    :foreground-color
                                        :default 0x2c9600}
                     :border-color     {:name    :border-color
                                        :default 0x8a0f91}
                     :border-width     {:name    :border-width
                                        :default 1}
                     :border-radius    {:name    :border-radius
                                        :default 10}})

(def background-params (utils/pick-params default-params [:width :height :background-color :border-width :border-color :border-radius]))
(def foreground-params (utils/pick-params default-params [:width :height :foreground-color :value]))
(def container-params (utils/pick-params default-params [:x :y]))

(defn- create-mask
  [{:keys [width height border-radius]}]
  (doto (Graphics.)
    (.beginFill 0x000000)
    (.drawRoundedRect 0 0 width height border-radius)
    (.endFill 0x000000)))

(defn- create-sprite
  [{:keys [fill width height border-width border-color]}]
  (let [sprite (doto (Sprite. WHITE)
                 (aset "tint" fill)
                 (aset "width" width)
                 (aset "height" height))]
    (when (and (-> border-width nil? not)
               (-> border-color nil? not))
      (apply-outline-filter sprite {:width border-width
                                    :color border-color}))
    sprite))

(defn- create-background
  [{:keys [width height background-color border-width border-color border-radius]}]
  (let [mask (create-mask {:width         width
                           :height        height
                           :border-radius border-radius})
        sprite (create-sprite {:fill         background-color
                               :width        width
                               :height       height
                               :border-width border-width
                               :border-color border-color})]
    (aset sprite "mask" mask)
    {:mask   mask
     :sprite sprite}))

(defn- create-foreground
  [{:keys [width height foreground-color value background-mask]}]
  (let [current-width (* width value)
        sprite (create-sprite {:fill   foreground-color
                               :width  current-width
                               :height height})]
    (aset sprite "mask" background-mask)
    {:sprite sprite}))

(defn- create-container
  [{:keys [x y]}]
  (doto (Container.)
    (utils/set-position {:x x :y y})))

(defn create-progress-bar
  [parent {:keys [object-name width] :as props}]
  (let [container (create-container (utils/get-specific-params props container-params))
        background (create-background (utils/get-specific-params props background-params))
        foreground (create-foreground (merge {:background-mask (:mask background)}
                                             (utils/get-specific-params props foreground-params)))
        wrapped-progress-bar (wrap object-name {:set-value (fn [value]
                                                             (aset (:sprite foreground) "width" (* value width)))})]

    (.addChild container (:mask background))
    (.addChild container (:sprite background))
    (.addChild container (:sprite foreground))
    (.addChild parent container)

    (utils/check-rest-props (str "ProgressBar <" (:object-name props) ">")
                            props
                            background-params
                            foreground-params
                            container-params
                            [:object-name :parent])

    (re-frame/dispatch [::state/register-object wrapped-progress-bar])))