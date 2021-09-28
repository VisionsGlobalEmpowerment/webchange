(ns webchange.common.image-selector.form.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.common.image-selector.form.state :as state]
    [webchange.ui-framework.components.index :refer [checkbox label select]]))

(defn- tag-selector
  [{:keys [id]}]
  (let [options (into [] @(re-frame/subscribe [::state/tags-options id]))
        handle-change (fn [{:keys [props text value checked?]}]
                        (re-frame/dispatch [::state/update-current-tags id value checked?]))]
    [:div.tag-selector
     (for [{:keys [text value selected?] :as props} options]
       ^{:key value}
       [:div {:class-name (if selected? "chip-active" "chip")
              :on-click   #(handle-change {:value    value
                                           :text     text
                                           :checked? (not selected?)
                                           :props    props})}
        text
        (if selected?
          [:span.closebtn "×"])])]))

(defn- assets-list-item
  [{:keys [on-click path thumbnail-path]}]
  (let [handle-click #(on-click path)]
    [:div {:class-name "assets-list-item"
           :on-click   handle-click}
     [:img {:src thumbnail-path}]]))

(defn- assets-list
  [{:keys [id on-click]}]
  (let [assets @(re-frame/subscribe [::state/assets id])]
    [:div.assets-list
     (if-not (empty? assets)
       (for [{:keys [id] :as asset} assets]
         ^{:key id}
         [assets-list-item (merge asset
                                  {:on-click on-click})])
       [:div.empty-list-message
        "No available images"])]))

(defn select-image-form
  [{:keys [id tags on-change]}]
  (r/with-let [_ (re-frame/dispatch [::state/init id tags])]
    [:div.select-image-form
    ;;  [selected-tag]
     [tag-selector {:id id}]
     [assets-list {:id       id
                   :on-click on-change}]]))
