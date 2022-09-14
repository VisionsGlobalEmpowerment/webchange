(ns webchange.login.registration.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.validation.specs.account :as account-spec]
    [webchange.login.registration.state :as state]
    [webchange.ui.index :as ui]))

(def register-account-model {:row-first {:type :group
                                         :class-name "row-first"
                                         :fields {:first-name       {:label "First Name"
                                                                     :type  :text}
                                                  :last-name        {:label "Last Name"
                                                                     :type  :text}}}
                             :email            {:label "Email"
                                                :type  :text}
                             :row-last {:type :group
                                        :class-name "row-last"
                                        :fields {:password         {:label "Password"
                                                                    :type  :password}
                                                 :password-confirm {:label "Confirm password"
                                                                    :type  :password}}}
                             })

(defn registration
  []
  (let [saving? @(re-frame/subscribe [::state/data-saving?])
        errors @(re-frame/subscribe [::state/custom-errors])
        handle-save #(re-frame/dispatch [::state/register %])]
    [:div.registration-form
     [:h1 "Sign Up"]
     [ui/form {:form-id (-> (str "register-account")
                            (keyword))
               :class-name "form"
               :model   register-account-model
               :data {}
               :errors  errors
               :spec    ::account-spec/register-account
               :on-save handle-save
               :saving? saving?
               :save-text "Sign Up Free"}]]))
