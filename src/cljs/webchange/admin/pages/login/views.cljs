(ns webchange.admin.pages.login.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.login.state :as state]
    [webchange.ui-framework.components.index :refer [button input]]))

(defn page
  []
  (let [username @(re-frame/subscribe [::state/username])
        password @(re-frame/subscribe [::state/password])
        handle-username-changed #(re-frame/dispatch [::state/set-username %])
        handle-password-changed #(re-frame/dispatch [::state/set-password %])
        handle-login-click #(re-frame/dispatch [::state/login])]
    [:div.page--login
     [:div.login-panel
      [:h1 "Log in"]
      [input {:label     "Username"
              :value     username
              :on-change handle-username-changed}]
      [input {:label     "Password"
              :type      "password"
              :value     password
              :on-change handle-password-changed}]
      [button {:on-click handle-login-click}
       "Log In"]]]))
