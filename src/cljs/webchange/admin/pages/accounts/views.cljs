(ns webchange.admin.pages.accounts.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.accounts.state :as state]
    [webchange.admin.widgets.accounts-list.views :refer [accounts-list]]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui-framework.components.index :as ui]))

(defn- content
  []
  (let [account-type @(re-frame/subscribe [::state/account-type])
        valid-type? @(re-frame/subscribe [::state/valid-account-type?])]
    [page/main-content
     (when valid-type?
       [accounts-list {:account-type account-type}])]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn [{:keys [account-type]}]
    (re-frame/dispatch [::state/set-account-type account-type])
    (let [header @(re-frame/subscribe [::state/header])
          handle-add-click #(re-frame/dispatch [::state/add-account])]
      [page/single-page {:class-name "page--accounts-admins"
                         :header     {:title   header
                                      :icon    "accounts"
                                      :actions (when (= "admin" account-type)
                                                 [{:text     "Add New Admin"
                                                   :icon     "account-add"
                                                   :on-click handle-add-click}])}}
       [content]])))
