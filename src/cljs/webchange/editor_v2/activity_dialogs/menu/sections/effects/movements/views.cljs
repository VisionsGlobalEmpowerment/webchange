(ns webchange.editor-v2.activity-dialogs.menu.sections.effects.movements.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.activity-dialogs.menu.sections.common.options-list.views :refer [options-list]]
    [webchange.editor-v2.activity-dialogs.menu.sections.effects.movements.state :as state]
    [webchange.ui-framework.components.index :refer [label select]]))

(defn- character-component
  []
  (let [value @(re-frame/subscribe [::state/current-character])
        handle-change #(re-frame/dispatch [::state/set-current-character %])
        options @(re-frame/subscribe [::state/available-characters-options])]
    [:div
     [label "Character"]
     [select {:value       value
              :on-change   handle-change
              :options     options
              :variant     "outlined"
              :placeholder "Select character"}]]))



(defn- movements-component
  []
  (let [value @(re-frame/subscribe [::state/current-movement])
        options @(re-frame/subscribe [::state/available-movements-options])
        handle-change #(re-frame/dispatch [::state/set-current-movement %])]
    [:div
     [label "Action"]
     [select {:value       value
              :on-change   handle-change
              :options     options
              :variant     "outlined"
              :placeholder "Select action"}]]))

(defn- target-component
  []
  (let [value @(re-frame/subscribe [::state/current-target])
        handle-change #(re-frame/dispatch [::state/set-current-target %])
        options @(re-frame/subscribe [::state/available-targets-options])]
    [:div
     [label "Target"]
     [select {:value       value
              :on-change   handle-change
              :options     options
              :variant     "outlined"
              :placeholder "Select target"}]]))

(defn- option-component
  []
  (let [options @(re-frame/subscribe [::state/dnd-options])]
    [options-list {:options       options
                   :option-key    :movement
                   :get-drag-data (fn [data]
                                    (merge data {:action "add-movement"}))}]))

(defn available-movements
  []
  [:div.movements-form
   [character-component]
   [movements-component]
   [target-component]
   [:hr]
   [option-component]])
