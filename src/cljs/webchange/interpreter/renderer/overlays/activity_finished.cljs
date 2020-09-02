(ns webchange.interpreter.renderer.overlays.activity-finished
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.events :as ie]
    [webchange.interpreter.renderer.state.overlays :as overlays]
    [webchange.interpreter.renderer.scene.components.create-component :refer [create-component]]
    [webchange.interpreter.renderer.overlays.utils :as utils]
    [webchange.interpreter.utils.i18n :refer [t]]))

(def menu-padding {:x 20 :y 20})

(defn- get-background
  [{:keys [x y width height]}]
  (let [border-width 10]
    {:type        "group"
     :object-name :activity-finished-background-group
     :children    [{:type        "image"
                    :src         "/raw/img/bg.jpg"
                    :object-name :activity-finished-background}
                   {:type            "rectangle"
                    :object-name     :activity-finished-frame
                    :x               x
                    :y               y
                    :width           width
                    :height          height
                    :border-color    0xbd13c7
                    :border-width    border-width
                    :border-radius   56
                    :fill            0xda12ea
                    :shadow-color    0x75016e
                    :shadow-distance 15}]}))

(defn- get-top-menu
  [{:keys [viewport on-close-click]}]
  (merge {:type        "image"
          :src         (get-in utils/common-elements [:close-button :src])
          :object-name :close-overall-progress
          :on-click    on-close-click}
         (utils/get-coordinates {:viewport   viewport
                                 :vertical   "top"
                                 :horizontal "right"
                                 :object     (get-in utils/common-elements [:close-button :size])
                                 :padding    menu-padding})))

(defn- get-title
  [{:keys [x y]}]
  (let [font-size 102]
    {:x               x
     :y               (- y (/ font-size 1.5))
     :type            "text"
     :text            "Nice Work!"
     :object-name     :overall-progress-title
     :align           "center"
     :vertical-align  "top"
     :font-size       font-size
     :font-family     "Luckiest Guy"
     :fill            0xffffff
     :shadow-color    0x1a1a1a
     :shadow-distance 10
     :shadow-blur     10
     :shadow-opacity  0.5}))

(defn- get-vera
  [{:keys [x y]}]
  (let [image-size {:width  315
                    :height 371}]
    {:type        "image"
     :src         "/raw/img/ui/vera_315x371.png"
     :object-name :activity-finished-vera
     :x           (- x (/ (:width image-size) 2))
     :y           (- y (:height image-size))}))

(defn- get-overall-progress
  [{:keys [x y width]}]
  {:type        "group"
   :object-name :overall-group
   :x           x
   :y           y
   :children    [{:x           0
                  :y           0
                  :type        "text"
                  :text        "Overall Progress"
                  :object-name :overall-progress-text
                  :align       "left"
                  :font-size   42
                  :font-family "Lexend Deca"
                  :font-weight "bold"
                  :fill        0xffffff}
                 {:x           0
                  :y           80
                  :type        "progress"
                  :object-name :overall-progress-bar
                  :width       width
                  :height      25
                  :value       0}]})

(defn- get-next-activity-card
  [{:keys [x y on-click]}]
  (let [card-width 400
        card-height 375
        border-radius 30
        image-width 400
        image-height 225
        text-padding 40]
    {:type        "group"
     :object-name :next-activity-card-group
     :x           x
     :y           y
     :on-click    on-click
     :children    [{:type          "rectangle"
                    :object-name   :next-activity-card-background
                    :x             0
                    :y             0
                    :width         card-width
                    :height        card-height
                    :fill          0xffffff
                    :border-color  0xffffff
                    :border-width  1
                    :border-radius border-radius}
                   {:type        "image"
                    :object-name :next-activity-card-image
                    :x           0
                    :y           0
                    :width       image-width
                    :height      image-height}
                   {:type           "text"
                    :object-name    :next-activity-card-name
                    :x              text-padding
                    :y              (+ image-height text-padding)
                    :align          "left"
                    :vertical-align "top"
                    :font-size      32
                    :font-family    "Lexend Deca"
                    :fill           0x000000}]}))

(defn- get-featured-content
  [{:keys [x y on-click]}]
  {:type        "group"
   :object-name :featured-content-group
   :x           x
   :y           y
   :children    [{:x           0
                  :y           0
                  :type        "text"
                  :text        "Featured Content"
                  :object-name :featured-content-text
                  :align       "left"
                  :font-size   42
                  :font-family "Lexend Deca"
                  :font-weight "bold"
                  :fill        0xffffff}
                 (get-next-activity-card {:x        0
                                          :y        80
                                          :on-click on-click})]})

(defn- get-menu
  [{:keys [x y on-restart-click on-next-click]}]
  (let [menu-height 100]
    {:type        "group"
     :object-name :overall-progress-menu
     :x           x
     :y           (- y (/ menu-height 2))
     :children    [{:type        "image"
                    :src         "/raw/img/ui/reload_button_01.png"
                    :object-name :overall-progress-menu-reset
                    :x           0
                    :y           0
                    :on-click    on-restart-click}
                   {:type        "button"
                    :object-name :overall-progress-menu-next
                    :x           130
                    :on-click    on-next-click
                    :text        (t "next")}]}))

(defn create-activity-finished-overlay
  [{:keys [parent viewport]}]
  (let [go-to-next-activity #(re-frame/dispatch [::ie/run-next-activity])
        restart-activity #(re-frame/dispatch [::ie/restart-scene])
        continue-playing #(re-frame/dispatch [::ie/next-scene])
        close-screen #(re-frame/dispatch [::overlays/close-activity-finished])
        score-window-size {:width  1200
                           :height 660}
        score-window-position (utils/get-coordinates {:viewport   viewport
                                                      :vertical   "center"
                                                      :horizontal "center"
                                                      :object     score-window-size})
        title-width 553]
    (create-component parent {:type        "group"
                              :parent      parent
                              :object-name :activity-finished-overlay
                              :visible     false
                              :children    [(get-background (merge score-window-position
                                                                   score-window-size))
                                            (get-top-menu {:viewport       viewport
                                                           :on-close-click close-screen})
                                            (get-title (merge {:y (:y score-window-position)}
                                                              (utils/get-coordinates {:viewport   viewport
                                                                                      :horizontal "center"
                                                                                      :object     title-width})))
                                            (get-vera {:x (+ (:x score-window-position)
                                                             (/ (:width score-window-size) 4))
                                                       :y (+ (:y score-window-position)
                                                             (:height score-window-size))})
                                            (get-overall-progress {:x     (+ (:x score-window-position) 65)
                                                                   :y     (+ (:y score-window-position) 120)
                                                                   :width (- (/ (:width score-window-size) 2) 130)})
                                            (get-featured-content {:x        (+ (:x score-window-position)
                                                                                (/ (:width score-window-size) 2)
                                                                                65)
                                                                   :y        (+ (:y score-window-position) 120)
                                                                   :on-click go-to-next-activity})
                                            (get-menu {:x                (+ (:x score-window-position) 20)
                                                       :y                (+ (:y score-window-position)
                                                                            (:height score-window-size) 10)
                                                       :on-restart-click restart-activity
                                                       :on-next-click    continue-playing})]})))