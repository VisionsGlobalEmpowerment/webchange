(ns webchange.admin.pages.login.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.login.state :as state]
    [webchange.ui-framework.components.index :refer [button circular-progress input]]))

(defn page
  []
  (let [username @(re-frame/subscribe [::state/username])
        password @(re-frame/subscribe [::state/password])
        loading? @(re-frame/subscribe [::state/loading?])

        handle-username-changed #(re-frame/dispatch [::state/set-username %])
        handle-password-changed #(re-frame/dispatch [::state/set-password %])

        submit #(re-frame/dispatch [::state/login])]
    [:div.page--login
     [:div.login-panel
      [:h1 "Log in"]
      [:div.login-form
       [input {:label          "Username"
               :value          username
               :on-change      handle-username-changed
               :disabled?      loading?
               :on-enter-press submit}]
       [input {:label          "Password"
               :type           "password"
               :value          password
               :on-change      handle-password-changed
               :disabled?      loading?
               :on-enter-press submit}]]
      [button {:on-click   submit
               :class-name "login-button"
               :disabled?  loading?}
       (if-not loading?
         "Log In"
         [circular-progress])]]]))
