(ns webchange.common.character-selector.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.common.character-selector.state :as state]
    [webchange.ui-framework.components.index :refer [label select-image]]))

(defn skeleton-selector
  [{:keys [class-name on-change value]}]
  (r/with-let [_ (re-frame/dispatch [::state/init])]
    (let [options @(re-frame/subscribe [::state/skeletons-options])]
      [select-image {:value      value
                     :on-change  on-change
                     :options    options
                     :class-name class-name}])))

(defn- single-skin
  [{:keys [class-name disabled? on-change skeleton value]}]
  (let [options @(re-frame/subscribe [::state/skins-options skeleton])]
    [select-image {:value      value
                   :on-change  on-change
                   :options    options
                   :disabled?  disabled?
                   :class-name class-name}]))

(defn- combined-skin-item
  [{:keys [on-change options title value]}]
  [:div
   [label title]
   [select-image {:value       value
                  :on-change   on-change
                  :options     options
                  :with-arrow? false
                  :show-image? false}]])

(defn combined-skins
  [{:keys [class-name on-change skeleton value]
    :or   {value {}}}]
  (let [handle-change (fn [option-key option-value]
                        (->> (assoc value option-key option-value)
                             (on-change)))]
    [:div (cond-> {} (some? class-name) (assoc :class-name class-name))
     [combined-skin-item {:value     (get value :body)
                          :title     "Select Body:"
                          :skeleton  skeleton
                          :options   @(re-frame/subscribe [::state/combined-skins-options skeleton :body])
                          :on-change #(handle-change :body %)}]
     [combined-skin-item {:value     (get value :clothes)
                          :title     "Select Clothes:"
                          :skeleton  skeleton
                          :options   @(re-frame/subscribe [::state/combined-skins-options skeleton :clothes])
                          :on-change #(handle-change :clothes %)}]
     [combined-skin-item {:value     (get value :head)
                          :title     "Select Head:"
                          :skeleton  skeleton
                          :options   @(re-frame/subscribe [::state/combined-skins-options skeleton :head])
                          :on-change #(handle-change :head %)}]]))

(defn skin-selector
  [{:keys [skeleton value] :as props}]
  (r/with-let [_ (re-frame/dispatch [::state/init])]
    (let [combined-skins? @(re-frame/subscribe [::state/combined-skins? skeleton])]
      (if combined-skins?
        ^{:key value}
        [combined-skins props]
        [single-skin props]))))
