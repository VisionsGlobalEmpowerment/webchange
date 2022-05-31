(ns webchange.admin.pages.account-edit.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.account-edit.state :as state]
    [webchange.admin.widgets.account-form.views :refer [edit-account-form]]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui-framework.components.index :as ui]))

(defn- child-card
  [{:keys [id name created-at device course-name]}]
  (let [removing? @(re-frame/subscribe [::state/child-removing? id])
        handle-remove-click #(re-frame/dispatch [::state/remove-child id])]
    [:article.child-card
     [:h1 name]
     [ui/icon-button {:icon       "remove"
                      :variant    "light"
                      :title      "Delete Child"
                      :class-name "remove-button"
                      :loading?   removing?
                      :disabled?  removing?
                      :on-click   handle-remove-click}]
     [:dl
      [:dt "Account Created"]
      [:dd created-at]
      [:dt "Course"]
      [:dd course-name]
      [:dt "Device"]
      [:dd device]]]))

(defn- loading-indicator
  []
  [:div.loading-indicator
   [ui/circular-progress]])

(defn- children-list
  []
  (let [children @(re-frame/subscribe [::state/children])
        loading? @(re-frame/subscribe [::state/account-loading?])]
    [:div.children-list
     (if loading?
       [loading-indicator]
       (for [{:keys [id] :as child} children]
         ^{:key id}
         [child-card child]))]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn [{:keys [account-id]}]
    (let [open-accounts-list #(re-frame/dispatch [::state/open-accounts-list])]
      [page/page {:class-name "page--account-edit"}
       [page/header {:title "Edit Account"
                     :icon  "user"}]
       [page/main-content {:id "page--account-edit--content"}
        [:div.left-side-panel
         [edit-account-form {:account-id account-id
                             :class-name "edit-account-form"
                             :on-save    open-accounts-list
                             :on-remove  open-accounts-list}]]
        [children-list]]])))
