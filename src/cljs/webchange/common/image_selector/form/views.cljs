(ns webchange.common.image-selector.form.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.common.image-selector.form.state :as state]
    [webchange.ui-framework.components.index :refer [input]]))

(defn- tag-chip
  [{:keys [text value selected? handle-change]}]
  [:div {:class-name (if selected? "chip-active" "chip")
         :on-click   #(handle-change {:text     text
                                      :value    value
                                      :checked? (not selected?)})}
   text])

(defn- tag-selector
  [{:keys [id]}]
  (let [handle-change (fn [{:keys [text value checked?]}]
                        (re-frame/dispatch [::state/set-current-tags id []]))
        current-tags @(re-frame/subscribe [::state/current-tags id])]
    [:div.tag-selector
     (for [{:keys [name id]} current-tags]
       ^{:key id}
       [tag-chip {:text          name
                  :value         id
                  :selected?     true
                  :handle-change handle-change}])]))

(defn- search-field
  [{:keys [id]}]
  (let [value @(re-frame/subscribe [::state/current-query id])
        handle-change #(re-frame/dispatch [::state/update-search-query id %])]
    [input {:value       value
            :placeholder "Search"
            :on-change   handle-change}]))

(defn- assets-list-item
  [form-id {:keys [on-click path thumbnail-path tags]}]
  (let [handle-click #(on-click path)
        handle-change (fn [{:keys [text value checked?]}]
                        (re-frame/dispatch [::state/set-current-tags form-id [{:id value :name text}]]))]
    [:div 
     [:div {:class-name "assets-list-item"
            :on-click   handle-click}
      [:img {:src thumbnail-path}]]
     [:div.tag-selector
      (for [{:keys [name id]} tags]
        ^{:key id}
        [tag-chip {:text          name
                   :value         id
                   :handle-change handle-change}])]]))

(defn- assets-list
  [{form-id :id :keys [on-click]}]
  (let [assets @(re-frame/subscribe [::state/assets form-id])]
    [:div.assets-list
     (if-not (empty? assets)
       (for [{:keys [id] :as asset} assets]
         ^{:key id}
         [assets-list-item form-id (merge asset
                                          {:on-click on-click})])
       [:div.empty-list-message
        "No available images"])]))

(defn select-image-form
  [{:keys [id tags on-change]}]
  (r/with-let [_ (re-frame/dispatch [::state/init id tags])]
    [:div.select-image-form
     [search-field {:id id}]
     [tag-selector {:id id}]
     [assets-list {:id       id
                   :on-click on-change}]]))
