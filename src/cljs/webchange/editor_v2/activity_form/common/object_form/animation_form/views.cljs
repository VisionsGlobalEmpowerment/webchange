(ns webchange.editor-v2.activity-form.common.object-form.animation-form.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.activity-form.common.object-form.animation-form.state :as state]
    [webchange.ui-framework.components.index :refer [label select-image]]))

(defn- combined-skins?
  [options]
  (-> options
      first
      :value
      (clojure.string/split #"/")
      count
      (> 1)))

(defn- single-skin
  [{:keys [class-name id]}]
  (let [value @(re-frame/subscribe [::state/current-skin id])
        options @(re-frame/subscribe [::state/skin-options id])]
    [:div (cond-> {} (some? class-name) (assoc :class-name class-name))
     [label "Select Skin:"]
     [select-image {:value       (or value "")
                    :on-change   #(re-frame/dispatch [::state/set-current-skin id %])
                    :options     options
                    :with-arrow? false
                    :show-image? false}]]))

(defn- check-skin-option
  [option type]
  (-> option
      :value
      (clojure.string/split #"/")
      first
      (clojure.string/lower-case)
      keyword
      (= type)))

(defn- multiple-skins
  [{:keys [class-name id]}]
  (let [value (re-frame/subscribe [::state/current-skin-names id])
        options @(re-frame/subscribe [::state/skin-options id])
        body-options (->> options (filter #(check-skin-option % :body)))
        clothes-options (->> options (filter #(check-skin-option % :clothes)))
        head-options (->> options (filter #(check-skin-option % :head)))]
    [:div (cond-> {} (some? class-name) (assoc :class-name class-name))
     [label "Select Body:"]
     [select-image {:value       (or (:body @value) "")
                    :on-change   #(re-frame/dispatch [::state/set-current-skin-names id (assoc @value :body %)])
                    :options     body-options
                    :with-arrow? false
                    :show-image? false}]
     [label "Select Clothes:"]
     [select-image {:value       (or (:clothes @value) "")
                    :on-change   #(re-frame/dispatch [::state/set-current-skin-names id (assoc @value :clothes %)])
                    :options     clothes-options
                    :with-arrow? false
                    :show-image? false}]
     [label "Select Head:"]
     [select-image {:value       (or (:head @value) "")
                    :on-change   #(re-frame/dispatch [::state/set-current-skin-names id (assoc @value :head %)])
                    :options     head-options
                    :with-arrow? false
                    :show-image? false}]]))
(defn form
  [{:keys [id objects-data objects-names] :as props}]
  (r/with-let [_ (re-frame/dispatch [::state/init id objects-data objects-names])]
    (let [options @(re-frame/subscribe [::state/skin-options id])]
      (if (combined-skins? options)
        [multiple-skins props]
        [single-skin props]))))
