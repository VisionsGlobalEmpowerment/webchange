(ns webchange.interpreter.renderer.scene.filters.filters-alpha-pulsation
  (:require
    [webchange.interpreter.renderer.scene.filters.filters-pulsation :as fp]
    [webchange.interpreter.renderer.scene.components.utils :as utils]))

(def intervals {:play 2 :pause 6})
(def interval-duration 800)

(defn animation-eager
  [object frame state speed no-interval]
  (fp/init-state! state (:time frame) interval-duration)
  (let [speed (if (not speed) 1 speed)
        max 1
        min 0.6
        time (cond-> (fp/get-time (:time frame) interval-duration state)
                     (not no-interval) (fp/apply-intervals! (:time frame) state intervals interval-duration)
                     true (fp/get-time-cycled speed)
                     )
        alpha (+ min (/  (/ (+ 1 time) 2) (+ max min)))]
    (utils/set-opacity object alpha)))
