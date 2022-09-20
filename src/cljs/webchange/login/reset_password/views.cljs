(ns webchange.login.reset-password.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.validation.specs.account :as account-spec]
    [webchange.login.reset-password.state :as state]
    [webchange.ui.index :as ui]))

(defn provide-email-form
  []
  (let [loading? @(re-frame/subscribe [::state/data-saving?])

        submitted? @(re-frame/subscribe [::state/email-submitted?])
        
        handle-email-changed #(re-frame/dispatch [::state/set-email %])
        submit #(re-frame/dispatch [::state/submit-email])]
    (if submitted?
      [:div.reset-password-form
       [:h1 "Please check your email"]]
      [:div.reset-password-form
       [:h1 "Provide Your Email"]
       [:div.form
        [ui/input {:label          "Email"
                   :name           "email"
                   :on-change      handle-email-changed
                   :on-enter-press submit}]
        [ui/button {:on-click   submit
                    :class-name "submit-button"
                    :shape      "rounded"
                    :loading?   loading?
                    :disabled?  loading?}
         "Submit"]]])))

(def reset-password-model {:password         {:label "Password"
                                              :type  :password}
                           :password-confirm {:label "Confirm password"
                                              :type  :password}})

(defn reset-password-form
  [props]
  (re-frame/dispatch [::state/init-reset props])
  (fn [props]
    (let [saving? @(re-frame/subscribe [::state/data-saving?])
          errors @(re-frame/subscribe [::state/custom-errors])
          handle-save #(re-frame/dispatch [::state/change-password %])]
      [:div.reset-password-form
       [:h1 "Reset Password"]
       [ui/form {:form-id :reset-password
                 :class-name "form"
                 :model   reset-password-model
                 :data    {}
                 :errors  errors
                 :spec    ::account-spec/change-password
                 :on-save handle-save
                 :saving? saving?
                 :save-text "Reset password"}]])))
