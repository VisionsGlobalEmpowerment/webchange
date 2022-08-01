(ns webchange.lesson-builder.widgets.audio-list.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.state :as state]
    [webchange.lesson-builder.stage-actions :as stage-actions]
    [webchange.utils.list :as list-utils]
    [webchange.utils.scene-data :as activity-utils]))

(def path-to-db :lesson-builder/audio-list)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

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

(def item-mode-key :item-mode)

(re-frame/reg-sub
  ::item-mode
  :<- [path-to-db]
  (fn [db [_ item-url]]
    (get-in db [item-mode-key item-url] "view")))

(re-frame/reg-event-fx
  ::set-item-mode
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ item-url value]]
    {:db (assoc-in db [item-mode-key item-url] value)}))

(re-frame/reg-event-fx
  ::bring-to-top
  (fn [{:keys [_]} [_ url]]
    {:dispatch [::stage-actions/update-asset {:asset-url  url
                                              :data-patch {:date (.now js/Date)}}]}))

(re-frame/reg-event-fx
  ::change-alias
  (fn [{:keys [_]} [_ url alias]]
    {:dispatch [::stage-actions/update-asset {:asset-url  url
                                              :data-patch {:alias alias}}]}))
