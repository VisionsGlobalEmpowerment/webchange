(ns webchange.editor-v2.layout.logo.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [webchange.editor-v2.layout.logo.svg :refer [get-shape]]
    [webchange.ui.theme :refer [get-in-theme]]))

(def styles {:logo {:display      "flex"
                    :flex-grow    0
                    :line-height  "20px"
                    :margin-right "48px"}})

(defn logo
  []
  [ui/typography {:variant "h1"
                  :style   (:logo styles)}
   [get-shape {:color (get-in-theme [:typography :h1 :color])}]])
