(ns webchange.error-pages.page-404)

(def w-colors
  {:primary        "#222342"
   :primary-darken "#191a31"
   :default        "#ffffff"
   :secondary      "#fd4142"
   :disabled       "#bababa"})

(defn page-404
  []
  [:div {:style {:background-color (:primary w-colors)
                 :display          "flex"
                 :flex-direction   "column"
                 :justify-content  "center"}}
   [:div {:style {:text-align "center"}}
    [:h1 {:style {:color     (:secondary w-colors)
                  :font-size 64}} "404"]]])
