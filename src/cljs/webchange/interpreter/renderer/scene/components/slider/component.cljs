(ns webchange.interpreter.renderer.scene.components.slider.component
  (:require
    [webchange.interpreter.renderer.pixi :refer [Container Sprite WHITE]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.scene.components.slider.wrapper :refer [wrap]]))

(defn- update-value
  [state]
  (let [{:keys [background foreground value]} @state]
    (->> value
         (* (.-width background))
         (aset foreground "width"))))

(defn- set-value
  [state value]
  (let [{:keys [on-change]} @state
        fixed-value (->> value (Math/min 1) (Math/max 0))]
    (swap! state assoc :value fixed-value)
    (update-value state)
    (on-change fixed-value)))

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
  (swap! state assoc :touched? false))

(def default-props {:x         {}
                    :y         {}
                    :width     {}
                    :on-change {}
                    :value     {}
                    :height    {:default 24}
                    :color     {:default 0x2c9600}
                    :fill      {:default 0xffffff}
                    :min-value {:default 0}
                    :max-value {:default 100}})

(defn- create-container
  [{:keys [x y]}]
  (let [position {:x x
                  :y y}]
    (doto (Container.)
      (utils/set-position position))))

(defn- create-background
  [{:keys [width height fill]} state]
  (doto (Sprite. WHITE)
    (aset "width" width)
    (aset "height" height)
    (aset "tint" fill)
    (utils/set-handler "pointerdown" (partial handle-pointer-down state))
    (utils/set-handler "pointermove" (partial handle-pointer-move state))
    (utils/set-handler "pointerup" (partial handle-pointer-up state))
    (utils/set-handler "pointerupoutside" (partial handle-pointer-up state))))

(defn- create-foreground
  [{:keys [color height]}]
  (doto (Sprite. WHITE)
    (aset "height" height)
    (aset "tint" color)))

(def component-type "slider")

(defn create
  [parent props]
  (let [{:keys [type object-name value]} props]
    (let [state (atom {:value      0
                       :touched?   false
                       :container  nil
                       :background nil
                       :foreground nil
                       :on-change  #()})
          set-value (let [{:keys [min-value max-value on-change]} props]
                      (swap! state assoc :on-change (fn [value] (on-change (+ min-value (* value (- max-value min-value))))))
                      (partial (fn [min-value max-value state value]
                                 (swap! state assoc :value (-> value
                                                               (/ (- max-value min-value))
                                                               (Math/min 1)
                                                               (Math/max 0)))
                                 (update-value state))
                               min-value max-value state))

          container (create-container props)
          background (create-background props state)
          foreground (create-foreground props)

          wrapped-slider (wrap type object-name set-value)]
      (swap! state assoc :container container)
      (swap! state assoc :background background)
      (swap! state assoc :foreground foreground)
      (set-value value)

      (.addChild container background)
      (.addChild container foreground)
      (.addChild parent container)

      wrapped-slider)))
