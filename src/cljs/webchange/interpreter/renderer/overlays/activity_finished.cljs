(ns webchange.interpreter.renderer.overlays.activity-finished
  (:require
    [re-frame.core :as re-frame]
    [webchange.i18n.translate :as i18n]
    [webchange.interpreter.events :as ie]
    [webchange.interpreter.renderer.overlays.utils :as utils]
    [webchange.interpreter.renderer.state.scene :as scene]
    [webchange.interpreter.renderer.scene.modes.modes :as modes]))

(defn show-overlay?
  [mode]
  (some #{mode} [::modes/game ::modes/game-with-nav]))

(def home-button-name :home-button)

(defn- get-background
  []
  {:type        "group"
   :object-name :activity-finished-background-group
   :children    [{:type        "image"
                  :src         "/raw/img/ui/activity_finished/bg.png"
                  :object-name :activity-finished-background}]})

(defn- get-home-button-position
  [viewport]
  (utils/get-coordinates {:viewport   viewport
                          :vertical   "top"
                          :horizontal "left"
                          :object     {:width 96 :height 96}
                          :padding    {:x 86 :y 86}}))

(defn- get-home-button
  [{:keys [viewport]}]
  (merge {:type        "image"
          :src         "/raw/img/ui/activity_finished/home.png"
          :object-name home-button-name
          :on-click    #(re-frame/dispatch [::ie/open-student-dashboard])}
         (get-home-button-position viewport)))

(defn- get-title
  []
  (let [font-size 90]
    {:x               964
     :y               232
     :type            "text"
     :text            (-> @(re-frame/subscribe [::i18n/t [:great-work]])
                          (clojure.string/upper-case)
                          (clojure.string/replace " " "\n"))
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
  [{:keys [viewport]}]
  {:type        "group"
   :object-name :activity-finished-overlay
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
                  :y           147}
                 {:type        "image"
                  :src         "/raw/img/ui/activity_finished/vera.png"
                  :object-name :form-vera
                  :x           828
                  :y           447}
                 {:type        "image"
                  :src         "/raw/img/ui/activity_finished/next.png"
                  :object-name :form-next
                  :x           872
                  :y           748
                  :on-click    #(re-frame/dispatch [::ie/run-next-activity])}
                 (get-title)
                 (get-home-button {:viewport viewport})]})

(defn update-viewport
  [{:keys [viewport]}]
  (re-frame/dispatch [::scene/change-scene-object home-button-name [[:set-position (get-home-button-position viewport)]]]))
