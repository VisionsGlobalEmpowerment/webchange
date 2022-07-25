(ns webchange.lesson-builder.tools.template-options.onset-rime-rounds.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.template-options.onset-rime-rounds.state :as state]
    [webchange.lesson-builder.widgets.select-image.views :refer [select-image]]
    [webchange.ui.index :as ui]))

(defn- round-panel
  [round-idx]
  (let [round-data @(re-frame/subscribe [::state/round-data round-idx])
        state @(re-frame/subscribe [::state/round-state round-idx])
        last-round? @(re-frame/subscribe [::state/last-round? round-idx])]
    [:div.round
     [:h3.round-header
      [:div.round-header-label {:on-click #(re-frame/dispatch [::state/toggle-round-state round-idx])}
       [ui/icon {:icon (if state "caret-down" "caret-up")
                 :color "grey-3"}]
       (str "Round " (inc round-idx))]
      (when last-round?
        [ui/icon {:icon "trash"
                  :color "grey-3"
                  :on-click #(re-frame/dispatch [::state/delete-last-round])}])]
     (when state
       [:<>
        [ui/input {:label "What word would be brought together"
                   :placeholder "Eg. bat"
                   :value (:whole-text round-data)
                   :required? true
                   :on-change #(re-frame/dispatch [::state/change-round-data round-idx :whole-text %])}]
        [ui/input {:label "Wrighting on left cloud"
                   :placeholder "Eg. b"
                   :value (:left-text round-data)
                   :required? true
                   :on-change #(re-frame/dispatch [::state/change-round-data round-idx :left-text %])}]
        [ui/input {:label "Wrighting on right cloud"
                   :placeholder "Eg. bat"
                   :value (:right-text round-data)
                   :required? true
                   :on-change #(re-frame/dispatch [::state/change-round-data round-idx :right-text %])}]
        [:div.image-description "Would you like to add an image for the word?"
         [:div.description-note "(will be shown when word is brought together)"]]
        [select-image {:label "Correct Answer"
                       :value (get-in round-data [:image :src])
                       :on-change #(re-frame/dispatch [::state/change-round-data round-idx :image {:src (:url %)}])}]])]))

(defn field
  [props]
  (re-frame/dispatch [::state/init props])
  (fn [props]
    (let [rounds @(re-frame/subscribe [::state/rounds-number])
          add-round #(re-frame/dispatch [::state/add-round])]
      [:div.onset-rime-rounds
       [:h3.onset-rime-rounds-header
        "Add Round"
        [:div.icon-wrapper
         [ui/icon {:icon "plus"
                   :color "white"
                   :on-click add-round}]]]
       (for [round-idx (range rounds)]
         ^{:key round-idx}
         [round-panel round-idx])])))
