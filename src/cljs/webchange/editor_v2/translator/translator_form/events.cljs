(ns webchange.editor-v2.translator.translator-form.events
  (:require
    [ajax.core :refer [json-request-format json-response-format]]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.db :refer [path-to-db]]
    [webchange.editor-v2.translator.translator-form.subs :as translator-form-subs]
    [webchange.editor-v2.translator.translator-form.utils-update-action :refer [get-action-update-data]]
    [webchange.subs :as subs]))

(re-frame/reg-event-fx
  ::set-current-selected-action
  (fn [{:keys [db]} [_ action]]
    {:db (assoc-in db (path-to-db [:selected-action]) action)}))

(re-frame/reg-event-fx
  ::clean-current-selected-action
  (fn [{:keys []} [_]]
    {:dispatch-n (list [::set-current-selected-action nil])}))

(re-frame/reg-event-fx
  ::reset-edited-data
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db (path-to-db [:edited-data]) {})}))

(re-frame/reg-event-fx
  ::set-action-edited-data
  (fn [{:keys [db]} [_ action-name action-id data]]
    {:db (assoc-in db (path-to-db [:edited-data :actions [action-name action-id]]) data)}))

(re-frame/reg-event-fx
  ::patch-current-action-edited-data
  (fn [{:keys [db]} [_ data-patch]]
    (let [data-store (translator-form-subs/edited-actions-data db)
          current-concept (translator-form-subs/current-concept db)
          selected-action-node (translator-form-subs/selected-action db)
          scene-data (subs/current-scene-data db)
          original-action-name (-> selected-action-node :path first)
          {:keys [id name type data]} (get-action-update-data {:scene-data           scene-data
                                                               :data-store           data-store
                                                               :current-concept      current-concept
                                                               :selected-action-node selected-action-node
                                                               :original-action-name original-action-name
                                                               :data-patch           data-patch})]
      {:dispatch-n (list [::set-action-edited-data name id {:type type :data data}])})))

(re-frame/reg-event-fx
  ::set-current-action-audio
  (fn [{:keys [_]} [_ audio-url]]
    {:dispatch-n (list [::patch-current-action-edited-data {:audio audio-url}])}))

(re-frame/reg-event-fx
  ::set-current-action-audio-region
  (fn [{:keys [_]} [_ audio-url start duration]]
    (let [region-data {:start    start
                       :duration duration}]
      {:dispatch-n (list [::patch-current-action-edited-data region-data]
                         [::load-lip-sync-data audio-url start duration])})))

(re-frame/reg-event-fx
  ::set-current-action-phrase-translated-text
  (fn [{:keys [_]} [_ text]]
    {:dispatch-n (list [::patch-current-action-edited-data {:phrase-text-translated text}])}))

(re-frame/reg-event-fx
  ::load-lip-sync-data
  (fn [{:keys [db]} [_ audio-url start duration]]
    {:db         (assoc-in db [:loading :load-lip-sync-data] true)
     :http-xhrio {:method          :get
                  :uri             (str "/api/actions/get-talk-animations")
                  :url-params      {:file     audio-url
                                    :start    start
                                    :duration duration}
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::load-lip-sync-data-success]
                  :on-failure      [::load-lip-sync-data-failure start duration]}}))

(re-frame/reg-event-fx
  ::load-lip-sync-data-success
  (fn [{:keys [_]} [_ lip-sync-data]]
    {:dispatch-n (list [:complete-request :load-lip-sync-data]
                       [::patch-current-action-edited-data {:data lip-sync-data}])}))

(re-frame/reg-event-fx
  ::load-lip-sync-data-failure
  (fn [{:keys [_]} [_ start duration]]
    (let [default-lip-sync-data [{:start start
                                  :end   (+ start duration)
                                  :anim  "talk"}]]
      {:dispatch-n (list [:api-request-error :load-lip-sync-data]
                         [::patch-current-action-edited-data {:data default-lip-sync-data}])})))

(re-frame/reg-event-fx
  ::set-current-concept
  (fn [{:keys [db]} [_ data]]
    {:db         (assoc-in db (path-to-db [:current-concept]) data)
     :dispatch-n (list [::clean-current-selected-action])}))

(re-frame/reg-event-fx
  ::reset-current-concept
  (fn [{:keys [_]} [_]]
    {:dispatch-n (list [::set-current-concept nil])}))
