(ns webchange.common.image-selector.views-modal
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.common.image-selector.form.views :refer [select-image-form]]
    [webchange.common.image-selector.state :as state]
    [webchange.ui-framework.components.index :refer [dialog]]))

(defn select-image-modal
  [{:keys [id on-change] :as props}]
  (let [open? @(re-frame/subscribe [::state/open? id])
        handle-close #(re-frame/dispatch [::state/close id])
        handle-change (fn [value]
                        (handle-close)
                        (when (fn? on-change)
                          (on-change value)))]
    [dialog {:open?    open?
             :on-close handle-close
             :title    "Select Image"
             :content-class-name "image-modal"}
     [select-image-form (merge props
                               {:id        id
                                :on-change handle-change})]]))

(defn with-image-modal
  [props]
  (r/with-let [id (->> (random-uuid) (str) (take 8) (clojure.string/join ""))
               [component component-props & children] (-> (r/current-component) (r/children) (first))
               handle-open #(re-frame/dispatch [::state/open id])]
    [:div
     (into [component (merge component-props {:on-click handle-open})] children)
     [select-image-modal (assoc props :id id)]]))
