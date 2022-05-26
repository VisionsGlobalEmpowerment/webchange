(ns webchange.admin.pages.password-reset.views
  (:require
    ;[re-frame.core :as re-frame]
    ;[webchange.admin.pages.account-edit.state :as state]
    ;[webchange.admin.widgets.account-form.views :refer [edit-account-form]]
    [webchange.admin.widgets.no-data.views :refer [no-data]]
    [webchange.admin.widgets.page.views :as page]
    ;[webchange.ui-framework.components.index :as ui]
    ))

(defn page
  [props]
  ;(re-frame/dispatch [::state/init props])
  (fn [{:keys [account-id] :as props}]
    (let [
          ;handle-save #(re-frame/dispatch [::state/open-accounts-list])
          ]
      [page/page {:class-name "page--password-reset"}
       [page/header {:title "Reset Password"
                     :icon  "user"}]
       [page/main-content
        [no-data]]])))