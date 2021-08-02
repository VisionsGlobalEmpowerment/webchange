(ns webchange.editor-v2.text-animation-editor.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.translator.translator-form.state.scene :as translator-form.scene]))

(def modal-state-path [:editor-v2 :translator :text :chunks-modal-state])
(def selected-chunk-path [:editor-v2 :translator :text :chunks :selected])
(def audio-path [:editor-v2 :translator :text :chunks :audio])
(def bounds-path [:editor-v2 :translator :text :chunks :bounds])
(def data-path [:editor-v2 :translator :text :chunks :data])
(def text-data-path [:editor-v2 :translator :text :text-data])
(def text-name [:editor-v2 :translator :text :text-name])

(defn- get-current-text-data
  [db]
  (get-in db text-data-path {}))

(re-frame/reg-sub
  ::current-text-data
  get-current-text-data)

(re-frame/reg-event-fx
  ::set-current-text-data
  (fn [{:keys [db]} [_ name data]]
    {:db (-> db
             (update-in text-data-path merge data)
             (assoc-in text-name name))}))

(re-frame/reg-event-fx
  ::reset-current-text-data
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db text-data-path {})}))

(re-frame/reg-sub
  ::modal-state
  (fn [db]
    (-> db
        (get-in modal-state-path)
        boolean)))

(re-frame/reg-sub
  ::text-object
  (fn []
    [(re-frame/subscribe [::translator-form.actions/current-phrase-action])
     (re-frame/subscribe [::translator-form.scene/objects-data])
     (re-frame/subscribe [::current-text-data])])
  (fn [[current-phrase-action objects current-text-data]]
    (let [text-animation-action (get-in current-phrase-action [:data 1])
          {:keys [target]} text-animation-action]
      [target (-> objects (get (keyword target)) (merge current-text-data))])))

(re-frame/reg-sub
  ::selected-chunk
  (fn [db]
    (get-in db selected-chunk-path)))

(defn- bound
  [{:keys [start end]} value]
  (cond
    (> start value) start
    (< end value) end
    :else value))

(re-frame/reg-sub
  ::selected-audio
  (fn [db]
    (let [index (get-in db selected-chunk-path)
          audio (get-in db audio-path)
          data (get-in db data-path)
          bounds (get-in db bounds-path)
          {:keys [start end]} (->> data (filter #(= index (:chunk %))) first)]
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
  ::select-chunk
  (fn [{:keys [db]} [_ index]]
    {:db (assoc-in db selected-chunk-path index)}))

(re-frame/reg-event-fx
  ::open
  (fn [{:keys [db]} [_]]
    (let [current-phrase-action (translator-form.actions/current-phrase-action db)
          {:keys [audio start duration data target]} (get-in current-phrase-action [:data 1])
          chunks-count (-> (translator-form.scene/objects-data db)
                           (get (keyword target))
                           (get :chunks)
                           count)
          filtered-data (remove #(<= chunks-count (:chunk %)) data)]
      {:db       (-> db
                     (assoc-in modal-state-path true)
                     (assoc-in data-path filtered-data)
                     (assoc-in audio-path audio)
                     (assoc-in bounds-path {:start start :end (+ start duration)}))
       :dispatch [::select-chunk 0]})))

(defn- get-text-name
  [db]
  (get-in db text-name))

(re-frame/reg-event-fx
  ::apply
  (fn [{:keys [db]} [_]]
    (let [data (get-in db data-path)
          text-name (get-text-name db)
          text-data-patch (get-current-text-data db)]
      {:db         (assoc-in db modal-state-path false)
       :dispatch-n (cond-> [[::translator-form.actions/update-action :phrase {:data data} [:data 1]]
                            [::reset-current-text-data]]
                           (some? text-name) (conj [::translator-form.scene/update-object [text-name] text-data-patch]))})))

(re-frame/reg-event-fx
  ::cancel
  (fn [{:keys [db]} [_]]
    {:db       (assoc-in db modal-state-path false)
     :dispatch [::reset-current-text-data]}))

(re-frame/reg-event-fx
  ::select-audio
  (fn [{:keys [db]} [_ {:keys [start end duration]}]]
    (let [index (get-in db selected-chunk-path)
          chunk {:at       start
                 :start    start
                 :end      end
                 :chunk    index
                 :duration duration}
          chunks (as-> (get-in db data-path) c
                       (remove #(= index (:chunk %)) c)
                       (conj c chunk)
                       (sort-by :chunk c))]
      {:db (assoc-in db data-path chunks)})))
