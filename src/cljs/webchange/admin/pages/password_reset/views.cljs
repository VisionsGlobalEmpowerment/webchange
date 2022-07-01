(ns webchange.admin.pages.password-reset.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.password-reset.state :as state]
    [webchange.admin.widgets.page.views :as page]
    [webchange.admin.widgets.password-reset.views :refer [reset-password-form]]))

(defn page
  []
  (fn [props]
    (re-frame/dispatch [::state/init props])
    (fn [{:keys [account-id]}]
      (let [account @(re-frame/subscribe [::state/account-info])
            handle-save #(re-frame/dispatch [::state/open-accounts-list :save])
            handle-cancel #(re-frame/dispatch [::state/open-accounts-list :cancel])]
        [page/single-page {:class-name "page--password-reset"
                           :header            {:title    (:name account)
                                               :icon     "accounts"
                                               :on-close handle-cancel
                                               :info     [{:key   "Account Created"
                                                           :value (:account-created account)}
                                                          {:key   "Last Login"
                                                           :value (:last-login account)}]}
                           :form-container?   true}
         [page/main-content {:class-name "page--password-reset--content"}
          [reset-password-form {:account-id account-id
                                :on-save    handle-save
                                :on-cancel  handle-cancel
                                :class-name "password-reset-form"}]]]))))
