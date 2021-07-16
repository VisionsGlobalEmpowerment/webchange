(ns webchange.interpreter.renderer.scene.components.animation.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]
    [webchange.interpreter.renderer.scene.filters.filters :as f]
    [webchange.interpreter.renderer.scene.components.animation.utils :as utils]))

(defn wrap
  [type name container spine-object]
  (create-wrapper {:name             name
                   :type             type
                   :object           container
                   :set-highlight    (fn [highlight]
                                       (let [highlight-filter-set (f/has-filter-by-name spine-object "glow")]
                                         (if (and (not highlight) highlight-filter-set) (f/set-filter spine-object "" {}))
                                         (if (and highlight (not highlight-filter-set))
                                           (f/set-filter spine-object "glow" {}))))
                   :set-slot         (fn [slot-name image-src slot-params]
                                       ;; ToDo: Remove double set-animation-slot call
                                       ;; Without this slot is not updated if new skin is created inside the method
                                       ;; If do no crete new skin and hack current skin, then updated slot has incorrect size
                                       ;; See https://trello.com/c/zCet3flh
                                       (utils/set-animation-slot image-src spine-object slot-name slot-params)
                                       (utils/set-animation-slot image-src spine-object slot-name slot-params))
                   :set-skin         (fn [skin-name]
                                       (utils/set-skin spine-object skin-name))
                   :set-combined-skin
                                     (fn [skin-names]
                                       (utils/set-combined-skin spine-object skin-names))
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
