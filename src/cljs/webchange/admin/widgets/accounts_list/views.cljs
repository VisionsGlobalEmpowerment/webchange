(ns webchange.admin.widgets.accounts-list.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.components.list.views :as l]
    [webchange.admin.components.pagination.views :as p]
    [webchange.admin.widgets.accounts-list.state :as state]
    [webchange.ui-framework.components.index :as ui]))

(defn- active-switcher
  [{:keys [active? id loading?]
    :or   {active? true}}]
  (let [handle-switch-click #(re-frame/dispatch [::state/toggle-active id (not active?)])]
    [:div {:class-name (ui/get-class-name {"active-switcher" true
                                           "active"          active?})}
     [:span (if active? "Active" "Inactive")]
     [ui/switcher {:checked?       active?
                   :indeterminate? loading?
                   :on-change      handle-switch-click}]]))

(defn- list-item
  [{:keys [created-at email id last-login name] :as props}]
  (let [handle-edit-click #(re-frame/dispatch [::state/edit-account id])]
    [l/list-item {:avatar      nil
                  :name        name
                  :description [["Email" email]
                                ["Account Created" created-at]
                                ["Last Login" last-login]]
                  :actions     [:<>
                                [active-switcher props]
                                [ui/icon-button {:icon     "edit"
                                                 :title    "Edit"
                                                 :variant  "light"
                                                 :on-click handle-edit-click}]]}]))

(defn- loading-indicator
  []
  [:div.loading-indicator
   [ui/circular-progress]])

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
  (fn []
    (let [data @(re-frame/subscribe [::state/accounts])
          loading? @(re-frame/subscribe [::state/loading?])]
      [:div.widget--accounts-list
       (if-not loading?
         [:<>
          [l/list {:class-name "accounts-list"}
           (for [{:keys [id] :as account} data]
             ^{:key id}
             [list-item account])]
          [pagination]]
         [loading-indicator])])))
