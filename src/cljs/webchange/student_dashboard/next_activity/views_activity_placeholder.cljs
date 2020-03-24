(ns webchange.student-dashboard.next-activity.views-activity-placeholder
  (:require
    [cljs-react-material-ui.reagent :as ui]))

(defn- get-background-styles
  [{:keys [png webp]}]
  (let [gradient-left-color "rgba(0,0,0,0.95)"
        gradient-left-size "15%"
        gradient-right-color "rgba(0,0,0,0)"
        gradient-right-size "75%"
        gradient (str "linear-gradient(to right, "
                      gradient-left-color " " gradient-left-size ", "
                      gradient-right-color " " gradient-right-size ")")]
    {:background-image  (str gradient ", url(" (or webp png) ") ")
     :background-repeat "no-repeat"
     :background-size   "cover"}))

(defn- get-styles
  []
  {:container   {:display         "flex"
                 :flex-direction  "column"
                 :justify-content "space-between"
                 :align-items     "start"
                 :border-radius   "8px"
                 :padding         "64px 48px"
                 :height          "416px"
                 :width           "100%"}
   :name        {:color "#ffffff"}
   :description {:color "#ffffff"
                 :width      "50%"
                 :max-width  "280px"
                 :margin     "18px 0"
                 :flex-grow  "1"
                 :overflow   "hidden"
                 :text-align "left"}
   :button      {:width "auto"}})

(defn activity-placeholder
  [{:keys [name description image on-click] :as activity}]
  (let [styles (get-styles)
        handle-click (fn [] (on-click activity))]
    [:div {:on-click handle-click
           :style    (merge (:container styles)
                            (get-background-styles image))}

     [ui/typography {:variant "h2"
                     :style   (:name styles)}
      name]
     [ui/typography {:variant "body1"
                     :style   (:description styles)}
      description]
     [ui/button {:variant "contained"
                 :color   "primary"
                 :style   (:button styles)}
      "Play"]]))
