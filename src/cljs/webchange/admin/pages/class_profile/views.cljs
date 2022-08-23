(ns webchange.admin.pages.class-profile.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.class-profile.state :as state]
    [webchange.admin.pages.class-profile.course-assign.views :refer [assign-class-course]]
    [webchange.admin.pages.class-profile.students-add.views :refer [class-students-add]]
    [webchange.admin.pages.class-profile.students-list.views :refer [class-students-list]]
    [webchange.admin.pages.class-profile.teacher-edit.views :refer [class-teacher-edit]]
    [webchange.admin.pages.class-profile.teachers-add.views :refer [class-teachers-add]]
    [webchange.admin.pages.class-profile.teachers-list.views :refer [class-teachers-list]]
    [webchange.admin.widgets.class-form.views :refer [class-edit-form]]
    [webchange.admin.widgets.no-data.views :refer [no-data]]
    [webchange.admin.widgets.page.counter.views :refer [counter]]
    [webchange.admin.widgets.page.views :as page]))

(defn- class-counter
  []
  (let [{:keys [students teachers]} @(re-frame/subscribe [::state/class-stats])
        courses @(re-frame/subscribe [::state/school-courses-number])
        class-course @(re-frame/subscribe [::state/class-course])
        handle-add-student-click #(re-frame/dispatch [::state/open-add-student-form])
        handle-manage-students-click #(re-frame/dispatch [::state/open-students-list])
        handle-add-teacher-click #(re-frame/dispatch [::state/open-add-teacher-form])
        handle-manage-teachers-click #(re-frame/dispatch [::state/open-teachers-list])
        handle-manage-courses-click #(re-frame/dispatch [::state/open-assign-course-form])
        handle-student-activities-click #(re-frame/dispatch [::state/open-students-activities])
        add-button-props {:color      "blue-1"
                          :chip       "plus"
                          :chip-color "yellow-1"}]
    [counter {:data [{:text    "Teachers"
                      :icon    "teachers"
                      :counter teachers
                      :actions [{:text     "Manage Teachers"
                                 :on-click handle-manage-teachers-click}
                                (merge add-button-props
                                       {:text     "Add Teacher"
                                        :on-click handle-add-teacher-click})]}
                     {:text    "Students"
                      :icon    "students"
                      :counter students
                      :actions [{:text     "Manage Students"
                                 :on-click handle-manage-students-click}
                                (merge add-button-props
                                       {:text     "Add Student"
                                        :on-click handle-add-student-click})]}
                     {:text       "Students Activities"
                      :icon       "games"
                      :counter    students
                      :background "blue-2"
                      :actions    [{:text     "Students Activities"
                                    :on-click handle-student-activities-click}]}
                     {:text       (:name class-course)
                      :icon       "courses"
                      :background "green-2"
                      :counter    courses
                      :actions    [{:text     "Manage Courses"
                                    :on-click handle-manage-courses-click}]}]}]))

(defn- statistics
  []
  [page/block {:title "Statistics"
               :icon  "statistics"}
   [no-data]])

(defn- side-bar-class-form
  [{:keys [class-id school-id]}]
  (let [form-editable? @(re-frame/subscribe [::state/form-editable?])
        handle-edit-click #(re-frame/dispatch [::state/set-form-editable true])
        handle-cancel-click #(re-frame/dispatch [::state/handle-class-edit-cancel])
        handle-data-save #(re-frame/dispatch [::state/update-class-data %])]
    [page/side-bar {:title    "Class Info"
                    :icon     "info"
                    :focused? form-editable?
                    :actions  (cond-> []
                                      form-editable? (conj {:icon     "close"
                                                            :on-click handle-cancel-click})
                                      (not form-editable?) (conj {:icon     "edit"
                                                                  :on-click handle-edit-click}))}
     [class-edit-form {:class-id  class-id
                       :school-id school-id
                       :editable? form-editable?
                       :on-save   handle-data-save
                       :on-cancel handle-cancel-click}]]))

(defn- side-bar
  [side-bar-props]
  (let [{:keys [component props]} @(re-frame/subscribe [::state/side-bar])
        props (merge side-bar-props props)]
    (case component
      :assign-course [assign-class-course props]
      :class-form [side-bar-class-form props]
      :students-add [class-students-add props]
      :students-list [class-students-list props]
      :teacher-edit [class-teacher-edit props]
      :teachers-add [class-teachers-add props]
      :teachers-list [class-teachers-list props]
      nil)))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn [props]
    [page/page
     [page/content {:title "Class Profile"
                    :icon  "classes"}
      [class-counter]
      [statistics]]
     [side-bar props]]))
