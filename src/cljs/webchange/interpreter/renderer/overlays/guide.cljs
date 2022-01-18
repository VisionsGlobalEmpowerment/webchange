(ns webchange.interpreter.renderer.overlays.guide
  (:require
    [re-frame.core :as re-frame]
    [webchange.common.events :as ce]
    [webchange.interpreter.renderer.scene.modes.modes :as modes]))


(defn show-overlay?
  [mode]
  true)

(defn- show-guide?
  [metadata]
  (get-in metadata [:guide-settings :show-guide] false))

(defn- guide-character
  [metadata]
  (let [character (get-in metadata [:guide-settings :character] "vaca")]
    (case character
      "vaca"
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
       :object-name :guide}

      "vera"
      {:type "animation",
       :x 1785
       :y 1175
       :scale {:x -0.37, :y 0.37},
       :anim "idle",
       :meshes true,
       :name "student",
       :skin "default",
       :speed 1,
       :idle-animation-enabled? false
       :start true
       :visible true
       :object-name :guide}

      "mari"
      {:type "animation",
       :x 1785
       :y 1075
       :scale {:x 0.37, :y 0.37},
       :anim "idle",
       :meshes true,
       :name "guide",
       :skin "default",
       :speed 1,
       :idle-animation-enabled? false
       :start true
       :visible true
       :object-name :guide}

      "lion"
      {:type "animation",
       :x 1785
       :y 1175
       :scale {:x -0.57, :y 0.57},
       :anim "idle",
       :meshes true,
       :name "lion",
       :skin "lion",
       :speed 1,
       :idle-animation-enabled? false
       :start true
       :visible true
       :object-name :guide})))

(defn- with-action
  [guide]
  (-> guide
      (assoc :on-click #(re-frame/dispatch [::ce/execute-action {:type "action"
                                                                 :id "tap-instructions"
                                                                 :display-name "tap-instructions"
                                                                 :user-event?  true}]))))

(defn create
  [{:keys [viewport metadata]}]
  {:type        "group"
   :object-name :guide-overlay
   :visible     (show-guide? metadata)
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
                 (-> (guide-character metadata)
                     (with-action))]})

(defn update-viewport
  [{:keys [viewport]}])
