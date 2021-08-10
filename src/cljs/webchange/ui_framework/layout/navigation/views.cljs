(ns webchange.ui-framework.layout.navigation.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.state.core :as state]
    [webchange.auth.subs :as as]
    [webchange.routes :refer [location redirect-to]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]
    [webchange.ui-framework.layout.user-widget.views :refer [user-widget]]
    [webchange.ui-framework.layout.logo.views :refer [logo]]
    [webchange.ui-framework.components.icon.index :as icon]))

(def menu-items
  [{:title         "Dashboard"
    :icon          ""
    :location-name :welcome-screen}
   {:title         "Create"
    :icon          ""
    :location-name :title}
   {:title         "Game library"
    :icon          ""
    :location-name :game-library}
   {:title         "Books library"
    :icon          ""
    :location-name :book-library}
   {:title         "Translate"
    :icon          ""
    :location-name :translate}
   {:title         "My Profile"
    :icon          ""
    :location-name :my-profile}
   {:title         "My Books"
    :icon          ""
    :location-name :my-books}
   {:title         "My Games"
    :icon          ""
    :location-name :my-games}])

(declare menu)

(defn- goto
  [{:keys [location-name route-name route-params]}]
  (cond
    (some? location-name) (location location-name)
    (some? route-name) (if (sequential? route-params)
                         (->> (concat [route-name] route-params)
                              (apply redirect-to))
                         (redirect-to route-name))))

(defn- menu-item
  [{:keys [children title] :as props}]
  (let [active? false
        handle-click #(goto props)]
    [:li {:class-name (get-class-name {"menu-item" true
                                       "active"    active?})}
     [:span
      [icon/component {:icon "user"}]]
     [:span   {:on-click handle-click}
      title]
     (when-not (empty? children)
       [menu {:items children}])]))

(defn- menu
  [{:keys [items]}]
  [:ul
   (for [{:keys [title] :as props} items]
     ^{:key title}
     [menu-item props])])

(defn navigation-menu
  []
  (let [current-user @(re-frame/subscribe [::as/user])]
    [:div.left-menu
     [:div.icon-top
      [logo]]
     [:div.nav-menu
      [menu {:items menu-items}]]
      [user-widget]]))
