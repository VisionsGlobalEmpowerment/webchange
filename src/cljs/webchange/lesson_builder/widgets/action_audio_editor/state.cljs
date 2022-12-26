(ns webchange.lesson-builder.widgets.action-audio-editor.state
  (:require
    [clojure.string :as s]
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.stage-actions :as stage-actions]
    [webchange.lesson-builder.state :as state]
    [webchange.state.warehouse :as warehouse]
    [webchange.utils.audio-analyzer.index :as audio-analyzer]
    [webchange.utils.audio-analyzer.talk-data :as talk-data]
    [webchange.utils.numbers :refer [to-precision try-parse-number]]
    [webchange.utils.scene-action-data :as action-utils]
    [webchange.utils.scene-data :as utils]))

(def path-to-db :widget/audio-editor)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(comment
  (-> @(re-frame/subscribe [path-to-db])))
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

(defn- get-action-text
  [action-data]
  (get action-data :phrase-text))

(defn- get-audio-url
  [action-data]
  (get action-data :audio))

(re-frame/reg-sub
  ::action-text
  (fn [[_ action-path]]
    (re-frame/subscribe [::action-data action-path]))
  (fn [action-data]
    (get-action-text action-data)))

(re-frame/reg-sub
  ::audio-url
  (fn [[_ action-path]]
    (re-frame/subscribe [::action-data action-path]))
  (fn [action-data]
    (get action-data :audio)))

(re-frame/reg-sub
  ::initial-region
  :<- [path-to-db]
  (fn [db [_ id]]
    (get-in db [id :initial-region])))

(re-frame/reg-event-fx
  ::init-region
  [(re-frame/inject-cofx :activity-data)
   (i/path path-to-db)]
  (fn [{:keys [activity-data db]} [_ id action-path]]
    (let [action-data (get-action-data activity-data action-path)]
      {:db (-> db
               (assoc-in [id :initial-region] (select-keys action-data [:start :end])))})))
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
    (call-method wave :play-first)
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

;; script data

(def script-data-key :script-data)

(defn- get-script-data
  [db id]
  (get-in db [id script-data-key]))

(defn- set-script-data
  [db id value]
  (assoc-in db [id script-data-key] value))

(re-frame/reg-sub
  ::script-data
  :<- [path-to-db]
  (fn [db [_ id]]
    (get-script-data db id)))

(re-frame/reg-event-fx
  ::load-audio-script
  [(re-frame/inject-cofx :activity-data)
   (i/path path-to-db)]
  (fn [{:keys [activity-data db]} [_ id {:keys [action-path audio-url]} {:keys [on-success on-failure]}]]
    (let [audio-url (or audio-url
                        (->> action-path
                             (get-action-data activity-data)
                             (get-audio-url)))]
      {:db (set-script-data db id nil)
       :dispatch [::warehouse/load-audio-script-polled
                  {:file audio-url}
                  (cond-> {:on-success [::load-audio-script-success id on-success]}
                          (some? on-failure) (assoc :on-failure on-failure))]})))

(re-frame/reg-event-fx
  ::load-audio-script-once
  [(re-frame/inject-cofx :activity-data)
   (i/path path-to-db)]
  (fn [{:keys [activity-data]} [_ id {:keys [action-path audio-url]} {:keys [on-success on-failure]}]]
    (let [audio-url (or audio-url
                        (->> action-path
                             (get-action-data activity-data)
                             (get-audio-url)))]
      {:dispatch [::warehouse/load-audio-script
                  {:file audio-url}
                  (cond-> {:on-success [::load-audio-script-success id on-success]
                           :suppress-api-error? true}
                          (some? on-failure) (assoc :on-failure on-failure))]})))

(re-frame/reg-event-fx
  ::load-audio-script-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ id on-success script-data]]
    (cond-> {:db (set-script-data db id script-data)}
            (some? on-success) (assoc :dispatch on-success))))

;; auto select

(def auto-select-loading-key :auto-select-loading?)

(defn- set-auto-select-loading
  [db id value]
  (assoc-in db [id auto-select-loading-key] value))

(re-frame/reg-sub
  ::auto-select-loading?
  :<- [path-to-db]
  (fn [db [_ id]]
    (get-in db [id auto-select-loading-key])))

(defn question-audio-data?
  [action]
  (not (:type action)))

(defn update-text-animation-data?
  [action]
  (or (question-audio-data? action) (action-utils/text-animation-action? action)))

(defn update-talk-animation-data?
  [action]
  (action-utils/animation-sequence-action? action))

(defn- with-animation-data
  [region-data action-data text audio-script]
  (cond-> region-data
          (update-text-animation-data? action-data)
          (assoc :data
                 (talk-data/get-chunks-data-if-possible {:text   text
                                                         :region region-data
                                                         :script audio-script}))

          (update-talk-animation-data? action-data)
          (assoc :data
                 (talk-data/get-talk-data-if-possible {:text   text
                                                       :region region-data
                                                       :script audio-script}))))

(re-frame/reg-event-fx
  ::auto-select-region
  [(re-frame/inject-cofx :activity-data)
   (i/path path-to-db)]
  (fn [{:keys [activity-data db]} [_ id action-path {:keys [retry? poll?] :or {retry? true poll? true}}]]
    (let [action-data (get-action-data activity-data action-path)
          audio-script (get-script-data db id)
          audio-url (get-audio-url action-data)]
      (cond
        (some? audio-script)
        (let [text (get-action-text action-data)
              region-data (-> (audio-analyzer/get-region-data text audio-script)
                              (with-animation-data action-data text audio-script))]
          {:db       (-> db
                         (set-auto-select-loading id false)
                         (assoc-in [id :initial-region] region-data))
           :dispatch [::set-action-region action-path region-data]})

        retry?
        {:db       (set-auto-select-loading db id true)
         :dispatch [::load-audio-script-once
                    id
                    {:audio-url audio-url}
                    {:on-success [::auto-select-region id action-path {:retry? false}]
                     :on-failure [::retry-audio-recognition audio-url id action-path]}]}

        poll?
        {:db       (set-auto-select-loading db id true)
         :dispatch [::load-audio-script
                    id
                    {:audio-url audio-url}
                    {:on-success [::auto-select-region id action-path {:retry? false :poll? false}]
                     :on-failure [::auto-select-region id action-path {:retry? false :poll? false}]}]}
        
        :else
        {:db (set-auto-select-loading db id false)}))))

(re-frame/reg-event-fx
  ::retry-audio-recognition
  [(re-frame/inject-cofx :activity-info)
   (i/path path-to-db)]
  (fn [{:keys [activity-info]} [_ url id action-path]]
    (let [lang (:lang activity-info)]
      {:dispatch [::warehouse/retry-audio-recognition
                  {:url url :lang lang}
                  {:on-success [::auto-select-region id action-path {:retry? false}]}]})))

;; selection options

(re-frame/reg-sub
  ::show-selection-options?
  (fn [[_ id action-path]]
    (re-frame/subscribe [::selection-options id action-path]))
  (fn [selection-options]
    (seq selection-options)))

(re-frame/reg-sub
  ::selection-options
  (fn [[_ id action-path]]
    [(re-frame/subscribe [::action-text action-path])
     (re-frame/subscribe [::script-data id])])
  (fn [[action-text script-data]]
    (if (and (some? action-text)
             (some? script-data))
      (->> (audio-analyzer/get-available-regions action-text script-data)
           (map (fn [{:keys [start end region-text]}]
                  {:text  (s/capitalize region-text)
                   :value (str start "-" end)})))
      [])))

(re-frame/reg-event-fx
  ::handle-selection-option
  [(re-frame/inject-cofx :activity-data)
   (i/path path-to-db)]
  (fn [{:keys [activity-data db]} [_ id action-path value]]
    (let [action-data (get-action-data activity-data action-path)
          audio-script (get-script-data db id)
          text (get-action-text action-data)
          [start end] (->> (s/split value "-")
                           (map try-parse-number))
          region-data (-> {:start    start
                           :end      end
                           :duration (-> (- end start) (to-precision 2))}
                          (with-animation-data action-data text audio-script))]
      {:db (assoc-in db [id :initial-region] region-data)
       :dispatch [::set-action-region action-path region-data]})))

;; events

(re-frame/reg-event-fx
  ::init
  [(re-frame/inject-cofx :activity-data)
   (i/path path-to-db)]
  (fn [{:keys [activity-data db]} [_ id {:keys [action-path]}]]
    (let [action-data (get-action-data activity-data action-path)
          volume (get action-data :volume 1)]
      {:db (-> db
               (assoc :action-path action-path)
               (set-current-volume id volume))})))

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
    {:dispatch [::stage-actions/set-action-phrase-region {:action-path action-path
                                                          :region      value}]}))
