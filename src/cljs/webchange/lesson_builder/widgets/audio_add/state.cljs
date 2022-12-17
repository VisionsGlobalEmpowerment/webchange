(ns webchange.lesson-builder.widgets.audio-add.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.stage-actions :as stage-actions]
    [webchange.lesson-builder.state :as state]
    [webchange.state.warehouse :as warehouse]
    [webchange.utils.audio-recorder-state :as recorder]))

(def path-to-db :lesson-builder/audio-add)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; current panel

(def current-panel-key :current-panel)

(re-frame/reg-sub
  ::current-panel
  :<- [path-to-db]
  #(get % current-panel-key "index"))

(defn- set-current-panel
  [db value]
  (assoc db current-panel-key value))

;; loading?

(def loading-key :loading?)

(re-frame/reg-sub
  ::loading?
  :<- [path-to-db]
  #(get % loading-key false))

(defn- set-loading
  [db value]
  (assoc db loading-key value))

;; record panel

(def state-key :state)

(defn- update-state
  [db data-patch]
  (update db state-key merge data-patch))

(re-frame/reg-sub
  ::state
  :<- [path-to-db]
  #(get % state-key))

(re-frame/reg-event-fx
  ::start-recording
  (fn [{:keys [_]} [_]]
    {::recorder/start-recording [::start-recording-success]}))

(re-frame/reg-event-fx
  ::start-recording-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-current-panel db "recording")}))

(re-frame/reg-event-fx
  ::stop-recording
  (fn [{:keys [_]} [_]]
    {::recorder/stop-recording [::stop-recording-success]}))

(re-frame/reg-event-fx
  ::stop-recording-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ file]]
    (let [language (:current-language db)
          asset-data {:alias "New record"
                      :date  (.now js/Date)
                      :lang  language}]
      {:db       (set-loading db true)
       :dispatch [::warehouse/upload-file
                  {:file        file
                   :form-params [["lang" language]
                                 ["type" "blob"]
                                 ["blob-type" "audio"]]}
                  {:on-success [::upload-audio-success asset-data]}]})))

(re-frame/reg-event-fx
  ::upload-audio-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ asset-data response]]
    (let [asset-data (merge response asset-data)]
      {:db       (-> db
                     (set-loading false)
                     (set-current-panel "index"))
       :dispatch [::stage-actions/add-asset asset-data]})))

;; events

(re-frame/reg-event-fx
  ::add-audio-asset
  (fn [{:keys [_]} [_ data]]
    {:dispatch [::stage-actions/add-asset data]}))

;; language

(re-frame/reg-sub
  ::current-language
  :<- [path-to-db]
  :<- [::state/activity-info]
  (fn [[db activity-info]]
    (let [default (:lang activity-info)]
      (get db :current-language default))))

(re-frame/reg-event-fx
  ::select-language
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ language]]
    {:db (-> db
             (assoc :current-language language))}))
