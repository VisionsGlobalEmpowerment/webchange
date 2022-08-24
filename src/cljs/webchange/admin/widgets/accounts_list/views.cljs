(ns webchange.admin.widgets.accounts-list.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.components.pagination.views :as p]
    [webchange.admin.widgets.accounts-list.state :as state]
    [webchange.ui.index :as ui]))

(defn- list-item
  [{:keys [active? email id last-login name loading?] :as props}]
  (let [account-type @(re-frame/subscribe [::state/account-type])
        handle-click #(re-frame/dispatch [::state/view-account id])
        handle-edit-click #(re-frame/dispatch [::state/edit-account id])
        handle-active-click #(re-frame/dispatch [::state/toggle-active id (not active?)])
        determinate? (boolean? active?)]
    [ui/list-item {:avatar   nil
                   :name     name
                   :info     [{:key   "Email"
                               :value email}
                              {:key   "Last Login"
                               :value last-login}]
                   :on-click handle-click
                   :controls (when (= "admin" account-type)
                               [ui/switch {:label          (cond
                                                             loading? "Saving.."
                                                             (not determinate?) "..."
                                                             active? "Active"
                                                             :else "Inactive")

                                           :checked?       active?
                                           :indeterminate? (not determinate?)
                                           :disabled?      loading?
                                           :on-change      handle-active-click
                                           :class-name     "active-switch"}])
                   :actions  [{:icon     "edit"
                               :title    "Edit account"
                               :on-click handle-edit-click}]}]))


(defn- pagination
  []
  (let [{:keys [current total]} @(re-frame/subscribe [::state/pagination])
        handle-click #(re-frame/dispatch [::state/load-accounts-page %])]
    (when (and (number? total)
               (> total 1))
      [p/pagination {:current  current
                     :total    total
                     :on-click handle-click}])))

(defn accounts-list
  [props]
  (re-frame/dispatch [::state/init props])
  (fn [{:keys [account-type]}]
    (re-frame/dispatch [::state/set-account-type account-type])
    (let [data @(re-frame/subscribe [::state/accounts])
          loading? @(re-frame/subscribe [::state/loading?])]
      [:div.widget--accounts-list
       (if-not loading?
         [:<>
          [ui/list {:class-name "accounts-list"}
           (for [{:keys [id] :as account} data]
             ^{:key id}
             [list-item account])]
          [pagination]]
         [ui/loading-overlay])])))
