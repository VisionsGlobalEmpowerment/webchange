(ns webchange.student-dashboard.toolbar.logo.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [webchange.student-dashboard.toolbar.logo.svg :refer [get-shape]]
    [webchange.ui.theme :refer [get-in-theme]]))


(def styles {:logo {:display     "flex"
                    :flex-grow   1
                    :line-height "20px"}})

(defn logo
  []
  [ui/typography {:variant "h1"
                  :style   (:logo styles)}
   [get-shape {:color (get-in-theme [:typography :h1 :color])}]])
