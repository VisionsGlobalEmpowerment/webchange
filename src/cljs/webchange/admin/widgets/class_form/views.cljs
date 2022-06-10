(ns webchange.admin.widgets.class-form.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.components.form.views :refer [form]]
    [webchange.admin.widgets.class-form.state :as state]
    [webchange.validation.specs.class-spec :as class-spec]))

(defn class-form
  [{:keys [class-id school-id] :as props}]
  (re-frame/dispatch [::state/init props])
  (fn [{:keys [editable? on-save]
        :or   {editable? true}}]
    (let [loading? @(re-frame/subscribe [::state/data-loading?])
          saving? @(re-frame/subscribe [::state/data-saving?])
          class-data @(re-frame/subscribe [::state/form-data])
          course-options @(re-frame/subscribe [::state/course-options])
          model {:name      {:label "Class Name"
                             :type  :text}
                 :course-id {:label        "Assign Course"
                             :type         :select
                             :options      course-options
                             :options-type "int"}}
          handle-save #(re-frame/dispatch [::state/save % {:on-success on-save}])]
      [form {:form-id   (-> (str "school-" school-id "-class-" class-id)
                            (keyword))
             :data      class-data
             :model     model
             :spec      ::class-spec/class
             :on-save   handle-save
             :disabled? (not editable?)
             :loading?  loading?
             :saving?   saving?}])))
