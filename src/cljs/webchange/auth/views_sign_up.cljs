(ns webchange.auth.views-sign-up
  (:require
    [cljsjs.material-ui]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.auth.events :as auth.events]
    [webchange.routes :as routes]
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

(defn sign-up-form
  []
  (let [data (r/atom {})]
    (fn []
      (let [form-error (get-in @(re-frame/subscribe [:errors]) [:login :form])]
        [:div
         [ui/text-field {:label     (translate [:form :first-name])
                         :on-change #(swap! data assoc :first-name (->> % .-target .-value))}]
         [ui/text-field {:label     (translate [:form :last-name])
                         :on-change #(swap! data assoc :last-name (->> % .-target .-value))}]
         [ui/text-field {:label     (translate [:form :email])
                         :on-change #(swap! data assoc :email (->> % .-target .-value))}]
         [ui/text-field {:type      "password"
                         :label     (translate [:form :password])
                         :on-change #(swap! data assoc :password (->> % .-target .-value))}]
         [ui/text-field {:type      "password"
                         :label     (translate [:form :confirm-password])
                         :on-change #(swap! data assoc :confirm-password (->> % .-target .-value))}]
         [ui/button {:on-click #(re-frame/dispatch [::auth.events/register-user @data])
                     :color    "primary"
                     :variant "contained"
                     :style    {:width        100
                                :margin-right 10
                                :margin-top   "20px"}}
          (translate [:buttons :sign-up])]
         [ui/button {:on-click #(routes/redirect-to :login)
                     :style    {:width      100
                                :margin-top "20px"}}
          (translate [:buttons :sign-in])]
         [:div {:style {:color      "#fd4142"
                        :height     "30px"
                        :margin-top 20}}
          (when form-error [:span form-error])]]))))
