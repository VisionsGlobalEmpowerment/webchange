(ns webchange.game-changer.steps.welcome-message.index
  (:require
   [webchange.game-changer.steps.welcome-message.views :refer [welcome-form]]))

(def data {:step-id        1
           :title          "Welcome"
           :timeline-label "Welcome"
           :component      welcome-form
           :passed?        {:keys [data]}})
