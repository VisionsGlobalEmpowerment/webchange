(ns webchange.editor-v2.wizard.state.activity
  (:require
    [re-frame.core :as re-frame]
    [ajax.core :refer [json-request-format json-response-format]]
    [webchange.editor-v2.wizard.state.db :refer [path-to-db]]
    [webchange.routes :refer [redirect-to]]))

(re-frame/reg-event-fx
  ::load-templates
  (fn [{:keys [db]} [_]]
    {:db         (assoc-in db [:loading :load-templates] true)
     :http-xhrio {:method          :get
                  :uri             (str "/api/templates")
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::load-templates-success]
                  :on-failure      [:api-request-error :load-templates]}}))

(re-frame/reg-event-fx
  ::load-templates-success
  (fn [{:keys [db]} [_ result]]
    {:db         (assoc-in db (path-to-db [:templates]) result)
     :dispatch-n (list [:complete-request :load-templates])}))

(re-frame/reg-sub
  ::templates
  (fn [db]
    (get-in db (path-to-db [:templates]))))

(re-frame/reg-event-fx
  ::load-skills
  (fn [{:keys [db]} [_]]
    {:db         (assoc-in db [:loading :load-skills] true)
     :http-xhrio {:method          :get
                  :uri             (str "/api/skills")
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::load-skills-success]
                  :on-failure      [:api-request-error :load-skills]}}))

(re-frame/reg-event-fx
  ::load-skills-success
  (fn [{:keys [db]} [_ result]]
    {:db         (assoc-in db (path-to-db [:skills]) result)
     :dispatch-n (list [:complete-request :load-skills])}))

(re-frame/reg-sub
  ::skills
  (fn [db]
    (get-in db (path-to-db [:skills]))))

(re-frame/reg-event-fx
  ::create-activity
  (fn [{:keys [db]} [_ course-slug data callback]]
    {:db         (assoc-in db [:loading :create-activity] true)
     :http-xhrio {:method          :post
                  :uri             (str "/api/courses/" course-slug "/create-activity")
                  :params          data
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::create-activity-success callback]
                  :on-failure      [:api-request-error :create-activity]}}))

(re-frame/reg-event-fx
  ::create-activity-version
  (fn [{:keys [db]} [_ course-slug scene-slug data callback]]
    {:db         (assoc-in db [:loading :create-activity-version] true)
     :http-xhrio {:method          :post
                  :uri             (str "/api/courses/" course-slug "/scenes/" scene-slug "/versions")
                  :params          data
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::create-activity-version-success callback]
                  :on-failure      [:api-request-error :create-activity-version]}}))

(re-frame/reg-event-fx
  ::update-activity
  (fn [{:keys [db]} [_ course-slug data scene-id callback]]
    {:db         (assoc-in db [:loading :update-activity] true)
     :http-xhrio {:method          :post
                  :uri             (str "/api/courses/" course-slug "/update-activity/"scene-id)
                  :params          data
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::update-activity-success callback]
                  :on-failure      [:api-request-error :update-activity]}}))

(re-frame/reg-event-fx
  ::create-activity-success
  (fn [{:keys [db]} [_ callback result]]
    (when (some? callback)
      (callback result))
    {:dispatch-n (list [:complete-request :create-activity])}))

(re-frame/reg-event-fx
  ::update-activity-success
  (fn [{:keys [db]} [_ callback result]]
    (when (some? callback)
      (callback result))
    {:dispatch-n (list [:complete-request :update-activity])}))

(re-frame/reg-event-fx
  ::create-activity-version-success
  (fn [{:keys [db]} [_ callback result]]
    (when (some? callback)
      (callback result))
    {:dispatch-n (list [:complete-request :create-activity-version])}))

(re-frame/reg-event-fx
  ::create-simple-activity
  (fn [{:keys [db]} [_ data]]
    (let [course-data (select-keys data [:name :lang])]
      {:db         (assoc-in db [:loading :create-course] true)
       :http-xhrio {:method          :post
                    :uri             (str "/api/courses")
                    :params          course-data
                    :format          (json-request-format)
                    :response-format (json-response-format {:keywords? true})
                    :on-success      [::create-simple-activity-success data]
                    :on-failure      [:api-request-error :create-course]}})))

(re-frame/reg-event-fx
  ::create-simple-activity-success
  (fn [{:keys [db]} [_ data result]]
    (let [redirect #(redirect-to :course-editor-v2-scene :id (:course-slug %) :scene-id (:scene-slug %))]
      {:dispatch-n (list [:complete-request :create-course]
                         [::create-activity (:slug result) data redirect])})))
