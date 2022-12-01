(ns webchange.login.sign-in.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.login.sign-in.state :as state]
    [webchange.ui.index :as ui]))

(defn sign-in-form
  [props]
  (let [username @(re-frame/subscribe [::state/username])
        password @(re-frame/subscribe [::state/password])
        loading? @(re-frame/subscribe [::state/loading?])

        sign-in-as-type @(re-frame/subscribe [::state/sign-in-as-type (:type props)])
        handle-username-changed #(re-frame/dispatch [::state/set-username %])
        handle-password-changed #(re-frame/dispatch [::state/set-password %])

        submit #(re-frame/dispatch [::state/login sign-in-as-type])]
    [:div.sign-in-form
     [:div.login-as
      [:h1 "Log In As:"]
      [ui/button {:href "/accounts/sign-in/parent"
                  :shape "rounded"
                  :target "_self"
                  :class-name (ui/get-class-name {"log-in-as-button"        true
                                                  "log-in-as-button-active" (= sign-in-as-type "parent")})}
       "Parent"]
      [ui/button {:href "/accounts/sign-in/admin"
                  :shape "rounded"
                  :target "_self"
                  :class-name (ui/get-class-name {"log-in-as-button"        true
                                                  "log-in-as-button-active" (= sign-in-as-type "admin")})}
       "Admin"]]
     [ui/logo-with-name {:class-name "sign-in-logo"}]
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

      [:div.forgot-password
       [:p "Forgot password?"]
       [:div.forgot-password-link
        [ui/link {:href "/accounts/reset-password"
                  :target "_self"} "Reset password"]]]
      [ui/button {:on-click   submit
                  :class-name "login-button"
                  :shape      "rounded"
                  :loading?   loading?
                  :disabled?  loading?}
       "Log In"]]]))
