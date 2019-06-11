(ns webchange.auth.views-teacher-login
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.auth.events :as auth.events]
    [webchange.auth.views-auth-page :refer [auth-page]]
    [webchange.config :as config]
    [webchange.routes :as routes]
    [webchange.ui.components :as wui]
    [webchange.ui.theme :refer [w-colors]]))

(defn translate
  [path]
  (get-in {:form        {:username "Username"
                         :password "Password"}
           :buttons     {:sign-in "Sign in"
                         :sign-up "Sign up"}
           :remember-me "Remember me"}
          path))

(def test-credentials
  {:email    "demo@example.com"
   :password "demo123"})

(def secondary-color (:secondary w-colors))

(defn teacher-login-page
  []
  (let [data (r/atom (if config/debug? test-credentials {}))]
    (fn []
      (let [form-error (get-in @(re-frame/subscribe [:errors]) [:login :form])]
        [auth-page
         [:div
          [wui/text-field {:hint-text (translate [:form :username])
                           :on-change #(swap! data assoc :email (->> % .-target .-value))}]
          [wui/text-field {:type      "password"
                           :hint-text (translate [:form :password])
                           :on-change #(swap! data assoc :password (->> % .-target .-value))}]
          [wui/checkbox {:label    (translate [:remember-me])
                         :on-check #(swap! data assoc :remember-me (->> % .-target .-checked))
                         :style    {:margin-top 10}}]
          [wui/raised-button {:on-click #(re-frame/dispatch [::auth.events/login @data])
                              :primary  true
                              :label    (translate [:buttons :sign-in])
                              :style    {:width        100
                                         :margin-right 10
                                         :margin-top   "20px"}}]
          [wui/flat-button {:on-click #(routes/redirect-to :register-user)
                            :primary  true
                            :label    (translate [:buttons :sign-up])
                            :style    {:width      100
                                       :margin-top "20px"}}]
          [:div {:style {:color      secondary-color
                         :height     "30px"
                         :margin-top 20}}
           (when form-error [:span form-error])]]]))))
