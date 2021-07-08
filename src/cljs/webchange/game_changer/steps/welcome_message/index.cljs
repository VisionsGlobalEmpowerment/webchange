(ns webchange.game-changer.steps.welcome-message.index
  (:require
   [webchange.game-changer.steps.welcome-message.views :refer [welcome-form]]))

(def data {:title          "Welcome"
           :timeline-label "Welcome"
           :component      welcome-form
           :passed?        {:keys [data]}})