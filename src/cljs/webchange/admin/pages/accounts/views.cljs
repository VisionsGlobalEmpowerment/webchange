(ns webchange.admin.pages.accounts.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.accounts.state :as state]
    [webchange.admin.widgets.accounts-list.views :refer [accounts-list]]
    [webchange.admin.widgets.accounts-list.state :as accounts-list-state]
    [webchange.admin.widgets.page.views :as page]
    [webchange.admin.widgets.search.views :refer [search]]
    [webchange.ui.index :as ui]))

(defn- search-bar
  []
  (let [value @(re-frame/subscribe [::accounts-list-state/search-string])
        handle-change #(re-frame/dispatch [::accounts-list-state/set-search-string %])]
    [search {:value      value
             :on-change  handle-change
             :class-name "activities-search-input"}]))

(defn- active-switcher
  []
  (let [show-only-active? @(re-frame/subscribe [::accounts-list-state/show-only-active?])
        handle-change #(re-frame/dispatch [::accounts-list-state/set-show-only-active (not show-only-active?)])]
    [ui/switch {:label      "Only Active"
                :checked?   show-only-active?
                :on-change  handle-change
                :color      "yellow-1"
                :class-name "show-global-selector"}]))

(defn- content
  []
  (let [account-type @(re-frame/subscribe [::state/account-type])
        valid-type? @(re-frame/subscribe [::state/valid-account-type?])]
    (when valid-type?
      [accounts-list {:account-type account-type}])))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn [{:keys [account-type]}]
    (re-frame/dispatch [::state/set-account-type account-type])
    (let [header @(re-frame/subscribe [::state/header])
          handle-add-click #(re-frame/dispatch [::state/add-account])]
      [page/single-page {:class-name "page--accounts-admins"
                         :search     [search-bar]
                         :header     {:title      header
                                      :icon       "accounts"
                                      :icon-color "green-2"
                                      :controls [[active-switcher]]
                                      :actions    (when (= "admin" account-type)
                                                    [{:text     "Add New Admin"
                                                      :icon     "account-add"
                                                      :on-click handle-add-click}])}}
       [content]])))
