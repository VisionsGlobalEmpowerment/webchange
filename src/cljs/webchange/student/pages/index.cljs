(ns webchange.student.pages.index
  (:require
    [webchange.student.pages.sign-in.views :as sign-in]))

(def pages
  {:sign-in sign-in/page})
