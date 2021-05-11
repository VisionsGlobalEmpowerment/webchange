(ns webchange.game-changer.steps.create-activity.index
  (:require
    [webchange.game-changer.steps.create-activity.views :refer [create-activity get-activity-name get-language]]))

(def data {:title          "Name New Activity"
           :timeline-label "Name Activity"
           :component      create-activity
           :passed?    (fn [{:keys [data]}]
                         (and (-> data get-activity-name empty? not)
                              (-> data get-language empty? not)))})
