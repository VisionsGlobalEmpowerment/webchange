(ns webchange.interpreter.renderer.scene.components.animation.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]
    [webchange.interpreter.renderer.scene.components.animation.utils :as utils]))

(defn wrap
  [type name container spine-object]
  (create-wrapper {:name             name
                   :type             type
                   :object           container
                   :set-slot         (fn [slot-name image-src slot-params]
                                       (utils/set-animation-slot image-src spine-object slot-name slot-params))
                   :set-skin         (fn [skin-name]
                                       (utils/set-skin spine-object skin-name))
                   :add-animation    (fn [track animation-name loop? delay]
                                       (utils/add-animation spine-object animation-name {:track-index track
                                                                                         :delay       delay
                                                                                         :loop?       loop?}))
                   :set-animation    (fn [track animation-name loop?]
                                       (utils/set-animation spine-object animation-name {:track-index track
                                                                                         :loop?       loop?}))
                   :remove-animation (fn [track mix-duration]
                                       (utils/set-empty-animation spine-object {:track-index  track
                                                                                :mix-duration mix-duration}))
                   :start-animation  (fn []
                                       (utils/set-auto-update spine-object true))}))
