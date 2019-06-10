(ns webchange.auth.views-teacher-login
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.auth.events :as auth.events]
    [webchange.config :as config]
    [webchange.routes :as routes]
    [webchange.ui.components :as wui]
    [webchange.ui.theme :refer [with-mui-theme]]))

(def t {:name            {:first "Popup"
                          :last  "School"}
        :form            {:username "Username"
                          :password "Password"
                          :sign-in  "Sign in"
                          :sign-up  "Sign up"}
        :remember-me     "Remember me"
        :forgot-password "Forgot Password?"
        :welcome         {:first "Welcome to"
                          :last  (str (get-in t [:name :first]) " "
                                      (get-in t [:name :last]))}})

(defn translate
  [path]
  (get-in t path))

(def test-credentials
  {:email    "demo@example.com"
   :password "demo123"})

(defn teacher-login-page
  []
  (let [data (r/atom (if config/debug? test-credentials {}))
        change-username #(swap! data assoc :email (->> % .-target .-value))
        change-password #(swap! data assoc :password (->> % .-target .-value))
        change-remember #(swap! data assoc :remember-me (->> % .-target .-checked))]
    (fn []
      (let [form-error (get-in @(re-frame/subscribe [:errors]) [:login :form])
            sign-in #(re-frame/dispatch [::auth.events/login @data])
            sign-up #(routes/redirect-to :register-user)]
        [with-mui-theme
         [:div.teacher-login-page
          [:div.login-window
           [:div.auth-panel
            [:header
             [:div.logo]
             [:h1
              [:span (translate [:name :first])]
              [:span (translate [:name :last])]]]
            [:main
             [wui/text-field {:on-change change-username
                              :hint-text (translate [:form :username])}]
             [wui/text-field {:on-change change-password
                              :type      "password"
                              :hint-text (translate [:form :password])}]
             [:div.error
              (when form-error [:span form-error])]
             [wui/raised-button {:on-click sign-in
                                 :primary  true
                                 :label    (translate [:form :sign-in])
                                 :style    {:width        100
                                            :margin-right 10}}]
             [wui/flat-button {:on-click sign-up
                               :primary  true
                               :label    (translate [:form :sign-up])
                               :style    {:width 100}}]]
            [:footer
             [:div.remember-me
              [wui/checkbox {:on-check change-remember
                             :label    (translate [:remember-me])}]]
             [:div.forgot-password
              [:a (translate [:forgot-password])]]]]
           [:div.welcome-screen
            [:div.welcome-text
             [:h1 (translate [:welcome :first])]
             [:h2 (translate [:welcome :last])]]]]]]))))
