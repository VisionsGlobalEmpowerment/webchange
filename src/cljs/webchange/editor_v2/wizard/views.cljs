(ns webchange.editor-v2.wizard.views
  (:require
    [webchange.editor-v2.wizard.views-activity :as activity]
    [webchange.editor-v2.wizard.views-course :as course]
    [webchange.editor-v2.wizard.views-game-changer :as game-changer]
    [webchange.editor-v2.wizard.views-wizard :as wizard]))

(def create-activity-panel activity/create-activity-panel)
(def create-course-panel course/create-course-panel)
(def game-changer-panel game-changer/game-changer-panel)
(def wizard wizard/wizard)
