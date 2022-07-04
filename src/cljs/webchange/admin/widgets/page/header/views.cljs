(ns webchange.admin.widgets.page.header.views
  (:require
    [clojure.spec.alpha :as s]
    [webchange.ui.components.icon.spec :as icon-spec]
    [webchange.ui.index :refer [get-class-name] :as ui]
    [webchange.ui.spec :as ui-spec]))


(s/def ::header-icon (s/or :empty nil? :defined ::icon-spec/navigation-icon))
(s/def ::header-icon-color (s/or :empty nil? :defined ::ui-spec/brand-color))
(s/def ::header-on-close (s/or :empty nil? :defined fn?))
(s/def ::header-title (s/or :empty nil? :defined string?))

(s/def :header-actions/text string?)
(s/def :header-actions/icon ::icon-spec/system-icon)
(s/def :header-actions/on-click fn?)
(s/def :header-actions/item (s/keys :req-un [:header-actions/text :header-actions/icon :header-actions/on-click]))
(s/def ::header-actions (s/or :empty empty? :defined (s/coll-of :header-actions/item)))

(s/def :header-info/key string?)
(s/def :header-info/value string?)
(s/def :header-info/item (s/keys :req-un [:header-info/key :header-info/value]))
(s/def ::header-info (s/or :empty empty? :defined (s/coll-of :header-info/item)))

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

(defn- header-info
  [{:keys [info]}]
  [:div.widget--page--header-info
   (for [{:keys [key value]} info]
     ^{:key key}
     [:div.widget--page--header-info-block
      [:div.widget--page--header-info-block--key key]
      [:div.widget--page--header-info-block--value value]])])

(defn- header-controls
  [{:keys [controls]}]
  (when (some? controls)
    (into [:div {:class-name (get-class-name {"widget--page--header-controls" true})}]
          controls)))

(defn- header-actions
  [{:keys [actions]}]
  (when (some? actions)
    [:div {:class-name (get-class-name {"widget--page--header-actions" true})}
     (for [[idx action] (map-indexed vector actions)]
       ^{:key idx}
       [header-action action])]))

(def component-class-name "widget--page--header")

(defn header
  [{:keys [actions icon icon-color info on-close stats title] :as props}]
  {:pre [(s/valid? ::header-actions actions)
         (s/valid? ::header-icon icon)
         (s/valid? ::header-icon-color icon-color)
         (s/valid? ::header-info info)
         (s/valid? ::header-on-close on-close)
         (s/valid? ::header-stats stats)
         (s/valid? ::header-title title)]}
  [:div {:class-name component-class-name}
   (when (some? icon)
     [ui/navigation-icon {:icon       icon
                          :class-name (get-class-name {"widget--page--header--navigation-icon"                          true
                                                       (str "widget--page--header--navigation-icon--color-" icon-color) true})}])
   (when (some? title)
     [:div.widget--page--header--title
      title])
   (when-not (empty? stats)
     [header-stats props])
   [header-info props]
   [:div.widget--page--header--filler]
   [header-controls props]
   [header-actions props]
   (when (fn? on-close)
     [ui/button {:icon       "close"
                 :class-name "widget--page--header--close-button"
                 :on-click   on-close}])])
