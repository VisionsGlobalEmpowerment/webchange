(ns webchange.warehouse
  (:require
    [re-frame.core :as re-frame]
    [ajax.core :refer [json-request-format json-response-format]]
    [webchange.state :as state]))

(defn- create-request
  [{:keys [method uri params]} {:keys [on-success on-failure]}]
  {:http-xhrio (cond-> {:method          method
                        :uri             uri
                        :format          (json-request-format)
                        :response-format (json-response-format {:keywords? true})
                        :on-success      [::empty-success-handler]}
                       (some? params) (assoc :params params)
                       (some? on-success) (assoc :on-success on-success)
                       (some? on-failure) (assoc :on-failure on-failure))})

(re-frame/reg-event-fx
  ::empty-success-handler
  (fn [] {}))

(re-frame/reg-event-fx
  ::load-skills
  (fn [{:keys [_]} [_ handlers]]
    (create-request {:method :get
                     :uri    (str "/api/skills")} handlers)))

(re-frame/reg-event-fx
  ::update-scene-skills
  (fn [{:keys [_]} [_ {:keys [course-id scene-id skills-ids]} handlers]]
    (create-request {:method :post
                     :uri    (str "/api/courses/" course-id "/scenes/" scene-id "/skills")
                     :params {:skills skills-ids}} handlers)))

(re-frame/reg-event-fx
  ::save-course
  (fn [{:keys [_]} [_ {:keys [course-id course-data]} handlers]]
    (create-request {:method :post
                     :uri    (str "/api/courses/" course-id)
                     :params {:course course-data}} handlers)))

;; Lesson sets

(re-frame/reg-event-fx
  ::save-lesson-set
  (fn [{:keys [_]} [_ {:keys [dataset-id name data]} handlers]]
    (create-request {:method :post
                     :uri    (str "/api/lesson-sets")
                     :params {:dataset-id dataset-id
                              :name       name
                              :data       data}}
                    {:on-success [::save-lesson-set-success handlers]
                     :on-failure (:on-failure handlers)})))

(re-frame/reg-event-fx
  ::save-lesson-set-success
  (fn [{:keys [_]} [_ {:keys [on-success]} {:keys [lesson] :as response}]]
    {:dispatch-n (cond-> [[::state/update-lesson-set (:name lesson) lesson]]
                         (some? on-success) (conj (conj on-success response)))}))

(re-frame/reg-event-fx
  ::update-lesson-set
  (fn [{:keys [_]} [_ {:keys [id data]} handlers]]
    (create-request {:method :put
                     :uri    (str "/api/lesson-sets/" id)
                     :params {:data data}} handlers)))

(re-frame/reg-event-fx
  ::delete-lesson-set
  (fn [{:keys [_]} [_ {:keys [id]} handlers]]
    (create-request {:method :delete
                     :uri    (str "/api/lesson-sets/" id)}
                    {:on-success [::delete-lesson-set-success handlers]
                     :on-failure (:on-failure handlers)})))

(re-frame/reg-event-fx
  ::delete-lesson-set-success
  (fn [{:keys [_]} [_ {:keys [on-success]} {:keys [id] :as response}]]
    {:dispatch-n (cond-> [[::state/delete-lesson-set-by-id id]]
                         (some? on-success) (conj (conj on-success response)))}))

(re-frame/reg-event-fx
  ::create-activity-placeholder
  (fn [_ [_ {:keys [course-id data]} handlers]]
    (create-request {:method :post
                     :uri    (str "/api/courses/" course-id "/activity-placeholder")
                     :params {:data data}} handlers)))
