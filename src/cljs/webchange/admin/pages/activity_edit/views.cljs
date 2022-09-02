(ns webchange.admin.pages.activity-edit.views
  (:require
    [webchange.admin.pages.activity-edit.activity.views :as activity-views]
    [webchange.admin.pages.activity-edit.book.views :as book-views]))

(def activity activity-views/page)
(def book book-views/page)
