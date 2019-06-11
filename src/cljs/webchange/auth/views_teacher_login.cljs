(ns webchange.auth.views-teacher-login
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.auth.events :as auth.events]
    [webchange.auth.views-auth-page :refer [auth-page]]
    [webchange.config :as config]
    [webchange.routes :as routes]
    [webchange.ui.components :as wui]
    [webchange.ui.theme :refer [with-mui-theme]]))

(def t {:form            {:username "Username"
                          :password "Password"
                          :sign-in  "Sign in"
                          :sign-up  "Sign up"}
        :remember-me     "Remember me"})

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
        [auth-page
         [:div
          [wui/text-field {:on-change change-username
                               :hint-text (translate [:form :username])}]
          [wui/text-field {:on-change change-password
                           :type      "password"
                           :hint-text (translate [:form :password])}]
          [wui/checkbox {:on-check change-remember
                         :label    (translate [:remember-me])
                         :style    {:margin-top 10}}]
          [wui/raised-button {:on-click sign-in
                              :primary  true
                              :label    (translate [:form :sign-in])
                              :style    {:width        100
                                         :margin-right 10
                                         :margin-top   "20px"}}]
          [wui/flat-button {:on-click sign-up
                            :primary  true
                            :label    (translate [:form :sign-up])
                            :style    {:width      100
                                       :margin-top "20px"}}]
          [:div {:style {:color      "#fd4142"
                         :height     "30px"
                         :margin-top 20}}
           (when form-error [:span form-error])]]]))))
