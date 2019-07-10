(ns webchange.student-dashboard.related-content.views-related-content-list-item
  (:require
    [reagent.core :as r]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [webchange.student-dashboard.related-content.views-related-content-styles :as styles]
    [webchange.ui.theme :refer [w-colors]]))

(def secondary-color (:secondary w-colors))

(def border-radius 3)
(def list-item-min-width 150)
(def list-item-height 130)
(def image-block-height (int (Math/ceil (* list-item-height 0.666))))
(def image-play-height (int (Math/ceil (* image-block-height 0.5))))
(def image-play-top (int (Math/ceil (* (- image-block-height image-play-height) 0.5))))
(def info-block-height (- list-item-height image-block-height))
(def info-block-padding 15)

(def list-item-styles
  {:border-radius border-radius
   :cursor        "pointer"
   :flex          "1 1 auto"
   :height        list-item-height
   :margin        (str styles/margin "px")
   :min-width     list-item-min-width
   :position      "relative"})

(def image-block-styles
  {:height              image-block-height
   :background-position "center"
   :background-repeat   "no-repeat"
   :background-size     "cover"
   :border-radius       (str border-radius "px " border-radius "px 0 0")})

(def image-play-styles
  {:width               "100%"
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
   :justify-content "space-between"})

(def info-block-name-styles
  {:font-size   12
   :line-height (str info-block-height "px")
   :padding     (str "0 " info-block-padding "px")})

(def tag-styles
  {:background    secondary-color
   :border-radius "0 0 0 20px"
   :padding       "3px"
   :position      "absolute"
   :right         0
   :width         25
   :text-align    "right"
   :top           0})

(def tag-icon-styles
  {:color  "#ffffff"
   :height 15
   :width  15})

(defn- image-block
  [url {:keys [hovered?]}]
  [:div {:style (merge image-block-styles {:background-image (str "url(" url ")")})}
   [:div {:style (merge image-play-styles (if hovered? {:opacity 0.8} {}))}]])

(defn- info-block
  [{:keys [name]}]
  [:div {:style info-block-wrapper-styles}
   [:div {:style info-block-name-styles}
    name]])

(defn- list-item
  [{:keys [image name]}
   {:keys [hovered?]}]
  [:div
   [image-block image {:hovered? hovered?}]
   [info-block {:name name}]])

(defn- tag
  [type]
  [:div {:style tag-styles}
   (case type
     :book [ic/book {:style tag-icon-styles}]
     :game [ic/videogame-asset {:style tag-icon-styles}]
     :video [ic/videocam {:style tag-icon-styles}]
     "")])

(defn related-content-list-item
  [{:keys [type] :as item}
   {:keys [on-click]}]
  (let [hovered? (r/atom false)]
    (fn []
      [ui/paper {:on-click      #(on-click item)
                 :on-mouse-over #(reset! hovered? true)
                 :on-mouse-out  #(reset! hovered? false)
                 :style         list-item-styles
                 :elevation     (if @hovered? 2 1)}
       [list-item item {:hovered? @hovered?}]
       [tag type]])))
