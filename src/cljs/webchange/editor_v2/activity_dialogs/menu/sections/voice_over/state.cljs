(ns webchange.editor-v2.activity-dialogs.menu.sections.voice-over.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.dialog.components.audio-assets.state :as audio-assets]
    [webchange.editor-v2.dialog.dialog-form.state.actions :as state-actions]
    [webchange.editor-v2.activity-dialogs.form.state :as state-dialog]
    [webchange.editor-v2.activity-dialogs.menu.state :as parent-state]
    [webchange.editor-v2.dialog.utils.dialog-action :refer [get-inner-action]]
    [webchange.editor-v2.translator.translator-form.state.scene :as state-scene]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:voice-over])
       (parent-state/path-to-db)))

;; Current Audio

(defn get-current-inner-action
  [db]
  (-> (parent-state/get-selected-action-data db)
      (get-inner-action)))

(re-frame/reg-sub
  ::current-inner-action
  (fn []
    [(re-frame/subscribe [::parent-state/selected-action-data])])
  (fn [[selected-action-data]]
    (get-inner-action selected-action-data)))

(re-frame/reg-event-fx
  ::set-current-audio
  (fn [{:keys [db]} [_ url]]
    (let [{:keys [path source]} (state-dialog/get-selected-action db)]
      {:dispatch-n [[::state-actions/update-inner-action-by-path {:action-path path
                                                                  :action-type source
                                                                  :data-patch  {:audio url}}]]})))

;; Audios List

(re-frame/reg-sub
  ::audios-list
  (fn []
    [(re-frame/subscribe [::audio-assets/audios-list])
     (re-frame/subscribe [::current-inner-action])])
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
