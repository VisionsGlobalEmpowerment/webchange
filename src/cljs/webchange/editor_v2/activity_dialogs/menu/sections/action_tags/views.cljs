(ns webchange.editor-v2.activity-dialogs.menu.sections.action-tags.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-dialogs.menu.sections.action-tags.state :as state]
    [webchange.editor-v2.activity-dialogs.menu.sections.common.section-block.views :refer [section-block]]
    [webchange.ui-framework.components.index :refer [chip label]]))

(defn- current-tags
  []
  (let [current-tags @(re-frame/subscribe [::state/current-tags])
        handle-remove (fn [tag]
                        (re-frame/dispatch [::state/remove-tag tag]))]
    [:div.tags-list
     (for [{:keys [text value]} current-tags]
       ^{:key value}
       [chip {:label      text
              :value      value
              :on-remove  handle-remove
              :class-name "tag-chip"}])]))

(defn- available-tags-form
  []
  (let [available-tags @(re-frame/subscribe [::state/available-tags])
        handle-tag-click (fn [tag]
                           (re-frame/dispatch [::state/add-tag tag]))]
    [:div.tags-list
     (for [{:keys [text value]} available-tags]
       ^{:key value}
       [chip {:label      text
              :value      value
              :on-click   handle-tag-click
              :class-name "tag-chip"}])]))

(defn form
  []
  [:div.action-tags-form
   [label "Current tags"]
   [current-tags]
   [label "Available tags"]
   [available-tags-form]])
