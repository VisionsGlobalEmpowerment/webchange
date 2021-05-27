(ns webchange.game-changer.steps.select-background.index
  (:require
    [re-frame.core :as re-frame]
    [webchange.game-changer.steps.select-background.state :as state]
    [webchange.game-changer.steps.select-background.views :refer [select-background]]
    [webchange.game-changer.steps.fill-template.template-options :refer [data->character-option]]))

(def data {:title       "Select Background"
           :component   select-background
           :handle-next (fn [{:keys [data callback]}]
                          (re-frame/dispatch [::state/change-background data callback])
                          )})
