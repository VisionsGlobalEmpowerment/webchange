(ns webchange.ui.components.list.views
  (:require
    [reagent.core :as r]
    [webchange.ui.components.avatar.views :refer [avatar]]
    [webchange.ui.components.button.views :refer [button]]
    [webchange.ui.components.chip.views :refer [chip]]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

(defn- item-avatar
  [props]
  (when (contains? props :avatar)
    [avatar]))

(defn- item-name
  [{:keys [name]}]
  [:div {:class-name "bbs--list-item--name"}
   name])

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

(defn list-item
  [{:keys [class-name dense?] :as props}]
  (into [:div {:class-name (get-class-name {"bbs--list-item"        true
                                            "bbs--list-item--dense" dense?
                                            class-name              (some? class-name)})}]
        (concat [[item-avatar props]
                 [item-name props]]
                (->> (r/current-component)
                     (r/children))
                [[item-info props]]
                [[item-stats props]]
                [[item-controls props]]
                [[item-actions props]])))

(defn list
  [{:keys [class-name]}]
  (->> (r/current-component)
       (r/children)
       (into [:div {:class-name (get-class-name (merge {"bbs--list" true
                                                        class-name  (some? class-name)}))}])))
