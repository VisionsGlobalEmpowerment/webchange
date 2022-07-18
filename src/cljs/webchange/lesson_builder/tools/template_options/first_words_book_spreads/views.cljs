(ns webchange.lesson-builder.tools.template-options.first-words-book-spreads.views
  (:require
    [reagent.core :as r]
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.template-options.first-words-book-spreads.state :as state]
    [webchange.lesson-builder.widgets.select-image.views :refer [select-image]]
    [webchange.ui.index :as ui]))

(defn- spread-panel
  [spread-idx]
  (let [spread-data @(re-frame/subscribe [::state/spread-data spread-idx])
        state @(re-frame/subscribe [::state/spread-state spread-idx])
        last-spread? @(re-frame/subscribe [::state/last-spread? spread-idx])]
    [:div.spread
     [:div.page
      [:h3.page-header
       [:div.page-header-label {:on-click #(re-frame/dispatch [::state/toggle-spread-state spread-idx :left])}
        [ui/icon {:icon (if (:left state) "caret-down" "caret-up")
                  :color "grey-3"}]
        "Left Page"]]
      (when (:left state)
        [:<>
         [ui/input {:label "Text"
                    :placeholder "Add Text"
                    :value (:text-left spread-data)
                    :required? true
                    :on-change #(re-frame/dispatch [::state/change-spread-data spread-idx :text-left %])}]
         [select-image {:label "Image"
                        :value (get-in spread-data [:image-left :src])
                        :on-change #(re-frame/dispatch [::state/change-spread-data spread-idx :image-left {:src (:url %)}])}]])]
     [:div.page
      [:h3.page-header
       [:div.page-header-label {:on-click #(re-frame/dispatch [::state/toggle-spread-state spread-idx :right])}
        [ui/icon {:icon (if (:right state) "caret-down" "caret-up")
                  :color "grey-3"}]
        "Right Page"]
       (when last-spread?
         [ui/icon {:icon "trash"
                   :color "grey-3"
                   :on-click #(re-frame/dispatch [::state/delete-last-spread])}])]
      (when (:right state)
        [:<>
         [ui/input {:label "Text"
                    :placeholder "Add Text"
                    :value (:text-right spread-data)
                    :required? true
                    :on-change #(re-frame/dispatch [::state/change-spread-data spread-idx :text-right %])}]
         [select-image {:label "Image"
                        :value (get-in spread-data [:image-right :src])
                        :on-change #(re-frame/dispatch [::state/change-spread-data spread-idx :image-right {:src (:url %)}])}]])]]))

(defn field
  [props]
  (re-frame/dispatch [::state/init props])
  (fn [props]
    (let [spreads @(re-frame/subscribe [::state/spreads-number])
          add-spread #(re-frame/dispatch [::state/add-spread])]
      [:div.first-words-book-spreads
       [:h3.first-words-book-spreads-header
        "Add Pages"
        [:div.icon-wrapper
         [ui/icon {:icon "plus"
                   :color "white"
                   :on-click add-spread}]]]
       (for [spread-idx (range spreads)]
         ^{:key spread-idx}
         [spread-panel spread-idx])])))
