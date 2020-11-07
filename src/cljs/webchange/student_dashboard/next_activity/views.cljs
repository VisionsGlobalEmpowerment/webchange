(ns webchange.student-dashboard.next-activity.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [webchange.student-dashboard.common.block-header.views :refer [block-header]]
    [webchange.student-dashboard.common.icons.views :as icons]))

(defn- get-background-styles
  [{:keys [image]}]
  (let [gradient-left-color "rgba(0,0,0,0.95)"
        gradient-left-size "15%"
        gradient-right-color "rgba(0,0,0,0)"
        gradient-right-size "75%"
        gradient (str "linear-gradient(to right, "
                      gradient-left-color " " gradient-left-size ", "
                      gradient-right-color " " gradient-right-size ")")]
    {:background-image  (str gradient ", url(" image ") ")
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

(defn- activity-placeholder
  [{:keys [activity on-click]}]
  (let [styles (get-styles)
        handle-click (fn [] (on-click activity))]
    [:div {:on-click handle-click
           :style    (merge (:container styles)
                            (get-background-styles activity))}

     [ui/typography {:variant "h2"
                     :style   (:name styles)}
      (get activity :name "")]
     [ui/typography {:variant "body1"
                     :style   (:description styles)}
      (get activity :description "")]
     [ui/button {:variant "contained"
                 :color   "primary"
                 :style   (:button styles)}
      "Play"]]))

(defn next-activity-block
  [props]
  [:div
   [block-header {:icon icons/play
                  :text "Next Scene"}]
   [activity-placeholder props]])
