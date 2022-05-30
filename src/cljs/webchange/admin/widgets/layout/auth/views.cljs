(ns webchange.admin.widgets.layout.auth.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.widgets.layout.auth.state :as state]
    [webchange.ui-framework.components.index :as c]))

(defn- menu-item
  [{:keys [text icon on-click]}]
  (let [handle-click #(do (.stopPropagation %)
                          (on-click))]
    [:div {:class-name "menu-item"
           :on-click   handle-click}
     text
     (when (some? icon)
       [c/icon {:icon icon}])]))

(defn- menu
  []
  (let [handle-logout #(re-frame/dispatch [::state/logout])]
    [:div.menu
     [menu-item {:text     "Log Out"
                 :on-click handle-logout}]]))

(defn auth
  []
  (re-frame/dispatch [::state/init])
  (fn []
    (let [user-name @(re-frame/subscribe [::state/user-name])
          handle-click #(re-frame/dispatch [::state/open-my-account-page])]
      [:div {:class-name "top-bar--auth"
             :on-click   handle-click}
       [c/avatar]
       [:div.user-data
        [:div.name user-name]]
       [menu]])))
