(ns webchange.lesson-builder.tools.background-music.state
  (:require
    [clojure.string :as str]
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.state :as state]
    [webchange.lesson-builder.layout.stage.state :as stage-state]
    [webchange.state.warehouse :as warehouse]))

(def default-volume 0.5)
(def path-to-db :lesson-builder/background-music)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(re-frame/reg-event-fx
  ::init
  [(re-frame/inject-cofx :activity-data)
   (i/path path-to-db)]
  (fn [{:keys [db activity-data]} [_]]
    (let [action-name (get-in activity-data [:triggers :music :action])
          volume (get-in activity-data [:actions (keyword action-name) :volume] default-volume)
          src (get-in activity-data [:actions (keyword action-name) :id])]
      {:db (assoc db
             :src src
             :volume volume)})))

(re-frame/reg-sub
  ::volume
  :<- [path-to-db]
  (fn [db]
    (get db :volume)))

(re-frame/reg-sub
  ::src
  :<- [path-to-db]
  (fn [db]
    (get db :src)))

(re-frame/reg-sub
  ::music-options
  (fn [_]
    [{:name "Map"
      :url  "/raw/audio/background/Map.mp3"}
     {:name "Casa"
      :url  "/raw/audio/background/Casa.mp3"}
     {:name "Parque"
      :url  "/raw/audio/background/Parque.mp3"}]))

(re-frame/reg-sub
  ::uploading?
  :<- [path-to-db]
  (fn [db]
    (get db :uploading?)))

(re-frame/reg-sub
  ::audio-name
  :<- [::src]
  (fn [src]
    (if src
      (as-> src n
            (str/split n "/")
            (last n)
            (str/split n ".")
            (drop-last n)
            (apply str n))
      "No File")))

(re-frame/reg-event-fx
  ::set-volume
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc db :volume value)}))

(re-frame/reg-event-fx
  ::set-src
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc db :src value)}))

(re-frame/reg-event-fx
  ::remove-src
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (dissoc db :src)}))

(re-frame/reg-event-fx
  ::upload-music
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ file]]
    {:db (assoc db :uploading? true)
     :dispatch [::warehouse/upload-file
                {:file        file
                 :form-params [["type" "audio"]]}
                {:on-success [::upload-success]
                 :on-failure [::upload-failure]}]}))

(re-frame/reg-event-fx
  ::upload-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [url]}]]
    {:db (-> db
             (assoc :uploading false)
             (assoc :src url))}))

(re-frame/reg-event-fx
  ::upload-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (assoc db :uploading false)}))

(re-frame/reg-event-fx
  ::apply
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [src (:src db)
          volume (:volume db)]
      {:dispatch [::state/update-background-music {:src src
                                                   :volume volume
                                                   :on-success [::stage-state/reset]}]})))
