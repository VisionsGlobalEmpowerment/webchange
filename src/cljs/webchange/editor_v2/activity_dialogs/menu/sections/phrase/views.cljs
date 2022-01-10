(ns webchange.editor-v2.activity-dialogs.menu.sections.phrase.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-dialogs.menu.sections.common.section-block.views :refer [section-block]]
    [webchange.editor-v2.activity-dialogs.menu.sections.common.options-list.views :refer [options-list]]
    [webchange.editor-v2.activity-dialogs.menu.sections.phrase.state :as state]
    [webchange.editor-v2.activity-dialogs.form.state :as form-state]
    [webchange.ui-framework.components.index :refer [icon-button label select]]))

(defn- concepts-control
  []
  (let [current-concept @(re-frame/subscribe [::form-state/current-concept])
        concepts-options @(re-frame/subscribe [::form-state/concepts-options])
        handle-concept-change #(re-frame/dispatch [::form-state/set-current-concept %])]
    [:div.control-block
     [label "Concept:"]
     [select {:value     current-concept
              :options   concepts-options
              :on-change handle-concept-change
              :type      "int"
              :variant   "outlined"}]]))

(defn- actions
  []
  (let [available-actions @(re-frame/subscribe [::state/available-actions])]
    [:div.actions
     [options-list {:options       available-actions
                    :get-drag-data (fn [{:keys [value]}]
                                     {:action      "add-phrase-action"
                                      :destination value})}]]))

(defn form
  []
  (let [show-concepts? @(re-frame/subscribe [::form-state/show-concepts?])]
    [:div.phrase-form
     (when show-concepts?
       [section-block {:title "Concepts"}
        [concepts-control]])
     [section-block {:title "Add"}
      [actions]]]))
