(ns webchange.admin.widgets.password-reset.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.components.form.views :refer [form]]
    [webchange.admin.widgets.password-reset.state :as state]
    [webchange.validation.specs.account :as account-spec]
    [webchange.ui-framework.components.index :as ui]))

(def model {:password {:label "Password"
                       :type  :password}
            :confirm  {:label "Confirm Password"
                       :type  :password}})

(defn reset-password-form
  []
  (r/create-class
    {:display-name "Reset Password Form"

     :component-will-unmount
     (fn [this]
       (re-frame/dispatch [::state/reset (r/props this)]))

     :reagent-render
     (fn [{:keys [account-id class-name on-save on-cancel]}]
       (let [saving? @(re-frame/subscribe [::state/data-saving?])
             errors @(re-frame/subscribe [::state/custom-errors])
             handle-save #(re-frame/dispatch [::state/change-password account-id % {:on-success on-save}])]
         [:div {:class-name (ui/get-class-name {"widget--change-password" true
                                                class-name                (some? class-name)})}
          [form {:form-id   (-> (str "change-password")
                                (keyword))
                 :model     model
                 :errors    errors
                 :spec      ::account-spec/change-password
                 :on-save   handle-save
                 :on-cancel on-cancel
                 :saving?   saving?}]]))}))
