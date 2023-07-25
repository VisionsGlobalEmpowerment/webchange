(ns webchange.admin.components.list.views
  (:require
    [reagent.core :as r]
    [webchange.ui.index :as ui]
    [webchange.ui-framework.components.index :as c]))

(defn- item-image
  [{:keys [avatar img] :as props}]
  (cond
    (contains? props :avatar) [c/avatar {:src avatar}]
    (contains? props :img) [c/image {:src        img
                                     :class-name "item-image"}]))

(defn- item-prefix
  [{:keys [pre] :as props}]
  (when (contains? props :pre)
    [:div.item-prefix
     pre]))

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

(defn- item-action
  [{:keys [] :as props}]
  [ui/button (merge {:color "grey-3"}
                    props)])

(defn- item-actions
  [{:keys [actions]}]
  (when (some? actions)
    (let [actions-data? (-> actions first map?)]
      [:div.item-actions
       (if actions-data?
         [:<>
          (for [[idx action] (map-indexed vector actions)]
            ^{:key idx}
            [item-action action])]
         actions)])))

(defn content-right
  [{:keys [class-name]}]
  (->> (r/current-component)
       (r/children)
       (into [:div {:class-name (ui/get-class-name {"content-right" true
                                                    class-name      (some? class-name)})}])))

(defn list-item
  [{:keys [on-click class-name ref html-attrs] :as props}]
  "Props:s
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
  (into [:div (cond-> {:class-name (ui/get-class-name {"list-item"  true
                                                       "with-hover" (fn? on-click)
                                                       class-name   (some? class-name)})}
                      (fn? on-click) (assoc :on-click on-click)
                      (some? html-attrs) (merge html-attrs)
                      (some? ref) (assoc :ref ref))]
        (concat [[item-prefix props]
                 [item-image props]
                 [item-name props]]
                (->> (r/current-component)
                     (r/children))
                [[item-description props]]
                [[item-actions props]])))

(defn list
  [{:keys [class-name]}]
  (->> (r/current-component)
       (r/children)
       (into [:div {:class-name (ui/get-class-name (merge {"component--list" true
                                                           class-name        (some? class-name)}))}])))
