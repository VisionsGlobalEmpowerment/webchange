(ns webchange.editor-v2.components.logo.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [webchange.editor-v2.components.logo.svg :refer [get-shape]]
    [webchange.ui-deprecated.theme :refer [get-in-theme]]))

(def styles {:logo {:display      "flex"
                    :flex-grow    0
                    :line-height  "20px"
                    :margin-left "24px"
                    :margin-right "48px"}})

(defn logo
  []
  [ui/typography {:variant "h1"
                  :style   (:logo styles)}
   [get-shape {:color (get-in-theme [:typography :h1 :color])}]])
