(ns webchange.editor-v2.wizard.state.course
  (:require
    [re-frame.core :as re-frame]
    [ajax.core :refer [json-request-format json-response-format]]
    [webchange.editor-v2.wizard.state.db :refer [path-to-db]]))

(re-frame/reg-event-fx
  ::load-datasets-library
  (fn [{:keys [db]} [_]]
    {:db         (assoc-in db [:loading :load-datasets-library] true)
     :http-xhrio {:method          :get
                  :uri             (str "/api/datasets-library")
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::load-datasets-library-success]
                  :on-failure      [:api-request-error :load-datasets-library]}}))

(re-frame/reg-event-fx
  ::load-datasets-library-success
  (fn [{:keys [db]} [_ result]]
    {:db         (assoc-in db (path-to-db [:datasets-library]) result)
     :dispatch-n (list [:complete-request :load-datasets-library])}))

(re-frame/reg-sub
  ::datasets-library
  (fn [db]
    (get-in db (path-to-db [:datasets-library]))))

(re-frame/reg-event-fx
  ::create-course
  (fn [{:keys [db]} [_ data callback]]
    {:db         (assoc-in db [:loading :create-course] true)
     :http-xhrio {:method          :post
                  :uri             (str "/api/courses")
                  :params          data
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::create-course-success callback]
                  :on-failure      [:api-request-error :create-course]}}))

(re-frame/reg-event-fx
  ::create-course-success
  (fn [{:keys [db]} [_ callback result]]
    (when (some? callback)
      (callback result))
    {:dispatch-n (list [:complete-request :create-course])}))

(re-frame/reg-event-fx
  ::redirect-to-editor
  (fn [{:keys [db]} [_ course-slug scene-slug]]
    {:redirect [:course-editor-scene :id course-slug :scene-id scene-slug]}))
