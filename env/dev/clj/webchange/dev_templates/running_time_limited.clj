(ns webchange.dev-templates.running-time-limited
  (:require [webchange.dev-templates :as t]))

(comment
  (def course-slug "my-course-eng-kuvdibec")
  (def scene-slug "run-with-timer")

  ;; update activity
  (let [dialog-action (t/get-dialog-actions course-slug scene-slug)
        update-result (t/update-activity course-slug scene-slug :actions dialog-action)
        inspect-object :mari]
    (cond
      (some? inspect-object) (get-in update-result [:data :objects inspect-object])
      :else update-result)))
