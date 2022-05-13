(ns webchange.admin.pages.add-student.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.components.form.views :refer [form]]
    [webchange.admin.pages.add-student.state :as state]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui-framework.components.index :as c]
    [webchange.validation.specs.student :as student-spec]))

(def gender-options
  [{:value 1 :text "Male"}
   {:value 2 :text "Female"}])

(defn- access-code
  [{:keys [id label]} {:keys [value error handle-change]}]
  (re-frame/dispatch [::state/generate-access-code {:on-success handle-change}])
  (fn [{:keys [id label]} {:keys [value error handle-change]}]
    (let [handle-generate-click #(re-frame/dispatch [::state/generate-access-code {:on-success handle-change}])]
      [:<>
       [c/label {:for id} label]
       [c/input {:id        id
                 :value     value
                 :error     error
                 :disabled? true
                 :on-change handle-change}]
       [c/button {:on-click handle-generate-click}
        "Generate Access Code"]])))

(defn- student-form
  []
  (let [loading? @(re-frame/subscribe [::state/data-loading?])
        saving? @(re-frame/subscribe [::state/data-saving?])
        class-options @(re-frame/subscribe [::state/class-options])
        model {:first-name {:label "First Name"
                            :type  :text}
               :last-name {:label "Last Name"
                           :type  :text}
               :gender {:label        "Gender"
                        :type         :select
                        :options      gender-options
                        :options-type "int"}
               :date-of-birth {:label "Date of Birth"
                               :type :text}
               :class-id {:label "Class"
                          :type :select
                          :options class-options
                          :options-type "int"}
               :access-code {:label "Access Code"
                             :type :custom
                             :control access-code}}
        handle-save #(re-frame/dispatch [::state/create-student %])]
    [form {:model     model
           :spec      ::student-spec/create-student
           :on-save   handle-save
           :loading?  loading?
           :saving?   saving?}]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn [props]
    [page/page
     [page/main-content {:title "Create Student Account"}
      [student-form props]]]))
