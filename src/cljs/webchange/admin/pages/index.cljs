(ns webchange.admin.pages.index
  (:require
    [webchange.admin.pages.dashboard.views :as dashboard]
    [webchange.admin.pages.not-found.views :as not-found]
    [webchange.admin.pages.schools.views :as schools]
    [webchange.admin.pages.school-profile.views :as school-profile]))

(def pages {:dashboard      dashboard/page
            :schools        schools/page
            :school-profile school-profile/page
            :404            not-found/page})
