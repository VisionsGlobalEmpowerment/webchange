(ns webchange.parent.pages.index
  (:require
    [webchange.parent.pages.dashboard.views :as dashboard]
    [webchange.parent.pages.not-found.views :as not-found]))

(def pages {:dashboard dashboard/page
            :404       not-found/page})