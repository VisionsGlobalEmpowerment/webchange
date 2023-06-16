(ns webchange.admin.pages.class-profile.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.class-profile.course-assign.views :refer [assign-class-course]]
    [webchange.admin.pages.class-profile.state :as state]
    [webchange.admin.pages.class-profile.students-add.views :refer [class-students-add]]
    [webchange.admin.pages.class-profile.students-list.views :refer [class-students-list]]
    [webchange.admin.pages.class-profile.teacher-edit.views :refer [class-teacher-edit]]
    [webchange.admin.pages.class-profile.teachers-add.views :refer [class-teachers-add]]
    [webchange.admin.pages.class-profile.teachers-list.views :refer [class-teachers-list]]
    [webchange.admin.widgets.class-form.views :refer [class-edit-form]]
    [webchange.admin.widgets.page.counter.views :refer [counter]]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui.index :as ui]))
(def add-button-props {:color      "blue-1"
                       :chip       "plus"
                       :chip-color "yellow-1"})

(defn- readonly-counter
  [students teachers class-course]
  [counter {:data [{:text    "Teachers"
                    :icon    "teachers"
                    :counter teachers
                    :actions [{:text     "Manage Teachers"
                               :on-click  #(re-frame/dispatch [::state/open-teachers-list])}
                              {:text     "Add Teacher"
                               :disabled? true
                               :on-click #(re-frame/dispatch [::state/open-add-teacher-form])}]}
                   {:text    "Students"
                    :icon    "students"
                    :counter students
                    :actions [{:text     "Manage Students"
                               :on-click  #(re-frame/dispatch [::state/open-students-list])}
                              {:text     "Add Student"
                               :disabled? true
                               :on-click #(re-frame/dispatch [::state/open-add-student-form])}]}
                   {:text       "Students Activities"
                    :icon       "games"
                    :counter    students
                    :background "blue-2"
                    :actions    [{:text     "Students Activities"
                                  :on-click #(re-frame/dispatch [::state/open-students-activities])}]}
                   {:text       (:name class-course)
                    :icon       "courses"
                    :background "green-2"
                    :actions    [{:text     "Manage Course"
                                  :disabled? true
                                  :on-click #(re-frame/dispatch [::state/open-assign-course-form])}]}]}])

(defn- personal-counter
  [students teachers class-course]
  [counter {:data [{:text    "Teachers"
                    :icon    "teachers"
                    :counter teachers
                    :actions [{:text     "Manage Teachers"
                               :disabled? true
                               :on-click  #(re-frame/dispatch [::state/open-teachers-list])}
                              {:text     "Add Teacher"
                               :disabled? true
                               :on-click #(re-frame/dispatch [::state/open-add-teacher-form])}]}
                   {:text    "Students"
                    :icon    "students"
                    :counter students
                    :actions [{:text     "Manage Students"
                               :on-click  #(re-frame/dispatch [::state/open-students-list])}
                              (merge add-button-props
                                     {:text     "Add Student"
                                      :on-click #(re-frame/dispatch [::state/open-add-student-form])})]}
                   {:text       "Students Activities"
                    :icon       "games"
                    :counter    students
                    :background "blue-2"
                    :actions    [{:text     "Students Activities"
                                  :on-click #(re-frame/dispatch [::state/open-students-activities])}]}
                   {:text       (:name class-course)
                    :icon       "courses"
                    :background "green-2"
                    :actions [{:text     "Manage Course"
                               :on-click #(re-frame/dispatch [::state/open-assign-course-form])}]}]}])

(defn- default-counter
  [students teachers class-course]
  [counter {:data [{:text    "Teachers"
                    :icon    "teachers"
                    :counter teachers
                    :actions [{:text     "Manage Teachers"
                               :on-click  #(re-frame/dispatch [::state/open-teachers-list])}
                              (merge add-button-props
                                     {:text     "Add Teacher"
                                      :on-click #(re-frame/dispatch [::state/open-add-teacher-form])})]}
                   {:text    "Students"
                    :icon    "students"
                    :counter students
                    :actions [{:text     "Manage Students"
                               :on-click  #(re-frame/dispatch [::state/open-students-list])}
                              (merge add-button-props
                                     {:text     "Add Student"
                                      :on-click #(re-frame/dispatch [::state/open-add-student-form])})]}
                   {:text       "Students Activities"
                    :icon       "games"
                    :counter    students
                    :background "blue-2"
                    :actions    [{:text     "Students Activities"
                                  :on-click #(re-frame/dispatch [::state/open-students-activities])}]}
                   {:text       (:name class-course)
                    :icon       "courses"
                    :background "green-2"
                    :actions    [{:text     "Manage Course"
                                  :on-click #(re-frame/dispatch [::state/open-assign-course-form])}]}]}])

(defn- class-counter
  []
  (let [{:keys [students teachers]} @(re-frame/subscribe [::state/class-stats])
        class-course @(re-frame/subscribe [::state/class-course])
        readonly? @(re-frame/subscribe [::state/readonly?])
        personal? @(re-frame/subscribe [::state/personal?])]
    (cond
      readonly? [readonly-counter students teachers class-course]
      personal? [personal-counter students teachers class-course]
      :else [default-counter students teachers class-course])))

(defn- statistics
  []
  (let [{:keys [activities-played books-read time-spent]} @(re-frame/subscribe [::state/class-activities-stats])]
    [page/block {:title "Statistics"
                 :icon  "statistics"
                 :class-name "statistics-block"
                 :class-name-content "statistics-block-content"}
     [ui/card {:text            "Activities Played"
               :icon            "games"
               :icon-background "yellow-2"
               :counter         activities-played
               :background      "transparent"}]
     [ui/card {:text            "Books Read"
               :icon            "book"
               :icon-background "yellow-2"
               :counter         books-read
               :background      "transparent"}]
     [ui/card {:text            "Time Spent"
               :icon            "students"
               :icon-background "yellow-2"
               :counter         time-spent
               :background      "transparent"}]]))

(defn- side-bar-class-form
  [{:keys [class-id school-id]}]
  (let [form-editable? @(re-frame/subscribe [::state/form-editable?])
        readonly? @(re-frame/subscribe [::state/readonly?])
        handle-edit-click #(re-frame/dispatch [::state/set-form-editable true])
        handle-cancel-click #(re-frame/dispatch [::state/handle-class-edit-cancel])
        handle-delete-click #(re-frame/dispatch [::state/handle-class-deleted])
        handle-data-save #(re-frame/dispatch [::state/update-class-data %])]
    [page/side-bar {:title    "Class Info"
                    :icon     "info"
                    :focused? form-editable?
                    :actions  (when-not readonly?
                                (cond-> []
                                        form-editable? (conj {:icon     "close"
                                                              :on-click handle-cancel-click})
                                        (not form-editable?) (conj {:icon     "edit"
                                                                    :on-click handle-edit-click})))}
     [class-edit-form {:class-id  class-id
                       :school-id school-id
                       :editable? form-editable?
                       :on-save   handle-data-save
                       :on-cancel handle-cancel-click
                       :on-delete handle-delete-click}]]))

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
    [page/page {:class-name "page--class-profile"}
     [page/content {:title "Class Profile"
                    :icon  "classes"}
      [class-counter]
      [statistics]]
     [side-bar props]]))
