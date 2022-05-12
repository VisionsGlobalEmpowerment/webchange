(ns webchange.admin.widgets.school-form.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.components.form.views :refer [form]]
    [webchange.admin.widgets.school-form.state :as state]
    [webchange.validation.specs.school-spec :as school-spec]
    [webchange.ui-framework.components.index :as ui]))

(def school-model {:name     {:label "School Name"
                              :type  :text}
                   :location {:label "Location"
                              :type  :text}
                   :about    {:label "About"
                              :type  :text-multiline}})

(defn edit-school-form
  []
  (r/create-class
    {:display-name "Edit School Form"

     :component-did-mount
     (fn [this]
       (re-frame/dispatch [::state/init (r/props this)]))

     :component-will-unmount
     (fn [this]
       (re-frame/dispatch [::state/reset-form (r/props this)]))

     :reagent-render
     (fn [{:keys [editable? on-save school-id]}]
       (let [loading? @(re-frame/subscribe [::state/data-loading?])
             saving? @(re-frame/subscribe [::state/data-saving?])
             school-data @(re-frame/subscribe [::state/form-data])
             model school-model
             handle-save #(re-frame/dispatch [::state/save % {:on-success on-save}])]
         [:div.widget--school-form
          [form {:form-id   (-> (str "school-" school-id)
                                (keyword))
                 :data      school-data
                 :model     model
                 :spec      ::school-spec/edit-school
                 :on-save   handle-save
                 :disabled? (not editable?)
                 :loading?  loading?
                 :saving?   saving?}]]))}))

(defn add-school-form
  []
  (r/create-class
    {:display-name "Add School Form"

     :component-did-mount
     (fn [this]
       (re-frame/dispatch [::state/init-add-form (r/props this)]))

     :component-will-unmount
     (fn [this]
       (re-frame/dispatch [::state/reset-form (r/props this)]))

     :reagent-render
     (fn [{:keys [class-name on-save]}]
       (let [saving? @(re-frame/subscribe [::state/data-saving?])
             school-data @(re-frame/subscribe [::state/form-data])
             model school-model
             handle-save #(re-frame/dispatch [::state/create-school % {:on-success on-save}])]
         [:div {:class-name (ui/get-class-name {"widget--school-form" true
                                                class-name            (some? class-name)})}
          [form {:form-id (-> (str "add-school")
                              (keyword))
                 :data    school-data
                 :model   model
                 :spec    ::school-spec/create-school
                 :on-save handle-save
                 :saving? saving?}]]))}))
