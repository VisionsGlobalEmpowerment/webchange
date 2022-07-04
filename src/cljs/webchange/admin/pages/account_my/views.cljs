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
    (let [account @(re-frame/subscribe [::state/account-info])
          account-loading? @(re-frame/subscribe [::state/account-loading?])
          handle-save #(re-frame/dispatch [::state/open-accounts-list])]
      [page/single-page {:class-name      "page--my-account"
                         :header          {:title      (:name account)
                                           :icon       "accounts"
                                           :icon-color "green-2"
                                           :info       [{:key   "Account Created"
                                                         :value (:account-created account)}
                                                        {:key   "Last Login"
                                                         :value (:last-login account)}]}
                         :form-container? true}
       [page/main-content {:id "page--my-account--content"}
        (cond
          account-loading? [ui/loading-overlay]
          (some? (:id account)) [edit-account-form {:account-id (:id account)
                                                    :class-name "my-account-form"
                                                    :on-save    handle-save}]
          :else nil)]])))
