(ns webchange.dev-templates.karaoke
  (:require [webchange.dev-templates :as templates]))

(comment
  (def course-slug "ee-qq-hiclznei")
  (def scene-slug "ww")

  ;; update activity
  (let [dialog-action (templates/get-dialog-actions course-slug scene-slug)
        update-result (templates/update-activity course-slug scene-slug :actions dialog-action)]
    (get-in update-result [:data :assets])))
