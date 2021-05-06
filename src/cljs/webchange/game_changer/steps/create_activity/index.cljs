(ns webchange.game-changer.steps.create-activity.index
  (:require
    [webchange.game-changer.steps.create-activity.views :refer [create-activity]]))

(def data {:title          "Name New Activity"
           :timeline-label "Name Activity"
           :component      create-activity})
