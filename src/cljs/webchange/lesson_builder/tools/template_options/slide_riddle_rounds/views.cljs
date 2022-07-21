(ns webchange.lesson-builder.tools.template-options.slide-riddle-rounds.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.template-options.slide-riddle-rounds.state :as state]
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
        [select-image {:label "Correct Answer"
                       :value (get-in round-data [:image-correct :src])
                       :on-change #(re-frame/dispatch [::state/change-round-data round-idx :image-correct {:src (:url %)}])}]
        [select-image {:label "Incorrect Answer 1"
                       :value (get-in round-data [:image-wrong-1 :src])
                       :on-change #(re-frame/dispatch [::state/change-round-data round-idx :image-wrong-1 {:src (:url %)}])}]
        [select-image {:label "Incorrect Answer 2"
                       :value (get-in round-data [:image-wrong-2 :src])
                       :on-change #(re-frame/dispatch [::state/change-round-data round-idx :image-wrong-2 {:src (:url %)}])}]])]))

(defn field
  [props]
  (re-frame/dispatch [::state/init props])
  (fn [props]
    (let [rounds @(re-frame/subscribe [::state/rounds-number])
          add-round #(re-frame/dispatch [::state/add-round])]
      [:div.slide-riddle-rounds
       [:h3.slide-riddle-rounds-header
        "Add Round"
        [:div.icon-wrapper
         [ui/icon {:icon "plus"
                   :color "white"
                   :on-click add-round}]]]
       (for [round-idx (range rounds)]
         ^{:key round-idx}
         [round-panel round-idx])])))
