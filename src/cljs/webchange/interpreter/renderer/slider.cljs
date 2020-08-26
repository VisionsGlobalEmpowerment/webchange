(ns webchange.interpreter.renderer.slider
  (:require
    [cljsjs.pixi]
    [re-frame.core :as re-frame]
    [webchange.interpreter.renderer.state.scene :as state]
    [webchange.interpreter.renderer.common-utils :as utils]
    [webchange.interpreter.renderer.slider-wrapper :refer [wrap]]))

(def Container (.. js/PIXI -Container))
(def Sprite (.. js/PIXI -Sprite))
(def WHITE (.. js/PIXI -Texture -WHITE))

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

(def default-params {:x         :x
                     :y         :y
                     :width     :width
                     :height    {:name    :height
                                 :default 24}
                     :color     {:name    :color
                                 :default 0x2c9600}
                     :fill      {:name    :fill
                                 :default 0xffffff}
                     :on-change :on-change
                     :min-value {:name    :min-value
                                 :default 0}
                     :max-value {:name    :max-value
                                 :default 100}})
(defn- pick-params
  [params-names]
  (->> params-names
       (select-keys default-params)
       (map second)))

(def container-params (pick-params [:x :y]))
(def background-params (pick-params [:width :height :fill]))
(def foreground-params (pick-params [:color :height]))

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

(defn- get-name
  [props]
  (str "Slider <" (:name props) ">"))

(defn create-slider
  [parent props]
  (let [{:keys [object-name value]} props]
    (let [state (atom {:value      0
                       :touched?   false
                       :container  nil
                       :background nil
                       :foreground nil
                       :on-change  #()})
          set-value (let [{:keys [min-value max-value on-change]} (utils/get-specific-params props (pick-params [:min-value :max-value :on-change]))]
                      (swap! state assoc :on-change (fn [value] (on-change (+ min-value (* value (- max-value min-value))))))
                      (partial (fn [min-value max-value state value]
                                 (swap! state assoc :value (-> value
                                                               (/ (- max-value min-value))
                                                               (Math/min 1)
                                                               (Math/max 0)))
                                 (update-value state))
                               min-value max-value state))

          container (create-container (utils/get-specific-params props container-params))
          background (create-background (utils/get-specific-params props background-params) state)
          foreground (create-foreground (utils/get-specific-params props foreground-params))

          wrapped-slider (wrap object-name set-value)]
      (swap! state assoc :container container)
      (swap! state assoc :background background)
      (swap! state assoc :foreground foreground)
      (set-value value)

      (.addChild container background)
      (.addChild container foreground)
      (.addChild parent container)

      (utils/check-rest-props (get-name props)
                              props
                              container-params
                              background-params
                              foreground-params
                              [:object-name :parent :value :on-change :min-value :max-value])

      (re-frame/dispatch [::state/register-object wrapped-slider]))))
