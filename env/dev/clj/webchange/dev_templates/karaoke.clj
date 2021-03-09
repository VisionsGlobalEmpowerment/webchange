(ns webchange.dev-templates.karaoke
  (:require [webchange.dev-templates :as t]))

(comment
  (def course-slug "ee-qq-hiclznei")
  (def scene-slug "ww")

  ;; update activity
  (let [dialog-action (t/get-dialog-actions course-slug scene-slug)
        update-result (t/update-activity course-slug scene-slug :actions dialog-action)]
    (get-in update-result [:data :assets])))
