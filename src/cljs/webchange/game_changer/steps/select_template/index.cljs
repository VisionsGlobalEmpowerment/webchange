(ns webchange.game-changer.steps.select-template.index
  (:require
    [webchange.game-changer.steps.select_template.views-list :refer [templates-list]]))

(def data {:title          "Choose from a variety of activities..."
           :timeline-label "Choose Activity"
           :component      templates-list
           :passed?        (fn [{:keys [data]}]
                             (some? (get-in data [:template :id])))})
