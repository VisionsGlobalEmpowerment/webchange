(ns webchange.common.image-selector.views-modal
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.common.image-selector.form.views :refer [select-image-form]]
    [webchange.common.image-selector.state :as state]
    [webchange.ui-framework.components.index :refer [dialog]]))

(defn select-image-modal
  [{:keys [on-change] :as props}]
  (let [open? @(re-frame/subscribe [::state/open?])
        handle-close #(re-frame/dispatch [::state/close])
        handle-change (fn [value]
                        (handle-close)
                        (when (fn? on-change)
                          (on-change value)))]
    [dialog {:open?    open?
             :on-close handle-close
             :title    "Select Image"}
     [select-image-form (merge props
                               {:on-change handle-change})]]))

(defn with-image-modal
  [props]
  (let [[component component-props & children] (-> (r/current-component) (r/children) (first))
        handle-open #(re-frame/dispatch [::state/open])]
    [:div
     (into [component (merge component-props {:on-click handle-open})] children)
     [select-image-modal props]]))
