(ns webchange.editor-v2.dialog.dialog-text-form.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.dialog.dialog-text-form.action-unit.views :refer [action-unit]]
    [webchange.editor-v2.dialog.dialog-text-form.state :as state]
    [webchange.ui-framework.components.index :refer [label select]]))

(defn- concepts-control
  []
  (let [current-concept @(re-frame/subscribe [::state/current-concept])
        concepts-options @(re-frame/subscribe [::state/concepts-options])
        handle-concept-change #(re-frame/dispatch [::state/set-current-concept %])]
    [:div.control-block
     [label "Concept:"]
     [select {:value     current-concept
              :options   concepts-options
              :on-change handle-concept-change
              :type      "int"
              :variant   "outlined"}]]))

(defn- header
  []
  (let [show-concepts? @(re-frame/subscribe [::state/show-concepts?])]
    [:div.dialog-form-header
     (when show-concepts?
       [concepts-control])]))

(defn dialog-form
  []
  (let [actions @(re-frame/subscribe [::state/phrase-actions])
        current-concept @(re-frame/subscribe [::state/current-concept])]
    [:div.dialog-form
     [header]
     [:div.work-field
      ^{:key current-concept}
      [:div.sheet
       (for [{:keys [path] :as action} actions]
         ^{:key path}
         [action-unit action])]]]))
