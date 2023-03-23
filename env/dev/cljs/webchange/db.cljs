(ns webchange.db
  (:require
    [re-frame.core :as re-frame]))

(comment
  (-> @re-frame.db/app-db
      (get-in [:course-data :scene-list]))

  )
