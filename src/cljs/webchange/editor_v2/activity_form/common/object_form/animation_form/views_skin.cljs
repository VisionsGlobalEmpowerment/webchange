(ns webchange.editor-v2.activity-form.common.object-form.animation-form.views-skin
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-form.common.object-form.animation-form.state :as state]
    [webchange.ui-framework.components.index :refer [label select-image]]))

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
                    :show-image? false
                    :variant     "outlined"}]]))

(defn- multiple-skins
  [{:keys [class-name id]}]
  (let [value (re-frame/subscribe [::state/current-skin-names id])
        body-options @(re-frame/subscribe [::state/skin-body-options id])
        clothes-options @(re-frame/subscribe [::state/skin-clothes-options id])
        head-options @(re-frame/subscribe [::state/skin-head-options id])]
    [:div (cond-> {} (some? class-name) (assoc :class-name class-name))
     [label "Select Body:"]
     [select-image {:value       (or (:body @value) "")
                    :on-change   #(re-frame/dispatch [::state/set-current-skin-names id (assoc @value :body %)])
                    :options     body-options
                    :with-arrow? false
                    :show-image? false
                    :variant     "outlined"}]
     [label "Select Clothes:"]
     [select-image {:value       (or (:clothes @value) "")
                    :on-change   #(re-frame/dispatch [::state/set-current-skin-names id (assoc @value :clothes %)])
                    :options     clothes-options
                    :with-arrow? false
                    :show-image? false
                    :variant     "outlined"}]
     [label "Select Head:"]
     [select-image {:value       (or (:head @value) "")
                    :on-change   #(re-frame/dispatch [::state/set-current-skin-names id (assoc @value :head %)])
                    :options     head-options
                    :with-arrow? false
                    :show-image? false
                    :variant     "outlined"}]]))

(defn skin
  [{:keys [id] :as props}]
  (let [combined-skins? @(re-frame/subscribe [::state/combined-skins? id])]
    (if combined-skins?
      [multiple-skins props]
      [single-skin props])))
