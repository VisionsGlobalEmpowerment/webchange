(ns webchange.dashboard.common.score-table.views-legend
  (:require
    [cljsjs.material-ui]
    [cljs-react-material-ui.reagent :as ui]
    [webchange.dashboard.common.score-table.views-theme :refer [score-colors]]))

(def legend-list-style
  {:list-style "none"
   :margin     0
   :padding    0})
(def legend-item-style
  {:display       "flex"
   :margin-bottom 10})
(def legend-item-size 20)
(def legend-item-color-style
  {:border-radius "50%"
   :height        legend-item-size
   :margin-right  (/ legend-item-size 2)
   :width         legend-item-size})

(defn score-table-legend-item
  [{:keys [legend color]}]
  [:li {:style legend-item-style}
   [:div {:style (merge legend-item-color-style {:background-color color})}]
   [ui/typography {:variant "body1"} legend]])

(defn score-table-legend
  [{gray-legend   :gray
    green-legend  :green
    yellow-legend :yellow
    red-legend    :red}]
  [:ul {:style legend-list-style}
   [score-table-legend-item {:legend gray-legend :color (:gray score-colors)}]
   [score-table-legend-item {:legend green-legend :color (:green score-colors)}]
   [score-table-legend-item {:legend yellow-legend :color (:yellow score-colors)}]
   [score-table-legend-item {:legend red-legend :color (:red score-colors)}]])
