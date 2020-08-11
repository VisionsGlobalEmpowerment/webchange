(ns webchange.interpreter.renderer.animation-wrapper
  (:require
    [webchange.interpreter.renderer.animation-utils :as utils]))

(defn wrap
  [name container spine-object]
  {:name             name
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
   :set-skin         (fn [skin]
                       ;; ToDo:  create set skin
                       (-> (str "Set skin method is not implemented") js/Error. throw))
   :start-animation  (fn []
                       ;; ToDo:  create start animation
                       (-> (str "Start animation method is not implemented") js/Error. throw))
   :remove-animation (fn [track mix-duration]
                       (utils/set-empty-animation spine-object {:track-index  track
                                                                :mix-duration mix-duration}))})
