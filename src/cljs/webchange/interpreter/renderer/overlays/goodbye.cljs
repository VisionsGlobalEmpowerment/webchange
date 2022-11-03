(ns webchange.interpreter.renderer.overlays.goodbye
  (:require
    [clojure.string :as str]
    [re-frame.core :as re-frame]
    [webchange.i18n.translate :as i18n]
    [webchange.interpreter.renderer.scene.modes.modes :as modes]))

(defn show-overlay?
  [mode]
  (some #{mode} [::modes/game ::modes/game-with-nav ::modes/sandbox]))

(defn- get-background
  []
  {:type        "group"
   :object-name :activity-finished-background-group
   :children    [{:type        "image"
                  :src         "/raw/img/ui/activity_finished/bg.png"
                  :object-name :activity-finished-background}]})

(defn- get-title
  []
  (let [font-size 90]
    {:x               964
     :y               232
     :type            "text"
     :text            (-> @(re-frame/subscribe [::i18n/t [:great-work]])
                          (str/upper-case)
                          (str/replace " " "\n"))
     :object-name     :form-title
     :vertical-align  "top"
     :align           "center"
     :font-size       font-size
     :font-family     "Luckiest Guy"
     :fill            0xffffff
     :line-height     98
     :shadow-color    0xa6006d
     :shadow-distance 12
     :shadow-blur     16
     :shadow-angle    1.5
     :shadow-opacity  1}))

(defn create
  [{:keys [_viewport]}]
  {:type        "group"
   :object-name :goodbye-overlay
   :visible     false
   :children    [(get-background)
                 {:type        "animation",
                  :x           960
                  :y           590
                  :scale       {:x 1, :y 1},
                  :anim        "opt-1",
                  :meshes      true,
                  :name        "ui-shooting-star",
                  :skin        "default",
                  :speed       1,
                  :start       false,
                  :visible     true
                  :object-name :form-shooting-star}
                 {:type        "image"
                  :src         "/raw/img/ui/activity_finished/form.png"
                  :object-name :form-bg
                  :x           612
                  :y           146}
                 {:type        "image"
                  :src         "/raw/img/ui/activity_finished/student.png"
                  :object-name :form-vera
                  :x           784
                  :y           418}
                 (get-title)]})

(defn update-viewport
  [{:keys [_viewport]}])

