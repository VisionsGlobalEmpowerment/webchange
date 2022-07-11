(ns webchange.lesson-builder.components.options-list.views
  (:require
    [webchange.ui.index :as ui]))

(defn- options-list-item
  [{:keys [id icon text on-click]}]
  (let [handle-click #(when (fn? on-click)
                        (on-click id))]
    [:div {:class-name "options-list-item"
           :on-click   handle-click}
     [:span.options-list-item--name
      text]
     [ui/icon {:icon       icon
               :class-name "options-list-item--icon"}]]))

(defn options-list
  [{:keys [items on-click]}]
  [:div.component--options-list
   (for [{:keys [id] :as item-data} items]
     ^{:key id}
     [options-list-item (cond-> item-data
                                (-> item-data (contains? :on-click) not) (assoc :on-click on-click))])])
