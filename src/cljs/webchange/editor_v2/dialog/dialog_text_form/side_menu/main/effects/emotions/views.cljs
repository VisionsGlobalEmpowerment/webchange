(ns webchange.editor-v2.dialog.dialog-text-form.side-menu.main.effects.emotions.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.main.common.options-list.views :refer [options-list]]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.main.effects.emotions.state :as state]
    [webchange.ui-framework.components.index :refer [select]]))

(defn- target-component
  []
  (let [value @(re-frame/subscribe [::state/current-target])
        handle-change #(re-frame/dispatch [::state/set-current-target %])
        options @(re-frame/subscribe [::state/available-characters-options])]
    [:div
     [:span "Character"]
     [select {:value     value
              :on-change handle-change
              :options   options}]]))

(defn- emotions-component
  []
  (let [options @(re-frame/subscribe [::state/available-emotions])]
    [:div
     [:span "Emotions"]
     [options-list {:options       options
                    :get-drag-data (fn [{:keys [target animation]}]
                                     {:action    "set-target-animation"
                                      :target    target
                                      :animation animation
                                      :track     "emotion"})}]]))

(defn available-emotions
  []
  (r/with-let [_ (re-frame/dispatch [::state/init])]
    (let []
      [:div "Emotions"
       [target-component]
       [emotions-component]
       [:hr]])))
