(ns webchange.ui.pages.colors.views
  (:require
    [webchange.ui.index :as ui]
    [webchange.ui.pages.layout :refer [layout]]))

(def colors ["blue-1" "blue-2" "green-1" "green-2" "yellow-1" "yellow-2"])

(defn- colors-list-item
  [{:keys [color]}]
  [:div {:class-name (ui/get-class-name {"colors-list-item"   true
                                         (str "color-" color) true})}
   [:div.content]])

(defn- colors-list
  []
  [:ul.colors-list
   (for [color colors]
     ^{:key color}
     [colors-list-item {:color color}])])

(defn page
  []
  [:div#page--colors
   [layout {:title "Colors"}
    [colors-list]]])
