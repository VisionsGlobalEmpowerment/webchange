(ns webchange.admin.pages.add-account.views
  (:require [re-frame.core :as re-frame]
            [webchange.admin.components.form.views :refer [form]]
            [webchange.admin.pages.add-account.state :as state]
            [webchange.admin.widgets.page.views :as page]
            [webchange.validation.specs.account :as account-spec]))

(def type-options
  [{:value "admin" :text "Admin"}])

(defn- account-form
  []
  (let [saving? @(re-frame/subscribe [::state/data-saving?])
        model {:first-name {:label "First Name"
                            :type :text}
               :last-name {:label "Last Name"
                           :type :text}
               :email {:label "Email"
                       :type :text}
               :password {:label "Password"
                          :type  :text
                          :input-type "password"}
               :type {:label "Type"
                      :type :select
                      :options type-options}}
        handle-save #(re-frame/dispatch [::state/create-account %])]
    [form {:data     {:type "admin"}
           :model    model
           :spec     ::account-spec/create-account
           :on-save  handle-save
           :saving?  saving?}]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn [props]
    [page/page
     [page/main-content {:title "Create Admin Account"}
      [account-form props]]]))
