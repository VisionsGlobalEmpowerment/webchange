(ns webchange.admin.pages.class-profile.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.components.counter.views :refer [counter]]
    [webchange.admin.pages.class-profile.state :as state]
    [webchange.admin.widgets.add-class-students.views :refer [add-class-students]]
    [webchange.admin.widgets.add-class-teachers.views :refer [add-class-teachers]]
    [webchange.admin.widgets.class-form.views :refer [class-form]]
    [webchange.admin.widgets.no-data.views :refer [no-data]]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui-framework.components.index :as c :refer [button input]]))

(defn- class-counter
  [{:keys [school-id class-id]}]
  (let [{:keys [students teachers]} @(re-frame/subscribe [::state/class-stats])
        handle-add-student-click #(re-frame/dispatch [::state/open-add-student-form])
        handle-manage-students-click #(re-frame/dispatch [::state/open-manage-students-page school-id class-id])
        handle-add-teacher-click #(re-frame/dispatch [::state/open-add-teacher-form])]
    [counter {:items [{:id      :teachers
                       :value   teachers
                       :title   "Teachers"
                       :icon    "teachers"
                       :actions [{:title    "Add Teacher"
                                  :icon     "add"
                                  :on-click handle-add-teacher-click}]}
                      {:id      :students
                       :value   students
                       :title   "Students"
                       :icon    "students"
                       :color   "blue"
                       :actions [{:title    "Manage Students"
                                  :color    "yellow"
                                  :on-click handle-manage-students-click}
                                 {:title    "Add Student"
                                  :icon     "add"
                                  :on-click handle-add-student-click}]}]}]))

(defn- statistics
  []
  [page/block {:title "Statistics"}
   [no-data]])

(defn- side-bar-class-form
  [{:keys [class-id school-id]}]
  (let [form-editable? @(re-frame/subscribe [::state/form-editable?])
        handle-edit-click #(re-frame/dispatch [::state/set-form-editable (not form-editable?)])
        handle-data-save #(re-frame/dispatch [::state/update-class-data %])]
    [page/side-bar {:title   "Class Info"
                    :actions [:<>
                              [c/icon-button {:icon     "edit"
                                              :variant  "light"
                                              :on-click handle-edit-click}]]}
     [class-form {:class-id  class-id
                  :school-id school-id
                  :editable? form-editable?
                  :on-save   handle-data-save}]]))

(defn- side-bar-add-student
  [{:keys [class-id school-id]}]
  (let [handle-cancel #(re-frame/dispatch [::state/open-class-form])
        handle-save #(re-frame/dispatch [::state/handle-students-added %])]
    [page/side-bar {:title "Add Student"}
     [add-class-students {:class-id  class-id
                          :school-id school-id
                          :on-save   handle-save
                          :on-cancel handle-cancel}]]))

(defn- side-bar-add-teacher
  [{:keys [class-id school-id]}]
  (let [handle-cancel #(re-frame/dispatch [::state/open-class-form])
        handle-save #(re-frame/dispatch [::state/handle-teachers-added %])]
    [page/side-bar {:title "Add Teacher"}
     [add-class-teachers {:class-id  class-id
                          :school-id school-id
                          :on-save   handle-save
                          :on-cancel handle-cancel}]]))

(defn- side-bar
  [props]
  (let [side-bar-content @(re-frame/subscribe [::state/side-bar])]
    (case side-bar-content
      :class-form [side-bar-class-form props]
      :add-student [side-bar-add-student props]
      :add-teacher [side-bar-add-teacher props]
      nil)))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn [props]
    [page/page
     [page/main-content {:title "Class Profile"}
      [class-counter props]
      [statistics]]
     [side-bar props]]))
