(ns webchange.lesson-builder.tools.select-view.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.select-view.state :as state]
    [webchange.lesson-builder.components.options-list.views :refer [options-list]]))

(defn select-view
  []
  (let [available-views @(re-frame/subscribe [::state/available-views])]
    [:div.widget--select-view
     [options-list {:items (->> available-views
                                (map (fn [[id view]]
                                       {:id       id
                                        :text     (:name view)
                                        :icon     "play"
                                        :on-click #(re-frame/dispatch [::state/select-view id])})))}]]))
