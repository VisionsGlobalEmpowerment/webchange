(ns webchange.admin.components.pagination.views
  (:require
    [webchange.ui.index :as ui]))

(defn pagination
  [{:keys [current total on-click]}]
  (let [handle-click #(when (fn? on-click) (on-click %))
        handle-next-click #(handle-click (inc current))
        handle-prev-click #(handle-click (dec current))]
    [:div.component--pagination
     [ui/button {:icon      "caret-left"
                 :disabled? (<= current 1)
                 :on-click  handle-prev-click
                 :color     "blue-1"}]
     [:label "Page " [:b current] " of " total]
     [ui/button {:icon      "caret-right"
                 :disabled? (>= current total)
                 :on-click  handle-next-click
                 :color     "blue-1"}]]))
