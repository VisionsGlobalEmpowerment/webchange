(ns webchange.interpreter.renderer.overlays.goodbye
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.renderer.overlays.utils :as utils]
    [webchange.interpreter.renderer.state.scene :as scene]
    [webchange.interpreter.renderer.scene.modes.modes :as modes]
    [webchange.interpreter.utils.i18n :refer [t]]))

(defn show-overlay?
  [mode]
  (some #{mode} [::modes/game ::modes/game-with-nav ::modes/sandbox]))


(def goodbye-window-name :goodbye-window)
(def goodbye-window-size {:width  600
                          :height 600})

(defn- get-goodbye-window-position
  [viewport]
  (utils/get-coordinates {:viewport   viewport
                          :vertical   "center"
                          :horizontal "center"
                          :object     goodbye-window-size}))

(defn- get-background
  []
  {:type        "group"
   :object-name :goodbye-background-group
   :children    [{:type        "image"
                  :src         "/raw/img/bg.png"
                  :object-name :goodbye-background}]})

(defn- get-title
  []
  (let [font-size 102]
    {:x              (/ (:width goodbye-window-size) 2)
     :y              (+ (:height goodbye-window-size) 20)
     :type           "text"
     :text           (t "goodbye!")
     :object-name    :goodbye-title
     :align          "center"
     :vertical-align "bottom"
     :font-size      font-size
     :font-family    "Luckiest Guy"
     :fill           0xffffff}))

(defn- get-character
  [{:keys [x y]}]
  (let [image-size {:width  321
                    :height 350}]
    {:type        "image"
     :src         "/raw/img/ui/ts_321x350.png"
     :object-name :goodbye-ts
     :x           (- x (/ (:width image-size) 2))
     :y           (- y (/ (:height image-size) 2))}))

(defn- get-goodbye-window
  [viewport]
  (merge
    {:type        "group"
     :object-name goodbye-window-name
     :children    [(merge
                     {:type          "rectangle"
                      :object-name   :goodbye-frame
                      :border-radius (/ (:width goodbye-window-size) 2)
                      :fill          0x2d0e7a}
                     goodbye-window-size)
                   (get-title)
                   (get-character {:x (/ (:width goodbye-window-size) 2)
                                   :y (/ (:height goodbye-window-size) 2)})]}
    (get-goodbye-window-position viewport)))

(defn create
  [{:keys [viewport]}]
  {:type        "group"
   :object-name :goodbye-overlay
   :visible     false
   :children    [(get-background)
                 (get-goodbye-window viewport)]})

(defn update-viewport
  [{:keys [viewport]}]
  (re-frame/dispatch [::scene/change-scene-object goodbye-window-name [[:set-position (get-goodbye-window-position viewport)]]]))
