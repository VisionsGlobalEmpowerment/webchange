(ns webchange.interpreter.renderer.waiting-screen
  (:require
    [reagent.core :as r]
    [webchange.interpreter.utils.i18n :refer [t]]))

(defn get-styles
  []
  {:background         {:background-image    "url(/raw/img/bg.png)"
                        :width               "100%"
                        :height              "100%"
                        :position            "absolute"
                        :top                 0
                        :left                0
                        :background-size     "cover"
                        :background-position "center"
                        :transform           "translateZ(0)"}
   :centered-container {:display         "flex"
                        :width           "100%"
                        :height          "100%"
                        :align-items     "center"
                        :justify-content "center"}
   :centered-content   {:display        "flex"
                        :flex-direction "column"
                        :align-items    "center"
                        :height         "520px"}
   :text-container     {:display         "flex"
                        :width           "100%"
                        :height          "100%"
                        :align-items     "center"
                        :justify-content "center"}
   :text               {:color       "#ffffff"
                        :font-size   "68px"
                        :font-family "Luckiest Guy"
                        :font-weight "normal"
                        :text-align  "center"}})

(defn- centered
  []
  (let [styles (get-styles)
        this (r/current-component)]
    [:div {:style (:centered-container styles)}
     (into [:div {:style (:centered-content styles)}]
           (r/children this))]))

(defn- title
  []
  (let [styles (get-styles)]
    [:div {:style (:text-container styles)}
     [:span {:style (:text styles)}
      (t "Please, wait...")]]))

(defn waiting-screen
  []
  (let [styles (get-styles)]
    [:div {:style (:background styles)}
     [centered
      [title]]]))
