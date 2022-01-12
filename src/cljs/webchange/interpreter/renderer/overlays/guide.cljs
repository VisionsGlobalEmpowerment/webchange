(ns webchange.interpreter.renderer.overlays.guide
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.events :as ie]
    [webchange.interpreter.renderer.overlays.utils :as utils]
    [webchange.interpreter.renderer.state.scene :as scene]
    [webchange.interpreter.renderer.scene.modes.modes :as modes]))


(defn show-overlay?
  [mode]
  true)

(defn create
  [{:keys [viewport]}]
  {:type        "group"
   :object-name :guide-overlay
   :visible     true
   :mask {:x 1500
          :y 600
          :width 420
          :height 480}
   :filters [{:name "brightness" :value 0}
             {:name "glow" :outer-strength 0 :color 0xffd700}]
   :children    [{:type        "image"
                  :src         "/raw/img/ui/guide/bg-01.png"
                  :x 1579
                  :y 766
                  :object-name :guide-background}
                 {:type "animation",
                  :x 1785
                  :y 1175
                  :scale {:x -0.37, :y 0.37},
                  :anim "idle",
                  :meshes true,
                  :name "teacher",
                  :skin "default",
                  :speed 1,
                  :idle-animation-enabled? false
                  :start true
                  :visible true
                  :object-name :guide}]})

(defn update-viewport
  [{:keys [viewport]}]
  )
