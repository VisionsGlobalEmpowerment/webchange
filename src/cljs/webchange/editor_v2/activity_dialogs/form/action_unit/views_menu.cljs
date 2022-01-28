(ns webchange.editor-v2.activity-dialogs.form.action-unit.views-menu
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-dialogs.form.state-actions :as state-actions]
    [webchange.ui-framework.components.index :refer [icon-button]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- pre-relative-position [value] (or (nil? value) (some #{value} [:after :before :parallel])))
(defn- pre-action-data [{:keys [node-data source]}] (and (some #{source} [:concept :scene])
                                                         (some? node-data)))

(defn add-scene-action
  [{:keys [action-data relative-position] :or {relative-position :after}}]
  {:pre [(pre-action-data action-data)
         (pre-relative-position relative-position)]}
  #_(re-frame/dispatch [::state-actions/add-scene-action (merge action-data
                                                              {:relative-position relative-position})]))

(defn add-concept-action
  [{:keys [action-data relative-position] :or {relative-position :after}}]
  {:pre [(pre-action-data action-data)
         (pre-relative-position relative-position)]}
  #_(re-frame/dispatch [::state-actions/add-concept-action (merge action-data
                                                                {:relative-position relative-position})]))

(defn remove-action
  [{:keys [action-data]}]
  {:pre [(pre-action-data action-data)]}
  (re-frame/dispatch [::state-actions/remove-action action-data]))

(defn unit-menu
  [{:keys [action-data]}]
  [:div {:class-name "action-unit-menu"}
   [icon-button {:icon       "remove"
                 :size       "small"
                 :title      "Remove action"
                 :class-name "remove-button"
                 :on-click   #(remove-action {:action-data action-data})}]])
