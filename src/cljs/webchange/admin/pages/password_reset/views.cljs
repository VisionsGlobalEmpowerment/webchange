(ns webchange.admin.pages.password-reset.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.password-reset.state :as state]
    [webchange.admin.widgets.page.views :as page]
    [webchange.admin.widgets.password-reset.views :refer [reset-password-form]]))

(defn page
  []
  (fn [{:keys [account-id]}]
    (let [handle-save #(re-frame/dispatch [::state/open-accounts-list :save])
          handle-cancel #(re-frame/dispatch [::state/open-accounts-list :cancel])]
      [page/page {:class-name "page--password-reset"}
       [page/header {:title "Reset Password"
                     :icon  "user"}]
       [page/main-content
        [reset-password-form {:account-id account-id
                              :on-save    handle-save
                              :on-cancel  handle-cancel}]]])))
