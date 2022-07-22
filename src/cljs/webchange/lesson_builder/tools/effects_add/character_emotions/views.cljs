(ns webchange.lesson-builder.tools.effects-add.character-emotions.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.components.draggable.views :refer [draggable draggable-list]]
    [webchange.lesson-builder.tools.effects-add.character-emotions.state :as state]
    [webchange.ui.index :as ui]))

(defn character-emotions
  []
  (let [current-character @(re-frame/subscribe [::state/current-character])
        character-options @(re-frame/subscribe [::state/character-options])
        handle-character-change #(re-frame/dispatch [::state/set-current-character %])
        emotion-options @(re-frame/subscribe [::state/emotion-options])]
    [:div.character-emotions
     [ui/select {:label     "Character"
                 :value     current-character
                 :options   character-options
                 :on-change handle-character-change}]
     (if-not (empty? emotion-options)
       [draggable-list {:class-name "options-list"}
        (for [{:keys [text value]} emotion-options]
          ^{:key value}
          [draggable {:text   text
                      :action "add-emotion"
                      :data   {:animation value
                               :target    current-character}}])
        [draggable {:text   "Reset"
                    :action "remove-emotion"
                    :data   {:target current-character}}]])]))
