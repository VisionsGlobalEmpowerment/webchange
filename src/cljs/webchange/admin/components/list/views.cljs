(ns webchange.admin.components.list.views
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.index :as c]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- item-image
  [{:keys [img] :as props}]
  (cond
    (contains? props :img) [c/image-preview {:src img}]))

(defn- item-name
  [{:keys [name description]}]
  [:div.item-name
   [:div.name name]
   (when (some? description)
     [:dl
      (for [[idx [t d]] (map-indexed vector description)]
        ^{:key idx}
        [:<>
         [:dt t]
         [:dd d]])])])

(defn- item-actions
  [{:keys [actions]}]
  (when (some? actions)
    [:div.item-actions actions]))

(defn content-right
  []
  (->> (r/current-component)
       (r/children)
       (into [:div.content-right])))

(defn list-item
  [{:keys [] :as props}]
  (into [:div.list-item]
        (concat [[item-image props]
                 [item-name props]]
                (->> (r/current-component)
                     (r/children))
                [[item-actions props]])))

(defn list
  [{:keys [class-name]}]
  (->> (r/current-component)
       (r/children)
       (into [:div {:class-name (get-class-name (merge {"component--list" true
                                                        class-name        (some? class-name)}))}])))
