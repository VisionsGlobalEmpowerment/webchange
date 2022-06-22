(ns webchange.admin.widgets.account-form.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.widgets.account-form.state :as state]
    [webchange.validation.specs.account :as account-spec]
    [webchange.ui-framework.components.index :as c]
    [webchange.ui.index :as ui]))

(def type-options (->> account-spec/type?
                       (map #(hash-map :value %
                                       :text (clojure.string/capitalize %)))
                       (sort-by :text)
                       (concat [{:text  "Select Account Type"
                                 :value ""}])))

(def account-model {:first-name {:label "First Name"
                                 :type  :text}
                    :last-name  {:label "Last Name"
                                 :type  :text}
                    :email      {:label "Email"
                                 :type  :text}
                    :password   {:label      "Password"
                                 :type       :text
                                 :input-type "password"}
                    :type       {:label   "Type"
                                 :type    :select
                                 :options type-options}})

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
             handle-save #(re-frame/dispatch [::state/create-account % {:on-success on-save}])]
         [:div {:class-name (c/get-class-name {"widget--account-form" true
                                               class-name             (some? class-name)})}
          [ui/form {:form-id (-> (str "add-account")
                                 (keyword))
                    :data    {:type account-type}
                    :model   account-model
                    :spec    ::account-spec/create-account
                    :on-save handle-save
                    :saving? saving?}]]))}))

(defn- account-info-item
  [{:keys [key value]}]
  [:<>
   [:dt key]
   [:dd value]])

(defn- account-info
  []
  (let [account-info @(re-frame/subscribe [::state/account-info])]
    [:dl.account-info
     (for [[key value] account-info]
       ^{:key key}
       [account-info-item {:key key :value value}])]))

(defn- account-actions
  [{:keys [account-id on-remove]}]
  (let [account-removing? @(re-frame/subscribe [::state/account-removing?])
        handle-delete-account (fn []
                                (c/confirm "Delete Account?"
                                           #(re-frame/dispatch [::state/remove-account account-id {:on-success on-remove}])))
        handle-reset-password #(re-frame/dispatch [::state/reset-password account-id])]
    [:div.account-actions
     [c/icon-button {:icon     "chevron-right"
                     :variant  "light"
                     :on-click handle-reset-password}
      "Reset Password"]
     [c/icon-button {:icon     "remove"
                     :variant  "light"
                     :loading? account-removing?
                     :on-click handle-delete-account}
      "Delete Account"]]))

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
     (fn [{:keys [account-id class-name on-save] :as props}]
       (let [saving? @(re-frame/subscribe [::state/data-saving?])
             data @(re-frame/subscribe [::state/form-data])
             handle-save #(re-frame/dispatch [::state/edit-account account-id % {:on-success on-save}])]
         [:div {:class-name (c/get-class-name {"widget--account-form" true
                                               class-name             (some? class-name)})}
          [ui/form {:form-id (-> (str "edit-account")
                                 (keyword))
                    :model   (dissoc account-model :password)
                    :data    data
                    :spec    ::account-spec/edit-account
                    :on-save handle-save
                    :saving? saving?}]
          [account-info]
          [account-actions props]]))}))
