(ns webchange.interpreter.renderer.scene.components.sound-bar.component
  (:require
    [webchange.interpreter.pixi :refer [Container Graphics Sprite Texture WHITE]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.scene.components.sound-bar.wrapper :refer [wrap]]))

(def default-props {:x      {}
                    :y      {}
                    :width  {}
                    :height {}})

(def bar-max-value 16)

(defn- get-item-size
  [total-size items-number gap]
  (Math/ceil (/ (- total-size
                   (* (dec items-number)
                      gap))
                items-number)))

(defn- get-bar-coordinates
  [value size]
  (let [gap 4
        item-width (get-item-size (:width size) (count value) gap)
        item-height (get-item-size (:height size) bar-max-value gap)]
    (->> value
         (map-indexed (fn [bar-idx bar-value]
                        (->> (range bar-value)
                             (map (fn [idx]
                                    {:x      (* bar-idx (+ item-width gap))
                                     :y      (* idx (+ item-height gap))
                                     :width  item-width
                                     :height item-height})))))
         (flatten)
         (map (fn [{:keys [y] :as item}]
                (-> (assoc item :y (- (:height size) y item-height))))))))

(defn- draw-rect
  [graphics {:keys [x y width height]}]
  (doto graphics
    (.beginFill 0x000000)
    (.drawRect x y width height)
    (.endFill 0x000000)))

(defn- create-mask
  [{:keys [value size]}]
  (let [graphics (Graphics.)
        bar-coordinates (get-bar-coordinates value size)]
    (doseq [item bar-coordinates]
      (draw-rect graphics item))
    graphics))

(defn- create-gradient
  [width colors]
  (let [canvas (doto (.createElement js/document "canvas")
                 (aset "width" width)
                 (aset "height" 1))
        ctx (.getContext canvas "2d")
        gradient (.createLinearGradient ctx 0 0 width 0)]
    (doseq [[position color] colors]
      (.addColorStop gradient position color))
    (aset ctx "fillStyle" gradient)
    (.fillRect ctx 0 0 width 1)
    (.from Texture canvas)))

(defn- create-sprite
  [{:keys [width height]}]
  (let [gradient (create-gradient width [[0 "#ec96bd"] [1 "#90268e"]])]
    (doto (Sprite. gradient)
      (aset "width" width)
      (aset "height" height))))

(defn- create-container
  [{:keys [x y]}]
  (doto (Container.)
    (utils/set-position {:x x :y y})))

(def component-type "sound-bar")

(defn create
  "Create `sound-bar` component.

  Props params:
  :x - component x-position.
  :y - component y-position.

"                                                           ;; ToDo: Update docs
  [{:keys [parent type object-name] :as props}]
  (let [bars-value [3 6 4 9 6 4 9 12 15 9 5 3 6 4 6 9 12 5]
        mask (create-mask {:value bars-value
                           :size  (select-keys props [:width :height])})
        sprite (create-sprite props)
        container (create-container props)
        wrapped-component (wrap type object-name container)]

    (aset sprite "mask" mask)
    (.addChild container sprite)
    (.addChild container mask)
    (.addChild parent container)

    wrapped-component))
