(ns webchange.lesson-builder.tools.template-options.first-words-book-spreads.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.template-options.first-words-book-spreads.state :as state]
    [webchange.ui.index :as ui]))

(defn- spread-panel
  [spread-idx]
  (let [spread-data @(re-frame/subscribe [::state/spread-data spread-idx])]
    [:div.spread
     [ui/input {:label "Left Text"
                :placeholder "Add Text"
                :value (:text-left spread-data)
                :required? true
                :on-change #(re-frame/dispatch [::state/change-spread-data spread-idx :text-left %])}]]))

(defn field
  [props]
  (re-frame/dispatch [::state/init props])
  (fn [props]
    (let [spreads @(re-frame/subscribe [::state/spreads-number])
          add-spread #(re-frame/dispatch [::state/add-spread])]
      [:div.first-words-book-spreads
       [:h3.first-words-book-spreads-header
        "Spreads"
        [ui/icon {:icon "plus"
                  :on-click add-spread}]]
       (for [spread-idx (range spreads)]
         ^{:key spread-idx}
         [spread-panel spread-idx])])))
