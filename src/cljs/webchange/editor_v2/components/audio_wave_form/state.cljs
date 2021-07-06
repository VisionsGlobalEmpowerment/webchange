(ns webchange.editor-v2.components.audio-wave-form.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.state :as parent-state]
    [webchange.state.warehouse-recognition :as recognition]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:waveform-data])
       (parent-state/path-to-db)))

(re-frame/reg-event-fx
  ::init-audio-script
  (fn [{:keys [db]} [_ file]]
    {:db       (assoc-in db (path-to-db [file :loading]) true)
     :dispatch [::recognition/get-audio-script-data
                {:audio-url file}
                {:on-success [::set-audio-script file]
                 :on-failure [::reset-script-loading file]}]}))

(re-frame/reg-event-fx
  ::set-audio-script
  (fn [{:keys [db]} [_ file data]]
    {:db (-> db
             (assoc-in (path-to-db [file :loading]) false)
             (assoc-in (path-to-db [file :data]) data))}))

(re-frame/reg-event-fx
  ::reset-script-loading
  (fn [{:keys [db]} [_ file]]
    {:db (-> db
             (assoc-in (path-to-db [file :loading]) false)
             (assoc-in (path-to-db [file :data]) nil))}))

(re-frame/reg-sub
  ::audio-script-data
  (fn [db [_ file]]
    (get-in db (path-to-db [file :data]))))

(re-frame/reg-sub
  ::audio-script-loading
  (fn [db [_ file]]
    (get-in db (path-to-db [file :loading]))))
