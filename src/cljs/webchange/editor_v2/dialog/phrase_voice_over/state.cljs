(ns webchange.editor-v2.dialog.phrase-voice-over.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.dialog.dialog-form.state.actions :as state-actions]
    [webchange.editor-v2.dialog.state :as parent-state]
    [webchange.editor-v2.dialog.utils.dialog-action :refer [get-inner-action]]
    [webchange.editor-v2.translator.translator-form.state.scene :as state-scene]))

(defn- action-data->phrase-data [action-data] (-> action-data (get-in [:node-data :data]) (get-inner-action)))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:phrase-voice-over])
       (parent-state/path-to-db)))

;; Modal state

(def modal-state-path (path-to-db [:modal-state]))

(re-frame/reg-sub
  ::modal-open?
  (fn [db]
    (get-in db modal-state-path false)))

(re-frame/reg-event-fx
  ::open-modal
  (fn [{:keys [db]} [_ action-data]]
    {:db       (assoc-in db modal-state-path true)
     :dispatch [::init action-data]}))

(re-frame/reg-event-fx
  ::close-modal
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db modal-state-path false)}))

;; Init

(re-frame/reg-event-fx
  ::init
  (fn [{:keys []} [_ action-data]]
    {:dispatch-n [[::set-current-phrase-action-data action-data]]}))

;; Action Data

(def current-phrase-action-data (path-to-db [:current-phrase-action-data]))

(defn- get-current-phrase-action-data
  [db]
  (get-in db current-phrase-action-data))

(re-frame/reg-sub
  ::current-phrase-action-data
  get-current-phrase-action-data)

(re-frame/reg-event-fx
  ::set-current-phrase-action-data
  (fn [{:keys [db]} [_ action-data]]
    {:db (assoc-in db current-phrase-action-data action-data)}))

;; Audio Data

(def audio-script-path (path-to-db [:audio-script]))

(re-frame/reg-sub
  ::audio-data
  (fn []
    [(re-frame/subscribe [::current-phrase-action-data])
     (re-frame/subscribe [::state-scene/audio-assets])])
  (fn [[phrase-action-data audio-assets]]
    (let [phrase-data (-> phrase-action-data (get-in [:node-data :data]) (get-inner-action))
          url (:audio phrase-data)
          audio-asset (some (fn [asset]
                              (and (= (:url asset) url)
                                   asset))
                            audio-assets)]
      {:url       url
       :start     (:start phrase-data)
       :end       (:end phrase-data)
       :file-name (or (:alias audio-asset) url)})))

(re-frame/reg-event-fx
  ::update-audio-region
  (fn [{:keys [db]} [_ region-data]]
    (let [{:keys [path source]} (get-current-phrase-action-data db)]
      {:dispatch [::state-actions/update-inner-action-by-path {:action-path path
                                                               :action-type source
                                                               :data-patch  region-data}]})))
