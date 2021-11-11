(ns webchange.editor-v2.text-animation-editor.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.state.db :as parent-state]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.translator.translator-form.state.scene :as translator-form.scene]
    [webchange.utils.scene-action-data :refer [get-inner-action]]))

(re-frame/reg-event-fx
  ::open
  (fn [{:keys [db]} [_]]
    (let [{:keys [audio start duration data target]} (-> (translator-form.actions/current-phrase-action db)
                                                         (get-inner-action))
          first-chunk (-> data first :chunk)
          chunks-count (-> (translator-form.scene/objects-data db)
                           (get (keyword target))
                           (get :chunks)
                           count)
          filtered-data (remove #(<= chunks-count (:chunk %)) data)]
      {:dispatch-n [[::set-current-audio audio]
                    [::set-bounds {:start start :end (+ start duration)}]
                    [::select-chunk first-chunk]
                    [::set-data filtered-data]
                    [::open-modal]]})))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:text-animation-editor])
       (parent-state/path-to-db)))

;; Modal state

(def modal-state-path (path-to-db [:chunks-modal-state]))

(re-frame/reg-sub
  ::modal-state
  (fn [db]
    (get-in db modal-state-path false)))

(re-frame/reg-event-fx
  ::open-modal
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db modal-state-path true)}))

(re-frame/reg-event-fx
  ::close-modal
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db modal-state-path false)}))

;; Selected chunk

(def selected-chunk-path (path-to-db [:selected-chunk]))

(defn- get-selected-chunk
  [db]
  (get-in db selected-chunk-path))

(re-frame/reg-sub
  ::selected-chunk
  get-selected-chunk)

(re-frame/reg-event-fx
  ::select-chunk
  (fn [{:keys [db]} [_ chunk-index]]
    {:db (assoc-in db selected-chunk-path chunk-index)}))

;; Current audio

(def current-audio-path (path-to-db [:current-audio]))

(defn- get-current-audio
  [db]
  (get-in db current-audio-path))

(re-frame/reg-sub
  ::current-audio
  get-current-audio)

(re-frame/reg-event-fx
  ::set-current-audio
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db current-audio-path value)}))

;; Bounds

(def bounds-path (path-to-db [:bounds]))

(defn- get-bounds
  [db]
  (get-in db bounds-path))

(re-frame/reg-sub
  ::bounds
  get-bounds)

(re-frame/reg-event-fx
  ::set-bounds
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db bounds-path value)}))

;; Data

(def data-path (path-to-db [:data]))

(defn- get-data
  [db]
  (get-in db data-path))

(re-frame/reg-sub
  ::data
  get-data)

(re-frame/reg-event-fx
  ::set-data
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db data-path value)}))

;; Text data

(def text-data-path (path-to-db [:text-data]))

(defn- get-text-data
  [db]
  (get-in db text-data-path))

(re-frame/reg-sub
  ::text-data
  get-text-data)

(re-frame/reg-event-fx
  ::set-text-data
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db text-data-path value)}))

(re-frame/reg-event-fx
  ::update-text-data
  (fn [{:keys [db]} [_ value]]
    {:db (update-in db text-data-path merge value)}))

(re-frame/reg-event-fx
  ::reset-text-data
  (fn [{:keys [_]} [_]]
    {:dispatch [::set-text-data {}]}))

;; Text name

(def text-name-path (path-to-db [:text-name]))

(defn- get-text-name
  [db]
  (get-in db text-name-path))

(re-frame/reg-sub
  ::text-name
  get-text-name)

(re-frame/reg-event-fx
  ::set-text-name
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db text-name-path value)}))

;; ---

(re-frame/reg-event-fx
  ::set-current-text-data
  (fn [{:keys [_]} [_ name data]]
    {:dispatch-n [[::set-text-name name]
                  [::update-text-data data]]}))

(re-frame/reg-sub
  ::text-object
  (fn []
    [(re-frame/subscribe [::translator-form.actions/current-phrase-action])
     (re-frame/subscribe [::translator-form.scene/objects-data])
     (re-frame/subscribe [::text-data])])
  (fn [[current-phrase-action objects current-text-data]]
    (let [text-animation-action (get-in current-phrase-action [:data 1])
          {:keys [target]} text-animation-action]
      [target (-> objects (get (keyword target)) (merge current-text-data))])))

(re-frame/reg-sub
  ::active-parts
  (fn []
    [(re-frame/subscribe [::translator-form.actions/current-phrase-action])])
  (fn [[current-phrase-action]]
    (let [text-animation-action (get-inner-action current-phrase-action)]
      (->> (get text-animation-action :data)
           (map :chunk)))))

(defn- bound
  [{:keys [start end]} value]
  (cond
    (> start value) start
    (< end value) end
    :else value))

(re-frame/reg-sub
  ::selected-audio
  (fn []
    [(re-frame/subscribe [::selected-chunk])
     (re-frame/subscribe [::current-audio])
     (re-frame/subscribe [::bounds])
     (re-frame/subscribe [::data])])
  (fn [[selected-chunk-index audio bounds data]]
    (let [{:keys [start end]} (->> data (filter #(= selected-chunk-index (:chunk %))) first)]
      (when (some? audio)
        {:url   audio
         :start (bound bounds start)
         :end   (bound bounds end)}))))

(re-frame/reg-sub
  ::form-available?
  (fn []
    [(re-frame/subscribe [::selected-audio])])
  (fn [[selected-audio]]
    (some? selected-audio)))

(re-frame/reg-event-fx
  ::apply
  (fn [{:keys [db]} [_]]
    (let [data (get-data db)
          text-name (get-text-name db)
          text-data-patch (get-text-data db)]
      {:dispatch-n (cond-> [[::translator-form.actions/update-action :phrase {:data data} [:data 1]]
                            [::reset-text-data]
                            [::close-modal]]
                           (some? text-name) (conj [::translator-form.scene/update-object [text-name] text-data-patch]))})))

(re-frame/reg-event-fx
  ::cancel
  (fn [{:keys [_]} [_]]
    {:dispatch-n [[::reset-text-data]
                  [::close-modal]]}))

(re-frame/reg-event-fx
  ::select-audio
  (fn [{:keys [db]} [_ {:keys [start end duration]}]]
    (let [selected-chunk-index (get-selected-chunk db)
          chunk {:at       start
                 :start    start
                 :end      end
                 :chunk    selected-chunk-index
                 :duration duration}
          chunks (as-> (get-data db) c
                       (remove #(= selected-chunk-index (:chunk %)) c)
                       (conj c chunk)
                       (sort-by :chunk c))]
      {:dispatch [::set-data chunks]})))
