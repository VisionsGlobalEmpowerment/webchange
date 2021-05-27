(ns webchange.game-changer.steps.select-background-music.index
  (:require
    [re-frame.core :as re-frame]
    [webchange.game-changer.steps.select-background-music.state :as state]
    [webchange.game-changer.steps.select-background-music.views :refer [select-background-music]]))

(def data {:title       "Select Music"
           :component   select-background-music
           :handle-next (fn [{:keys [data]}]
                          (re-frame/dispatch [::state/change-background-music data]))})
