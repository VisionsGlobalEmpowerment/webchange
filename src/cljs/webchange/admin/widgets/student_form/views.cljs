(ns webchange.admin.widgets.student-form.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.components.form.views :refer [form]]
    [webchange.admin.utils :refer [get-uid]]
    [webchange.admin.widgets.student-form.state :as state]
    [webchange.ui-framework.components.index :as ui]
    [webchange.validation.specs.student :as student-spec]))

(def gender-options
  [{:value 1 :text "Male"}
   {:value 2 :text "Female"}])

(defn- access-code
  [{:keys [id label]} {:keys [value error handle-change]}]
  (when-not (some? value)
    (re-frame/dispatch [::state/generate-access-code {:on-success handle-change}]))
  (fn [{:keys [disabled? id label] :as a} {:keys [value error handle-change] :as b}]
    (let [handle-generate-click #(re-frame/dispatch [::state/generate-access-code {:on-success handle-change}])]
      [:<>
       [ui/label {:for id} label]
       [ui/input {:id        id
                  :value     value
                  :error     error
                  :disabled? true
                  :on-change handle-change
                  :action    (when-not disabled?
                               [ui/icon-button {:icon     "sync"
                                                :variant  "light"
                                                :on-click handle-generate-click}])}]])))

(defn student-form
  [{:keys [student-id] :as props}]
  (re-frame/dispatch [::state/init props])
  (fn [{:keys [editable? on-save]
        :or   {editable? true}}]
    (let [loading? @(re-frame/subscribe [::state/data-loading?])
          saving? @(re-frame/subscribe [::state/student-saving?])
          class-options @(re-frame/subscribe [::state/class-options])
          student-data @(re-frame/subscribe [::state/form-data])
          model {:first-name    {:label "First Name"
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
                                 :options      class-options
                                 :options-type "int"}
                 :access-code   {:label   "Access Code"
                                 :type    :custom
                                 :control access-code}}
          handle-save #(re-frame/dispatch [::state/save % {:on-success on-save}])]
      [form {:form-id   (-> (str "student-" student-id)
                            (keyword))
             :data      student-data
             :model     model
             :spec      ::student-spec/student
             :on-save   handle-save
             :disabled? (not editable?)
             :loading?  loading?
             :saving?   saving?}])))
