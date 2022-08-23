(ns webchange.admin.widgets.teacher-form.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.widgets.teacher-form.state :as state]
    [webchange.validation.specs.teacher :as teacher-spec]
    [webchange.ui-framework.components.index :as c]
    [webchange.ui.index :refer [get-class-name] :as ui]))

(def add-teacher-model {:first-name       {:label "First Name"
                                           :type  :text}
                        :last-name        {:label "Last Name"
                                           :type  :text}
                        :email            {:label "Email"
                                           :type  :text}
                        :password         {:label "Password"
                                           :type  :password}
                        :password-confirm {:label "Confirm password"
                                           :type  :password}
                        :type             {:label   "Teacher Type"
                                           :type    :select
                                           :options [{:text  "Select Teacher Type"
                                                      :value ""}
                                                     {:text  "Admin"
                                                      :value "admin"}
                                                     {:text  "Teacher"
                                                      :value "teacher"}]}})

(defn- show-remove-window
  []
  (re-frame/dispatch [::state/open-remove-window]))

(defn- get-edit-teacher-model
  [actions]
  (merge add-teacher-model
         (if (some? actions)
           actions
           {:remove {:label    "Delete account"
                     :type     :action
                     :icon     "trash"
                     :on-click show-remove-window}})))

(defn add-teacher-form
  []
  (r/create-class
    {:display-name "Add Teacher Form"

     :component-did-mount
     (fn [this]
       (re-frame/dispatch [::state/init-add-form (r/props this)]))

     :component-will-unmount
     (fn [this]
       (re-frame/dispatch [::state/reset-form (r/props this)]))

     :reagent-render
     (fn [{:keys [class-name on-save]}]
       (let [saving? @(re-frame/subscribe [::state/data-saving?])
             errors @(re-frame/subscribe [::state/custom-errors])
             handle-save #(re-frame/dispatch [::state/create-teacher % {:on-success on-save}])]
         [:div {:class-name (c/get-class-name {"widget--teacher-form" true
                                               class-name             (some? class-name)})}
          [ui/form {:form-id (-> (str "add-teacher")
                                 (keyword))
                    :model   add-teacher-model
                    :errors  errors
                    :spec    ::teacher-spec/create-teacher
                    :on-save handle-save
                    :saving? saving?}]]))}))

(defn- remove-window
  [{:keys [teacher-id]}]
  (let [{:keys [done? open? in-progress?]} @(re-frame/subscribe [::state/remove-window-state])
        remove #(re-frame/dispatch [::state/remove-teacher teacher-id])
        close-window #(re-frame/dispatch [::state/close-remove-window])
        confirm-removed #(re-frame/dispatch [::state/handle-removed])]
    [ui/confirm {:open?        open?
                 :loading?     in-progress?
                 :confirm-text (if done? "Ok" "Yes")
                 :on-confirm   (if done? confirm-removed remove)
                 :on-cancel    (when-not done? close-window)}
     (if done?
       "Teacher account successfully deleted"
       "Are you sure you want to delete teacher account?")]))

(defn edit-teacher-form
  []
  (r/create-class
    {:display-name "Edit Teacher Form"

     :component-did-mount
     (fn [this]
       (re-frame/dispatch [::state/init-edit-form (r/props this)]))

     :component-will-unmount
     (fn [this]
       (re-frame/dispatch [::state/reset-form (r/props this)]))

     :reagent-render
     (fn [{:keys [actions disabled? teacher-id class-name on-cancel on-save]
           :or   {disabled? false}}]
       (let [saving? @(re-frame/subscribe [::state/data-saving?])
             data @(re-frame/subscribe [::state/form-data])
             handle-save #(re-frame/dispatch [::state/edit-teacher teacher-id % {:on-success on-save}])]
         [:<>
          [ui/form {:form-id    (-> (str "edit-teacher")
                                    (keyword))
                    :model      (get-edit-teacher-model actions)
                    :data       data
                    :spec       ::teacher-spec/edit-teacher
                    :on-save    handle-save
                    :on-cancel  on-cancel
                    :saving?    saving?
                    :disabled?  disabled?
                    :class-name (get-class-name {"widget--teacher-form" true
                                                 class-name             (some? class-name)})}]
          [remove-window {:teacher-id teacher-id}]]))}))
