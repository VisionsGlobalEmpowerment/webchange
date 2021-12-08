(ns webchange.interpreter.renderer.overlays.activity-finished
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.events :as ie]
    [webchange.interpreter.renderer.state.overlays :as overlays]
    [webchange.interpreter.renderer.overlays.utils :as utils]
    [webchange.interpreter.renderer.state.scene :as scene]
    [webchange.interpreter.renderer.scene.modes.modes :as modes]
    [webchange.interpreter.utils.i18n :refer [t]]))

(defn show-overlay?
  [mode]
  (some #{mode} [::modes/game ::modes/game-with-nav]))

(def menu-padding {:x 20 :y 20})

(def top-menu-name :close-overall-progress)
(defn- get-top-menu-position
  [viewport]
  (utils/get-coordinates {:viewport   viewport
                          :vertical   "top"
                          :horizontal "right"
                          :object     (get-in utils/common-elements [:close-button :size])
                          :padding    menu-padding}))

(def score-window-name :score-window)

(def score-window-size {:width  1200
                        :height 660})

(defn- get-score-window-position
  [viewport]
  (utils/get-coordinates {:viewport   viewport
                          :vertical   "center"
                          :horizontal "center"
                          :object     score-window-size}))

(defn- get-background
  []
  {:type        "group"
   :object-name :activity-finished-background-group
   :children    [{:type        "image"
                  :src         "/raw/img/bg.png"
                  :object-name :activity-finished-background}]})

(defn- get-top-menu
  [{:keys [viewport on-close-click]}]
  (merge {:type        "image"
          :src         (get-in utils/common-elements [:close-button :src])
          :object-name top-menu-name
          :on-click    on-close-click}
         (get-top-menu-position viewport)))

(defn- get-title
  []
  (let [font-size 102]
    {:x               (/ (:width score-window-size) 2)
     :y               (/ font-size -1.5)
     :type            "text"
     :text            "Great Job!"
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

(defn- get-character
  [{:keys [x y]}]
  (let [image-size {:width  287
                    :height 385}]
    {:type        "image"
     :src         "/raw/img/ui/ts_287x385.png"
     :object-name :activity-finished-ts
     :x           (- x (/ (:width image-size) 2))
     :y           (- y (:height image-size))}))

(defn- get-overall-progress
  [{:keys [x y width]}]
  {:type        "group"
   :object-name :overall-group
   :x           x
   :y           y
   :children    [{:x           0
                  :y           50
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
                   {:type          "image"
                    :object-name   :next-activity-card-image
                    :x             0
                    :y             0
                    :width         image-width
                    :height        image-height
                    :image-size    "cover"
                    :border-radius [(- border-radius 5) (- border-radius 5) 0 0]}
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
                  :y           50
                  :type        "text"
                  :text        "Next"
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
     :children    [#_{:type        "image"
                      :src         "/raw/img/ui/reload_button_01.png"
                      :object-name :overall-progress-menu-reset
                      :x           0
                      :y           0
                      :on-click    on-restart-click}
                   {:type        "button"
                    :object-name :overall-progress-menu-next
                    :x           60
                    :on-click    on-next-click
                    :text        (t "continue")}]}))

(defn- get-score-window
  [viewport]
  (let [go-to-next-activity #(re-frame/dispatch [::ie/run-next-activity])
        ;restart-activity #(re-frame/dispatch [::ie/restart-scene])
        ;continue-playing #(re-frame/dispatch [::ie/next-scene])
        ]
    (merge
      {:type        "group"
       :object-name score-window-name
       :children    [(merge
                       {:type          "rectangle"
                        :object-name   :activity-finished-frame
                        :border-radius 56
                        :fill          0x2d0e7a}
                       score-window-size)
                     (get-title)
                     (get-character {:x (/ (:width score-window-size) 4)
                                     :y (:height score-window-size)})
                     #_(get-overall-progress {:x     65
                                            :y     120
                                            :width (- (/ (:width score-window-size) 2) 130)})
                     (get-featured-content {:x        (+ (/ (:width score-window-size) 2) 65)
                                            :y        120
                                            :on-click go-to-next-activity})
                     (get-menu {:x             20
                                :y             (+ (:height score-window-size) 10)
                                ;:on-restart-click restart-activity
                                :on-next-click go-to-next-activity})]}
      (get-score-window-position viewport))))

(defn create
  [{:keys [viewport]}]
  (let [close-screen #(re-frame/dispatch [::overlays/hide-activity-finished])]
    {:type        "group"
     :object-name :activity-finished-overlay
     :visible     false
     :children    [(get-background)
                   #_(get-top-menu {:viewport       viewport
                                    :on-close-click close-screen})
                   (get-score-window viewport)]}))

(defn update-viewport
  [{:keys [viewport]}]
  (re-frame/dispatch [::scene/change-scene-object top-menu-name [[:set-position (get-top-menu-position viewport)]]])
  (re-frame/dispatch [::scene/change-scene-object score-window-name [[:set-position (get-score-window-position viewport)]]]))
