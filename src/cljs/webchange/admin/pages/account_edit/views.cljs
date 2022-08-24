(ns webchange.admin.pages.account-edit.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.account-edit.state :as state]
    [webchange.admin.widgets.account-form.views :refer [edit-account-form]]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui.index :as ui]))

(defn- child-card
  [{:keys [id name created-at device course-name]}]
  (let [removing? @(re-frame/subscribe [::state/child-removing? id])
        handle-remove-click #(re-frame/dispatch [::state/remove-child id])]
    [:article.child-card
     [:h1 name]
     [ui/button {:icon       "trash"
                 :color      "blue-1"
                 :title      "Delete Child"
                 :class-name "remove-button"
                 :loading?   removing?
                 :on-click   handle-remove-click}]
     [:p (str "Account Created: " created-at)]
     [:dl
      [:dt "Course"]
      [:dd course-name]
      [:dt "Device"]
      [:dd device]]]))

(defn- children-list
  []
  (let [children @(re-frame/subscribe [::state/children])
        loading? @(re-frame/subscribe [::state/account-loading?])]
    [:div.children-list
     [:h3.students-header "Students"]
     [:div.students-filler]
     (if loading?
       [ui/loading-overlay]
       (for [{:keys [id] :as child} children]
         ^{:key id}
         [child-card child]))]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn [{:keys [account-id]}]
    (let [editable? @(re-frame/subscribe [::state/form-editable?])
          account @(re-frame/subscribe [::state/account-info])
          open-accounts-list #(re-frame/dispatch [::state/open-accounts-list])]
      [page/single-page {:class-name         "page--account-edit"
                         :class-name-content "page--account-edit--content"
                         :header             {:title      (get account :name "")
                                              :icon       "accounts"
                                              :icon-color "green-2"
                                              :on-close   open-accounts-list
                                              :info       [{:key   "Account Created"
                                                            :value (get account :account-created "")}
                                                           {:key   "Last Login"
                                                            :value (get account :last-login "")}]}
                         :form-container?    true}
       [:div.left-side-panel
        [edit-account-form {:account-id account-id
                            :class-name "edit-account-form"
                            :disabled?  (not editable?)
                            :on-save    open-accounts-list
                            :on-remove  open-accounts-list}]]
       (when editable?
         [children-list])])))
