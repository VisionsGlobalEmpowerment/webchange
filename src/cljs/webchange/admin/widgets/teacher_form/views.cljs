(ns webchange.admin.widgets.teacher-form.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.widgets.teacher-form.state :as state]
    [webchange.validation.specs.teacher :as teacher-spec]
    [webchange.ui-framework.components.index :as c]
    [webchange.ui.index :as ui]))

(def teacher-model {:first-name {:label "First Name"
                                 :type  :text}
                    :last-name  {:label "Last Name"
                                 :type  :text}
                    :email      {:label "Email"
                                 :type  :text}
                    :password   {:label      "Password"
                                 :type       :password
                                 :input-type "password"}
                    :type       {:label   "Teacher Type"
                                 :type    :select
                                 :options [{:text  "Select Teacher Type"
                                            :value ""}
                                           {:text  "Admin"
                                            :value "admin"}
                                           {:text  "Teacher"
                                            :value "teacher"}]}})

(defn- teacher-actions
  [{:keys [teacher-id on-remove]}]
  (let [teacher-removing? @(re-frame/subscribe [::state/teacher-removing?])
        remove-teacher #(re-frame/dispatch [::state/remove-teacher teacher-id {:on-success on-remove}])
        handle-delete-teacher #(c/with-confirmation {:message    "Remove Teacher?"
                                                     :on-confirm remove-teacher})]
    [:div.teacher-actions
     [c/icon-button {:icon     "remove"
                     :variant  "light"
                     :loading? teacher-removing?
                     :on-click handle-delete-teacher}
      "Delete Teacher Account"]]))

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
             handle-save #(re-frame/dispatch [::state/create-teacher % {:on-success on-save}])]
         [:div {:class-name (c/get-class-name {"widget--teacher-form" true
                                               class-name             (some? class-name)})}
          [ui/form {:form-id (-> (str "add-teacher")
                                 (keyword))
                    :model   teacher-model
                    :spec    ::teacher-spec/create-teacher
                    :on-save handle-save
                    :saving? saving?}]]))}))

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
     (fn [{:keys [teacher-id class-name on-save] :as props}]
       (let [saving? @(re-frame/subscribe [::state/data-saving?])
             data @(re-frame/subscribe [::state/form-data])
             handle-save #(re-frame/dispatch [::state/edit-teacher teacher-id % {:on-success on-save}])]
         [:div {:class-name (c/get-class-name {"widget--teacher-form" true
                                               class-name             (some? class-name)})}
          [ui/form {:form-id (-> (str "edit-teacher")
                                 (keyword))
                    :model   teacher-model
                    :data    data
                    :spec    ::teacher-spec/edit-teacher
                    :on-save handle-save
                    :saving? saving?}]
          [teacher-actions props]]))}))
