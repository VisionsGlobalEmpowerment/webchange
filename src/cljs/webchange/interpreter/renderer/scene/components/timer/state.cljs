(ns webchange.interpreter.renderer.scene.components.timer.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.events-utils :refer [reg-simple-effect-executor]]))

(reg-simple-effect-executor :timer-start)

(re-frame/reg-fx
  :timer-start
  (fn [{:keys [component-wrapper]}]
    ((:start component-wrapper))))
