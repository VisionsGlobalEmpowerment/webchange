(ns webchange.login.sign-in.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.login.sign-in.state :as state]
    [webchange.ui.index :as ui]))

(defn sign-in-form
  []
  (let [username @(re-frame/subscribe [::state/username])
        password @(re-frame/subscribe [::state/password])
        loading? @(re-frame/subscribe [::state/loading?])

        handle-username-changed #(re-frame/dispatch [::state/set-username %])
        handle-password-changed #(re-frame/dispatch [::state/set-password %])

        submit #(re-frame/dispatch [::state/login])]
    [:div.sign-in-form
     [:h1 "Log in"]
     [:div.form
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
                    :on-enter-press submit}]
      [ui/button {:on-click   submit
                  :class-name "login-button"
                  :shape      "rounded"
                  :loading?   loading?
                  :disabled?  loading?}
       "Log In"]]
     ]))
