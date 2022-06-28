(ns webchange.admin.pages.class-profile.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.class-profile.state :as state]
    [webchange.admin.widgets.add-class-students.views :refer [add-class-students]]
    [webchange.admin.widgets.add-class-teachers.views :refer [add-class-teachers]]
    [webchange.admin.widgets.class-form.views :refer [class-edit-form]]
    [webchange.admin.widgets.no-data.views :refer [no-data]]
    [webchange.admin.widgets.page.counter.views :refer [counter]]
    [webchange.admin.widgets.page.side-bar-page.views :as page]))

(defn- class-counter
  [{:keys [school-id class-id]}]
  (let [{:keys [students teachers]} @(re-frame/subscribe [::state/class-stats])
        courses @(re-frame/subscribe [::state/school-courses-number])
        class-course @(re-frame/subscribe [::state/class-course])
        handle-add-student-click #(re-frame/dispatch [::state/open-add-student-form])
        handle-manage-students-click #(re-frame/dispatch [::state/open-manage-students-page school-id class-id])
        handle-add-teacher-click #(re-frame/dispatch [::state/open-add-teacher-form])
        handle-manage-teachers-click #(re-frame/dispatch [::state/open-manage-teachers-page school-id class-id])
        handle-manage-courses-click #(re-frame/dispatch [::state/open-assign-course-form])
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
        handle-edit-click #(re-frame/dispatch [::state/set-form-editable (not form-editable?)])
        handle-data-save #(re-frame/dispatch [::state/update-class-data %])]
    [page/side-bar {:title   "Class Info"
                    :actions [{:icon     "edit"
                               :variant  "light"
                               :on-click handle-edit-click}]}
     [class-edit-form {:class-id  class-id
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

(defn- side-bar-assign-course
  [{:keys [class-id school-id]}]
  (let [handle-cancel #(re-frame/dispatch [::state/open-class-form])]
    [page/side-bar {:title "Assign a Course"}
     ]))

(defn- side-bar
  [props]
  (let [side-bar-content @(re-frame/subscribe [::state/side-bar])]
    (case side-bar-content
      :class-form [side-bar-class-form props]
      :add-student [side-bar-add-student props]
      :add-teacher [side-bar-add-teacher props]
      :assign-course [side-bar-assign-course props]
      nil)))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn [props]
    [page/side-bar-page
     [page/main-content {:title "Class Profile"
                         :icon  "classes"}
      [class-counter props]
      [statistics]]
     [side-bar props]]))
