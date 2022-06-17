(ns webchange.interpreter.renderer.loader-screen
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.i18n.translate :as i18n]))

(defn get-styles
  []
  {:background             {:background-image    "url(/raw/img/ui/loading_screen.png)"
                            :width               "100%"
                            :height              "100%"
                            :position            "absolute"
                            :top                 0
                            :left                0
                            :background-size     "cover"
                            :background-position "center"
                            :transform           "translateZ(0)"}
   :centered-container     {:display         "flex"
                            :width           "100%"
                            :height          "100%"
                            :align-items     "center"
                            :justify-content "center"}
   :left-content           {:display        "flex"
                            :flex "3"
                            :flex-direction "column"
                            :align-items    "center"
                            :height         "520px"}
   :right-content          {:display        "flex"
                            :flex "4"
                            :flex-direction "column"
                            :align-items    "center"
                            :height         "520px"
                            :margin-top     "250px"}
   :progress-bar-container {:display         "flex"
                            :width           "100%"
                            :height          "100%"
                            :align-items     "center"
                            :justify-content "center"}
   :progress-bar           {:background-color "#80BFE5"
                            :border-radius    "25px"
                            :width            "824px"
                            :height           "27px"
                            :overflow         "hidden"}
   :progress-bar-value     {:background-color "#3453A1"
                            :height           "100%"
                            :transition       "width 1s"
                            :width            "1%"}
   :button-container       {:display         "flex"
                            :width           "100%"
                            :height          "100%"
                            :align-items     "center"
                            :justify-content "center"}
   :button                 {:background-color "#ff9000"
                            :border-radius    "48px"
                            :border-width     "0px"
                            :padding          "32px 68px"
                            :height           "72px"
                            :cursor           "pointer"
                            :color            "#ffffff"
                            :font-size        "48px"
                            :font-family      "Luckiest Guy"
                            :font-weight      "normal"
                            :text-align       "center"
                            :outline          "none"}})

(defn- centered
  []
  (let [styles (get-styles)
        this (r/current-component)]
    [:div {:style (:centered-container styles)}
     [:div {:style (:left-content styles)}]
     (into [:div {:style (:right-content styles)}]
           (r/children this))]))

(defn- start-button
  [{:keys [on-click]}]
  (let [styles (get-styles)]
    [:div {:style (:button-container styles)}
     [:button {:on-click on-click
               :style    (:button styles)}
      @(re-frame/subscribe [::i18n/t [:play]])]]))

(defn- progress-bar
  [{:keys [value]}]
  (let [progress->width #(-> % (* 100) (str "%"))
        styles (get-styles)]
    [:div {:style (:progress-bar-container styles)}
     [:div {:style (:progress-bar styles)}
      [:div {:style (merge (:progress-bar-value styles)
                           {:width (progress->width value)})}]]]))

(defn loader-screen
  [{:keys [on-start-click loading]}]
  (let [styles (get-styles)]
    [:div {:style (:background styles)}
     [centered
      (if (:done loading)
        [start-button {:on-click on-start-click}]
        [progress-bar {:value (:progress loading)}])]]))
