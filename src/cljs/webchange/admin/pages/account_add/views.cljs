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
          handle-save #(re-frame/dispatch [::state/open-accounts-list])]
      [page/page {:class-name "page--account-add"}
       [page/_header {:title title
                     :icon  "user"}]
       [page/main-content
        [add-account-form {:account-type account-type
                           :class-name   "add-account-form"
                           :on-save      handle-save}]]])))
