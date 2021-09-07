(ns webchange.common.image-selector.form.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.common.image-selector.form.state :as state]
    [webchange.ui-framework.components.index :refer [label select]]))

(defn- tag-selector
  []
  (let [value @(re-frame/subscribe [::state/current-tag])
        options @(re-frame/subscribe [::state/tags-options])
        handle-change #(re-frame/dispatch [::state/set-current-tags %])]
    [:div.tag-selector
     [label "Tag:"]
     [select {:placeholder "Select tag"
              :variant     "outlined"
              :class-name  "tag-selector-control"
              :value       value
              :options     options
              :on-change   handle-change
              :multiple?   true
              :type        "int"}]]))

(defn- assets-list-item
  [{:keys [on-click path thumbnail-path]}]
  (let [handle-click #(on-click path)]
    [:div {:class-name "assets-list-item"
           :on-click   handle-click}
     [:img {:src thumbnail-path}]]))

(defn- assets-list
  [{:keys [on-click]}]
  (let [assets @(re-frame/subscribe [::state/assets])]
    [:div.assets-list
     (for [{:keys [id] :as asset} assets]
       ^{:key id}
       [assets-list-item (merge asset
                                {:on-click on-click})])]))

(defn select-image-form
  [{:keys [tags on-change]}]
  (r/with-let [_ (re-frame/dispatch [::state/init tags])]
    [:div.select-image-form
     [tag-selector]
     [assets-list {:on-click on-change}]]))