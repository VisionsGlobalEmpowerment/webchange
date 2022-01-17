(ns webchange.editor-v2.activity-form.generic.components.guide-settings.views
  (:require
    [reagent.core :as r]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-form.generic.components.guide-settings.state :as state]
    [webchange.ui-framework.components.index :refer [button dialog switcher label select]]))

(defn- guide-settings-form
  []
  (let [show-guide @(re-frame/subscribe [::state/show-guide])
        character @(re-frame/subscribe [::state/character])]
    [:div
     [switcher {:checked?  show-guide
                :on-change #(re-frame/dispatch [::state/set-show-guide %])
                :label     "Enable guide?"}]
     [:div.option-group
      [label "Guide type"]
      [select {:value     character
               :on-change #(re-frame/dispatch [::state/set-character %])
               :options   [{:text  "Vaca"
                            :value "vaca"}
                           {:text  "Vera"
                            :value "vera"}
                           {:text  "Mari"
                            :value "mari"}
                           {:text  "Lion"
                            :value "lion"}]
               :variant   "outlined"}]]]))

(defn guide-settings-window
  []
  (r/with-let [open? (re-frame/subscribe [::state/open?])
               handle-save #(re-frame/dispatch [::state/save-settings])
               handle-close #(re-frame/dispatch [::state/close])]
    (when @open?
      [dialog {:open?    @open?
               :on-close handle-close
               :title    "Guide Settings"
               :actions  [[button {:on-click handle-save
                                   :size     "big"}
                           "Save"]
                          [button {:on-click handle-close
                                   :color    "default"
                                   :variant  "outlined"
                                   :size     "big"}
                           "Close"]]}
       [guide-settings-form]])))

(defn open-guide-settings-window
  []
  (re-frame/dispatch [::state/open]))
