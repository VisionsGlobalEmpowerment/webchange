(ns webchange.dashboard.schools.views
  (:require
    [webchange.dashboard.schools.school-modal.views :as school-modal-views]
    [webchange.dashboard.schools.schools-list.views :as schools-list-views]
))

(def school-modal school-modal-views/school-modal)
(def school-delete-modal school-modal-views/school-delete-modal)
(def schools-list schools-list-views/schools-list-page)