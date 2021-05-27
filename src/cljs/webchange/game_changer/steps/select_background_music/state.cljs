(ns webchange.game-changer.steps.select-background-music.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.game-changer.steps.state-common :as state-common]
    [webchange.state.warehouse :as warehouse]
    [webchange.utils.scene-data :as scene-data-utils]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:game-changer])))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_ scene-data]]
    (let [value {:url (scene-data-utils/get-background-music-src scene-data)}]
      {:dispatch-n [[::set-initial-value value]
                    [::set-current-value value]]})))

;; Initial Value

(def initial-value-path (path-to-db [:initial-value]))

(defn- get-initial-value
  [db]
  (get-in db initial-value-path))

(re-frame/reg-event-fx
  ::set-initial-value
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db initial-value-path value)}))


;; Current Value

(def current-value-path (path-to-db [:current-value]))

(defn- get-current-value
  [db]
  (get-in db current-value-path))

(re-frame/reg-sub
  ::current-value
  get-current-value)

(re-frame/reg-event-fx
  ::set-current-value
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db current-value-path value)}))

(re-frame/reg-event-fx
  ::reset-current-value
  (fn [{:keys [db]}]
    {:db (assoc-in db current-value-path nil)}))

;; Available audios

(re-frame/reg-sub
  ::music-options
  (fn [_]
    [{:name "Map"
      :url  "/raw/audio/background/Map.mp3"}
     {:name "Casa"
      :url  "/raw/audio/background/Casa.mp3"}
     {:name "Parque"
      :url  "/raw/audio/background/Parque.mp3"}]))

;; Save

(re-frame/reg-event-fx
  ::change-background-music
  (fn [{:keys [db]} [_ data]]
    (let [{:keys [course-slug scene-slug]} (:activity @data)

          initial-value (get-initial-value db)
          current-value (get-current-value db)]
      (if-not (= initial-value current-value)
        (let [scene-data (:scene-data @data)
              music-url (:url current-value)]
          {:dispatch [::warehouse/save-scene
                      {:course-slug course-slug
                       :scene-slug  scene-slug
                       :scene-data  (scene-data-utils/set-background-music scene-data music-url)}
                      {:on-success [::state-common/redirect-to-editor {:course-slug course-slug
                                                                       :scene-slug  scene-slug}]}]})
        {:dispatch [::state-common/redirect-to-editor {:course-slug course-slug
                                                       :scene-slug  scene-slug}]}))))
