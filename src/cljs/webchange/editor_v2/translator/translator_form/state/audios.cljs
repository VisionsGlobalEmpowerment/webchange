(ns webchange.editor-v2.translator.translator-form.state.audios
  (:require
    [ajax.core :refer [json-response-format]]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.components.audio-wave-form.state :as audio-wave-state]
    [webchange.editor-v2.translator.translator-form.state.db :refer [path-to-db]]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.translator.translator-form.state.concepts :as translator-form.concepts]
    [webchange.editor-v2.translator.translator-form.state.scene :as translator-form.scene]
    [webchange.editor-v2.translator.translator-form.state.audios-utils :refer [get-audio-assets-data
                                                                               get-form-data]]
    [webchange.state.warehouse :as warehouse]))

;; Subs

(re-frame/reg-sub
  ::audios-list
  (fn []
    [(re-frame/subscribe [::translator-form.actions/current-phrase-action])
     (re-frame/subscribe [::translator-form.scene/audio-assets])])
  (fn [[current-phrase-action scene-audios]]
    (get-audio-assets-data current-phrase-action scene-audios)))

(re-frame/reg-sub
  ::available-audio-targets
  (fn []
    [(re-frame/subscribe [::translator-form.scene/audio-assets])])
  (fn [[audio-assets]]
    (->> audio-assets
         (map :target)
         (filter #(-> % nil? not))
         (distinct))))

;; Current language

(def current-lang-path (path-to-db [:current-lang]))

(defn get-current-lang
  [db]
  (let [scene-id (translator-form.scene/scene-id db)]
    (or
      (get-in db [:scenes scene-id :language])
      (get-in db [:scenes scene-id :metadata :language])
      (get-in db [:current-course]))))

(re-frame/reg-sub
  ::current-lang
  get-current-lang)

(re-frame/reg-event-fx
  ::set-current-lang
  (fn [{:keys [db]} [_ current-lang]]
    (let [scene-id (translator-form.scene/scene-id db)]
      {:db (assoc-in db [:scenes scene-id :language] current-lang)})))

;; Events

(re-frame/reg-event-fx
  ::init-state
  (fn [{:keys [db]} [_]]
    (let [concept-audios (translator-form.concepts/concepts-audios db)]
      {:dispatch-n (map (fn [{:keys [url] :as data}]
                          [::translator-form.scene/add-asset-if-not-exist url data])
                        concept-audios)})))

(re-frame/reg-event-fx
  ::upload-audio
  (fn [{:keys [db]} [_ js-file-value audio-props form-params]]
    (let [lang (get-current-lang db)
          asset-data {:date (.now js/Date)
                      :lang lang}]
      {:db       (assoc-in db [:loading :upload-audio] true)
       :dispatch [::warehouse/upload-file
                  {:file        js-file-value
                   :form-params (concat [["lang" lang]] form-params)}
                  {:on-success [::upload-audio-success (merge audio-props asset-data)]}]})))

(re-frame/reg-event-fx
  ::upload-audio-success
  (fn [_ [_ audio-props data]]
    (let [asset-data (merge audio-props data)]
      (if (:on-success audio-props)  ((:on-success audio-props)))
      {:dispatch-n (list [:complete-request :upload-audio]
                         [::translator-form.scene/add-asset asset-data]
                         [::set-default-target asset-data])})))

(re-frame/reg-event-fx
  ::set-current-target
  (fn [{:keys [db]} [_ current-target]]
    {:db (assoc-in db [:current-target] current-target)}))

(re-frame/reg-event-fx
  ::set-default-target
  (fn [{:keys [db]} [_ asset-data]]
    (let [asset-url (:url asset-data)
          current-target (get-in db [:current-target])]
      (if (some? current-target)
        {:dispatch [::translator-form.scene/update-asset asset-url {:target current-target}]}
        {}))))
