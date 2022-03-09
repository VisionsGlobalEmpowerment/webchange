(ns webchange.interpreter.renderer.scene.components.timer.component
  (:require
    [webchange.interpreter.pixi :refer [Container WHITE Graphics Sprite]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.scene.components.timer.wrapper :refer [wrap]]
    [webchange.interpreter.renderer.scene.components.timer.clock.clock :refer [clock]]
    [webchange.interpreter.renderer.scene.components.timer.progress-circle.progress :refer [progress]]
    [webchange.interpreter.renderer.scene.filters.filters :refer [apply-filters]]
    [webchange.interpreter.renderer.scene.components.timer.utils :refer [time->min-sec]]))

(def default-props {:x                 {}
                    :y                 {}
                    :time              {:default 0}
                    :show-minutes      {:default true}
                    :show-progress     {:default false}
                    :show-leading-zero {:default true}
                    :size              {}
                    :color             {:default 0xff9000}
                    :thickness         {:default 60}
                    :ref               {}
                    :font-family       "Luckiest Guy"
                    :font-size         68
                    :font-weight       "normal"
                    :progress-color    0xff9000
                    :on-end            {:default #()}
                    :filters           {}})

(def component-type "timer")

(defn- create-container
  [{:keys [x y]}]
  (doto (Container.)
    (utils/set-position {:x x
                         :y y})))

(defn- get-clock-params
  [{:keys [color show-leading-zero show-minutes time] :as props}]
  (merge (time->min-sec time)
         {:show-leading-zero? show-leading-zero
          :show-minutes?      show-minutes
          :color              color}
         (select-keys props [:font-family :font-size :font-weight])))

(defn- get-progress-params
  [{:keys [color size progress thickness progress-color]
    :or   {progress 1}}]
  (let [thickness-half (-> (/ thickness 2) (Math/ceil))
        progress-size size
        progress-half-size (-> progress-size (/ 2) (Math/floor))]
    {:x         progress-half-size
     :y         progress-half-size
     :radius    (- progress-half-size thickness-half)
     :progress  progress
     :thickness thickness
     :color     (if progress-color progress-color color)}))

(defn- debug-position
  [container size]
  (let [create-rectangle (fn [{:keys [x y width height color]}]
                           (doto (Sprite. WHITE)
                             (aset "tint" color)
                             (aset "width" width)
                             (aset "height" height)
                             (utils/set-position {:x x :y y})))]
    (.addChild container (create-rectangle {:x 0 :y 0 :width size :height size :color 0x70bb4b}))
    (.addChild container (create-rectangle (let [rect-size 50]
                                             {:x      (-> (- size rect-size) (/ 2))
                                              :y      (-> (- size rect-size) (/ 2))
                                              :width  rect-size
                                              :height rect-size
                                              :color  0x4b86bb})))))

(defn- move-clock-to-center
  [clock size]
  (let [clock-bounds (utils/get-local-bounds clock)]
    (utils/set-position clock {:x (- (/ size 2)
                                     (/ (:width clock-bounds) 2)
                                     (:x clock-bounds))
                               :y (- (/ size 2)
                                     (/ (:height clock-bounds) 2)
                                     (:y clock-bounds))})))

(defn create
  [{:keys [parent size type ref object-name show-progress on-end filters] :as props}]
  (let [state (atom {:initial-time (:time props)
                     :current-time (:time props)})
        container (create-container props)

        {clock :component set-clock-value :set-value set-clock-activated :set-activated} (clock (get-clock-params props))
        {progress :component set-progress :set-value} (when show-progress
                                                        (progress (get-progress-params props)))

        wrapped-timer (wrap type object-name container state {:on-end              on-end
                                                              :set-clock-value     #(do (set-clock-value %)
                                                                                        (move-clock-to-center clock size))
                                                              :set-progress        set-progress
                                                              :set-clock-activated set-clock-activated})]

    (.addChild parent container)
    #_(debug-position container size)
    (.addChild container clock)
    (move-clock-to-center clock size)
    (apply-filters container filters)
    (apply-filters clock filters)

    (when show-progress (.addChild container progress))

    (when-not (nil? ref) (ref wrapped-timer))

    wrapped-timer))
