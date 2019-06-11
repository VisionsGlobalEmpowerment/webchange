(ns webchange.auth.views-registration
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.auth.events :as auth.events]
    [webchange.auth.views-auth-page :refer [auth-page]]
    [webchange.routes :as routes]
    [webchange.ui.components :as wui]
    [webchange.ui.theme :refer [w-colors]]))

(defn translate
  [path]
  (get-in {:form    {:first-name       "First Name"
                     :last-name        "Last Name"
                     :email            "E-mail address"
                     :password         "Password"
                     :confirm-password "Confirm Password"}
           :buttons {:sign-in "Sign in"
                     :sign-up "Sign up"}}
          path))

(def secondary-color (:secondary w-colors))

(defn registration-page
  []
  (let [data (r/atom {})]
    (fn []
      (let [form-error (get-in @(re-frame/subscribe [:errors]) [:login :form])]
        [auth-page
         [:div
          [wui/text-field {:hint-text (translate [:form :first-name])
                           :on-change #(swap! data assoc :first-name (->> % .-target .-value))}]
          [wui/text-field {:hint-text (translate [:form :last-name])
                           :on-change #(swap! data assoc :last-name (->> % .-target .-value))}]
          [wui/text-field {:hint-text (translate [:form :email])
                           :on-change #(swap! data assoc :email (->> % .-target .-value))}]
          [wui/text-field {:type      "password"
                           :hint-text (translate [:form :password])
                           :on-change #(swap! data assoc :password (->> % .-target .-value))}]
          [wui/text-field {:type      "password"
                           :hint-text (translate [:form :confirm-password])
                           :on-change #(swap! data assoc :confirm-password (->> % .-target .-value))}]
          [wui/raised-button {:on-click #(re-frame/dispatch [::auth.events/register-user @data])
                              :primary  true
                              :label    (translate [:buttons :sign-up])
                              :style    {:width        100
                                         :margin-right 10
                                         :margin-top   "20px"}}]
          [wui/flat-button {:on-click #(routes/redirect-to :login)
                            :primary  true
                            :label    (translate [:buttons :sign-in])
                            :style    {:width      100
                                       :margin-top "20px"}}]
          [:div {:style {:color      "#fd4142"
                         :height     "30px"
                         :margin-top 20}}
           (when form-error [:span form-error])]]]))))
