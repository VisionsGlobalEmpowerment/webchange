(ns webchange.admin.widgets.layout.auth.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.widgets.layout.auth.state :as state]))

(defn auth
  []
  (let [handle-click #(re-frame/dispatch [::state/open-login-page])]
    [:div.top-bar--auth {:on-click handle-click}
     "User"]))
