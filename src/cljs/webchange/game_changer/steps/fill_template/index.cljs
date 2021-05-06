(ns webchange.game-changer.steps.fill-template.index
  (:require
    [re-frame.core :as re-frame]
    [webchange.game-changer.steps.fill-template.state :as state]
    [webchange.game-changer.steps.fill-template.views :refer [template-form]]))

(def data {:title       "Add Content"
           :component   template-form
           :handle-next (fn [{:keys [data]}]
                          (re-frame/dispatch [::state/create-activity data]))})
