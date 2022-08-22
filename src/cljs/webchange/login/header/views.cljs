(ns webchange.login.header.views
  (:require
    [webchange.ui.index :as ui]))

(def links [{:title "Features"
             :url   "https://bluebrickschool.org/#feat"}
            {:title "Partners"
             :url   "https://bluebrickschool.org/#part"}
            {:title "FAQ"
             :url   "https://bluebrickschool.org/#faq"}
            {:title "Contact"
             :url   "https://bluebrickschool.org/#cont"}
            {:title "Blue Brick School Beta"
             :url   "https://bluebrickschool.org/sign-up"}])

(defn- navigation-item
  [{:keys [title url]}]
  [:div {:class-name "page-header--navigation-item"}
   [ui/link {:href url}
    title]])

(defn- navigation
  []
  [:div {:class-name "page-header--navigation"}
   (for [[idx item-data] (map-indexed vector links)]
     ^{:key idx}
     [navigation-item item-data])])

(defn- sign-up
  []
  [ui/button {:color      "blue-1"
              :class-name "page-header--sign-up"
              :href       "https://bluebrickschool.org/sign-up/"}
   "Sign Up Free"])

(defn header
  []
  [:div {:class-name "widget--page-header"}
   [ui/logo-with-name {:class-name "page-header--logo"}]
   [navigation]
   [sign-up]])

