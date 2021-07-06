(ns webchange.editor-v2.dialog.dialog-text-form.side-menu.main.voice-over.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.audio-analyzer.region-data :refer [get-region-data-if-possible]]
    [webchange.editor-v2.dialog.components.audio-assets.state :as audio-assets]
    [webchange.editor-v2.dialog.dialog-form.state.actions :as state-actions]
    [webchange.editor-v2.dialog.dialog-text-form.state :as state-dialog]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.state :as parent-state]
    [webchange.editor-v2.dialog.utils.dialog-action :refer [get-inner-action]]
    [webchange.editor-v2.translator.translator-form.state.scene :as state-scene]
    [webchange.state.warehouse-recognition :as recognition]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:voice-over])
       (parent-state/path-to-db)))

;; Current Audio

(defn- get-current-audio
  [db]
  (-> (parent-state/get-selected-action-data db)
      (get-inner-action)))

(re-frame/reg-sub
  ::current-audio
  (fn []
    [(re-frame/subscribe [::parent-state/selected-action-data])])
  (fn [[selected-action-data]]
    (get-inner-action selected-action-data)))

(re-frame/reg-event-fx
  ::set-current-audio
  (fn [{:keys [db]} [_ url]]
    (let [{:keys [path source]} (state-dialog/get-selected-action db)
          phrase-text (-> (get-current-audio db) (get :phrase-text ""))]
      {:dispatch-n [[::state-actions/update-inner-action-by-path {:action-path path
                                                                  :action-type source
                                                                  :data-patch  {:audio url}}]
                    [::recognition/get-audio-script-region
                     {:audio-url   url
                      :script-text phrase-text}
                     {:on-success [::audio-script-loaded {:action-path path
                                                          :action-type source}]}]]})))

(re-frame/reg-event-fx
  ::audio-script-loaded
  (fn [{:keys []} [_ {:keys [action-path action-type]} region-data]]
    {:dispatch [::state-actions/update-inner-action-by-path {:action-path action-path
                                                             :action-type action-type
                                                             :data-patch  region-data}]}))

(re-frame/reg-event-fx
  ::set-current-audio-region
  (fn [{:keys [db]} [_ region-data]]
    (let [{:keys [path source]} (state-dialog/get-selected-action db)]
      {:dispatch [::state-actions/update-inner-action-by-path {:action-path path
                                                               :action-type source
                                                               :data-patch  region-data}]})))

(re-frame/reg-event-fx
  ::recognition-retry
  (fn [{:keys [db]} [_]]
    (let [{:keys [audio phrase-text]} (get-current-audio db)]
      {:dispatch [::recognition/get-audio-script-region
                  {:audio-url   audio
                   :script-text phrase-text}
                  {:on-success [::recognition-retry-success]}]})))

(re-frame/reg-event-fx
  ::recognition-retry-success
  (fn [{:keys [db]} [_ region-data]]
    (let [{:keys [path source]} (state-dialog/get-selected-action db)]
      {:dispatch [::state-actions/update-inner-action-by-path {:action-path path
                                                               :action-type source
                                                               :data-patch  region-data}]})))

;; Audios List

(re-frame/reg-sub
  ::audios-list
  (fn []
    [(re-frame/subscribe [::audio-assets/audios-list])
     (re-frame/subscribe [::current-audio])])
  (fn [[audios-list {:keys [audio]}]]
    (->> audios-list
         (map (fn [{:keys [url] :as audio-asset}]
                (assoc audio-asset :selected? (= url audio)))))))

(re-frame/reg-event-fx
  ::set-audio-alias
  (fn [{:keys [_]} [_ url alias]]
    {:dispatch [::state-scene/update-asset-alias url alias]}))

(re-frame/reg-event-fx
  ::remove-audio
  (fn [{:keys [_]} [_ url]]
    {:dispatch [::state-scene/delete-asset url]}))

(re-frame/reg-event-fx
  ::bring-to-top
  (fn [{:keys [_]} [_ url]]
    {:dispatch [::state-scene/update-asset-date url (.now js/Date)]}))
