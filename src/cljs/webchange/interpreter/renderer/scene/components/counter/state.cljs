(ns webchange.interpreter.renderer.scene.components.counter.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.events-utils :refer [reg-simple-effect-executor]]))

(reg-simple-effect-executor :counter-inc)

(re-frame/reg-fx
  :counter-inc
  (fn [{:keys [component-wrapper]}]
    ((:inc component-wrapper))))
