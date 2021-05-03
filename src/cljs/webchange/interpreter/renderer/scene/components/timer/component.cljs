(ns webchange.interpreter.renderer.scene.components.timer.component
  (:require
    [webchange.interpreter.pixi :refer [Container]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.scene.components.timer.wrapper :refer [wrap]]
    [webchange.interpreter.renderer.scene.components.timer.clock.clock :refer [clock]]
    [webchange.interpreter.renderer.scene.components.timer.progress-circle.progress :refer [progress]]
    [webchange.interpreter.renderer.scene.filters.filters :refer [apply-filters]]
    [webchange.interpreter.renderer.scene.components.timer.utils :refer [time->min-sec]]))

(def default-props {:x             {}
                    :y             {}
                    :time          {:default 0}
                    :show-minutes  {:default true}
                    :show-progress {:default false}
                    :color         {:default 0xff9000}
                    :thickness     {:default 15}
                    :ref           {}
                    :font-family "Luckiest Guy"
                    :font-size   68
                    :font-weight "normal"
                    :progress-color 0xff9000
                    :on-end        {:default #()}
                    :filters         {}})

(def component-type "timer")

(defn- create-container
  [{:keys [x y]}]
  (doto (Container.)
    (utils/set-position {:x x
                         :y y})))

(defn- get-clock-params
  [{:keys [color show-minutes time] :as props}]
  (merge (time->min-sec time)
         {:show-minutes? show-minutes
          :color         color}
         (select-keys props [:font-family :font-size :font-weight])
         ))

(defn- get-progress-params
  [clock-size {:keys [color progress thickness progress-color]
               :or   {progress 1}}]
  {:x         (/ (:width clock-size) 2)
   :y         (+ (/ (:height clock-size) 2) 0)
   :progress  progress
   :radius    (+ (/ (:width clock-size) 2) 20)
   :thickness thickness
   :color     (if progress-color progress-color color)})

(defn create
  [{:keys [parent type ref object-name show-progress time on-end filters] :as props}]
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

    (apply-filters container filters)
    (apply-filters clock filters)

    (when show-progress (.addChild container progress))

    (when-not (nil? ref) (ref wrapped-timer))

    wrapped-timer))
