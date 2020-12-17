(ns webchange.warehouse
  (:require
    [re-frame.core :as re-frame]
    [ajax.core :refer [json-request-format json-response-format]]))

(re-frame/reg-event-fx
  ::load-skills
  (fn [{:keys [_]} [_ {:keys [on-success on-failure]}]]
    {:http-xhrio (cond-> {:method          :get
                          :uri             (str "/api/skills")
                          :format          (json-request-format)
                          :response-format (json-response-format {:keywords? true})}
                         (some? on-success) (assoc :on-success on-success)
                         (some? on-failure) (assoc :on-failure on-failure))}))

(re-frame/reg-event-fx
  ::update-scene-skills
  (fn [{:keys [_]} [_ {:keys [course-id scene-id skills-ids]} {:keys [on-success on-failure]}]]
    {:http-xhrio (cond-> {:method          :post
                          :uri             (str "/api/courses/" course-id "/scenes/" scene-id "/skills")
                          :params          {:skills skills-ids}
                          :format          (json-request-format)
                          :response-format (json-response-format {:keywords? true})}
                         (some? on-success) (assoc :on-success on-success)
                         (some? on-failure) (assoc :on-failure on-failure))}))

(re-frame/reg-event-fx
  ::save-course
  (fn [{:keys [_]} [_ {:keys [course-id course-data]} {:keys [on-success on-failure]}]]
    {:http-xhrio (cond-> {:method          :post
                          :uri             (str "/api/courses/" course-id)
                          :params          {:course course-data}
                          :format          (json-request-format)
                          :response-format (json-response-format {:keywords? true})}
                         (some? on-success) (assoc :on-success on-success)
                         (some? on-failure) (assoc :on-failure on-failure))}))

(re-frame/reg-event-fx
  ::update-lesson-set
  (fn [{:keys [db]} [_ {:keys [id data]} {:keys [on-success on-failure]}]]
    {:db         (assoc-in db [:loading :edit-lesson-set] true)
     :http-xhrio (cond-> {:method          :put
                          :uri             (str "/api/lesson-sets/" id)
                          :params          {:data data}
                          :format          (json-request-format)
                          :response-format (json-response-format {:keywords? true})}
                         (some? on-success) (assoc :on-success on-success)
                         (some? on-failure) (assoc :on-failure on-failure))}))
