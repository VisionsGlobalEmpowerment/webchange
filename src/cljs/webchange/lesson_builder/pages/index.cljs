(ns webchange.lesson-builder.pages.index
  (:require
    [webchange.lesson-builder.pages.create.views :as create]
    [webchange.lesson-builder.pages.create-activity.views :as create-activity]
    [webchange.lesson-builder.pages.create-book.views :as create-book]
    [webchange.lesson-builder.pages.not-found.views :as not-found]))

(def pages {:create create/page
            :create-activity create-activity/page
            :create-book create-book/page
            :404    not-found/page})
