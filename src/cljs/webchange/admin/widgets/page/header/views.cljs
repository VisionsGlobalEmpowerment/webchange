(ns webchange.admin.widgets.page.header.views
  (:require
    [clojure.spec.alpha :as s]
    [webchange.ui.components.icon.spec :as icon-spec]
    [webchange.ui.index :refer [get-class-name] :as ui]))


(s/def ::header-icon (s/or :empty nil? :defined ::icon-spec/navigation-icon))
(s/def ::header-on-close (s/or :empty nil? :defined fn?))
(s/def ::header-title (s/or :empty nil? :defined string?))

(s/def :header-actions/text string?)
(s/def :header-actions/icon ::icon-spec/system-icon)
(s/def :header-actions/on-click fn?)
(s/def :header-actions/item (s/keys :req-un [:header-actions/text :header-actions/icon :header-actions/on-click]))
(s/def ::header-actions (s/or :empty empty? :defined (s/coll-of :header-actions/item)))

(s/def :header-stat/icon ::icon-spec/navigation-icon)
(s/def :header-stat/counter (s/or :empty nil? :defined number?))
(s/def :header-stat/label string?)
(s/def :header-stat/item (s/keys :req-un [:header-stat/icon :header-stat/counter :header-stat/label]))
(s/def ::header-stats (s/or :empty empty? :defined (s/coll-of :header-stat/item)))

(defn- header-stats
  [{:keys [stats]}]
  [:div.widget--page--header--stats
   (for [[idx {:keys [counter icon label]}] (map-indexed vector stats)]
     ^{:key idx}
     [:div.widget--page--header--stats-item
      [ui/navigation-icon {:icon icon}]
      [:span counter] label])])

(defn- header-action
  [{:keys [icon on-click text]}]
  (let [handle-click #(when (fn? on-click) (on-click))]
    [ui/tab {:action     icon
             :on-click   handle-click
             :class-name "widget--page--header-action"}
     text]))

(defn- header-actions
  [{:keys [actions]}]
  [:div {:class-name (get-class-name {"widget--page--header-actions" true})}
   (for [[idx action] (map-indexed vector actions)]
     ^{:key idx}
     [header-action action])])

(defn header
  [{:keys [actions icon on-close stats title] :as props}]
  {:pre [(s/valid? ::header-actions actions)
         (s/valid? ::header-icon icon)
         (s/valid? ::header-on-close on-close)
         (s/valid? ::header-stats stats)
         (s/valid? ::header-title title)]}
  [:div.widget--page--header
   (when (some? icon)
     [ui/navigation-icon {:icon       icon
                          :class-name "widget--page--header--navigation-icon"}])
   (when (some? title)
     [:div.widget--page--header--title
      title])
   (when-not (empty? stats)
     [header-stats props])
   [:div.widget--page--header--filler]
   [header-actions props]
   (when (fn? on-close)
     [ui/button {:icon       "close"
                 :class-name "widget--page--header--close-button"
                 :on-click   on-close}])])
