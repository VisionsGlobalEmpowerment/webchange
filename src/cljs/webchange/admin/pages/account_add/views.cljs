(ns webchange.admin.pages.account-add.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.account-add.state :as state]
    [webchange.admin.widgets.account-form.views :refer [add-account-form]]
    [webchange.admin.widgets.page.views :as page]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    (let [account-type @(re-frame/subscribe [::state/account-type])
          title @(re-frame/subscribe [::state/title])
          open-accounts-list #(re-frame/dispatch [::state/open-accounts-list])]
      [page/single-page {:class-name "page--account-add"
                         :header            {:title    "Add New Account"
                                             :icon     "accounts"
                                             :on-close open-accounts-list}
                         :form-container?   true}
       [add-account-form {:account-type account-type
                          :class-name   "add-account-form"
                          :on-save      open-accounts-list
                          :on-remove    open-accounts-list}]])))
