(ns webchange.editor-v2.translator.events
  (:require
    [re-frame.core :as re-frame]
    [ajax.core :refer [json-request-format json-response-format]]))

(re-frame/reg-event-fx
  ::open-translator-modal
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db [:editor-v2 :translator :translator-modal-state] true)}))

(re-frame/reg-event-fx
  ::close-translator-modal
  (fn [{:keys [db]} [_]]
    {:db (-> db
             (assoc-in [:editor-v2 :translator :translator-modal-state] false)
             (assoc-in [:editor-v2 :translator :phrase-translation-data] {}))}))

(re-frame/reg-event-fx
  ::set-current-selected-action
  (fn [{:keys [db]} [_ action]]
    {:db (assoc-in db [:editor-v2 :translator :selected-action] action)}))

(re-frame/reg-event-fx
  ::clean-current-selected-action
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db [:editor-v2 :translator :selected-action] nil)}))

(re-frame/reg-event-fx
  ::set-phrase-translation-action
  (fn [{:keys [db]} [_ action-name data]]
    {:db (assoc-in db [:editor-v2 :translator :phrase-translation-data action-name] data)}))

(re-frame/reg-event-fx
  ::set-current-concept
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db [:editor-v2 :translator :current-concept] data)}))

(re-frame/reg-event-fx
  ::update-current-concept-field
  (fn [{:keys [db]} [_ concept-id field-name field-data]]
    (let [current-concept-id (get-in db [:editor-v2 :translator :current-concept :id])]
      (if (= current-concept-id concept-id)
        {:db (assoc-in db [:editor-v2 :translator :current-concept :data field-name] field-data)}))))

(re-frame/reg-event-fx
  ::save-current-concept
  (fn [{:keys [db]} _]
    (let [{:keys [id name data]} (get-in db [:editor-v2 :translator :current-concept])]
      {:db (assoc-in db [:loading :save-current-concept] true)
       :http-xhrio {:method          :put
                    :uri             (str "/api/dataset-items/" id)
                    :params          {:data data :name name}
                    :format          (json-request-format)
                    :response-format (json-response-format {:keywords? true})
                    :on-success      [::save-current-concept-success]
                    :on-failure      [:api-request-error :save-current-concept]}})))

(re-frame/reg-event-fx
  ::save-current-concept-success
  (fn [_ _]
    {:dispatch-n (list [:complete-request :save-current-concept])}))
