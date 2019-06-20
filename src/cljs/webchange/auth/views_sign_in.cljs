(ns webchange.auth.views-sign-in
  (:require
    [cljsjs.material-ui]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.auth.events :as auth.events]
    [webchange.config :as config]
    [webchange.routes :as routes]
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

(defn sign-in-form
  []
  (let [data (r/atom (if config/debug? test-credentials {}))]
    (fn []
      (let [form-error (get-in @(re-frame/subscribe [:errors]) [:login :form])]
        [:div
         [ui/text-field
          {:label     (translate [:form :username])
           :on-change #(swap! data assoc :email (->> % .-target .-value))}]

         [ui/text-field
          {:type      "password"
           :label     (translate [:form :password])
           :on-change #(swap! data assoc :password (->> % .-target .-value))}]

         [ui/form-control-label
          {:label   (translate [:remember-me])
           :control (r/as-element [ui/checkbox {:on-change #(swap! data assoc :remember-me (->> % .-target .-checked))}])
           :style   {:margin-top 10
                     :width      "100%"}}]

         [ui/button
          {:on-click #(re-frame/dispatch [::auth.events/login @data])
           :color    "primary"
           :variant  "contained"
           :style    {:width        100
                      :margin-right 10
                      :margin-top   "20px"}}
          (translate [:buttons :sign-in])]

         [ui/button
          {:on-click #(routes/redirect-to :register-user)

           :style    {:width      100
                      :margin-top "20px"}}
          (translate [:buttons :sign-up])]

         [:div
          {:style {:color      secondary-color
                   :height     "30px"
                   :margin-top 20}}
          (when form-error [:span form-error])]]))))
