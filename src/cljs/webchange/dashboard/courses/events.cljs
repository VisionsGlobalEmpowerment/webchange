(ns webchange.dashboard.courses.events
  (:require
    [re-frame.core :as re-frame]
    [ajax.core :refer [json-request-format json-response-format]]))

(re-frame/reg-event-fx
  ::load-courses
  (fn [{:keys [db]} _]
    (let [type "course"
          status "in-review"]
      {:db         (-> db
                       (assoc-in [:loading :courses] true))
       :http-xhrio {:method          :get
                    :uri             (str "/api/courses/list/" type "/" status)
                    :format          (json-request-format)
                    :response-format (json-response-format {:keywords? true})
                    :on-success      [::load-courses-success]
                    :on-failure      [:api-request-error :courses]}})))

(re-frame/reg-event-fx
  ::load-books
  (fn [{:keys [db]} _]
    (let [type "book"
          status "in-review"]
      {:db         (-> db
                       (assoc-in [:loading :books] true))
       :http-xhrio {:method          :get
                    :uri             (str "/api/courses/list/" type "/" status)
                    :format          (json-request-format)
                    :response-format (json-response-format {:keywords? true})
                    :on-success      [::load-books-success]
                    :on-failure      [:api-request-error :courses]}})))

(re-frame/reg-event-fx
  ::load-courses-success
  (fn [{:keys [db]} [_ result]]
    {:db         (assoc-in db [:dashboard :courses :courses] result)
     :dispatch-n (list [:complete-request :courses])}))

(re-frame/reg-event-fx
  ::load-books-success
  (fn [{:keys [db]} [_ result]]
    {:db         (assoc-in db [:dashboard :courses :books] result)
     :dispatch-n (list [:complete-request :books])}))

(re-frame/reg-event-fx
  ::approve
  (fn [{:keys [db]} [_ course-id]]
    (let [data {:status "published"}]
      {:db         (assoc-in db [:loading :approve-course] true)
       :http-xhrio {:method          :post
                    :uri             (str "/api/courses/" course-id "/review")
                    :params          data
                    :format          (json-request-format)
                    :response-format (json-response-format {:keywords? true})
                    :on-success      [::approve-success]
                    :on-failure      [:api-request-error :approve-course]}})))


(re-frame/reg-event-fx
  ::approve-success
  (fn [_ _]
    {:dispatch-n (list [:complete-request :approve-course]
                       [::load-courses]
                       [::load-books])}))

(re-frame/reg-event-fx
  ::request-changes
  (fn [{:keys [db]} [_ course-id]]
    (let [data {:status "changes-requested"}]
      {:db         (assoc-in db [:loading :request-changes-course] true)
       :http-xhrio {:method          :post
                    :uri             (str "/api/courses/" course-id "/review")
                    :params          data
                    :format          (json-request-format)
                    :response-format (json-response-format {:keywords? true})
                    :on-success      [::request-changes-success]
                    :on-failure      [:api-request-error :request-changes-course]}})))


(re-frame/reg-event-fx
  ::request-changes-success
  (fn [_ _]
    {:dispatch-n (list [:complete-request :request-changes-course]
                       [::load-courses]
                       [::load-books])}))

(re-frame/reg-event-fx
  ::decline
  (fn [{:keys [db]} [_ course-id]]
    (let [data {:status "declined"}]
      {:db         (assoc-in db [:loading :decline-course] true)
       :http-xhrio {:method          :post
                    :uri             (str "/api/courses/" course-id "/review")
                    :params          data
                    :format          (json-request-format)
                    :response-format (json-response-format {:keywords? true})
                    :on-success      [::decline-success]
                    :on-failure      [:api-request-error :decline-course]}})))


(re-frame/reg-event-fx
  ::decline-success
  (fn [_ _]
    {:dispatch-n (list [:complete-request :decline-course]
                       [::load-courses]
                       [::load-books])}))
