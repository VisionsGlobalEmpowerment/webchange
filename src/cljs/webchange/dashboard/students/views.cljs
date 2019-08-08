(ns webchange.dashboard.students.views
  (:require
    [webchange.dashboard.students.student-modal.views :as student-modal-views]
    [webchange.dashboard.students.student-profile.views :as student-profile-views]
    [webchange.dashboard.students.students-list.views :as students-list-views]
    [webchange.dashboard.students.students-menu.views :as students-menu-views]))

(def student-modal student-modal-views/student-modal)
(def student-delete-modal student-modal-views/student-delete-modal)
(def student-profile student-profile-views/student-profile-page)
(def students-list students-list-views/students-list-page)
(def students-menu students-menu-views/students-menu)
