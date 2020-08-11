(ns webchange.editor-v2.concepts.events
  (:require
    [re-frame.core :as re-frame]
    [day8.re-frame.http-fx]
    [ajax.core :refer [json-request-format json-response-format]]
    [webchange.editor-v2.events :as ee]
    [webchange.config :refer [api-url]]))

(re-frame/reg-event-fx
  ::add-dataset-item
  (fn [{:keys [db]} [_ {:keys [name dataset-id data]}]]
    {:db (assoc-in db [:loading :add-dataset-item] true)
     :http-xhrio {:method          :post
                  :uri             (api-url "/dataset-items")
                  :params          {:dataset-id dataset-id :name name :data data}
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::add-dataset-item-success]
                  :on-failure      [:api-request-error :add-dataset-item]}}))


(re-frame/reg-event-fx
  ::add-dataset-item-success
  (fn [{:keys [db]} _]
    (let [course-id (:current-course db)]
      {:dispatch-n (list [:complete-request :add-dataset-item] [::ee/load-lesson-sets course-id])
       :redirect [:course-editor-v2 :id course-id]})))

(re-frame/reg-event-fx
  ::edit-dataset-item
  (fn [{:keys [db]} [_ item-id {:keys [data name]}]]
    {:db (assoc-in db [:loading :edit-dataset-item] true)
     :http-xhrio {:method          :put
                  :uri             (api-url "/dataset-items/" item-id)
                  :params          {:data data :name name}
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::edit-dataset-item-success]
                  :on-failure      [:api-request-error :edit-dataset-item]}}))

(re-frame/reg-event-fx
  ::edit-dataset-item-success
  (fn [{:keys [db]} _]
    (let [course-id (:current-course db)]
      {:dispatch-n (list [:complete-request :edit-dataset-item] [::ee/load-lesson-sets course-id])
       :redirect [:course-editor-v2 :id course-id]})))

(re-frame/reg-event-fx
  ::delete-dataset-item
  (fn [{:keys [db]} [_ item-id]]
    {:db (assoc-in db [:loading :delete-dataset-item] true)
     :http-xhrio {:method          :delete
                  :uri             (api-url "/dataset-items/" item-id)
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::delete-dataset-item-success]
                  :on-failure      [:api-request-error :delete-dataset-item]}}))


(re-frame/reg-event-fx
  ::delete-dataset-item-success
  (fn [{:keys [db]} _]
    (let [course-id (:current-course db)]
      {:dispatch-n (list [:complete-request :delete-dataset-item] [::ee/load-lesson-sets course-id] [::close-delete-dataset-item-modal])})))

(re-frame/reg-event-fx
  ::upload-asset
  (fn [{:keys [db]} [_ js-file-value {:keys [type] :as params}]]
    (let [form-data (doto
                      (js/FormData.)
                      (.append "file" js-file-value)
                      (.append "type" (name type)))]
      {:db (assoc-in db [:loading :upload-asset] true)
       :http-xhrio {:method          :post
                    :uri             (api-url "/assets/")
                    :body            form-data
                    :response-format (json-response-format {:keywords? true})
                    :on-success      [::upload-asset-success params]
                    :on-failure      [:api-request-error :upload-asset]}})))

(re-frame/reg-event-fx
  ::upload-asset-success
  (fn [{:keys [db]} [_ {:keys [on-finish]} result]]
    (on-finish result)
    {:dispatch-n (list [:complete-request :upload-asset])}))

(re-frame/reg-event-fx
  ::open-delete-dataset-item-modal
  (fn [{:keys [db]} [_ state]]
    {:db (assoc-in db [:editor-v2 :concepts :delete-dataset-item-modal-state] state)}))

(re-frame/reg-event-fx
  ::close-delete-dataset-item-modal
  (fn [{:keys [db]} _]
    {:db (assoc-in db [:editor-v2 :concepts :delete-dataset-item-modal-state] false)}))
