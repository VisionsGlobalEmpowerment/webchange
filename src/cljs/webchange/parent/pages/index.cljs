(ns webchange.parent.pages.index
  (:require
    [webchange.parent.pages.not-found.views :as not-found]
    [webchange.parent.pages.student-add.views :as student-add]
    [webchange.parent.pages.students.views :as students]))

(def pages {:student-add student-add/page
            :students    students/page
            :404         not-found/page})
