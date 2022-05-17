(ns webchange.admin.components.form.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.components.form.state :as state]
    [webchange.ui-framework.components.index :as c]))

(defn- date-control
  [{:keys [disabled? id form-id label]}]
  (let [value @(re-frame/subscribe [::state/field-value form-id id])
        error @(re-frame/subscribe [::state/field-error form-id id])
        handle-change #(re-frame/dispatch [::state/set-field-value form-id id %])]
    [:<>
     [c/label {:for id} label]
     [c/date {:id        id
              :value     value
              :error     error
              :disabled? disabled?
              :on-change handle-change}]]))

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

(defn- text-multiline-control
  [{:keys [disabled? id form-id label]}]
  (let [value @(re-frame/subscribe [::state/field-value form-id id])
        error @(re-frame/subscribe [::state/field-error form-id id])
        handle-change #(re-frame/dispatch [::state/set-field-value form-id id %])]
    [:<>
     [c/label {:for id} label]
     [c/text-area {:id        id
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

(defn- custom-control
  [{:keys [id form-id control] :as control-props}]
  (let [value @(re-frame/subscribe [::state/field-value form-id id])
        error @(re-frame/subscribe [::state/field-error form-id id])
        handle-change #(re-frame/dispatch [::state/set-field-value form-id id %])]
    [control control-props {:value         value
                            :error         error
                            :handle-change handle-change}]))

(defn- form-control
  [{:keys [disabled? id form-id options]}]
  (let [{:keys [type]} options
        control-props (merge {:id        id
                              :form-id   form-id
                              :disabled? disabled?}
                             options)]
    (case type
      :date [date-control control-props]
      :text [text-control control-props]
      :text-multiline [text-multiline-control control-props]
      :select [select-control control-props]
      :custom [custom-control control-props])))

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
  []
  (r/create-class
    {:display-name "Generic Form"

     :component-did-mount
     (fn [this]
       (re-frame/dispatch [::state/init (r/props this)]))

     :component-did-update
     (fn [this [_ prev-props]]
       (let [{:keys [form-id data model]} (r/props this)]
         (when (not= data (:data prev-props))
           (re-frame/dispatch [::state/set-form-data form-id data model]))))

     :component-will-unmount
     (fn [this]
       (re-frame/dispatch [::state/reset (r/props this)]))

     :reagent-render
     (fn [{:keys [disabled? form-id loading? model on-save saving? spec]
           :or   {disabled? false
                  loading?  false
                  saving?   false}}]
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
                   :on-success on-save}])])}))
