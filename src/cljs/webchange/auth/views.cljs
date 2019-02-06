(ns webchange.auth.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.routes :as routes]
    [webchange.events :as events]
    [webchange.subs :as subs]
    [webchange.auth.events :as auth.events]
    [sodium.core :as na]
    [soda-ash.core :as sa]
    [reagent.core :as r]))

(defn login-form []
  (r/with-let [data (r/atom {})]
              (let [loading  @(re-frame/subscribe [:loading])
                    errors   @(re-frame/subscribe [:errors])]
              [:div {:class-name "login-form"}
               [na/grid {:text-align "center" :style {:height "100%"} :vertical-align "middle"}
                [na/grid-column {:style {:max-width 450}}
                 [na/header {:as "h2" :color "teal" :text-align "center" :content "Log-in to your account"}]
                 [na/form {:size "large" :loading? (when (:login loading)) :error? (when (:login errors))}
                  [na/segment {:stacked? true}
                   [na/form-input {:fluid? true :icon "user" :icon-position "left" :placeholder "E-mail address"
                                   :on-change #(swap! data assoc :email (-> %2 .-value))}]
                   [na/form-input {:fluid? true :icon "lock" :icon-position "left" :placeholder "Password" :type "password"
                                   :on-change #(swap! data assoc :password (-> %2 .-value))}]

                   (when-let [form-error (get-in errors [:login :form])]
                     [sa/Message {:error true :visible true :header "Error" :content form-error}])

                   [na/button {:color "teal" :fluid? true :size "large" :content "Login"
                               :on-click #(re-frame/dispatch [::auth.events/login @data])}]
                   ]]
                 [sa/Message {} "New to us? " [:a {:href (routes/url-for :register-user)} "Sign-up"]]]]])))

(defn register-form []
  (r/with-let [data (r/atom {})
               loading  @(re-frame/subscribe [:loading])
               errors   @(re-frame/subscribe [:errors])]
              [:div {:class-name "register-user-form"}
               [na/grid {:text-align "center" :style {:height "100%"} :vertical-align "middle"}
                [na/grid-column {:style {:max-width 450}}
                 [na/header {:as "h2" :color "teal" :text-align "center" :content "Register new account"}]
                 [na/form {:size "large" :loading? (when (:register-user loading)) :error? (when (:register-user errors))}
                  [na/segment {:stacked? true}
                   [na/form-input {:fluid? true :placeholder "First Name"
                                   :on-change #(swap! data assoc :first-name (-> %2 .-value))}]
                   [na/form-input {:fluid? true :placeholder "Last Name"
                                   :on-change #(swap! data assoc :last-name (-> %2 .-value))}]
                   [na/form-input {:fluid? true :placeholder "E-mail address"
                                   :on-change #(swap! data assoc :email (-> %2 .-value))}]
                   [na/form-input {:fluid? true :placeholder "Password" :type "password"
                                   :on-change #(swap! data assoc :password (-> %2 .-value))}]
                   [na/form-input {:fluid? true :placeholder "Confirm Password" :type "password"
                                   :on-change #(swap! data assoc :confirm-password (-> %2 .-value))}]

                   (when-let [form-error (get-in errors [:register-user :form])]
                     [sa/Message {:error true :visible true :header "Error" :content form-error}])
                   [na/button {:color "teal" :fluid? true :size "large" :content "Register"
                               :on-click #(re-frame/dispatch [::auth.events/register-user @data])}]
                   ]]
                 [sa/Message {} "Already have an account? "  [:a {:href (routes/url-for :login)} "Log in"]]]]]))