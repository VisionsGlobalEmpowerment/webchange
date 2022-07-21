(ns webchange.lesson-builder.tools.template-options.rhyming-sides.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.template-options.rhyming-sides.state :as state]
    [webchange.lesson-builder.widgets.select-image.views :refer [select-image]]
    [webchange.ui.index :as ui]))

(defn field
  [props]
  (re-frame/dispatch [::state/init props])
  (fn [props]
    [:div.rhyming-sides
     [:h3.rhyming-sides-header
      "Choose a side to edit options"]
     [:div.rhyming-sides-list
      [:div.rhyming-sides-list-item {:on-click #(re-frame/dispatch [::state/pick-side :left])}
       "Left Side"
       [ui/icon {:icon "caret-right"
                 :color "grey-3"}]]
      [:div.rhyming-sides-list-item {:on-click #(re-frame/dispatch [::state/pick-side :right])}
       "Right Side"
       [ui/icon {:icon "caret-right"
                 :color "grey-3"}]]]]))

(defn overlay
  []
  (let [word-value @(re-frame/subscribe [::state/word])
        answers @(re-frame/subscribe [::state/answers])
        add-answer #(re-frame/dispatch [::state/add-answer])]
    [:div.rhyming-sides-overlay
     [ui/input {:label "Choose Main Word"
                :placeholder "Ex. pig"
                :value word-value
                :required? true
                :on-change #(re-frame/dispatch [::state/set-word %])}]
     [:div.rhyming-sides-answers
      [:h3.rhyming-sides-answers-header
       "Add Answers (Max. 6)"
       [:div.icon-wrapper
        [ui/icon {:icon "plus"
                  :color "white"
                  :on-click add-answer}]]]
      (for [[i {:keys [object idx text img]}] (map-indexed vector answers)]
        ^{:key idx}
        [:div.rhyming-sides-answer
         [:div.rhyming-sides-answer-header
          (str "Correct Answer " (inc i))
          [ui/icon {:icon "trash"
                    :color "grey-3"
                    :on-click #(re-frame/dispatch [::state/delete-answer idx])}]]
         [ui/input {:label "Word"
                    :placeholder "Word should rhyme with main word"
                    :value text
                    :required? true
                    :on-change #(re-frame/dispatch [::state/set-answer-word idx %])}]
         [select-image {:value (:src img)
                        :on-change #(re-frame/dispatch [::state/set-answer-img idx {:src (:url %)}])}]])]]))
