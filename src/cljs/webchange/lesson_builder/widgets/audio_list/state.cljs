(ns webchange.lesson-builder.widgets.audio-list.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.state :as state]
    [webchange.lesson-builder.stage-actions :as stage-actions]
    [webchange.utils.list :as list-utils]
    [webchange.utils.scene-data :as activity-utils]))

(re-frame/reg-sub
  ::available-audios
  :<- [::state/activity-data]
  (fn [activity-data]
    (->> (activity-utils/get-assets activity-data)
         (filter #(= (:type %) "audio"))
         (list-utils/distinct-by-key :url)
         (map (fn [{:keys [alias url] :as audio-asset}]
                (-> (select-keys audio-asset [:url :date])
                    (assoc :name (or alias url)))))
         (sort-by :date)
         (reverse))))



(re-frame/reg-event-fx
  ::bring-to-top
  (fn [{:keys [_]} [_ url]]
    {:dispatch [::stage-actions/update-asset {:asset-url  url
                                              :data-patch {:date (.now js/Date)}}]}))
