(ns webchange.game-changer.steps.fill-template.index
  (:require
    [re-frame.core :as re-frame]
    [webchange.game-changer.steps.fill-template.state :as state]
    [webchange.game-changer.steps.fill-template.views :refer [template-form]]))

(def data {:title       "Add Content"
           :component   template-form
           :passed?     (fn [{:keys [data]}]
                          (get-in data [:options :valid?]))
           :handle-next (fn [{:keys [data callback]}]
                          (re-frame/dispatch [::state/create-activity data callback]))})
