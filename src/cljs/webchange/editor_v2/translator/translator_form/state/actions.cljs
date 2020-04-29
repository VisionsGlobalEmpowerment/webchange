(ns webchange.editor-v2.translator.translator-form.state.actions
  (:require
    [ajax.core :refer [json-request-format json-response-format]]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.state.db :refer [path-to-db]]
    [webchange.editor-v2.translator.translator-form.state.actions-utils :as actions]
    [webchange.editor-v2.translator.translator-form.state.concepts :as translator-form.concepts]
    [webchange.editor-v2.translator.translator-form.state.scene :as translator-form.scene]))

;; Subs

(defn current-dialog-action-info
  [db]
  (get-in db (path-to-db [:current-dialog-action])))

(re-frame/reg-sub
  ::current-dialog-action-info
  current-dialog-action-info)

(re-frame/reg-sub
  ::current-dialog-action
  (fn []
    [(re-frame/subscribe [::current-dialog-action-info])
     (re-frame/subscribe [::translator-form.scene/actions-data])])
  (fn [[{:keys [path]} actions]]
    (get-in actions path)))

(re-frame/reg-sub
  ::current-dialog-action-name
  (fn []
    [(re-frame/subscribe [::current-dialog-action-info])])
  (fn [[{:keys [path]}]]
    (first path)))

(defn current-phrase-action-info
  [db]
  (get-in db (path-to-db [:current-phrase-action])))

(re-frame/reg-sub
  ::current-phrase-action-info
  current-phrase-action-info)

(re-frame/reg-sub
  ::current-phrase-action
  (fn []
    [(re-frame/subscribe [::current-phrase-action-info])
     (re-frame/subscribe [::translator-form.scene/actions-data])
     (re-frame/subscribe [::translator-form.concepts/current-concept-data])])
  (fn [[{:keys [path type]} actions-data current-concept]]
    (when-not (nil? path)
      (cond
        (= type :concept-action) (get-in current-concept path)
        (= type :scene-action) (get-in actions-data path)
        :else nil))))

;; Events

(re-frame/reg-event-fx
  ::reset-state
  (fn [{:keys [_]} [_]]
    {:dispatch-n (list [::clean-current-dialog-action]
                       [::clean-current-phrase-action])}))

(re-frame/reg-event-fx
  ::update-action
  (fn [{:keys [db]} [_ target-action data-patch]]
    (let [{:keys [path type]} (cond
                                (= target-action :dialog) (current-dialog-action-info db)
                                (= target-action :phrase) (current-phrase-action-info db))]
      (cond
        (= type :concept-action) {:dispatch-n (list [::translator-form.concepts/update-current-concept path data-patch])}
        (= type :scene-action) {:dispatch-n (list [::translator-form.scene/update-action path data-patch])}))))

;; Dialog Action

(re-frame/reg-event-fx
  ::set-current-dialog-action
  (fn [{:keys [db]} [_ action-node]]
    {:db (assoc-in db (path-to-db [:current-dialog-action]) (actions/node->info action-node))}))

(re-frame/reg-event-fx
  ::clean-current-dialog-action
  (fn [{:keys []} [_]]
    {:dispatch-n (list [::set-current-dialog-action nil])}))

(re-frame/reg-event-fx
  ::set-dialog-action-description-translated
  (fn [{:keys [_]} [_ text]]
    {:dispatch-n (list [::update-action :dialog {:phrase-description-translated text}])}))

;; Phrase Action

(re-frame/reg-event-fx
  ::set-current-phrase-action
  (fn [{:keys [db]} [_ action-node]]
    {:db (assoc-in db (path-to-db [:current-phrase-action]) (actions/node->info action-node))}))

(re-frame/reg-event-fx
  ::clean-current-phrase-action
  (fn [{:keys []} [_]]
    {:dispatch-n (list [::set-current-phrase-action nil])}))

(re-frame/reg-event-fx
  ::set-phrase-action-phrase-translated
  (fn [{:keys [_]} [_ text]]
    {:dispatch-n (list [::update-action :phrase {:phrase-text-translated text}])}))

(re-frame/reg-event-fx
  ::set-phrase-action-audio
  (fn [{:keys [_]} [_ audio-url]]
    {:dispatch-n (list [::update-action :phrase {:audio audio-url}])}))

(re-frame/reg-event-fx
  ::set-phrase-action-audio-region
  (fn [{:keys [_]} [_ audio-url start duration]]
    (let [region-data {:audio    audio-url
                       :start    start
                       :duration duration}]
      {:dispatch-n (list [::update-action :phrase region-data]
                         [::load-lip-sync-data audio-url start duration])})))

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
                       [::update-action :phrase {:data lip-sync-data}])}))

(re-frame/reg-event-fx
  ::load-lip-sync-data-failure
  (fn [{:keys [_]} [_ start duration]]
    (let [default-lip-sync-data [{:start start
                                  :end   (+ start duration)
                                  :anim  "talk"}]]
      {:dispatch-n (list [:api-request-error :load-lip-sync-data]
                         [::update-action :phrase {:data default-lip-sync-data}])})))

(re-frame/reg-event-fx
  ::add-new-phrase-action
  (fn [{:keys [_]} [_ trigger-node position]]
    (println "::add-empty-action" position trigger-node)
    {}))
