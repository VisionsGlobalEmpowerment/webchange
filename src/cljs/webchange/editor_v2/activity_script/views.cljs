(ns webchange.editor-v2.activity-script.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.activity-script.state :as state]
    [webchange.editor-v2.dialog.dialog-form.views-form-actions :refer [form-actions]]
    [webchange.editor-v2.dialog.dialog-text-form.action-unit.views :refer [action-unit]]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.views :refer [side-menu]]
    [webchange.ui-framework.components.index :refer [button dialog icon-button]]))

(defn- dialog-form
  [{:keys [nodes title]}]
  [:div.sheet
   [:h3 title]
   (for [[idx {:keys [path] :as action}] (map-indexed vector nodes)]
     ^{:key (concat [(count nodes)] path)}
     [action-unit (merge action
                         {:idx idx})])])

(defn- track-selector
  []
  (let [current-track @(re-frame/subscribe [::state/current-track])
        tracks @(re-frame/subscribe [::state/available-tracks])
        handle-click #(re-frame/dispatch [::state/set-current-track %])]
    [:div.track-selector
     (for [{:keys [text idx]} tracks]
       ^{:key idx}
       [button {:color      (if (= current-track idx) "primary" "default")
                :shape      "rectangle"
                :class-name "track-button"
                :on-click   #(handle-click idx)}
        text])]))

(defn- script-form
  []
  (r/with-let []
    (let [script-data @(re-frame/subscribe [::state/script-data])]
      [:div {:class-name "activity-script-form"}
       [:div.work-field
        [track-selector]
        [:div.actions-script
         (for [{:keys [action-path] :as dialog-data} script-data]
           ^{:key action-path}
           [dialog-form dialog-data])]
        [side-menu]]])))

(defn script-modal
  []
  (let [open? @(re-frame/subscribe [::state/window-open?])
        handle-close #(re-frame/dispatch [::state/close-window])]
    [dialog
     {:title        "Activity Script"
      :open?        open?
      :on-close     handle-close
      :actions      [[form-actions {:on-close handle-close}]]
      :full-screen? true}
     [script-form]]))
