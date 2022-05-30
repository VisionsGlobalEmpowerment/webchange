(ns webchange.admin.pages.account-my.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.account-my.state :as state]
    [webchange.admin.widgets.account-form.views :refer [edit-account-form]]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui-framework.components.index :as ui]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    (let [{:keys [id]} @(re-frame/subscribe [::state/account-data])
          account-loading? @(re-frame/subscribe [::state/account-loading?])
          handle-save #(re-frame/dispatch [::state/open-accounts-list])]
      [page/page {:class-name "page--my-account"}
       [page/header {:title "My Account"
                     :icon  "user"}]
       [page/main-content {:id "page--my-account--content"}
        (cond
          account-loading? [ui/loading-overlay]
          (some? id) [edit-account-form {:account-id id
                                         :class-name "my-account-form"
                                         :on-save    handle-save}]
          :default nil)]])))
