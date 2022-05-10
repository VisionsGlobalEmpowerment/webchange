(ns webchange.admin.components.form.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.components.form.state :as state]
    [webchange.ui-framework.components.index :as c]))

(defn- text-control
  [{:keys [disabled? id form-id label]}]
  (let [value @(re-frame/subscribe [::state/field-value form-id id])
        error @(re-frame/subscribe [::state/field-error form-id id])
        handle-change #(re-frame/dispatch [::state/set-field-value form-id id %])]
    [:<>
     [c/label {:for id} label]
     [c/input {:id        id
               :value     value
               :error     error
               :disabled? disabled?
               :on-change handle-change}]]))

(defn- select-control
  [{:keys [disabled? id form-id label options options-type]}]
  (let [value @(re-frame/subscribe [::state/field-value form-id id])
        error @(re-frame/subscribe [::state/field-error form-id id])
        handle-change #(re-frame/dispatch [::state/set-field-value form-id id %])]
    [:<>
     [c/label {:for id} label]
     [c/select {:id        id
                :value     value
                :options   options
                :error     error
                :disabled? disabled?
                :on-change handle-change
                :type      options-type}]]))

(defn- form-control
  [{:keys [disabled? id form-id options]}]
  (let [{:keys [type]} options
        control-props (merge {:id        id
                              :form-id   form-id
                              :disabled? disabled?}
                             options)]
    (case type
      :text [text-control control-props]
      :select [select-control control-props])))

(defn- loading-indicator
  []
  [:div.data-loading-indicator
   [c/circular-progress]])

(defn- submit
  [{:keys [form-id disabled? on-success saving? spec]}]
  (let [handle-click #(re-frame/dispatch [::state/save form-id spec on-success])]
    [c/button {:on-click   handle-click
               :disabled?  (or disabled? saving?)
               :class-name "submit"}
     (if-not saving?
       "Save"
       [c/circular-progress])]))

(defn form
  [{:keys [form-id on-save spec] :as props}]
  (re-frame/dispatch [::state/init props])
  (fn [{:keys [data disabled? loading? model saving?]
        :or   {disabled? false
               loading?  false
               saving?   false}}]
    (re-frame/dispatch [::state/set-form-data form-id data model])
    [:div.component--form
     (if-not loading?
       [:div.controls
        (for [[field-name field-options] model]
          ^{:key field-name}
          [form-control {:id        field-name
                         :form-id   form-id
                         :options   field-options
                         :disabled? disabled?}])]
       [loading-indicator])
     (when-not disabled?
       [submit {:form-id    form-id
                :disabled?  loading?
                :saving?    saving?
                :spec       spec
                :on-success on-save}])]))
