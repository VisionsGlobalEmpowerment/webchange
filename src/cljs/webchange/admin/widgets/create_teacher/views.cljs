(ns webchange.admin.widgets.create-teacher.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.components.form.views :refer [form]]
    [webchange.admin.widgets.create-teacher.state :as state]
    [webchange.ui-framework.components.index :as ui]
    [webchange.validation.specs.teacher :as teacher-spec]))

(defn create-teacher
  []
  (r/create-class
    {:display-name "Create New Teacher"

     :component-did-mount
     (fn [this]
       (re-frame/dispatch [::state/init (r/props this)]))

     :component-will-unmount
     (fn [this]
       (re-frame/dispatch [::state/reset (r/props this)]))

     :reagent-render
     (fn [{:keys [school-id class-id on-save]}]
       (let [model {:first-name {:label "First Name"
                                 :type  :text}
                    :last-name  {:label "Last Name"
                                 :type  :text}
                    :email      {:label "Email"
                                 :type  :text}
                    :password   {:label "Password"
                                 :type  :text}
                    :type       {:label   "Teacher Type"
                                 :type    :select
                                 :options [{:text  "Select type"
                                            :value nil}
                                           {:text  "Admin"
                                            :value "admin"}
                                           {:text  "Teacher"
                                            :value "teacher"}]}}
             handle-save #(re-frame/dispatch [::state/save school-id class-id % {:on-success on-save}])]
         [:div {:class-name "widget--create-teacher"}
          [form {:form-id (-> (str "create-teacher/school-" school-id)
                              (keyword))
                 :model   model
                 :spec    ::teacher-spec/create-teacher
                 :on-save handle-save
                 ;:disabled? (not editable?)
                 ;:loading?  loading?
                 ;:saving?   saving?

                 :data    {:first-name "first-name-"
                           :last-name  "last-name"
                           :email      "email@email.org"
                           :password   "password"
                           :type       "teacher"}
                 }]]))}))
