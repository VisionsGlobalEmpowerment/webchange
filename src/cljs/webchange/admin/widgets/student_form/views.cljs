(ns webchange.admin.widgets.student-form.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.utils :refer [get-uid]]
    [webchange.admin.widgets.access-code.views :refer [access-code]]
    [webchange.admin.widgets.student-form.state :as state]
    [webchange.ui.index :as ui]
    [webchange.validation.specs.student :as student-spec]))

(def gender-options
  [{:value 1 :text "Male"}
   {:value 2 :text "Female"}])

(def add-student-model {:first-name    {:label "First Name"
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

(defn- show-remove-account-window
  []
  (re-frame/dispatch [::state/open-remove-window]))

(defn- show-remove-from-class-window
  []
  (re-frame/dispatch [::state/open-remove-from-class-window]))


(def edit-student-model (merge add-student-model
                               {:remove-account    {:label    "Delete account"
                                                    :type     :action
                                                    :icon     "trash"
                                                    :on-click show-remove-account-window}
                                :remove-from-class {:label    "Remove From Class"
                                                    :type     :action
                                                    :icon     "account-remove"
                                                    :on-click show-remove-from-class-window}}))


(defn add-student-form
  []
  (r/create-class
    {:display-name "Add Student Form"

     :component-did-mount
     (fn [this]
       (re-frame/dispatch [::state/init-add-form (r/props this)]))

     :component-will-unmount
     (fn [this]
       (re-frame/dispatch [::state/reset-form (r/props this)]))

     :reagent-render
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
         [ui/form {:form-id   (-> (str "add-student")
                                  (keyword))
                   :data      student-data
                   :model     (-> add-student-model
                                  (assoc-in [:class-id :options] class-options)
                                  (assoc-in [:access-code :control] access-code-control))
                   :spec      ::student-spec/create-student
                   :on-save   handle-save
                   :disabled? (not editable?)
                   :loading?  loading?
                   :saving?   saving?}]))}))

(defn- remove-account-window
  [{:keys [student-id]}]
  (let [{:keys [done? open? in-progress?]} @(re-frame/subscribe [::state/remove-window-state])
        remove #(re-frame/dispatch [::state/remove-student student-id])
        close-window #(re-frame/dispatch [::state/close-remove-window])
        confirm-removed #(re-frame/dispatch [::state/handle-removed])]
    [ui/confirm {:open?        open?
                 :loading?     in-progress?
                 :confirm-text (if done? "Ok" "Yes")
                 :on-confirm   (if done? confirm-removed remove)
                 :on-cancel    (when-not done? close-window)}
     (if done?
       "Student account successfully deleted"
       "Are you sure you want to delete student account?")]))

(defn- remove-from-class-window
  [{:keys [student-id]}]
  (let [{:keys [done? open? in-progress?]} @(re-frame/subscribe [::state/remove-from-class-window-state])
        remove-from-class #(re-frame/dispatch [::state/remove-from-class-student student-id])
        close-window #(re-frame/dispatch [::state/close-remove-from-class-window])
        confirm-removed-from-class #(re-frame/dispatch [::state/handle-removed-from-class])]
    [ui/confirm {:open?        open?
                 :loading?     in-progress?
                 :confirm-text (if done? "Ok" "Yes")
                 :on-confirm   (if done? confirm-removed-from-class remove-from-class)
                 :on-cancel    (when-not done? close-window)}
     (if done?
       "Student account successfully remove from class"
       "Are you sure you want to remove student from class?")]))

(defn- filter-actions
  [model actions]
  (reduce (fn [model [action available?]]
            (cond-> model
                    (not available?) (dissoc action)))
          model
          actions))

(defn edit-student-form
  []
  (r/create-class
    {:display-name "Edit Student Form"

     :component-did-mount
     (fn [this]
       (re-frame/dispatch [::state/init-edit-form (r/props this)]))

     :component-will-unmount
     (fn [this]
       (re-frame/dispatch [::state/reset-form (r/props this)]))

     :reagent-render
     (fn [{:keys [actions editable? on-cancel on-save student-id]
           :or   {actions   {:remove-account    true
                             :remove-from-class false}
                  editable? true}}]
       (let [loading? @(re-frame/subscribe [::state/data-loading?])
             saving? @(re-frame/subscribe [::state/student-saving?])
             class-options @(re-frame/subscribe [::state/class-options])
             student-data @(re-frame/subscribe [::state/form-data])
             handle-save #(re-frame/dispatch [::state/save % {:on-success on-save}])
             access-code-control (fn [props]
                                   [access-code (merge props
                                                       {:school-id 1})])]
         [:<>
          [ui/form {:form-id   (-> (str "student-" student-id)
                                   (keyword))
                    :data      student-data
                    :model     (-> edit-student-model
                                   (assoc-in [:class-id :options] class-options)
                                   (assoc-in [:access-code :control] access-code-control)
                                   (filter-actions actions))
                    :spec      ::student-spec/student
                    :on-save   handle-save
                    :on-cancel on-cancel
                    :disabled? (not editable?)
                    :loading?  loading?
                    :saving?   saving?}]
          [remove-account-window {:student-id student-id}]
          [remove-from-class-window {:student-id student-id}]]))}))
