(ns webchange.state.state-assets
  (:require
    [re-frame.core :as re-frame]
    [day8.re-frame.http-fx]
    [ajax.core :refer [json-response-format]]))

(re-frame/reg-event-fx
  ::upload-asset
  (fn [{:keys [db]} [_ js-file-value {:keys [type options] :as params}]]
    (let [form-data (doto
                        (js/FormData.)
                      (.append "file" js-file-value)
                      (.append "type" (name type)))]
      (when options (.append form-data "options" options))
      {:db (assoc-in db [:loading :upload-asset] true)
       :http-xhrio {:method          :post
                    :uri             (str "/api/assets/")
                    :body            form-data
                    :response-format (json-response-format {:keywords? true})
                    :on-success      [::upload-asset-success params]
                    :on-failure      [:api-request-error :upload-asset]}})))

(re-frame/reg-event-fx
  ::upload-asset-success
  (fn [{:keys [_db]} [_ {:keys [on-finish]} result]]
    (on-finish result)
    {:dispatch-n (list [:complete-request :upload-asset])}))
