(ns webchange.editor-v2.activity-dialogs.menu.sections.effects.emotions.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.activity-dialogs.menu.sections.common.options-list.views :refer [options-list]]
    [webchange.editor-v2.activity-dialogs.menu.sections.effects.emotions.state :as state]
    [webchange.ui-framework.components.index :refer [label select]]))

(defn- target-component
  []
  (let [value @(re-frame/subscribe [::state/current-target])
        handle-change #(re-frame/dispatch [::state/set-current-target %])
        options @(re-frame/subscribe [::state/available-characters-options])]
    [:div
     [label "Character"]
     [select {:value       value
              :on-change   handle-change
              :options     options
              :variant     "outlined"
              :placeholder "Select character"}]]))

(defn- emotions-component
  []
  (let [show-component? @(re-frame/subscribe [::state/show-emotions?])
        options @(re-frame/subscribe [::state/available-emotions])]
    (when show-component?
      [:div
       [label "Emotions"]
       [options-list {:options       options
                      :option-key    :animation
                      :get-drag-data (fn [{:keys [target animation]}]
                                       (cond-> {:target target
                                                :track  "emotion"}
                                               (= animation "reset") (merge {:action "remove-target-animation"})
                                               (not= animation "reset") (merge {:action    "set-target-animation"
                                                                                :animation animation})))}]])))

(defn available-emotions
  []
  (r/with-let [_ (re-frame/dispatch [::state/init])]
    [:div.emotions-form
     [target-component]
     [emotions-component]]))
