(ns webchange.interpreter.renderer.loader-screen
  (:require
    [reagent.core :as r]
    [webchange.interpreter.utils.i18n :refer [t]]))

(defn get-styles
  []
  {:background             {:background-image    "url(/raw/img/bg.png)"
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
   :centered-content       {:display        "flex"
                            :flex-direction "column"
                            :align-items    "center"
                            :height         "520px"}
   :logo                   {:position      "relative"
                            :width         "627px"
                            :height        "123px"
                            :margin-bottom "60px"}
   :progress-bar-container {:display         "flex"
                            :width           "100%"
                            :height          "100%"
                            :align-items     "center"
                            :justify-content "center"}
   :progress-bar           {:background-color "#ffffff"
                            :border-radius    "25px"
                            :width            "460px"
                            :height           "24px"
                            :overflow         "hidden"}
   :progress-bar-value     {:background-color "#00C3FF"
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
                            :padding          "23px 68px"
                            :height           "96px"
                            :cursor           "pointer"
                            :color            "#ffffff"
                            :font-size        "68px"
                            :font-family      "Luckiest Guy"
                            :font-weight      "normal"
                            :text-align       "center"
                            :outline          "none"}})

(defn- centered
  []
  (let [styles (get-styles)
        this (r/current-component)]
    [:div {:style (:centered-container styles)}
     (into [:div {:style (:centered-content styles)}]
           (r/children this))]))

(defn- logo
  []
  (let [styles (get-styles)]
    [:img {:src   "/raw/img/ui/logo.png"
           :style (:logo styles)}]))

(defn- start-button
  [{:keys [on-click]}]
  (let [styles (get-styles)]
    [:div {:style (:button-container styles)}
     [:button {:on-click on-click
               :style    (:button styles)}
      (t "play")]]))

(defn- progress-bar
  [{:keys [value]}]
  (let [progress->width #(-> % (* 100) (str "%"))
        styles (get-styles)]
    [:div {:style (:progress-bar-container styles)}
     [:div {:style (:progress-bar styles)}
      [:div {:style (merge (:progress-bar-value styles)
                           {:width (progress->width value)})}]]]))

(defn loader-screen
  [{:keys [on-start-click done progress]}]
  (let [styles (get-styles)]
    [:div {:style (:background styles)}
     [centered
      [logo]
      (if done
        [start-button {:on-click on-start-click}]
        [progress-bar {:value progress}])]]))
