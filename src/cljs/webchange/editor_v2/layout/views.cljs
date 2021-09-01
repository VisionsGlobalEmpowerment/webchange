(ns webchange.editor-v2.layout.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.ui-framework.layout.views :as ui]
    [webchange.editor-v2.activity-form.common.object-form.state :as object-form-state]
    [webchange.editor-v2.activity-form.common.object-form.views :refer [object-form]]
    [webchange.editor-v2.activity-form.common.state :as activity-form-state]
    [webchange.editor-v2.layout.components.course-status.views :refer [review-status]]
    [webchange.editor-v2.layout.components.sandbox.views :refer [share-button]]
    [webchange.editor-v2.layout.components.sync-status.views :refer [sync-status]]))

(defn layout
  [{:keys [show-preview show-review? scene-data]
    :or   {show-preview true
           show-review? false}}]
  (r/with-let [this (r/current-component)]
    (let [show-edit-menu? @(re-frame/subscribe [::object-form-state/show-edit-menu?])
          handle-edit-menu-back #(re-frame/dispatch [::activity-form-state/reset-selection])]
      (into [ui/layout {:actions           (cond-> [[sync-status {:class-name "sync-status"}]]
                                                   show-review? (conj [review-status])
                                                   show-preview (conj [share-button]))
                        :scene-data        scene-data
                        :edit-menu-content [[object-form]]
                        :show-edit-menu?   show-edit-menu?
                        :on-edit-menu-back handle-edit-menu-back}]
            (r/children this)))))
