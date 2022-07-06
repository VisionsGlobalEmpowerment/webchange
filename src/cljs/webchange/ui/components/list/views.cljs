(ns webchange.ui.components.list.views
  (:require
    [reagent.core :as r]
    [webchange.ui.components.avatar.views :refer [avatar]]
    [webchange.ui.components.button.views :refer [button]]
    [webchange.ui.components.chip.views :refer [chip]]
    [webchange.ui.components.complete-progress.views :refer [complete-progress]]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

(defn- item-avatar
  [props]
  (when (contains? props :avatar)
    [:div {:class-name "bbs--list-item--avatar"}
     [avatar]]))

(defn- item-name
  [{:keys [class-name--name description name]}]
  [:div {:class-name "bbs--list-item--name"}
   [:div {:class-name (get-class-name {"bbs--list-item--name--value" true
                                       class-name--name              (some? class-name--name)})
          :title      name}
    name]
   (when (some? description)
     [:div.bbs--list-item--name--description description])])

(defn- item-info
  [{:keys [info]}]
  (when-not (empty? info)
    [:div {:class-name "bbs--list-item--info"}
     (for [{:keys [key value]} info]
       ^{:key key}
       [:div {:class-name "bbs--list-item--info-item"}
        [:div {:class-name "bbs--list-item--info-item--key"} key]
        [:div {:class-name "bbs--list-item--info-item--value"} value]])]))

(defn- item-stats
  [{:keys [stats]}]
  (when-not (empty? stats)
    [:div {:class-name "bbs--list-item--stats"}
     (for [[idx {:keys [text] :as stat-props}] (map-indexed vector stats)]
       ^{:key idx}
       [chip stat-props text])]))

(defn- get-children-count
  [children]
  (let [props (-> children last last)]
    (if (map? props)
      (count children)
      (if-let [first-child (first children)]
        (get-children-count first-child)
        0))))

(defn- item-children
  [{:keys [children]}]
  (when (some? children)
    (let [children-number (get-children-count children)]
      (into [:div {:class-name (get-class-name {"bbs--list-item--children"                                 true
                                                (str "bbs--list-item--children--columns-" children-number) true})}]
            children))))

(defn- item-pre
  [{:keys [pre]}]
  (when-not (empty? pre)
    [:div {:class-name "bbs--list-item--pre"}
     pre]))

(defn- item-controls
  [{:keys [controls]}]
  (when-not (empty? controls)
    [:div {:class-name "bbs--list-item--controls"}
     controls]))

(defn- item-actions
  [{:keys [actions]}]
  (when-not (empty? actions)
    [:div {:class-name "bbs--list-item--actions"}
     (for [[idx action] (map-indexed vector actions)]
       ^{:key idx}
       [button (merge {:color "grey-3"}
                      action)])]))

(defn- item-filler
  [{:keys [children]}]
  (when (empty? children)
    [:div.bbs--list-item--filler]))

(defn list-item
  [{:keys [class-name dense? on-click] :as props}]
  (let [props (assoc props :children (->> (r/current-component)
                                          (r/children)))]
    [:div (cond-> {:class-name (get-class-name {"bbs--list-item"            true
                                                "bbs--list-item--dense"     dense?
                                                "bbs--list-item--clickable" (fn? on-click)
                                                class-name                  (some? class-name)})}
                  (fn? on-click) (assoc :on-click on-click))
     [item-pre props]
     [item-avatar props]
     [item-name props]
     [item-filler props]
     [item-info props]
     [item-stats props]
     [item-children props]
     [item-controls props]
     [item-actions props]]))

(defn list
  [{:keys [class-name]}]
  (->> (r/current-component)
       (r/children)
       (into [:div {:class-name (get-class-name (merge {"bbs--list" true
                                                        class-name  (some? class-name)}))}])))
