(ns webchange.admin.pages.user-accounts.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.pages.user-accounts.state :as state]
    [webchange.admin.components.list.views :as l]
    [webchange.admin.components.pagination.views :refer [pagination]]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui-framework.components.index :as ui]))

(defn- list-item
  [{:keys [created email id last-login name]}]
  (r/with-let [checked? (r/atom true)
               handle-edit-click #(re-frame/dispatch [::state/edit-user id])
               handle-switch-click #(swap! checked? not)]
    [l/list-item {:avatar      nil
                  :name        name
                  :description [["Email" email]
                                ["Account Created" created]
                                ["Last Login" last-login]]
                  :actions     [:<>
                                [ui/switcher {:checked?  @checked?
                                              :label     (if @checked? "Active" "Inactive")
                                              :on-change #(swap! checked? handle-switch-click)}]
                                [ui/icon-button {:icon     "edit"
                                                 :title    "Edit"
                                                 :variant  "light"
                                                 :on-click handle-edit-click}]]}]))

(defn- loading-indicator
  []
  [:div.loading-indicator
   [ui/circular-progress]])

(defn- content
  []
  (let [data @(re-frame/subscribe [::state/users])
        loading? @(re-frame/subscribe [::state/loading?])]
    [page/main-content {:class-name "users-content"}
     (if-not loading?
       [:<>
        [l/list {:class-name "users-list"}
         (for [{:keys [id] :as user} data]
           ^{:key id}
           [list-item user])]
        [pagination {:total     100
                     :per-page  30
                     :on-change #(print "Page" %)}]]
       [loading-indicator])]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    [page/page {:class-name "page--user-accounts"}
     [page/header {:title "Live Users"
                   :icon  "users"}]
     [content]]))
