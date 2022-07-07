(ns webchange.parent.pages.index
  (:require
    [webchange.parent.pages.dashboard.views :as dashboard]
    [webchange.parent.pages.not-found.views :as not-found]
    [webchange.parent.pages.students.views :as students]))

(def pages {:dashboard dashboard/page
            :students  students/page
            :404       not-found/page})