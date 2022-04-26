(ns webchange.admin.pages.index
  (:require
    [webchange.admin.pages.class-profile.views :as class-profile]
    [webchange.admin.pages.dashboard.views :as dashboard]
    [webchange.admin.pages.login.views :as login]
    [webchange.admin.pages.not-found.views :as not-found]
    [webchange.admin.pages.school-profile.views :as school-profile]
    [webchange.admin.pages.schools.views :as schools]
    [webchange.admin.pages.students.views :as students]
    [webchange.admin.pages.school-profile.views :as school-profile]
    [webchange.admin.pages.new-school.views :as new-school]))

(def pages {:class-profile  class-profile/page
            :dashboard      dashboard/page
            :login          login/page
            :school-profile school-profile/page
            :schools        schools/page
            :students       students/page
            :new-school     new-school/page
            :404            not-found/page})
