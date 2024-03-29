(ns webchange.admin.pages.index
  (:require
    [webchange.admin.pages.accounts.views :as accounts]
    [webchange.admin.pages.account-add.views :as account-add]
    [webchange.admin.pages.account-edit.views :as account-edit]
    [webchange.admin.pages.account-my.views :as account-my]
    [webchange.admin.pages.activities.views :as activities]
    [webchange.admin.pages.activity-edit.views :as activity-edit]
    [webchange.admin.pages.books.views :as books]
    [webchange.admin.pages.class-add.views :as class-add]
    [webchange.admin.pages.class-profile.views :as class-profile]
    [webchange.admin.pages.classes.views :as classes]
    [webchange.admin.pages.course-add.views :as course-add]
    [webchange.admin.pages.course-edit.views :as course-edit]
    [webchange.admin.pages.courses.views :as courses]
    [webchange.admin.pages.create.views :as create]
    [webchange.admin.pages.create-activity.views :as create-activity]
    [webchange.admin.pages.create-book.views :as create-book]
    [webchange.admin.pages.dashboard.views :as dashboard]
    [webchange.admin.pages.lesson-builder.views :as lesson-builder]
    [webchange.admin.pages.not-found.views :as not-found]
    [webchange.admin.pages.password-reset.views :as password-reset]
    [webchange.admin.pages.school-profile.views :as school-profile]
    [webchange.admin.pages.school-courses.views :as school-courses]
    [webchange.admin.pages.schools.views :as schools]
    [webchange.admin.pages.schools-archived.views :as schools-archived]
    [webchange.admin.pages.student-add.views :as student-add]
    [webchange.admin.pages.student-profile.views :as student-profile]
    [webchange.admin.pages.student-view.views :as student-view]
    [webchange.admin.pages.students.views :as students]
    [webchange.admin.pages.school-add.views :as school-add]
    [webchange.admin.pages.teachers.views :as teachers]
    [webchange.admin.pages.teacher-add.views :as teacher-add]
    [webchange.admin.pages.teacher-transfer.views :as teacher-transfer]
    [webchange.admin.pages.teacher-school.views :as teacher-school]
    [webchange.admin.pages.teacher-profile.views :as teacher-profile]
    [webchange.admin.pages.class-students.views :as class-students]
    [webchange.admin.pages.class-students-add.views :as class-students-add]
    [webchange.admin.pages.update-status.views :as update-status]))

(def pages {:accounts           accounts/page
            :account-add        account-add/page
            :account-edit       account-edit/page
            :account-my         account-my/page
            :activities         activities/page
            :activity-edit      activity-edit/activity
            :books              books/page
            :book-edit          activity-edit/book
            :class-add          class-add/page
            :class-profile      class-profile/page
            :class-students     class-students/page
            :class-students-add class-students-add/page
            :classes            classes/page
            :course-add         course-add/page
            :course-edit        course-edit/page
            :courses            courses/page
            :create             create/page
            :create-activity    create-activity/page
            :create-book        create-book/page
            :dashboard          dashboard/page
            :lesson-builder     lesson-builder/page
            :password-reset     password-reset/page
            :school-add         school-add/page
            :school-courses     school-courses/page
            :school-profile     school-profile/page
            :schools            schools/page
            :schools-archived   schools-archived/page
            :student-add        student-add/page
            :student-profile    student-profile/page
            :student-view       student-view/page
            :students           students/page
            :teacher-add        teacher-add/page
            :teacher-profile    teacher-profile/page
            :teacher-transfer   teacher-transfer/page
            :teacher-school     teacher-school/page
            :teachers           teachers/page
            :update-status      update-status/page
            :404                not-found/page})
