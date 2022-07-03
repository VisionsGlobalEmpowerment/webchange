(ns webchange.admin.pages.index
  (:require
    [webchange.admin.pages.accounts.views :as accounts]
    [webchange.admin.pages.account-add.views :as account-add]
    [webchange.admin.pages.account-edit.views :as account-edit]
    [webchange.admin.pages.account-my.views :as account-my]
    [webchange.admin.pages.activities.views :as activities]
    [webchange.admin.pages.activity-edit.views :as activity-edit]
    [webchange.admin.pages.books.views :as books]
    [webchange.admin.pages.book-edit.views :as book-edit]
    [webchange.admin.pages.class-add.views :as class-add]
    [webchange.admin.pages.class-profile.views :as class-profile]
    [webchange.admin.pages.classes.views :as classes]
    [webchange.admin.pages.course-add.views :as course-add]
    [webchange.admin.pages.course-edit.views :as course-edit]
    [webchange.admin.pages.course-view.views :as course-view]
    [webchange.admin.pages.courses.views :as courses]
    [webchange.admin.pages.dashboard.views :as dashboard]
    [webchange.admin.pages.not-found.views :as not-found]
    [webchange.admin.pages.password-reset.views :as password-reset]
    [webchange.admin.pages.school-profile.views :as school-profile]
    [webchange.admin.pages.school-courses.views :as school-courses]
    [webchange.admin.pages.schools.views :as schools]
    [webchange.admin.pages.schools-archived.views :as schools-archived]
    [webchange.admin.pages.student-add.views :as student-add]
    [webchange.admin.pages.student-edit.views :as student-edit]
    [webchange.admin.pages.student-profile.views :as student-profile]
    [webchange.admin.pages.students.views :as students]
    [webchange.admin.pages.school-add.views :as school-add]
    [webchange.admin.pages.teachers.views :as teachers]
    [webchange.admin.pages.teacher-add.views :as teacher-add]
    [webchange.admin.pages.teacher-profile.views :as teacher-profile]
    [webchange.admin.pages.class-students.views :as class-students]
    [webchange.admin.pages.class-students-add.views :as class-students-add]))

(def pages {:accounts           accounts/page
            :account-add        account-add/page
            :account-edit       account-edit/page
            :account-my         account-my/page
            :activities         activities/page
            :activity-edit      activity-edit/page
            :books              books/page
            :book-edit          book-edit/page
            :class-add          class-add/page
            :class-profile      class-profile/page
            :class-students     class-students/page
            :class-students-add class-students-add/page
            :classes            classes/page
            :course-add         course-add/page
            :course-edit        course-edit/page
            :course-profile     course-view/page
            :courses            courses/page
            :dashboard          dashboard/page
            :password-reset     password-reset/page
            :school-add         school-add/page
            :school-courses     school-courses/page
            :school-profile     school-profile/page
            :schools            schools/page
            :schools-archived   schools-archived/page
            :student-add        student-add/page
            :student-edit       student-edit/page
            :student-profile    student-profile/page
            :students           students/page
            :teacher-add        teacher-add/page
            :teacher-profile    teacher-profile/page
            :teachers           teachers/page
            :404                not-found/page})
