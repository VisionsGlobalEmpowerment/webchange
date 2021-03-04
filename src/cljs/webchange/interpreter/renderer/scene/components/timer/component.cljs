(ns webchange.interpreter.renderer.scene.components.timer.component
  (:require
    [webchange.interpreter.pixi :refer [Container]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.scene.components.timer.wrapper :refer [wrap]]
    [webchange.interpreter.renderer.scene.components.timer.clock.clock :refer [clock]]
    [webchange.interpreter.renderer.scene.components.timer.progress-circle.progress :refer [progress]]
    [webchange.interpreter.renderer.scene.components.timer.utils :refer [time->min-sec]]))

(def default-props {:x             {}
                    :y             {}
                    :time          {:default 0}
                    :show-minutes  {:default true}
                    :show-progress {:default false}
                    :color         {:default 0xff9000}
                    :thickness     {:default 15}
                    :ref           {}
                    :on-end        {:default #()}})

(def component-type "timer")

(defn- create-container
  [{:keys [x y]}]
  (doto (Container.)
    (utils/set-position {:x x
                         :y y})))

(defn- get-clock-params
  [{:keys [color show-minutes time]}]
  (merge (time->min-sec time)
         {:show-minutes? show-minutes
          :color         color}))

(defn- get-progress-params
  [clock-size {:keys [color progress thickness]
               :or   {progress 1}}]
  {:x         (/ (:width clock-size) 2)
   :y         (+ (/ (:height clock-size) 2) 5)
   :progress  progress
   :radius    (+ (/ (:width clock-size) 2) 20)
   :thickness thickness
   :color     color})

(defn create
  [{:keys [parent type ref object-name show-progress time on-end] :as props}]
  (let [state (atom {:initial-time time
                     :current-time time})
        container (create-container props)

        {clock :component set-clock-value :set-value clock-size :size set-clock-activated :set-activated} (clock (get-clock-params props))
        {progress :component set-progress :set-value} (when show-progress
                                                        (progress (get-progress-params clock-size props)))

        wrapped-timer (wrap type object-name container state {:on-end              on-end
                                                              :set-clock-value     set-clock-value
                                                              :set-progress        set-progress
                                                              :set-clock-activated set-clock-activated})]
    (.addChild container clock)
    (.addChild parent container)
    (when show-progress (.addChild container progress))

    (when-not (nil? ref) (ref wrapped-timer))

    wrapped-timer))
