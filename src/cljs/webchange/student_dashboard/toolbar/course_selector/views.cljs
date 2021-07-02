(ns webchange.student-dashboard.toolbar.course-selector.views
  (:require
   [cljs-react-material-ui.reagent :as ui]
   [re-frame.core :as re-frame]
   [webchange.subs :as subs]))

(defn- get-styles
  []
  {:main      {:width          "150px"
               :margin         "0 20px"
               :text-transform "capitalize"
               :font-weight    "bold"}})

(defn course-selector
  []
  (let [current-course @(re-frame/subscribe [::subs/current-course])
        styles (get-styles)]
    [ui/typography {:variant "h6"
                    :style   (:main styles)}
     (str current-course)]))