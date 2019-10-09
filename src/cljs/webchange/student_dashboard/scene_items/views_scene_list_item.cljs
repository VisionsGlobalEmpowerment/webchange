(ns webchange.student-dashboard.scene-items.views-scene-list-item
  (:require
    [reagent.core :as r]
    [cljsjs.material-ui]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]))

(def completed-mark-color "#3d9a00")
(def list-item-width 200)
(def list-item-height 150)
(def image-block-height (int (Math/ceil (* list-item-height 0.666))))
(def image-play-height (int (Math/ceil (* image-block-height 0.5))))
(def image-play-top (int (Math/ceil (* (- image-block-height image-play-height) 0.5))))
(def info-block-height (- list-item-height image-block-height))
(def info-block-padding 15)

(def border-radius 3)

(def list-item-styles
  {:border-radius border-radius
   :cursor        "pointer"
   :height        list-item-height
   :margin        "0 30px 30px 0"
   :width         list-item-width})

(def image-block-styles
  {:height              image-block-height
   :background-position "center"
   :background-repeat   "no-repeat"
   :background-size     "cover"
   :border-radius       (str border-radius "px " border-radius "px 0 0")})

(def image-play-styles
  {:width               list-item-width
   :height              image-play-height
   :background-image    "url(/images/student_dashboard/play.png)"
   :background-position "center"
   :background-repeat   "no-repeat"
   :background-size     "contain"
   :opacity             0.6
   :position            "relative"
   :top                 (str image-play-top "px")})

(def info-block-wrapper-styles
  {:display         "flex"
   :justify-content "space-between"
   :&:hover         {:background-color "light-blue"}})

(def info-block-name-styles
  {:font-size   16
   :line-height (str info-block-height "px")
   :padding     (str "0 " info-block-padding "px")})

(def info-block-status-styles
  {:display         "flex"
   :padding         (str "0 " info-block-padding "px")
   :flex-direction  "column"
   :justify-content "center"})

(def show-more-styles
  {:align-items      "center"
   :background-color "#e56a93"
   :border-radius    border-radius
   :color            "#ffffff"
   :display          "flex"
   :font-size        "24px"
   :height           "100%"
   :justify-content  "center"})

(defn- image-block
  [url {:keys [hovered?]}]
  [:div {:style (merge image-block-styles {:background-image (str "url(" url ")")})}
   [:div {:style (merge image-play-styles (if hovered? {:opacity 0.8} {}))}]])

(defn- info-block
  [{:keys [name completed]}]
  [:div {:style info-block-wrapper-styles}
   [:div {:style info-block-name-styles}
    name]
   [:div {:style info-block-status-styles}
    (when completed [ic/check-circle {:style {:color completed-mark-color}}])
    ]])

(defn- list-item
  [{:keys [image name completed]}
   {:keys [hovered?]}]
  [:div
   [image-block image {:hovered? hovered?}]
   [info-block {:name      name
                :completed completed}]])

(defn show-more-item
  [{:keys [on-click]}]
  (let [hovered? (r/atom false)]
    (fn []
      [ui/paper {:on-click      on-click
                 :on-mouse-over #(reset! hovered? true)
                 :on-mouse-out  #(reset! hovered? false)
                 :style         list-item-styles
                 :elevation     (if @hovered? 2 1)}
       [:div {:style show-more-styles}
        [:span "+ More"]]]
      )))

(defn scene-list-item
  [item {:keys [on-click]}]
  (let [hovered? (r/atom false)]
    (fn []
      [ui/paper {:on-click      #(on-click item)
                 :on-mouse-over #(reset! hovered? true)
                 :on-mouse-out  #(reset! hovered? false)
                 :style         list-item-styles
                 :elevation     (if @hovered? 2 1)}
       [list-item item {:hovered? @hovered?}]]
      )))
