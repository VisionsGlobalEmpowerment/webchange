(ns webchange.admin.widgets.account-form.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.components.form.views :refer [form]]
    [webchange.admin.widgets.account-form.state :as state]
    [webchange.validation.specs.account :as account-spec]
    [webchange.ui-framework.components.index :as ui]))

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
         [:div {:class-name (ui/get-class-name {"widget--account-form" true
                                                class-name             (some? class-name)})}
          [form {:form-id (-> (str "add-account")
                              (keyword))
                 :data    {:type account-type}
                 :model   account-model
                 :spec    ::account-spec/create-account
                 :on-save handle-save
                 :saving? saving?}]]))}))

(defn edit-account-form
  []
  (r/create-class
    {:display-name "Edit Account Form"

     :component-did-mount
     (fn [this]
       (re-frame/dispatch [::state/init-add-form (r/props this)]))

     :component-will-unmount
     (fn [this]
       (re-frame/dispatch [::state/reset-form (r/props this)]))

     :reagent-render
     (fn [{:keys [account-type class-name on-save]}]
       (let [saving? @(re-frame/subscribe [::state/data-saving?])
             handle-save #(re-frame/dispatch [::state/edit-account % {:on-success on-save}])]
         [:div {:class-name (ui/get-class-name {"widget--account-form" true
                                                class-name             (some? class-name)})}
          [form {:form-id (-> (str "edit-account")
                              (keyword))
                 :data    {:type account-type}
                 :model   account-model
                 :spec    ::account-spec/edit-account
                 :on-save handle-save
                 :saving? saving?}]]))}))
