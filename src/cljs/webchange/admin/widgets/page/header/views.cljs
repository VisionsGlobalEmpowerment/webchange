(ns webchange.admin.widgets.page.header.views
  (:require
    [clojure.spec.alpha :as s]
    [webchange.ui.components.icon.spec :as icon-spec]
    [webchange.ui.index :refer [get-class-name] :as ui]))


(s/def :header/text string?)
(s/def :header/icon ::icon-spec/system-icon)
(s/def :header/on-click fn?)
(s/def :header/action (s/keys :req-un [:header/text :header/icon :header/on-click]))
(s/def :header/actions (s/or :empty nil?
                             :defined (s/coll-of :header/action)))

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
  [{:keys [actions icon on-close title] :as props}]
  {:pre [(s/valid? :header/actions actions)
         (or (nil? icon) (s/valid? ::icon-spec/navigation-icon icon))
         (or (nil? on-close) (fn? on-close))
         (or (nil? title) (string? title))]}
  [:div.widget--page--header
   (when (some? icon)
     [ui/navigation-icon {:icon       icon
                          :class-name "widget--page--header--navigation-icon"}])
   (when (some? title)
     [:div.widget--page--header--title
      title])
   [:div.widget--page--header--filler]
   [header-actions props]
   (when (fn? on-close)
     [ui/button {:icon       "close"
                 :class-name "widget--page--header--close-button"
                 :on-click   on-close}])])
