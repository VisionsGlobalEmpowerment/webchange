(ns webchange.admin.pages.accounts.views
  (:require [re-frame.core :as re-frame]
            [webchange.admin.components.list.views :as l]
            [webchange.admin.pages.accounts.state :as state]
            [webchange.admin.widgets.page.views :as page]
            [webchange.ui-framework.components.index :as c]))

(defn- header
  []
  (let [handle-add-click #(re-frame/dispatch [::state/add-admin])]
    [page/header {:title   "Admins"
                  :icon    "school"
                  :actions [c/icon-button {:icon       "add"
                                           :title      "Add Admin Account"
                                           :on-click   handle-add-click}
                            "Add Admin Account"]}]))

(defn- toggle-active-button
  [{:keys [id active]}]
  (let [handle-click #(do (.stopPropagation %)
                          (re-frame/dispatch [::state/toggle-active id (not active)]))]
    [c/icon-button {:icon       "edit"
                    :title      "Toggle Active"
                    :variant    "light"
                    :on-click   handle-click}
     (if active "Active" "Inactive")]))

(defn account-item
  [{:keys [id] :as props}]
  (let [handle-item-click #(re-frame/dispatch [::state/edit-account id])]
    [l/list-item (merge props
                        {:on-click handle-item-click})
     [l/content-right {:class-name "item-content-right"}
      [:p (str "email: " (:email props))]
      [:p (str "created: " (:created-at props))]
      [:p (str "last login:" (:last-login props))]
      [:p (str "type:" (:type props))]
      [toggle-active-button {:id id :active (:active props)}]]]))

(defn- content
  []
  (let [accounts @(re-frame/subscribe [::state/accounts-list])]
    [page/main-content
     [l/list {:class-name "accounts-list"}
      (for [account accounts]
        ^{:key (:id account)}
        [account-item account])]]))

(defn page
  []
  (re-frame/dispatch [::state/init])
  (fn []
    [page/page {:class-name "page--accounts-admins"}
     [header]
     [content]]))
