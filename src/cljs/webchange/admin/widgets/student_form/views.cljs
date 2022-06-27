(ns webchange.admin.widgets.student-form.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.utils :refer [get-uid]]
    [webchange.admin.widgets.access-code.views :refer [access-code]]
    [webchange.admin.widgets.student-form.state :as state]
    [webchange.ui.index :as ui]
    [webchange.validation.specs.student :as student-spec]))

(def gender-options
  [{:value 1 :text "Male"}
   {:value 2 :text "Female"}])

(def student-model {:first-name    {:label "First Name"
                                    :type  :text}
                    :last-name     {:label "Last Name"
                                    :type  :text}
                    :gender        {:label        "Gender"
                                    :type         :select
                                    :options      gender-options
                                    :options-type "int"}
                    :date-of-birth {:label "Date of Birth"
                                    :type  :date}
                    :class-id      {:label        "Class"
                                    :type         :select
                                    :options      []
                                    :options-type "int"}
                    :access-code   {:label   "Access Code"
                                    :type    :custom
                                    :control nil}})

(defn add-student-form
  [{:keys [student-id] :as props}]
  (re-frame/dispatch [::state/init-add-form props])
  (fn [{:keys [editable? on-save]
        :or   {editable? true}}]
    (let [loading? @(re-frame/subscribe [::state/data-loading?])
          saving? @(re-frame/subscribe [::state/student-saving?])
          class-options @(re-frame/subscribe [::state/class-options])
          student-data @(re-frame/subscribe [::state/form-data])
          handle-save #(re-frame/dispatch [::state/create % {:on-success on-save}])
          access-code-control (fn [props]
                                [access-code (merge props
                                                    {:school-id 1})])]
      [ui/form {:form-id   (-> (str "student-" student-id)
                               (keyword))
                :data      student-data
                :model     (-> student-model
                               (assoc-in [:class-id :options] class-options)
                               (assoc-in [:access-code :control] access-code-control))
                :spec      ::student-spec/create-student
                :on-save   handle-save
                :disabled? (not editable?)
                :loading?  loading?
                :saving?   saving?}])))

(defn edit-student-form
  [{:keys [student-id] :as props}]
  (re-frame/dispatch [::state/init-edit-form props])
  (fn [{:keys [editable? on-save]
        :or   {editable? true}}]
    (let [loading? @(re-frame/subscribe [::state/data-loading?])
          saving? @(re-frame/subscribe [::state/student-saving?])
          class-options @(re-frame/subscribe [::state/class-options])
          student-data @(re-frame/subscribe [::state/form-data])
          handle-save #(re-frame/dispatch [::state/save % {:on-success on-save}])
          access-code-control (fn [props]
                                [access-code (merge props
                                                    {:school-id 1})])]
      [ui/form {:form-id   (-> (str "student-" student-id)
                               (keyword))
                :data      student-data
                :model     (-> student-model
                               (assoc-in [:class-id :options] class-options)
                               (assoc-in [:access-code :control] access-code-control))
                :spec      ::student-spec/student
                :on-save   handle-save
                :disabled? (not editable?)
                :loading?  loading?
                :saving?   saving?}])))
