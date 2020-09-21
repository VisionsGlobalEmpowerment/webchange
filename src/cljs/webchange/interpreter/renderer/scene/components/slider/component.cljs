(ns webchange.interpreter.renderer.scene.components.slider.component
  (:require
    [webchange.interpreter.pixi :refer [Circle Container Graphics Sprite WHITE hex2rgb rgb2hex]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.scene.components.slider.wrapper :refer [wrap]]))

(defn- update-value
  [state]
  (let [{:keys [background foreground control value]} @state
        foreground-width (* value (.-width background))]
    (aset foreground "width" foreground-width)
    (utils/set-position control {:x foreground-width})))

(defn- set-value
  [state value]
  (let [{:keys [on-slide]} @state
        fixed-value (->> value (Math/min 1) (Math/max 0))]
    (swap! state assoc :value fixed-value)
    (update-value state)
    (on-slide fixed-value)))

(defn- handle-pointer-move
  [state event]
  (let [{:keys [touched? container background]} @state]
    (when touched?
      (let [local-x (-> event (.-data) (.getLocalPosition container) (.-x))]
        (set-value state (/ local-x (.-width background)))))))

(defn- handle-pointer-down
  [state event]
  (swap! state assoc :touched? true)
  (handle-pointer-move state event))

(defn- handle-pointer-up
  [state]
  (swap! state assoc :touched? false)
  (let [{:keys [on-change value]} @state]
    (on-change value)))

(defn- set-handlers
  [display-object state]
  (doto display-object
    (utils/set-handler "pointerdown" (partial handle-pointer-down state))
    (utils/set-handler "pointermove" (partial handle-pointer-move state))
    (utils/set-handler "pointerup" (partial handle-pointer-up state))
    (utils/set-handler "pointerupoutside" (partial handle-pointer-up state))))

(def default-props {:x             {}
                    :y             {}
                    :width         {}
                    :on-change     {}
                    :on-slide      {}
                    :value         {}
                    :height        {:default 24}
                    :color         {:default 0x2c9600}
                    :fill          {:default 0xffffff}
                    :min-value     {:default 0}
                    :max-value     {:default 100}
                    :border-radius {:default 10}})

(defn- create-container
  [{:keys [x y]}]
  (let [position {:x x
                  :y y}]
    (doto (Container.)
      (utils/set-position position))))

(defn- create-background
  [{:keys [width height fill]}]
  (doto (Sprite. WHITE)
    (aset "width" width)
    (aset "height" height)
    (aset "tint" fill)))

(defn- create-foreground
  [{:keys [color height]}]
  (doto (Sprite. WHITE)
    (aset "height" height)
    (aset "tint" color)))

(defn- create-control
  [{:keys [color height]} state]
  (let [size (* height 1.5)
        position-shift (/ (- size height) 2)
        color (->> (hex2rgb color) (js->clj) (map #(* 1.1 %)) (clj->js) (rgb2hex))
        sprite (doto (Sprite. WHITE)
                 (aset "width" size)
                 (aset "height" size)
                 (aset "tint" color)
                 (aset "interactive" true)
                 (aset "buttonMode" true)
                 (aset "hitArea" (Circle. position-shift position-shift (/ size 2)))
                 (set-handlers state)
                 (utils/set-position {:x (- (/ size 2))
                                      :y (- position-shift)}))
        mask (doto (Graphics.)
               (.beginFill 0x000000)
               (.drawCircle (/ size 2) (/ size 2) (/ size 2))
               (.endFill 0x000000)
               (utils/set-position {:x (- (/ size 2))
                                    :y (- position-shift)}))
        container (Container.)]
    (aset sprite "mask" mask)
    (.addChild container sprite)
    (.addChild container mask)
    container))

(defn- create-mask
  [{:keys [width height border-radius]}]
  (doto (Graphics.)
    (.beginFill 0x000000)
    (.drawRoundedRect 0 0 width height border-radius)
    (.endFill 0x000000)))

(defn- scale-value
  [value min-value max-value]
  (->> (- max-value min-value)
       (* value)
       (+ min-value)))

(def component-type "slider")

(defn create
  [{:keys [parent type object-name value] :as props}]
  (let [state (atom {:value      0
                     :touched?   false
                     :container  nil
                     :background nil
                     :foreground nil
                     :control    nil
                     :on-change  #()
                     :on-slide   #()})
        set-value (let [{:keys [min-value max-value on-change on-slide]} props]
                    (swap! state assoc :on-change (fn [value] (on-change (scale-value value min-value max-value))))
                    (swap! state assoc :on-slide (fn [value] (on-slide (scale-value value min-value max-value))))
                    (partial (fn [min-value max-value state value]
                               (swap! state assoc :value (-> value
                                                             (/ (- max-value min-value))
                                                             (Math/min 1)
                                                             (Math/max 0)))
                               (update-value state))
                             min-value max-value state))

        container (create-container props)
        background (create-background props)
        foreground (create-foreground props)
        control (create-control props state)
        mask (create-mask props)

        wrapped-slider (wrap type object-name container set-value)]
    (swap! state assoc :container container)
    (swap! state assoc :background background)
    (swap! state assoc :foreground foreground)
    (swap! state assoc :control control)
    (set-value value)

    (.addChild container background)
    (.addChild container foreground)
    (.addChild container control)
    (.addChild parent container)

    (aset background "mask" mask)
    (aset foreground "mask" mask)
    (.addChild container mask)

    wrapped-slider))
