(ns webchange.auth.views-teacher-login
  (:require
    [webchange.ui.components :as wui]
    [webchange.ui.theme :refer [with-mui-theme]]))

(def t {:name            {:first "Popup"
                          :last  "School"}
        :form            {:username "Username"
                          :password "Password"
                          :sign-in  "Sign in"}
        :remember-me     "Remember me"
        :forgot-password "Forgot Password?"
        :welcome         {:first "Welcome to"
                          :last  (str (get-in t [:name :first]) " "
                                      (get-in t [:name :last]))}})

(defn translate
  [path]
  (get-in t path))

(defn teacher-login-page
  []
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
       [:form
        [wui/text-field {:hint-text (translate [:form :username])}]
        [wui/text-field {:type      "password"
                         :hint-text (translate [:form :password])}]
        [wui/raised-button {:primary true
                            :label   (translate [:form :sign-in])
                            :style   {:width      150
                                      :margin-top 20}}]]]
      [:footer
       [:div.remember-me
        [wui/checkbox {:label (translate [:remember-me])}]]
       [:div.forgot-password
        [:a (translate [:forgot-password])]]]]
     [:div.welcome-screen
      [:div.welcome-text
       [:h1 (translate [:welcome :first])]
       [:h2 (translate [:welcome :last])]]]]]])

;(defn login-form []
;  (r/with-let [data (r/atom (if config/debug? {:email    "demo@example.com"
;                                               :password "demo123"} {}))]
;              (let [loading  @(re-frame/subscribe [:loading])
;                    errors   @(re-frame/subscribe [:errors])]
;                [:div {:class-name "login-form"}
;                 [:style "body > div, body > div > div, body > div > div > div.login-form {height: 100%;}"]
;                 [na/grid {:text-align "center" :style {:height "100%"} :vertical-align "middle"}
;                  [na/grid-column {:style {:max-width 450}}
;                   [na/header {:as "h2" :color "teal" :text-align "center" :content "Log-in to your account"}]
;                   [na/form {:size "large" :loading? (when (:login loading)) :error? (when (:login errors))}
;                    [na/segment {:stacked? true}
;                     [na/form-input {:fluid? true :icon "user" :icon-position "left" :placeholder "E-mail address"
;                                     :on-change #(swap! data assoc :email (-> %2 .-value))}]
;                     [na/form-input {:fluid? true :icon "lock" :icon-position "left" :placeholder "Password" :type "password"
;                                     :on-change #(swap! data assoc :password (-> %2 .-value))}]
;
;                     (when-let [form-error (get-in errors [:login :form])]
;                       [sa/Message {:error true :visible true :header "Error" :content form-error}])
;
;                     [na/button {:color "teal" :fluid? true :size "large" :content "Login"
;                                 :on-click #(re-frame/dispatch [::auth.events/login @data])}]
;                     ]]
;                   [sa/Message {} "New to us? " [:a {:href (routes/url-for :register-user)} "Sign-up"]]]]])))