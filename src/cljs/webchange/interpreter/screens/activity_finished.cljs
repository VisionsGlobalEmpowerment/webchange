(ns webchange.interpreter.screens.activity-finished
  (:require
    [re-frame.core :as re-frame]
    [react-konva :refer [Group Rect Text]]
    [webchange.common.kimage :refer [kimage]]
    [webchange.interpreter.components.image-button :refer [image-button] :rename {image-button button}]
    [webchange.interpreter.core :refer [get-data-as-url]]
    [webchange.interpreter.events :as ie]
    [webchange.interpreter.screens.state :as screens]
    [webchange.interpreter.screens.common.background :refer [background]]
    [webchange.interpreter.utils.position :refer [top-right]]
    [webchange.student-dashboard.subs :as subs]
    [webchange.common.scene-components.components :as components]
    [webchange.interpreter.utils.i18n :refer [t]]))

(defn- title
  [{:keys [x y]}]
  (let [text "Nice Work!"
        font-size 102]
    [:> Text {:x              x
              :y              (- y (/ font-size 2))
              :text           text
              :align          "center"
              :font-size      font-size
              :font-family    "Luckiest Guy"
              :fill           "white"
              :shadow-color   "#1a1a1a"
              :shadow-offset  {:x 5 :y 5}
              :shadow-blur    5
              :shadow-opacity 0.5
              :ref            (fn [component]
                                (when component (.offsetX component (/ (.width component) 2))))}]))

(defn- vera
  [{:keys [x y]}]
  (let [image-size {:width  315
                    :height 371}]
    [kimage {:src (get-data-as-url "/raw/img/ui/vera_315x371.png")
             :x   (- x (/ (:width image-size) 2))
             :y   (- y (:height image-size))}]))

(defn- next-activity-card
  [{:keys [x y activity on-click]}]
  (let [{:keys [image name]} activity
        card-width 400
        card-height 375
        border-radius 30
        image-width 400
        image-height 225
        text-padding 40]
    [:> Group {:x        x
               :y        y
               :on-click on-click
               :on-tap   on-click}
     [:> Rect {:x             0
               :y             0
               :width         card-width
               :height        card-height
               :fill          "#ffffff"
               :corner-radius border-radius}]
     [kimage {:src           (get-data-as-url image)
              :width         image-width
              :height        image-height
              :stroke        "#bd13c7"
              :stroke-width  1
              :border-radius [border-radius border-radius 0 0]}]
     [:> Text {:x         text-padding
               :y         (+ image-height text-padding)
               :font-size 32
               :text      name
               :color     "#000000"}]]))

(defn- progress-bar
  [{:keys [x y width height value]}]
  (let [border-radius 25
        border-width 2]
    [:> Group {:x x :y y}
     [:> Rect {:x             0
               :y             0
               :width         width
               :height        height
               :fill          "#ffffff"
               :corner-radius border-radius
               :stroke        "#bd13c7"
               :stroke-width  border-width}]
     [:> Group {:clip-x      0
                :clip-y      0
                :clip-width  (* value width)
                :clip-height height}
      [:> Rect {:x             (/ border-width 2)
                :y             (/ border-width 2)
                :width         (- width border-width)
                :height        (- height border-width)
                :fill          "#2c9600"
                :corner-radius border-radius
                :stroke        "#258001"
                :stroke-width  1}]]]))

(defn- overall-progress
  [{:keys [x y width value]}]
  (let [title "Overall Progress"]
    [:> Group {:x x
               :y y}
     [:> Text {:x           0
               :y           0
               :text        title
               :align       "center"
               :font-size   42
               :font-family "Lexend Deca"
               :font-weight "bold"
               :fill        "white"}]
     [progress-bar {:x      0
                    :y      80
                    :width  width
                    :height 25
                    :value  value}]]))

(defn- featured-content
  [{:keys [x y next-activity on-click]}]
  (let [title "Featured Content"]
    [:> Group {:x x
               :y y}
     [:> Text {:x           0
               :y           0
               :text        title
               :align       "center"
               :font-size   42
               :font-family "Lexend Deca"
               :font-weight "bold"
               :fill        "white"}]
     [next-activity-card {:x        0
                          :y        80
                          :activity next-activity
                          :on-click on-click}]]))

(defn- top-menu
  [{:keys [on-close-click on-settings-click]}]
  (let [margin-top 20]
    [:> Group (top-right)
     [button {:x        (- 120)
              :y        margin-top
              :skin     "close"
              :on-click on-close-click}]
     [button {:x        (- 240)
              :y        margin-top
              :skin     "settings"
              :on-click on-settings-click}]]))

(defn- menu
  [{:keys [x y on-restart-click on-next-click]}]
  (let [menu-height 100]
    [:> Group {:x x
               :y (- y (/ menu-height 2))}
     [button {:x        0
              :y        0
              :skin     "reload"
              :on-click on-restart-click}]
     [components/button-interpreter {:x 130 :on-click on-next-click
                                     :text (t "next")}]]))

(defn activity-finished-screen
  []
  (let [window-width 1200
        window-height 660
        offset-left (/ (- 1980 window-width) 2)
        offset-top (/ (- 1080 window-height) 2)

        next-activity @(re-frame/subscribe [::subs/next-activity])
        progress-value @(re-frame/subscribe [::subs/lesson-progress])

        go-to-next-activity #(re-frame/dispatch [::ie/run-next-activity])
        restart-activity #(re-frame/dispatch [::ie/restart-scene])
        continue-playing #(re-frame/dispatch [::ie/next-scene])
        close-screen #(re-frame/dispatch [::screens/reset-ui-screen])
        open-settings #(re-frame/dispatch [::ie/open-settings])]
    [:> Group
     [background {:x      offset-left
                  :y      offset-top
                  :width  window-width
                  :height window-height}]
     [top-menu {:on-close-click    close-screen
                :on-settings-click open-settings}]
     [title {:x (/ 1980 2) :y offset-top}]
     [vera {:x (+ offset-left (/ window-width 4))
            :y (+ offset-top window-height -30)}]
     [overall-progress {:x     (+ offset-left 65)
                        :y     (+ offset-top 120)
                        :width (- (/ window-width 2) 130)
                        :value progress-value}]
     [featured-content {:x             (+ offset-left (/ window-width 2) 65)
                        :y             (+ offset-top 120)
                        :next-activity next-activity
                        :on-click      go-to-next-activity}]
     [menu {:x                (+ offset-left 20)
            :y                (+ offset-top window-height 10)
            :on-restart-click restart-activity
            :on-next-click    continue-playing}]]))
