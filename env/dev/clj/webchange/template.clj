(ns webchange.template
  (:require [webchange.tts :as tts]
            [webchange.dev-templates :as dev-templates]))

(defn update-template
  [course-slug scene-slug]
  (dev-templates/update-activity course-slug scene-slug)
  (tts/process-audio course-slug scene-slug))