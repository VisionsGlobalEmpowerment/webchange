(ns webchange.admin.components.list.views
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.index :as c]))

(defn- item-image
  [{:keys [avatar img] :as props}]
  (cond
    (contains? props :avatar) [c/avatar {:src avatar}]
    (contains? props :img) [c/image-preview {:src img}]))

(defn- description-item
  [{:keys [t d]}]
  [:<>
   [:dt t]
   [:dd d]])

(defn- item-name
  [{:keys [name name-description]}]
  [:div.item-name
   [:div.name name]
   (when (some? name-description)
     [:dl
      (for [[idx [t d]] (map-indexed vector name-description)]
        ^{:key idx}
        [description-item {:t t
                           :d d}])])])

(defn item-description
  [{:keys [description]}]
  (when (some? description)
    [:dl.item-description
     (for [[idx [t d]] (map-indexed vector description)]
       ^{:key idx}
       [description-item {:t t
                          :d d}])]))

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
  [{:keys [on-click class-name] :as props}]
  "Props:
   - img
   - avatar
   - name
   - name-description
     [key value]
   - description
     [key value]
   - actions
   - class-name
   "
  (into [:div (cond-> {:class-name (c/get-class-name {"list-item"  true
                                                      "with-hover" (fn? on-click)
                                                      class-name   (some? class-name)})}
                      (fn? on-click) (assoc :on-click on-click))]
        (concat [[item-image props]
                 [item-name props]]
                (->> (r/current-component)
                     (r/children))
                [[item-description props]]
                [[item-actions props]])))

(defn list
  [{:keys [class-name]}]
  (->> (r/current-component)
       (r/children)
       (into [:div {:class-name (c/get-class-name (merge {"component--list" true
                                                          class-name        (some? class-name)}))}])))
