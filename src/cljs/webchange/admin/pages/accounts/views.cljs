(ns webchange.admin.pages.accounts.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.accounts.state :as state]
    [webchange.admin.widgets.accounts-list.views :refer [accounts-list]]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui-framework.components.index :as ui]))

(defn- header
  []
  (let [header @(re-frame/subscribe [::state/header])
        {:keys [title] :as add-button} @(re-frame/subscribe [::state/add-button])
        handle-add-click #(re-frame/dispatch [::state/add-account])]
    [page/header (cond-> {:title header
                          :icon  "user"}
                         (some? add-button)
                         (assoc :actions [ui/icon-button {:icon     "add"
                                                          :title    title
                                                          :on-click handle-add-click}
                                          title]))]))

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
    [page/page {:class-name "page--accounts-admins"}
     [header]
     [content]]))
