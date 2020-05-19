(ns webchange.student-dashboard.history.views-history-list-item
  (:require
    [cljs-react-material-ui.reagent :as ui]))

(def months ["January"
             "February"
             "March"
             "April"
             "May"
             "June"
             "July"
             "August"
             "September"
             "October"
             "November"
             "December"])

(defn- date->str
  [date]
  (let [day (.getDate date)
        month (->> date (.getMonth) (get months))
        year (.getFullYear date)]
    (str day " " month " " year)))

(defn- get-styles
  []
  {:block        {:padding "16px 0"}
   :img          {:width  "64px"
                  :height "64px"}
   :text         {:padding "0 24px"}
   :text-primary {:font-weight "bold"}})

(defn history-list-item
  [{:keys [name level lesson date image style on-click] :as activity}]
  (let [styles (get-styles)]
    [ui/list-item {:button   true
                   :on-click #(on-click activity)
                   :style    (merge (or style {})
                                    (:block styles))}
     [ui/list-item-avatar
      [ui/avatar {:alt   name
                  :src   (:png image)
                  :style (:img styles)}]]
     [ui/list-item-text {:primary                name
                         :secondary              (str "Level " level " Lesson " lesson)
                         :primaryTypographyProps {:style (:text-primary styles)}
                         :style                  (:text styles)}]
     [ui/list-item-secondary-action
      [ui/button {:color    "primary"
                  :on-click #(on-click activity)}
       "Play"]]]))
