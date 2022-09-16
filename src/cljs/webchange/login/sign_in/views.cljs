(ns webchange.login.sign-in.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.login.sign-in.state :as state]
    [webchange.ui.index :as ui]))

(defn sign-in-form
  []
  (re-frame/dispatch [::state/load-current-user])
  (fn []
    (let [current-user-loaded? @(re-frame/subscribe [::state/current-user-loaded?])
          
          username @(re-frame/subscribe [::state/username])
          password @(re-frame/subscribe [::state/password])
          loading? @(re-frame/subscribe [::state/loading?])

          handle-username-changed #(re-frame/dispatch [::state/set-username %])
          handle-password-changed #(re-frame/dispatch [::state/set-password %])

          submit #(re-frame/dispatch [::state/login])]
      (if current-user-loaded?
        [:div.sign-in-form
         [:h1 "Log in"]
         [:div.form
          [:div.fields
           [ui/input {:label          "Username"
                      :value          username
                      :name           "email"
                      :on-change      handle-username-changed
                      :disabled?      loading?
                      :on-enter-press submit}]
           [ui/password {:label          "Password"
                         :value          password
                         :name           "password"
                         :on-change      handle-password-changed
                         :disabled?      loading?
                         :on-enter-press submit}]]
          [:div.forgot-password-link
           [ui/link {:href "/accounts/reset-password"
                     :target "_self"} "Forgot your password?"]]
          [ui/button {:on-click   submit
                      :class-name "login-button"
                      :shape      "rounded"
                      :loading?   loading?
                      :disabled?  loading?}
           "Log In"]
          [:div.sign-up-link
           [ui/link {:href "/accounts/sign-up"
                     :target "_self"} "Sign Up"]]]]
        [ui/loading-overlay]))))
