(ns webchange.lesson-builder.widgets.audio-list.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.state :as state]
    [webchange.lesson-builder.stage-actions :as stage-actions]
    [webchange.lesson-builder.widgets.confirm.state :as confirm-state]
    [webchange.utils.list :as list-utils]
    [webchange.utils.scene-data :as activity-utils]))

(def path-to-db :lesson-builder/audio-list)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [on-audio-item-mode-change]}]]
    {:db (assoc db :on-audio-item-mode-change on-audio-item-mode-change)}))

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
    (let [item-modes (-> (get db item-mode-key)
                         (assoc item-url value))
          has-edit? (->> item-modes
                         vals
                         (some #{"edit"}))
          on-audio-item-mode-change (:on-audio-item-mode-change db)]
      (when (fn? on-audio-item-mode-change)
        (on-audio-item-mode-change has-edit?))
      {:db (assoc db item-mode-key item-modes)})))

(re-frame/reg-event-fx
  ::remove
  [(re-frame/inject-cofx :activity-data)]
  (fn [{:keys [activity-data]} [_ url]]
    (let [actions-use-audio (activity-utils/find-actions-paths activity-data #(= (:audio %) url))]
      (if (empty? actions-use-audio)
        {:dispatch [::confirm-state/show-confirm-window {:title      "Are you sure you want to delete this asset?"
                                                         :on-confirm [::stage-actions/remove-asset {:asset-url url}]}]}
        (let [actions (->> actions-use-audio
                           (group-by first)
                           (map (fn [[action-name]]
                                  (-> (activity-utils/get-scene-actions activity-data)
                                      (get action-name)
                                      (get :phrase-description (clojure.core/name action-name))))))]
          {:dispatch [::confirm-state/show-message-window {:title   "This asset can not be deleted. It is used in some actions:"
                                                           :message [:ul
                                                                     (for [action actions]
                                                                       ^{:key action}
                                                                       [:li action])]}]})))))

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
