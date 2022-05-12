(ns webchange.admin.pages.index
  (:require
    [webchange.admin.pages.add-class.views :as add-class]
    [webchange.admin.pages.class-profile.views :as class-profile]
    [webchange.admin.pages.classes.views :as classes]
    [webchange.admin.pages.dashboard.views :as dashboard]
    [webchange.admin.pages.login.views :as login]
    [webchange.admin.pages.not-found.views :as not-found]
    [webchange.admin.pages.school-profile.views :as school-profile]
    [webchange.admin.pages.school-courses.views :as school-courses]
    [webchange.admin.pages.schools.views :as schools]
    [webchange.admin.pages.schools-archived.views :as schools-archived]
    [webchange.admin.pages.students.views :as students]
    [webchange.admin.pages.add-school.views :as add-school]
    [webchange.admin.pages.teachers.views :as teachers]
    [webchange.admin.pages.add-teacher.views :as add-teacher]
    [webchange.admin.pages.teacher-profile.views :as teacher-profile]))

(def pages {:add-class        add-class/page
            :class-profile    class-profile/page
            :classes          classes/page
            :dashboard        dashboard/page
            :login            login/page
            :school-profile   school-profile/page
            :schools          schools/page
            :schools-archived schools-archived/page
            :students         students/page
            :add-school       add-school/page
            :school-courses   school-courses/page
            :teachers         teachers/page
            :add-teacher      add-teacher/page
            :teacher-profile  teacher-profile/page
            :404              not-found/page})
