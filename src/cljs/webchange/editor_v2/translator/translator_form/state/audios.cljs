(ns webchange.editor-v2.translator.translator-form.state.audios
  (:require
    [ajax.core :refer [json-response-format]]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.audio-assets.add-audio.state.select-method :as add-audio.select-method]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.translator.translator-form.state.concepts :as translator-form.concepts]
    [webchange.editor-v2.translator.translator-form.state.scene :as translator-form.scene]
    [webchange.editor-v2.translator.translator-form.state.audios-utils :refer [get-audio-assets-data
                                                                               get-form-data]]
    [webchange.config :refer [api-url]]))

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

;; Events

(re-frame/reg-event-fx
  ::init-state
  (fn [{:keys [db]} [_]]
    (let [concept-audios (translator-form.concepts/concepts-audios db)]
      {:dispatch-n (->> concept-audios
                        (map (fn [{:keys [url] :as data}] [::translator-form.scene/add-asset-if-not-exist url data]))
                        (concat (list [::add-audio.select-method/init-state])))})))

(re-frame/reg-event-fx
  ::upload-audio
  (fn [{:keys [db]} [_ js-file-value audio-props form-params]]
    (let [form-data (get-form-data (concat [["file" js-file-value]] (or form-params [])))
          asset-data {:date (.now js/Date)}]
      {:db         (assoc-in db [:loading :upload-audio] true)
       :http-xhrio {:method          :post
                    :uri             (api-url "/assets/")
                    :body            form-data
                    :response-format (json-response-format {:keywords? true})
                    :on-success      [::upload-audio-success (merge audio-props asset-data)]
                    :on-failure      [:api-request-error :upload-audio]}})))

(re-frame/reg-event-fx
  ::upload-audio-success
  (fn [_ [_ audio-props data]]
    (let [asset-data (merge audio-props data)]
      {:dispatch-n (list [:complete-request :upload-audio]
                         [::translator-form.scene/add-asset asset-data])})))
