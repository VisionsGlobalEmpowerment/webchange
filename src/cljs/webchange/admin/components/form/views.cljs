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
  [{:keys [disabled? id form-id label input-type]}]
  (let [value @(re-frame/subscribe [::state/field-value form-id id])
        error @(re-frame/subscribe [::state/field-error form-id id])
        handle-change #(re-frame/dispatch [::state/set-field-value form-id id %])
        input-type (or input-type "str")]
    [:<>
     [c/label {:for id} label]
     [c/input {:id        id
               :type      input-type
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

(defn- password-control
  [{:keys [disabled? id form-id label input-type]}]
  (let [value @(re-frame/subscribe [::state/field-value form-id id])
        error @(re-frame/subscribe [::state/field-error form-id id])
        handle-change #(re-frame/dispatch [::state/set-field-value form-id id %])
        input-type (or input-type "str")]
    [:<>
     [c/label {:for id} label]
     [c/password {:id        id
                  :type      input-type
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
      :password [password-control control-props]
      :select [select-control control-props]
      :custom [custom-control control-props])))

(defn- loading-indicator
  []
  [:div.data-loading-indicator
   [c/circular-progress]])

(defn- form-actions
  [{:keys [form-id disabled? on-save on-cancel saving? spec]}]
  (let [handle-save-click #(re-frame/dispatch [::state/save form-id spec on-save])
        handle-cancel-click #(when (fn? on-cancel) (on-cancel))]
    [:div.form-actions
     (when (some? on-cancel)
       [c/button {:on-click   handle-cancel-click
                  :disabled?  (or disabled? saving?)
                  :class-name "cancel"}
        "Cancel"])
     [c/button {:on-click   handle-save-click
                :disabled?  (or disabled? saving?)
                :loading?   saving?
                :class-name "submit"}
      "Save"]]))

(defn form
  []
  (r/create-class
    {:display-name "Generic Form"

     :component-did-mount
     (fn [this]
       (re-frame/dispatch [::state/init (r/props this)]))

     :component-did-update
     (fn [this [_ prev-props]]
       (let [{:keys [form-id data model errors]} (r/props this)]
         (re-frame/dispatch [::state/set-custom-errors form-id errors])
         (when (not= data (:data prev-props))
           (re-frame/dispatch [::state/set-form-data form-id data model]))))

     :component-will-unmount
     (fn [this]
       (re-frame/dispatch [::state/reset (r/props this)]))

     :reagent-render
     (fn [{:keys [class-name disabled? errors form-id loading? model on-cancel on-save saving? spec]
           :or   {disabled? false
                  errors    {}
                  loading?  false
                  saving?   false}}]
       [:div {:class-name (c/get-class-name {"component--form" true
                                             class-name        (some? class-name)})}
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
          [form-actions {:form-id   form-id
                         :disabled? loading?
                         :saving?   saving?
                         :spec      spec
                         :on-save   on-save
                         :on-cancel on-cancel}])])}))
