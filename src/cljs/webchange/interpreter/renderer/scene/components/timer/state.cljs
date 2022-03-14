(ns webchange.interpreter.renderer.scene.components.timer.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.events-utils :refer [reg-simple-effect-executor]]))

(reg-simple-effect-executor :timer-start)
(reg-simple-effect-executor :timer-reset)

(re-frame/reg-fx
  :timer-start
  (fn [{:keys [component-wrapper]}]
    ((:start component-wrapper))))

(re-frame/reg-fx
  :timer-reset
  (fn [{:keys [component-wrapper]}]
    ((:reset component-wrapper))))
