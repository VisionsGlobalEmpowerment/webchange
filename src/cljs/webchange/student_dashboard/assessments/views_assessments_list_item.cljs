(ns webchange.student-dashboard.assessments.views-assessments-list-item
  (:require
    [reagent.core :as r]
    [cljs-react-material-ui.reagent :as ui]
    [webchange.ui.theme :refer [get-in-theme]]))

(def card-action-area (r/adapt-react-class (aget js/MaterialUI "CardActionArea")))

(defn- get-styles
  []
  {:card    {:max-width "300px"}
   :image   {:height "146px"}
   :name    {:font-size   "1.125rem"
             :font-weight "bold"}
   :actions {:justify-content "space-between"
             :padding         "16px 24px"}
   :button  {:margin "0"}})

(defn assessments-list-item
  [{:keys [id activity-id name image style on-click]}]
  (let [styles (get-styles)]
    [ui/card {:on-click #(on-click id activity-id)
              :style    (merge (or style {})
                               (:card styles))}
     [card-action-area
      [ui/card-media {:image (:png image)
                      :title name
                      :style (:image styles)}]]
     [ui/card-actions {:style (:actions styles)}
      [ui/typography {:variant "h6"
                      :style   (:name styles)}
       name]
      [ui/button {:on-click #(on-click id)
                  :variant  "contained"
                  :color    "primary"
                  :size     "small"
                  :style    (:button styles)}
       "Play"]]]))
