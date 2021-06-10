(ns webchange.editor-v2.dialog.phrase-voice-over.audios-menu.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.audio-analyzer :refer [get-region-data-if-possible]]
    [webchange.editor-v2.dialog.dialog-form.state.actions :as state-actions]
    [webchange.editor-v2.dialog.state :as parent-state]
    [webchange.editor-v2.dialog.utils.dialog-action :refer [get-inner-action]]
    [webchange.editor-v2.translator.translator-form.state.scene :as state-scene]
    [webchange.state.warehouse :as warehouse]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:audio-menu])
       (parent-state/path-to-db)))

(re-frame/reg-sub
  ::audios-script-match-status
  (fn [db [_ phrase-action-path]]
    (get-in db (path-to-db [phrase-action-path]))))

(re-frame/reg-event-fx
  ::set-audio-script-match-status
  (fn [{:keys [db]} [_ phrase-action-path audio-url status]]
    {:db (assoc-in db (path-to-db [phrase-action-path audio-url]) status)}))

(re-frame/reg-sub
  ::audios-list
  (fn [[_ action-data]]
    [(re-frame/subscribe [::state-scene/audio-assets])
     (re-frame/subscribe [::audios-script-match-status (get action-data :path)])])
  (fn [[audio-assets script-match-status] [_ action-data]]
    (let [current-audio-url (-> action-data (get-in [:node-data :data]) (get-inner-action) (get :audio))]
      (map (fn [{:keys [alias url]}]
             {:alias        (or alias url)
              :url          url
              :selected?    (= url current-audio-url)
              :match-status (get script-match-status url)})
           audio-assets))))

(re-frame/reg-event-fx
  ::audio-selected
  (fn [{:keys []} [_ {:keys [action-data url]}]]
    (let [phrase-text (-> (get-in action-data [:node-data :data]) (get-inner-action) (get :phrase-text ""))
          action-type (get action-data :type)
          action-path (get action-data :path)]
      {:dispatch-n [[::state-actions/update-inner-action-by-path {:action-path action-path
                                                                  :action-type action-type
                                                                  :data-patch  {:audio url}}]
                    [::set-audio-script-match-status action-path url :loading]
                    [::warehouse/load-audio-script-polled
                     {:file url}
                     {:on-success [::audio-script-loaded {:audio-url   url
                                                          :phrase-text phrase-text
                                                          :action-path action-path
                                                          :action-type action-type}]
                      :on-failure [::set-audio-script-match-status action-path url :mismatched]}]]})))

(re-frame/reg-event-fx
  ::audio-script-loaded
  (fn [{:keys []} [_ {:keys [audio-url phrase-text action-path action-type]} script-data]]
    (let [{:keys [matched? region-data]} (get-region-data-if-possible {:text   phrase-text
                                                                       :script script-data})]
      (if matched?
        {:dispatch-n [[::state-actions/update-inner-action-by-path {:action-path action-path
                                                                    :action-type action-type
                                                                    :data-patch  region-data}]
                      [::set-audio-script-match-status action-path audio-url :matched]]}
        {:dispatch [::set-audio-script-match-status action-path audio-url :mismatched]}))))
