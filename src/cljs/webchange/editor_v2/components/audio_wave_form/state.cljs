(ns webchange.editor-v2.components.audio-wave-form.state
  (:require
    [re-frame.core :as re-frame]
    [ajax.core :refer [json-request-format json-response-format]]
    [webchange.editor-v2.state :as db]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:waveform-data])
       (db/path-to-db)))

(re-frame/reg-event-fx
  ::init-audio-script
  (fn [{:keys [db]} [_ file]]
    {:db       (assoc-in db (path-to-db [file :loading]) true)
     :dispatch [::load-audio-script {:file file} {:on-success [::set-audio-script file]
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

(re-frame/reg-event-fx
  ::load-audio-script
  (fn [{:keys [db]} [_ {:keys [file start duration]} {:keys [on-success on-failure]}]]
    {:db         (assoc-in db [:loading :lesson-sets] true)
     :http-xhrio (cond-> {:method          :get
                          :uri             "/api/actions/get-subtitles"
                          :url-params      (cond-> {:file file}
                                                   (some? start) (assoc :start start)
                                                   (some? duration) (assoc :duration duration))
                          :format          (json-request-format)
                          :response-format (json-response-format {:keywords? true})}
                         (some? on-success) (assoc :on-success on-success)
                         (some? on-failure) (assoc :on-failure on-failure))}))

(re-frame/reg-event-fx
  ::init-audio-script-poll
  (fn [{:keys [db]} [_ file timeout attempts]]
    {:db       (assoc-in db (path-to-db [file :loading]) true)
     :dispatch [::load-audio-script {:file file} {:on-success [::set-audio-script file]
                                                  :on-failure [::init-audio-script-poll-failed file timeout attempts]}]}))

(re-frame/reg-event-fx
  ::init-audio-script-poll-failed
  (fn [{:keys [_]} [_ file timeout attempts]]
    (if (= attempts 0)
      {:dispatch [::reset-script-loading file]}
      {:timeout {:event [::init-audio-script-poll file timeout (dec attempts)]
                 :time  timeout}})))

(re-frame.core/reg-fx
  :timeout
  (fn [{:keys [event time]}]
    (js/setTimeout
      (fn [] (re-frame.core/dispatch event))
      time)))
