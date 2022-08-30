(ns webchange.ui.components.form.views
  (:require
    [clojure.spec.alpha :as s]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.ui.components.button.views :refer [button]]
    [webchange.ui.components.copy-link.views :refer [copy-link]]
    [webchange.ui.components.date.views :refer [date]]
    [webchange.ui.components.form.form-action.views :refer [form-action]]
    [webchange.ui.components.input.views :refer [input]]
    [webchange.ui.components.input-label.views :refer [input-label]]
    [webchange.ui.components.form.state :as state]
    [webchange.ui.components.overlay.views :refer [loading-overlay]]
    [webchange.ui.components.password.views :refer [password]]
    [webchange.ui.components.select.views :refer [select]]
    [webchange.ui.components.switch.views :refer [switch]]
    [webchange.ui.components.text-area.views :refer [text-area]]
    [webchange.ui-framework.components.index :as c]))

(defn- ->spec-data
  [spec]
  (->> (s/form spec)
       (rest)
       (partition 2)
       (map (fn [[a b]]
              [(keyword a)
               (map #(-> % name keyword) b)]))
       (into {})))

(defn- required-field?
  [spec field]
  (if (some? spec)
    (let [{:keys [req-un]} (->spec-data spec)]
      (-> #{field}
          (some req-un)
          (boolean)))
    false))

(defn- action-control
  [{:keys [disabled? form-id id] :as props}]
  (when-not disabled?
    (let [value @(re-frame/subscribe [::state/field-value form-id id])]
      [form-action (assoc props :value value)])))

(defn- date-control
  [{:keys [disabled? id form-id label required?]}]
  (let [value @(re-frame/subscribe [::state/field-value form-id id])
        error @(re-frame/subscribe [::state/field-error form-id id])
        handle-change #(re-frame/dispatch [::state/set-field-value form-id id %])]
    [date {:id        id
           :value     value
           :label     label
           :error     error
           :disabled? disabled?
           :required? required?
           :on-change handle-change}]))

(defn- link-control
  [{:keys [form-id id] :as props}]
  (let [value @(re-frame/subscribe [::state/field-value form-id id])]
    [copy-link (assoc props :value value)]))

(defn- text-control
  [{:keys [disabled? id form-id label input-type required?]}]
  (let [value @(re-frame/subscribe [::state/field-value form-id id])
        error @(re-frame/subscribe [::state/field-error form-id id])
        handle-change #(re-frame/dispatch [::state/set-field-value form-id id %])
        input-type (or input-type "str")]
    [input {:id        id
            :type      input-type
            :value     value
            :label     label
            :error     error
            :disabled? disabled?
            :required? required?
            :on-change handle-change}]))

(defn- text-multiline-control
  [{:keys [disabled? id form-id label required?]}]
  (let [value @(re-frame/subscribe [::state/field-value form-id id])
        error @(re-frame/subscribe [::state/field-error form-id id])
        handle-change #(re-frame/dispatch [::state/set-field-value form-id id %])]
    [text-area {:id        id
                :value     value
                :error     error
                :label     label
                :disabled? disabled?
                :required? required?
                :on-change handle-change}]))

(defn- password-control
  [{:keys [disabled? id form-id label input-type required?]}]
  (let [value @(re-frame/subscribe [::state/field-value form-id id])
        error @(re-frame/subscribe [::state/field-error form-id id])
        handle-change #(re-frame/dispatch [::state/set-field-value form-id id %])
        input-type (or input-type "str")]
    [password {:id        id
               :type      input-type
               :value     value
               :error     error
               :label     label
               :disabled? disabled?
               :required? required?
               :on-change handle-change}]))

(defn- select-control
  [{:keys [disabled? id form-id label options options-type required?]}]
  (let [value @(re-frame/subscribe [::state/field-value form-id id])
        error @(re-frame/subscribe [::state/field-error form-id id])
        handle-change #(re-frame/dispatch [::state/set-field-value form-id id %])]
    [select {:id        id
             :value     value
             :options   options
             :error     error
             :label     label
             :disabled? disabled?
             :required? required?
             :on-change handle-change
             :type      options-type}]))

(defn- custom-control
  [{:keys [id label form-id control] :as props}]
  (let [value @(re-frame/subscribe [::state/field-value form-id id])
        error @(re-frame/subscribe [::state/field-error form-id id])
        handle-change #(re-frame/dispatch [::state/set-field-value form-id id %])]
    [:<>
     (when (some? label)
       [input-label label])
     [control (merge props
                     {:value     value
                      :error     error
                      :on-change handle-change})]]))
(declare form-control)

(defn- group-control
  [{:keys [form-id disabled? spec class-name fields]}]
  [:div {:class-name (c/get-class-name {"form--group-control" true
                                        class-name            (some? class-name)})}
   (for [[field-name field-options] fields]
     ^{:key field-name}
     [form-control {:id        field-name
                    :form-id   form-id
                    :options   field-options
                    :disabled? disabled?
                    :spec      spec}])])

(defn- label-control
  [{:keys [class-name label]}]
  [:div {:class-name (c/get-class-name {class-name (some? class-name)})}
   [input-label label]])

(defn- switch-control
  [{:keys [disabled? id form-id label]}]
  (let [value @(re-frame/subscribe [::state/field-value form-id id])
        error @(re-frame/subscribe [::state/field-error form-id id])
        handle-change #(re-frame/dispatch [::state/set-field-value form-id id (not value)])]
    [switch {:id        id
             :checked?  value
             :label     label
             :error     error
             :disabled? disabled?
             :on-change handle-change}]))

(defn- form-control
  [{:keys [disabled? id form-id options spec]}]
  (let [{:keys [type]} options
        control-props (merge {:id        id
                              :form-id   form-id
                              :disabled? disabled?
                              :required? (required-field? spec id)}
                             options)]
    (case type
      :action [action-control control-props]
      :date [date-control control-props]
      :link [link-control control-props]
      :text [text-control control-props]
      :text-multiline [text-multiline-control control-props]
      :password [password-control control-props]
      :select [select-control control-props]
      :custom [custom-control control-props]
      :group [group-control control-props]
      :label [label-control control-props]
      :switch [switch-control control-props]
      :empty [:div])))

(defn- form-actions
  [{:keys [form-id disabled? on-save on-cancel saving? spec]}]
  (let [handle-save-click #(re-frame/dispatch [::state/save form-id spec on-save])
        handle-cancel-click #(when (fn? on-cancel) (on-cancel))]
    [:div.form-actions
     (when (some? on-cancel)
       [button {:on-click   handle-cancel-click
                :disabled?  (or disabled? saving?)
                :color      "blue-1"
                :class-name "cancel"}
        "Cancel"])
     [button {:on-click   handle-save-click
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
       (let [{:keys [form-id data model errors] :or {data {}}} (r/props this)]
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
        [:div.controls
         (for [[field-name field-options] model]
           ^{:key field-name}
           [form-control {:id        field-name
                          :form-id   form-id
                          :options   field-options
                          :disabled? disabled?
                          :spec      spec}])]
        (when-not disabled?
          [form-actions {:form-id   form-id
                         :disabled? loading?
                         :saving?   saving?
                         :spec      spec
                         :on-save   on-save
                         :on-cancel on-cancel}])
        (when loading?
          [loading-overlay])])}))
