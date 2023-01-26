(ns webchange.lesson-builder.tools.voice-translate.transcription.state
  (:require
    [clojure.string :as s]
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :lesson-builder/voice-translate-transcription)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(defn- selected-region
  [db]
  (let [id (:selected-region db)]
    (->> db
         :regions
         (filter #(= (:id %) id))
         first)))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [_db]} [_]]
    {}))

(re-frame/reg-event-fx
  ::reset
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (dissoc db :window-state :audio-url :script :regions)}))

(re-frame/reg-sub
  ::window-state
  :<- [path-to-db]
  (fn [db]
    (get db :window-state {:open? false :in-progress false})))

(re-frame/reg-sub
  ::play-button-state
  :<- [path-to-db]
  (fn [db]
    (get db :play-button-state "stop")))

(re-frame/reg-sub
  ::audio-url
  :<- [path-to-db]
  (fn [db]
    (get db :audio-url)))

(defn- regions->script
  [regions]
  (->> regions
       (map (fn [{:keys [start end data]}]
              {:start start
               :end end
               :word (get data "word" "")}))
       (sort-by :start)))

(re-frame/reg-sub
  ::script-data
  :<- [path-to-db]
  (fn [db]
    (-> db
        (get :regions)
        (regions->script))))

(re-frame/reg-event-fx
  ::open-edit-window
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ url]]
    {:db (assoc db
                :window-state {:open? true}
                :audio-url url)
     :dispatch [::load-audio-script-once url]}))

(re-frame/reg-event-fx
  ::close-window
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (dissoc db :window-state :audio-url :script)}))

(re-frame/reg-event-fx
  ::load-audio-script-once
  [(i/path path-to-db)]
  (fn [{:keys [_db]} [_ audio-url]]
    {:dispatch [::warehouse/load-audio-script
                {:file audio-url}
                {:on-success [::load-audio-script-success]
                 :on-failure [::load-audio-script-failure]
                 :suppress-api-error? true}]}))

(re-frame/reg-event-fx
  ::load-audio-script-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ script-data]]
    {:db (-> db
             (assoc :script script-data)
             (assoc-in [:window-state :in-progress] false))}))

(re-frame/reg-event-fx
  ::load-audio-script-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db [:window-state :in-progress] false)}))

(defn- call-method
  [controls method & args]
  (when (some? controls)
    (let [handler (get @controls method)]
      (when (fn? handler)
        (apply handler args)))))

(re-frame/reg-event-fx
  ::start-playing
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ wave]]
    (call-method wave :play)
    {:db (assoc db :play-button-state "play")}))

(re-frame/reg-event-fx
  ::start-selected-playing
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ wave]]
    (let [{:keys [start end]} (selected-region db)]
      (call-method wave :play start end)
      {:db (assoc db :play-button-state "play")})))

(re-frame/reg-event-fx
  ::stop-playing
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ wave]]
    (call-method wave :stop)
    {:db (assoc db :play-button-state "stop")}))

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

(re-frame/reg-sub
  ::initial-regions
  :<- [path-to-db]
  (fn [db]
    (let [script (:script db)]
      (->> script
           (map (fn [{:keys [start end word]}]
                  {:start start
                   :end end
                   :data {:word word}}))))))

(re-frame/reg-event-fx
  ::handle-create
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ region]]
    {:db (-> db
             (update :regions conj region)
             (assoc :selected-region (:id region)))}))

(re-frame/reg-event-fx
  ::handle-change
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ region]]
    (let [id (:id region)
          old-region (->> db
                          :regions
                          (filter #(= (:id region) (:id %)))
                          (first))
          regions (->> db :regions (remove #(= id (:id %))))]
      {:db (cond-> db
                   :always (assoc :regions (conj regions (merge old-region (select-keys region [:start :end]))))
                   (-> region :data :changed not) (assoc :selected-region (:id region)))})))

(re-frame/reg-event-fx
  ::handle-select
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ region]]
    {:db (assoc db :selected-region (:id region))}))

(re-frame/reg-sub
  ::selected-region
  :<- [path-to-db]
  (fn [db]
    (selected-region db)))

(re-frame/reg-event-fx
  ::change-selected-word
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    (let [id (:selected-region db)
          region (-> db
                     (selected-region)
                     (assoc-in [:data "word"] value))
          regions (->> db :regions (remove #(= id (:id %))))]
      {:db (assoc db :regions (conj regions region))})))

(re-frame/reg-event-fx
  ::save-script
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [script (-> db (get :regions) (regions->script))
          audio-url (:audio-url db)]
      {:dispatch [::warehouse/save-subtitles
                  {:filename audio-url :result script}
                  {:on-success [::save-subtitles-success]
                   :on-failure [::save-subtitles-failure]
                   :suppress-api-error? true}]
       :db (assoc-in db [:window-state :in-progress] true)})))

(re-frame/reg-event-fx
  ::save-subtitles-success
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_]]
    {:dispatch [::close-window]}))

(re-frame/reg-event-fx
  ::save-subtitles-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db [:window-state :in-progress] false)}))

;;reset transcription
(re-frame/reg-sub
  ::reset-transcription-window-open?
  :<- [path-to-db]
  (fn [db]
    (get db :reset-transcription-window-open?)))

(re-frame/reg-event-fx
  ::open-reset-transcription-window
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (assoc db :reset-transcription-window-open? true)}))

(re-frame/reg-event-fx
  ::close-reset-transcription-window
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (assoc db :reset-transcription-window-open? false)}))

(re-frame/reg-event-fx
  ::change-reset-transcription-value
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc db :reset-transcription-value value)}))

(re-frame/reg-sub
  ::reset-transcription-value
  :<- [path-to-db]
  (fn [db]
    (get db :reset-transcription-value)))

(re-frame/reg-event-fx
  ::reset-transcription
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [words (-> (get db :reset-transcription-value)
                    (s/replace #"[_~.<>{}()!№%:,;#$%^&*+='’`\"/?'\\@]" " ")
                    (s/replace #" +" " ")
                    (s/split #"\s"))
          regions (->> words
                       (reduce (fn [acc word]
                                 (let [start (:start acc)
                                       end (+ start 0.3)]
                                   (-> acc
                                       (update :regions conj {:start start
                                                              :end end
                                                              :data {"word" word}})
                                       (assoc :start end))))
                               {:start 0
                                :regions []})
                       :regions)]
      {:db (-> db
               (assoc :regions [])
               (assoc :script (regions->script regions))
               (assoc :reset-transcription-window-open? false))})))

(comment
  (-> @(re-frame/subscribe [path-to-db]))
  (-> @(re-frame/subscribe [::initial-regions]))
  (-> @(re-frame/subscribe [::script-data]))
  (-> @(re-frame/subscribe [::selected-region]))
  (-> @(re-frame/subscribe [::reset-transcription-value])
      (s/replace #"[_~.<>{}()!№%:,;#$%^&*+='’`\"/?'\\@]" " ")
      (s/replace #" +" " ")
      (s/split #"\s")))
