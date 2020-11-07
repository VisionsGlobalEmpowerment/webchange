(ns webchange.interpreter.renderer.scene.components.progress.component
  (:require
    [webchange.interpreter.pixi :refer [Container Graphics Sprite WHITE]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.scene.filters.filters :refer [apply-outline-filter]]
    [webchange.interpreter.renderer.scene.components.progress.wrapper :refer [wrap]]))

(def default-props {:x                {}
                    :y                {}
                    :width            {}
                    :height           {:default 25}
                    :value            {:default 0.2}
                    :background-color {:default 0xffffff}
                    :foreground-color {:default 0x00c3ff}
                    :border-color     {:default 0x8a0f91}
                    :border-width     {:default 1}
                    :border-radius    {:default 10}})

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

(def component-type "progress")

(defn create
  "Create `progress` component. Shows a progress bar

  Props params:
  :x - component x-position.
  :y - component y-position.
  :width - image width.
  :height - image height.
  :value - value of progress bar. Default 0.2
  :background-color - background color of progress bar. Default 0xffffff
  :foreground-color - progress color. Default 0x2c9600
  :border-color - color of border. Default 0x8a0f91
  :border-width - width of border. Default 1
  :border-radius - make rounded corners. Radius in pixels. Default 10"
  [{:keys [parent type object-name width] :as props}]
  (let [container (create-container props)
        background (create-background props)
        foreground (create-foreground (merge props {:background-mask (:mask background)}))
        wrapped-progress-bar (wrap type object-name container {:set-value (fn [value]
                                                                            (aset (:sprite foreground) "width" (* value width)))})]

    (.addChild container (:mask background))
    (.addChild container (:sprite background))
    (.addChild container (:sprite foreground))
    (.addChild parent container)

    wrapped-progress-bar))
