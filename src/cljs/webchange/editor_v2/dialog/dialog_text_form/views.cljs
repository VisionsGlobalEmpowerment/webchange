(ns webchange.editor-v2.dialog.dialog-text-form.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.dialog.dialog-text-form.action-unit.views :refer [action-unit]]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.views :refer [side-menu]]
    [webchange.editor-v2.dialog.dialog-text-form.state :as state]
    [webchange.editor-v2.dialog.dialog-form.state.actions :as state-actions]
    [webchange.editor-v2.text-animation-editor.views :refer [text-chunks-modal]]
    [webchange.ui-framework.components.index :refer [button label select]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

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

(defn- actions-block
  []
  (let [actions [["Phrase" #(re-frame/dispatch [::state-actions/append-empty-phrase-action])]
                 ["Text Animation" #(re-frame/dispatch [::state-actions/append-empty-text-animation-action])]]]
    [:div.actions-block
     [label {:class-name "actions-label"} "Add:"]
     (for [[idx [title click-handler]] (map-indexed vector actions)]
       ^{:key idx}
       [button {:on-click   click-handler
                :class-name "action-button"
                :variant    "outlined"
                :color      "default"}
        title])]))

(defn dialog-form
  []
  (r/with-let []
    (let [actions @(re-frame/subscribe [::state/phrase-actions])
          current-concept @(re-frame/subscribe [::state/current-concept])]
      [:div {:class-name (get-class-name {"dialog-form"      true
                                          "dialog-text-form" true})}
       [header]
       [:div.work-field
        ^{:key current-concept}
        [:div.sheet
         (for [[idx {:keys [path] :as action}] (map-indexed vector actions)]
           ^{:key (concat [(count actions)] path)}
           [action-unit (merge action
                               {:idx idx})])
         [actions-block]]
        [side-menu]]
       [text-chunks-modal]])
    (finally
      (re-frame/dispatch [::state/reset-form]))))
