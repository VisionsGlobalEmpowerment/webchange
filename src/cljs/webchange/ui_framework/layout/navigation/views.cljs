(ns webchange.ui-framework.layout.navigation.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.state.core :as state]
    [webchange.auth.subs :as as]
    [webchange.routes :refer [location redirect-to]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]
    [webchange.ui-framework.layout.avatar.views :refer [avatar]]
    [webchange.ui-framework.layout.logo.views :refer [logo]]
    [webchange.ui-framework.layout.navigation.svg :as user-icn]))

(def menu-items
  [{:title         "Dashboard"
    :location-name :welcome-screen}
   {:title         "Create"
    :location-name :title}
   {:title         "Game library"
    :location-name :game-library}
   {:title         "Books library"
    :location-name :book-library}
   {:title         "Translate"
    :location-name :translate}
   {:title         "My Profile"
    :location-name :my-profile}
   {:title         "My Books"
    :location-name :my-books}
   {:title         "My Games"
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
      [user-icn/data]]
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
  (let [current-user @(re-frame/subscribe [::as/user])
        current-course-id @(re-frame/subscribe [::state/current-course-id])]
    [:div.left-side-bar
     [:div.pad-fit-l.mb-lg.d-inline
      [logo]]
     [:div.nav-menu
      [menu {:items menu-items}]]
     [:div.g-icon-bottom
      [:span
       [avatar]]
      [:span
       (str (:first-name current-user) " " (:last-name current-user))
       [:small 
        "Los Angeles, CA, USA"]]]]))
