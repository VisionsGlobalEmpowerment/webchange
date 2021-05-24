(ns webchange.editor-v2.activity-form.common.object-form.animation-form.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.activity-form.common.object-form.animation-form.state :as state]
    [webchange.ui-framework.components.index :refer [select select-image]]))

;thumbnail value

(defn form
  [{:keys [id objects-data objects-names]}]
  (r/with-let [_ (re-frame/dispatch [::state/init id objects-data objects-names])]
    (let [value @(re-frame/subscribe [::state/current-skin id])
          options @(re-frame/subscribe [::state/skin-options id])
          handle-change #(re-frame/dispatch [::state/set-current-skin id %])]
      [:div
       [select-image {:value       (or value "")
                      :on-change   handle-change
                      :options     options
                      :with-arrow? false
                      :show-image? false}]])))
