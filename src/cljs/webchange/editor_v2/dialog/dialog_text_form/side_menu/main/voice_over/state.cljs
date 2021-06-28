(ns webchange.editor-v2.dialog.dialog-text-form.side-menu.main.voice-over.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.audio-analyzer :refer [get-region-data-if-possible]]
    [webchange.editor-v2.dialog.dialog-form.state.actions :as state-actions]
    [webchange.editor-v2.dialog.dialog-text-form.state :as state-dialog]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.state :as parent-state]
    [webchange.editor-v2.dialog.utils.dialog-action :refer [get-inner-action]]
    [webchange.editor-v2.translator.translator-form.state.scene :as state-scene]
    [webchange.state.warehouse :as warehouse]))

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
                    [::warehouse/load-audio-script-polled
                     {:file url}
                     {:on-success [::audio-script-loaded {:phrase-text phrase-text
                                                          :action-path path
                                                          :action-type source}]}]]})))

(re-frame/reg-event-fx
  ::audio-script-loaded
  (fn [{:keys []} [_ {:keys [phrase-text action-path action-type]} script-data]]
    (let [{:keys [matched? region-data]} (get-region-data-if-possible {:text   phrase-text
                                                                       :script script-data})]
      (if matched?
        {:dispatch [::state-actions/update-inner-action-by-path {:action-path action-path
                                                                 :action-type action-type
                                                                 :data-patch  region-data}]}
        {}))))

(re-frame/reg-event-fx
  ::set-current-audio-region
  (fn [{:keys [db]} [_ region-data]]
    (let [{:keys [path source]} (state-dialog/get-selected-action db)]
      {:dispatch [::state-actions/update-inner-action-by-path {:action-path path
                                                               :action-type source
                                                               :data-patch  region-data}]})))

;; Audios List

(re-frame/reg-sub
  ::audios-list
  (fn []
    [(re-frame/subscribe [::state-scene/audio-assets])
     (re-frame/subscribe [::current-audio])])
  (fn [[audio-assets {:keys [audio]}]]
    (map (fn [{:keys [alias url]}]
           {:alias     (or alias url)
            :url       url
            :selected? (= url audio)})
         audio-assets)))
