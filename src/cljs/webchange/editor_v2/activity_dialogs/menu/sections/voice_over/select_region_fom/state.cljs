(ns webchange.editor-v2.activity-dialogs.menu.sections.voice-over.select-region-fom.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-dialogs.form.state :as state-dialog]
    [webchange.editor-v2.activity-dialogs.menu.sections.voice-over.select-region-fom.utils :as utils]
    [webchange.editor-v2.activity-dialogs.menu.sections.voice-over.state :as parent-state]
    [webchange.editor-v2.components.audio-wave-form.utils :as ws-utils]
    [webchange.editor-v2.dialog.dialog-form.state.actions :as state-actions]
    [webchange.editor-v2.translator.translator-form.state.scene :as state-scene]
    [webchange.state.warehouse-recognition :as recognition]
    [webchange.utils.numbers :refer [to-precision]]
    [webchange.utils.scene-action-data :refer [text-animation-action?
                                               animation-sequence-action?]]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:select-region-form])
       (parent-state/path-to-db)))

;; Window

(def window-state-path (path-to-db [:window-state]))

(re-frame/reg-sub
  ::window-open?
  (fn [db]
    (-> (get-in db window-state-path)
        (true?))))

(re-frame/reg-event-fx
  ::open-window
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db window-state-path true)}))

(re-frame/reg-event-fx
  ::close-window
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db window-state-path false)}))


;; Form

(defn- get-current-text
  ([db]
   (get-current-text db (parent-state/get-current-inner-action db)))
  ([db inner-action]
   (cond
     (text-animation-action? inner-action) (->> (get inner-action :target)
                                                (keyword)
                                                (state-scene/object-data db)
                                                (:text))
     (animation-sequence-action? inner-action) (get inner-action :phrase-text))))

(defn- get-wave-form-data
  ([db]
   (get-wave-form-data (parent-state/get-current-inner-action db) nil))
  ([{:keys [audio] :as inner-action} _]
   (-> inner-action
       (select-keys [:start :end :duration])
       (assoc :url audio))))

(re-frame/reg-sub
  ::wave-form-data
  (fn []
    [(re-frame/subscribe [::parent-state/current-inner-action])])
  (fn [[inner-action]]
    (get-wave-form-data inner-action nil)))

(re-frame/reg-event-fx
  ::scroll-to-start
  (fn [{:keys [db]} [_ ws]]
    (let [{:keys [start]} (get-wave-form-data db)]
      {:scroll-to-time {:ws   ws
                        :time start}})))

(re-frame/reg-event-fx
  ::scroll-to-end
  (fn [{:keys [db]} [_ ws]]
    (let [{:keys [end]} (get-wave-form-data db)]
      {:scroll-to-time {:ws   ws
                        :time end}})))

(re-frame/reg-fx
  :scroll-to-time
  (fn [{:keys [ws time]}]
    (ws-utils/center-to-time ws time)))

;; Options

(def region-options-path (path-to-db [:region-options]))

(defn- get-region-options
  [db]
  (get-in db region-options-path []))

(re-frame/reg-event-fx
  ::init-region-options
  (fn [{:keys [db]} [_]]
    (let [{:keys [url]} (get-wave-form-data db)
          current-text (get-current-text db)
          audio-script (recognition/get-audio-script db url)]
      {:db (->> (utils/get-available-regions current-text audio-script)
                (assoc-in db region-options-path))})))

(re-frame/reg-event-fx
  ::reset-region-options
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db region-options-path [])}))

(re-frame/reg-sub
  ::region-options
  (fn [db]
    (let [sec->time (fn [sec]
                      (str (-> sec (quot 60) (int))
                           ":"
                           (-> sec (rem 60) (to-precision 2))))]
      (->> (get-region-options db)
           (map-indexed (fn [idx {:keys [start end region-text]}]
                          {:text  (str "[" (sec->time start) " - " (sec->time end) "] " region-text)
                           :value idx}))))))

(def selected-region-option-path (path-to-db [:selected-region-option]))

(re-frame/reg-sub
  ::selected-option
  (fn [db]
    (get-in db selected-region-option-path)))

(re-frame/reg-event-fx
  ::set-selected-option
  (fn [{:keys [db]} [_ option-idx]]
    (let [region-options (get-region-options db)
          selected-region (nth region-options option-idx)]
      {:db       (assoc-in db selected-region-option-path option-idx)
       :dispatch [::change-region selected-region]})))

(re-frame/reg-event-fx
  ::reset-selected-option
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db selected-region-option-path nil)}))

;; Init

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [db]} [_]]
    (let [{:keys [url]} (get-wave-form-data db)]
      {:dispatch-n [[::reset-region-options]
                    [::reset-selected-option]
                    [::recognition/load-audio-script-data
                     {:audio-url url}
                     {:on-success [::init-region-options]}]]})))

(re-frame/reg-event-fx
  ::auto-select
  (fn [{:keys [db]} [_ ws]]
    (let [{:keys [url]} (get-wave-form-data db)
          current-text (get-current-text db)
          audio-script (recognition/get-audio-script db url)
          region-data (utils/get-region-data current-text audio-script)]
      (when (some? region-data)
        {:dispatch       [::change-region region-data]
         :scroll-to-time {:ws   ws
                          :time (:start region-data)}}))))

(re-frame/reg-event-fx
  ::change-region
  (fn [{:keys [db]} [_ region-data]]
    (let [{:keys [url] :as form-data} (get-wave-form-data db)
          current-text (get-current-text db)
          audio-script (recognition/get-audio-script db url)
          inner-action (parent-state/get-current-inner-action db)
          data-patch (cond-> region-data
                             (text-animation-action? inner-action)
                             (assoc :data (utils/get-text-animation-data {:text         current-text
                                                                          :audio-script audio-script}))
                             (animation-sequence-action? inner-action)
                             (assoc :data (utils/get-animation-sequence-data {:text         current-text
                                                                              :audio-script audio-script
                                                                              :form-data    form-data})))]
      {:dispatch [::update-action data-patch]})))

(re-frame/reg-event-fx
  ::update-action
  (fn [{:keys [db]} [_ data-patch]]
    (let [{:keys [path source]} (state-dialog/get-selected-action db)]
      {:dispatch [::state-actions/update-inner-action-by-path {:action-path path
                                                               :action-type source
                                                               :data-patch  data-patch}]})))
