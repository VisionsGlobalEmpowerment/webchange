(ns webchange.lesson-builder.widgets.action-audio-editor.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.stage-actions :as stage-actions]
    [webchange.lesson-builder.state :as state]
    [webchange.utils.scene-data :as utils]
    [webchange.utils.scene-action-data :as action-utils]))

(def path-to-db :widget/audio-editor)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; loading?

(def loading-key :loading?)

(defn- get-loading
  [db id]
  (get-in db [id loading-key] true))

(defn- set-loading
  [db id value]
  (assoc-in db [id loading-key] value))

(re-frame/reg-sub
  ::loading?
  :<- [path-to-db]
  (fn [db [_ id]]
    (get-loading db id)))

(re-frame/reg-event-fx
  ::set-loading
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ id value]]
    {:db (set-loading db id value)}))

;;

(defn get-action-data
  [activity-data action-path]
  (-> (utils/get-action activity-data action-path)
      (action-utils/get-inner-action)))

(re-frame/reg-sub
  ::action-data
  :<- [::state/activity-data]
  (fn [activity-data [_ action-path]]
    (get-action-data activity-data action-path)))

(re-frame/reg-sub
  ::audio-url
  (fn [[_ action-path]]
    (re-frame/subscribe [::action-data action-path]))
  (fn [action-data]
    (get action-data :audio)))

(re-frame/reg-sub
  ::audio-region
  (fn [[_ action-path]]
    (re-frame/subscribe [::action-data action-path]))
  (fn [action-data]
    (select-keys action-data [:start :end])))

;; volume

(def current-volume-key :volume)

(defn- get-current-volume
  [db id]
  (get-in db [id current-volume-key]))

(defn- set-current-volume
  [db id volume]
  (assoc-in db [id current-volume-key] volume))

(re-frame/reg-sub
  ::current-volume
  :<- [path-to-db]
  (fn [db [_ id]]
    (get-current-volume db id)))

(re-frame/reg-event-fx
  ::set-current-volume
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ id volume]]
    {:db (set-current-volume db id volume)}))

;; play button

(def play-button-key :play-button)

(defn- get-play-button
  [db id]
  (get-in db [id play-button-key] "stop"))

(defn- set-play-button
  [db id value]
  (assoc-in db [id play-button-key] value))

(re-frame/reg-sub
  ::play-button-state
  :<- [path-to-db]
  (fn [db [_ id]]
    (get-play-button db id)))

(defn- call-method
  [controls method & args]
  (when (some? controls)
    (let [handler (get @controls method)]
      (when (fn? handler)
        (apply handler args)))))

(re-frame/reg-event-fx
  ::start-playing
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ id wave]]
    (call-method wave :play)
    {:db (set-play-button db id "play")}))

(re-frame/reg-event-fx
  ::stop-playing
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ id wave]]
    (call-method wave :stop)
    {:db (set-play-button db id "stop")}))

(re-frame/reg-event-fx
  ::rewind-to-start
  (fn [{:keys [_]} [_ wave]]
    (call-method wave :rewind-to-start)
    {}))

(re-frame/reg-event-fx
  ::rewind-to-end
  (fn [{:keys [_]} [_ wave]]
    (call-method wave :rewind-to-end)
    {}))

(re-frame/reg-event-fx
  ::zoom-in
  (fn [{:keys [_]} [_ wave]]
    (call-method wave :zoom-in)
    {}))

(re-frame/reg-event-fx
  ::zoom-out
  (fn [{:keys [_]} [_ wave]]
    (call-method wave :zoom-out)
    {}))

;; events

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)
   (re-frame/inject-cofx :activity-data)]
  (fn [{:keys [activity-data db]} [_ id {:keys [action-path]}]]
    (let [volume (-> (get-action-data activity-data action-path)
                     (get :volume 1))]
      {:db (-> db (set-current-volume id volume))})))

(re-frame/reg-event-fx
  ::reset
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ id]]
    {:db (dissoc db id)}))

(re-frame/reg-event-fx
  ::set-action-volume
  (fn [{:keys [_]} [_ action-path value]]
    {:dispatch [::stage-actions/set-action-phrase-volume {:action-path action-path
                                                          :volume      value}]}))

(re-frame/reg-event-fx
  ::set-action-region
  (fn [{:keys [_]} [_ action-path value]]
    (print "::set-action-region" value)
    {:dispatch [::stage-actions/set-action-phrase-region {:action-path action-path
                                                          :region      value}]}))
