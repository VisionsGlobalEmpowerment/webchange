(ns webchange.warehouse
  (:require
    [re-frame.core :as re-frame]
    [ajax.core :refer [json-request-format json-response-format]]))

(re-frame/reg-event-fx
  ::load-skills
  (fn [{:keys [db]} [_ {:keys [on-success on-failure]}]]
    {:db         (assoc-in db [:loading :load-skills] true)
     :http-xhrio (cond-> {:method          :get
                          :uri             (str "/api/skills")
                          :format          (json-request-format)
                          :response-format (json-response-format {:keywords? true})}
                         (some? on-success) (assoc :on-success on-success)
                         (some? on-failure) (assoc :on-failure on-failure))}))

(re-frame/reg-event-fx
  ::update-scene-skills
  (fn [{:keys [db]} [_ {:keys [course-id scene-id skills-ids]} {:keys [on-success on-failure]}]]
    {:db         (assoc-in db [:loading :update-scene-skills] true)
     :http-xhrio (cond-> {:method          :post
                          :uri             (str "/api/courses/" course-id "/scenes/" scene-id "/skills")
                          :params          {:skills skills-ids}
                          :format          (json-request-format)
                          :response-format (json-response-format {:keywords? true})}
                         (some? on-success) (assoc :on-success on-success)
                         (some? on-failure) (assoc :on-failure on-failure))}))
