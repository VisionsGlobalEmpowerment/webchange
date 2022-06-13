(ns webchange.admin.widgets.class-form.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.components.form.views :refer [form]]
    [webchange.admin.widgets.class-form.state :as state]
    [webchange.validation.specs.class-spec :as class-spec]))

(def class-model {:name      {:label "Class Name"
                              :type  :text}
                  :course-id {:label        "Assign Course"
                              :type         :select
                              :options      []
                              :options-type "int"}})

(defn class-add-form
  [{:keys [school-id] :as props}]
  (re-frame/dispatch [::state/init-add-form props])
  (fn [{:keys [editable? on-save]
        :or   {editable? true}}]
    (let [loading? @(re-frame/subscribe [::state/data-loading?])
          saving? @(re-frame/subscribe [::state/data-saving?])

          course-options @(re-frame/subscribe [::state/course-options])
          handle-save #(re-frame/dispatch [::state/create % {:on-success on-save}])]
      [form {:form-id   (-> (str "school-" school-id "-class-new")
                            (keyword))
             :model     (assoc-in class-model [:course-id :options] course-options)
             :spec      ::class-spec/create-class
             :on-save   handle-save
             :disabled? (not editable?)
             :loading?  loading?
             :saving?   saving?}])))

(defn class-edit-form
  [{:keys [class-id school-id] :as props}]
  (re-frame/dispatch [::state/init-edit-form props])
  (fn [{:keys [editable? on-save]
        :or   {editable? true}}]
    (let [loading? @(re-frame/subscribe [::state/data-loading?])
          saving? @(re-frame/subscribe [::state/data-saving?])
          class-data @(re-frame/subscribe [::state/form-data])
          course-options @(re-frame/subscribe [::state/course-options])
          handle-save #(re-frame/dispatch [::state/save % {:on-success on-save}])]
      [form {:form-id   (-> (str "school-" school-id "-class-" class-id)
                            (keyword))
             :data      class-data
             :model     (assoc-in class-model [:course-id :options] course-options)
             :spec      ::class-spec/class
             :on-save   handle-save
             :disabled? (not editable?)
             :loading?  loading?
             :saving?   saving?}])))
