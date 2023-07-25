(ns webchange.admin.widgets.layout.auth.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.widgets.layout.auth.state :as state]
    [webchange.ui.index :as ui]))

(defn auth
  []
  (re-frame/dispatch [::state/init])
  (fn []
    (let [user-name @(re-frame/subscribe [::state/user-name])
          handle-click #(re-frame/dispatch [::state/open-my-account-page])
          handle-logout #(do (re-frame/dispatch [::state/logout])
                             (.stopPropagation %))]
      [:div {:class-name "top-bar--auth"
             :on-click   handle-click}
       [:span {:class-name "logout"
               :on-click   handle-logout}
        "Log Out"]
       [ui/avatar]
       [:div.user-data
        [:div.name user-name]]])))
