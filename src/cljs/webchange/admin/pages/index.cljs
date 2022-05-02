(ns webchange.admin.pages.index
  (:require
    [webchange.admin.pages.add-class.views :as add-class]
    [webchange.admin.pages.class-profile.views :as class-profile]
    [webchange.admin.pages.classes.views :as classes]
    [webchange.admin.pages.dashboard.views :as dashboard]
    [webchange.admin.pages.login.views :as login]
    [webchange.admin.pages.not-found.views :as not-found]
    [webchange.admin.pages.school-profile.views :as school-profile]
    [webchange.admin.pages.schools.views :as schools]
    [webchange.admin.pages.students.views :as students]))

(def pages {:add-class      add-class/page
            :class-profile  class-profile/page
            :classes        classes/page
            :dashboard      dashboard/page
            :login          login/page
            :school-profile school-profile/page
            :schools        schools/page
            :students       students/page
            :404            not-found/page})
