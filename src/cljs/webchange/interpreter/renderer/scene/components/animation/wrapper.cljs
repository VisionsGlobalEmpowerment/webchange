(ns webchange.interpreter.renderer.scene.components.animation.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]
    [webchange.interpreter.renderer.scene.components.animation.utils :as utils]))

(defn wrap
  [type name container spine-object]
  (create-wrapper {:name             name
                   :type             type
                   :get-position     (fn []
                                       (utils/get-position container))
                   :set-position     (fn [position]
                                       (utils/set-position container position))
                   :set-scale-x      (fn [scale]
                                       (utils/set-scale container {:x scale}))
                   :set-scale-y      (fn [scale]
                                       (utils/set-scale container {:y scale}))
                   :set-slot         (fn [slot-name image-src slot-params]
                                       (utils/set-animation-slot image-src spine-object slot-name slot-params))
                   :add-animation    (fn [track animation-name loop? delay]
                                       (utils/add-animation spine-object animation-name {:track-index track
                                                                                         :delay       delay
                                                                                         :loop?       loop?}))
                   :set-animation    (fn [track animation-name loop?]
                                       (utils/set-animation spine-object animation-name {:track-index track
                                                                                         :loop?       loop?}))
                   :remove-animation (fn [track mix-duration]
                                       (utils/set-empty-animation spine-object {:track-index  track
                                                                                :mix-duration mix-duration}))}))
