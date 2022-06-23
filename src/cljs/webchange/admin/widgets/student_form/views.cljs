(ns webchange.admin.widgets.student-form.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.utils :refer [get-uid]]
    [webchange.admin.widgets.student-form.state :as state]
    [webchange.ui-framework.components.index :as c]
    [webchange.ui.index :as ui]
    [webchange.validation.specs.student :as student-spec]))

(def gender-options
  [{:value 1 :text "Male"}
   {:value 2 :text "Female"}])

(defn- access-code
  [{:keys [disabled? id label]} {:keys [value error handle-change]}]
  (let [handle-generate-click #(re-frame/dispatch [::state/generate-access-code {:on-success handle-change}])]
    [:<>
     [c/label {:for id} label]
     [c/input {:id        id
               :value     value
               :error     error
               :disabled? true
               :on-change handle-change
               :action    (when-not disabled?
                            [c/icon-button {:icon     "sync"
                                            :variant  "light"
                                            :on-click handle-generate-click}])}]]))

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
                                    :control access-code}})

(defn add-student-form
  [{:keys [student-id] :as props}]
  (re-frame/dispatch [::state/init-add-form props])
  (fn [{:keys [editable? on-save]
        :or   {editable? true}}]
    (let [loading? @(re-frame/subscribe [::state/data-loading?])
          saving? @(re-frame/subscribe [::state/student-saving?])
          class-options @(re-frame/subscribe [::state/class-options])
          student-data @(re-frame/subscribe [::state/form-data])
          handle-save #(re-frame/dispatch [::state/create % {:on-success on-save}])]
      [ui/form {:form-id   (-> (str "student-" student-id)
                               (keyword))
                :data      student-data
                :model     (assoc-in student-model [:class-id :options] class-options)
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
          handle-save #(re-frame/dispatch [::state/save % {:on-success on-save}])]
      [ui/form {:form-id   (-> (str "student-" student-id)
                               (keyword))
                :data      student-data
                :model     (assoc-in student-model [:class-id :options] class-options)
                :spec      ::student-spec/student
                :on-save   handle-save
                :disabled? (not editable?)
                :loading?  loading?
                :saving?   saving?}])))
