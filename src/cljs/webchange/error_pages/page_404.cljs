(ns webchange.error-pages.page-404
  (:require
    [webchange.ui-deprecated.theme :refer [w-colors]]))

(defn page-404
  []
  [:div {:style {:background-color (:primary w-colors)
                 :display          "flex"
                 :flex-direction   "column"
                 :justify-content  "center"}}
   [:div {:style {:text-align "center"}}
    [:h1 {:style {:color     (:secondary w-colors)
                  :font-size 64}} "404"]]])
