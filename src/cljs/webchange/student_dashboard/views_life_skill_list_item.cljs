(ns webchange.student-dashboard.views-life-skill-list-item
  (:require
    [reagent.core :as r]
    [webchange.student-dashboard.views-related-content-styles :as styles]
    [webchange.ui.components :as wui]))

(def border-radius 15)
(def list-item-width 140)
(def list-item-height 70)
(def image-block-height (int (Math/ceil (* list-item-height 0.666))))
(def image-play-height (int (Math/ceil (* image-block-height 0.5))))
(def image-play-top (int (Math/ceil (* (- image-block-height image-play-height) 0.5))))
(def info-block-height (- list-item-height image-block-height))
(def info-block-padding 15)

(def list-item-styles
  {:background-color "#1f1f1f"
   :border-radius border-radius
   :cursor        "pointer"
   :flex          "1 1 auto"
   :height        list-item-height
   :margin        (str styles/margin "px")
   :position      "relative"})

(def image-block-styles
  {:height              image-block-height
   :background-position "center"
   :background-repeat   "no-repeat"
   :background-size     "cover"
   :border-radius       (str border-radius "px " border-radius "px 0 0")})

(def info-block-wrapper-styles
  {:display         "flex"
   :justify-content "space-between"})

(def info-block-name-styles
  {:color            "#ffffff"
   :font-size        12
   :line-height      (str info-block-height "px")
   :padding          (str "0 " info-block-padding "px")})

(defn- image-block
  [url]
  [:div {:style (merge image-block-styles {:background-image (str "url(" url ")")})}])

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

(defn life-skill-list-item
  [{:keys [type] :as item}
   {:keys [on-click]}]
  (let [hovered? (r/atom false)]
    (fn []
      [wui/paper {:on-click      #(on-click item)
                  :on-mouse-over #(reset! hovered? true)
                  :on-mouse-out  #(reset! hovered? false)
                  :style         list-item-styles
                  :z-depth       (if @hovered? 2 1)}
       [list-item item {:hovered? @hovered?}]])))
