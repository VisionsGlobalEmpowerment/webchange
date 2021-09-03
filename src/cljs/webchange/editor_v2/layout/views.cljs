(ns webchange.editor-v2.layout.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-dialogs.form.views :refer [activity-dialogs-form]]
    [webchange.editor-v2.activity-dialogs.menu.views :refer [activity-dialogs-menu]]
    [webchange.editor-v2.activity-form.common.object-form.state :as object-form-state]
    [webchange.editor-v2.activity-form.common.object-form.views :refer [object-form]]
    [webchange.editor-v2.activity-form.common.state :as activity-form-state]
    [webchange.editor-v2.activity-form.views :refer [activity-form]]
    [webchange.editor-v2.layout.components.course-status.views :refer [review-status]]
    [webchange.editor-v2.layout.components.sandbox.views :refer [share-button]]
    [webchange.editor-v2.layout.components.sync-status.views :refer [sync-status]]
    [webchange.editor-v2.layout.state :as state]
    [webchange.ui-framework.components.index :refer [icon-button]]
    [webchange.ui-framework.layout.views :as ui]))

(defn- change-activity-mode
  []
  (let [handle-click #(re-frame/dispatch [::state/switch-content-mode])]
    [icon-button {:icon     "swap"
                  :on-click handle-click}
     "Change mode"]))

(defn layout
  [{:keys [show-preview show-review? scene-data]
    :or   {show-preview true
           show-review? false}}]
  (let [show-object-form-menu? @(re-frame/subscribe [::object-form-state/show-edit-menu?])
        reset-selection #(re-frame/dispatch [::activity-form-state/reset-selection])
        current-content @(re-frame/subscribe [::state/current-content])
        content-props {:scene-data scene-data}]
    [ui/layout {:actions           (cond-> [[sync-status {:class-name "sync-status"}]]
                                           show-review? (conj [review-status])
                                           show-preview (conj [share-button]))
                :scene-data        scene-data
                :edit-menu-content (case current-content
                                     :activity-dialogs [[activity-dialogs-menu]]
                                     :activity-stage [[object-form]])
                :show-edit-menu?   (or show-object-form-menu?
                                       (= current-content :activity-dialogs))
                :on-edit-menu-back (case current-content
                                     :activity-dialogs nil
                                     :activity-stage reset-selection)
                :content-title     (case current-content
                                     :activity-dialogs "Dialogue"
                                     :activity-stage "Stage")
                :title-actions     [[change-activity-mode]]}
     (case current-content
       :activity-dialogs [activity-dialogs-form content-props]
       :activity-stage [activity-form content-props])]))
