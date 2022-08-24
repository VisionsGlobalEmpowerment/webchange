(ns webchange.admin.widgets.account-form.views
  (:require
    [clojure.string :as str]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.widgets.account-form.state :as state]
    [webchange.ui-framework.components.index :as c]
    [webchange.ui.index :as ui]
    [webchange.validation.specs.account :as account-spec]))

(def type-options (->> account-spec/type?
                       (map #(hash-map :value %
                                       :text (str/capitalize %)))
                       (sort-by :text)
                       (concat [{:text  "Select Account Type"
                                 :value ""}])))

(def add-account-model {:first-name       {:label "First Name"
                                           :type  :text}
                        :last-name        {:label "Last Name"
                                           :type  :text}
                        :email            {:label "Email"
                                           :type  :text}
                        :password         {:label "Password"
                                           :type  :password}
                        :password-confirm {:label "Confirm password"
                                           :type  :password}
                        :type             {:label   "Type"
                                           :type    :select
                                           :options type-options}})

(defn filler
  []
  [:div.account-from-actions-filler])

(def edit-account-model (-> add-account-model
                            (dissoc :password :password-confirm)
                            (merge {:filler {:type    :custom
                                             :control filler}
                                    :reset  {:label    "Reset password"
                                             :type     :action
                                             :icon     "caret-right"
                                             :on-click #(re-frame/dispatch [::state/reset-password])}
                                    :remove {:label    "Delete account"
                                             :type     :action
                                             :icon     "trash"
                                             :on-click #(re-frame/dispatch [::state/open-remove-window])}})))

(defn add-account-form
  []
  (r/create-class
    {:display-name "Add Account Form"

     :component-did-mount
     (fn [this]
       (re-frame/dispatch [::state/init-add-form (r/props this)]))

     :component-will-unmount
     (fn [this]
       (re-frame/dispatch [::state/reset-form (r/props this)]))

     :reagent-render
     (fn [{:keys [account-type class-name on-save]}]
       (let [saving? @(re-frame/subscribe [::state/data-saving?])
             errors @(re-frame/subscribe [::state/custom-errors])
             handle-save #(re-frame/dispatch [::state/create-account % {:on-success on-save}])]
         [:div {:class-name (c/get-class-name {"widget--account-form" true
                                               class-name             (some? class-name)})}
          [:h3.account-details-header "Account Details"]
          [ui/form {:form-id (-> (str "add-account")
                                 (keyword))
                    :data    {:type account-type}
                    :model   add-account-model
                    :errors  errors
                    :spec    ::account-spec/create-account
                    :on-save handle-save
                    :saving? saving?}]]))}))

(defn- remove-window
  [{:keys [account-id on-remove]}]
  (let [{:keys [done? open? in-progress?]} @(re-frame/subscribe [::state/remove-window-state])
        remove #(re-frame/dispatch [::state/remove-account account-id])
        close-window #(re-frame/dispatch [::state/close-remove-window])
        confirm-removed #(re-frame/dispatch [::state/handle-removed])]
    [ui/confirm {:open?        open?
                 :loading?     in-progress?
                 :confirm-text (if done? "Ok" "Yes")
                 :on-confirm   (if done? confirm-removed remove)
                 :on-cancel    (when-not done? close-window)}
     (if done?
       "Account successfully deleted"
       "Are you sure you want to delete account?")]))

(defn edit-account-form
  []
  (r/create-class
    {:display-name "Edit Account Form"

     :component-did-mount
     (fn [this]
       (re-frame/dispatch [::state/init-edit-form (r/props this)]))

     :component-will-unmount
     (fn [this]
       (re-frame/dispatch [::state/reset-form (r/props this)]))

     :reagent-render
     (fn [{:keys [account-id class-name disabled? on-save]}]
       (let [saving? @(re-frame/subscribe [::state/data-saving?])
             data @(re-frame/subscribe [::state/form-data])
             handle-save #(re-frame/dispatch [::state/edit-account account-id % {:on-success on-save}])]
         [:div {:class-name (c/get-class-name {"widget--account-form" true
                                               class-name             (some? class-name)})}
          [:h3.account-details-header "Account Settings"]
          [ui/form {:form-id   (-> (str "edit-account")
                                   (keyword))
                    :model     edit-account-model
                    :data      data
                    :spec      ::account-spec/edit-account
                    :on-save   handle-save
                    :disabled? disabled?
                    :saving?   saving?}]
          [remove-window {:account-id account-id}]]))}))
