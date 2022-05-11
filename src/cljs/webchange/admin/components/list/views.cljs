(ns webchange.admin.components.list.views
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.index :as c]))

(defn- item-image
  [{:keys [img] :as props}]
  (cond
    (contains? props :img) [c/image-preview {:src img}]))

(defn- description-item
  [{:keys [t d]}]
  [:<>
   [:dt t]
   [:dd d]])

(defn- item-name
  [{:keys [name description]}]
  [:div.item-name
   [:div.name name]
   (when (some? description)
     [:dl
      (for [[idx [t d]] (map-indexed vector description)]
        ^{:key idx}
        [description-item {:t t
                           :d d}])])])

(defn- item-actions
  [{:keys [actions]}]
  (when (some? actions)
    [:div.item-actions actions]))

(defn content-right
  [{:keys [class-name]}]
  (->> (r/current-component)
       (r/children)
       (into [:div {:class-name (c/get-class-name {"content-right" true
                                                   class-name      (some? class-name)})}])))

(defn list-item
  [{:keys [on-click] :as props}]
  (into [:div (cond-> {:class-name (c/get-class-name {"list-item"  true
                                                      "with-hover" (fn? on-click)})}
                      (fn? on-click) (assoc :on-click on-click))]
        (concat [[item-image props]
                 [item-name props]]
                (->> (r/current-component)
                     (r/children))
                [[item-actions props]])))

(defn list
  [{:keys [class-name]}]
  (->> (r/current-component)
       (r/children)
       (into [:div {:class-name (c/get-class-name (merge {"component--list" true
                                                          class-name        (some? class-name)}))}])))
