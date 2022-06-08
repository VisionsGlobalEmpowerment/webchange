(ns webchange.admin.pages.index
  (:require
    [webchange.admin.pages.accounts.views :as accounts]
    [webchange.admin.pages.account-add.views :as account-add]
    [webchange.admin.pages.account-edit.views :as account-edit]
    [webchange.admin.pages.account-my.views :as account-my]
    [webchange.admin.pages.activities.views :as activities]
    [webchange.admin.pages.activity-edit.views :as activity-edit]
    [webchange.admin.pages.add-class.views :as add-class]
    [webchange.admin.pages.class-profile.views :as class-profile]
    [webchange.admin.pages.classes.views :as classes]
    [webchange.admin.pages.course-edit.views :as course-edit]
    [webchange.admin.pages.courses.views :as courses]
    [webchange.admin.pages.dashboard.views :as dashboard]
    [webchange.admin.pages.not-found.views :as not-found]
    [webchange.admin.pages.password-reset.views :as password-reset]
    [webchange.admin.pages.school-profile.views :as school-profile]
    [webchange.admin.pages.school-courses.views :as school-courses]
    [webchange.admin.pages.schools.views :as schools]
    [webchange.admin.pages.schools-archived.views :as schools-archived]
    [webchange.admin.pages.students.views :as students]
    [webchange.admin.pages.student-profile.views :as student-profile]
    [webchange.admin.pages.add-student.views :as add-student]
    [webchange.admin.pages.add-school.views :as add-school]
    [webchange.admin.pages.teachers.views :as teachers]
    [webchange.admin.pages.add-teacher.views :as add-teacher]
    [webchange.admin.pages.teacher-profile.views :as teacher-profile]
    [webchange.admin.pages.class-students.views :as class-students]))

(def pages {:accounts         accounts/page
            :account-add      account-add/page
            :account-edit     account-edit/page
            :account-my       account-my/page
            :activities       activities/page
            :activity-edit    activity-edit/page
            :add-class        add-class/page
            :add-school       add-school/page
            :add-student      add-student/page
            :add-teacher      add-teacher/page
            :class-profile    class-profile/page
            :class-students   class-students/page
            :classes          classes/page
            :course-edit      course-edit/page
            :courses          courses/page
            :dashboard        dashboard/page
            :password-reset   password-reset/page
            :school-courses   school-courses/page
            :school-profile   school-profile/page
            :schools          schools/page
            :schools-archived schools-archived/page
            :student-profile  student-profile/page
            :students         students/page
            :teacher-profile  teacher-profile/page
            :teachers         teachers/page
            :404              not-found/page})
