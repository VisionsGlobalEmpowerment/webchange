(ns webchange.student-dashboard.toolbar.course-selector.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.interpreter.events :as ie]
    [webchange.subs :as subs]))

;; ToDo: get courses list from API
(def courses
  [{:key "en" :value "english" :text "English"}
   {:key "es" :value "spanish" :text "Español"}])

(defn- get-styles
  []
  {:main {:width  "150px"
          :margin "0 20px"}
   :icon {:background-size     "cover"
          :background-repeat   "no-repeat"
          :background-position "center"
          :border-radius       "3px"
          :display             "inline-block"
          :height              "14px"
          :margin-right        "16px"
          :position            "relative"
          :top                 "2px"
          :width               "18px"}})

(defn- flag
  [name]
  (let [src (case name
              "en" "/icons/flags/us.svg"
              "es" "/icons/flags/es.svg")
        styles (get-styles)]
    [:div {:style (merge (:icon styles)
                         {:background-image (str "url(" src ")")})}]))

(defn- menu-item
  [{:keys [key value text]}]
  [ui/menu-item {:key value :value value}
   [flag key]
   [ui/typography {:inline  true
                   :variant "inherit"}
    text]])

(defn course-selector
  []
  (let [current-course @(re-frame/subscribe [::subs/current-course])
        styles (get-styles)]
    [ui/select {:value     current-course
                :variant   "outlined"
                :on-change #(re-frame/dispatch [::ie/open-student-course-dashboard (-> % .-target .-value)])
                :style     (:main styles)}
     (for [course courses]
       (menu-item course))]))
