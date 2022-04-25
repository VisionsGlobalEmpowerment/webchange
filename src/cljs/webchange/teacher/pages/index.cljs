(ns webchange.teacher.pages.index
  (:require
    [webchange.teacher.pages.classes.views :as classes]
    [webchange.teacher.pages.dashboard.views :as dashboard]
    [webchange.teacher.pages.not-found.views :as not-found]))

(def pages {:classes   classes/page
            :dashboard dashboard/page
            :404       not-found/page})
