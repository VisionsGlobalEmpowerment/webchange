(ns webchange.editor-v2.wizard.state.activity
  (:require
    [re-frame.core :as re-frame]
    [ajax.core :refer [json-request-format json-response-format]]
    [webchange.editor-v2.wizard.state.db :refer [path-to-db]]))

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
  (fn [{:keys [db]} [_]]
    {:db         (assoc-in db [:loading :create-activity] true)
     :http-xhrio {:method          :post
                  :uri             (str "/api/datasets-library")
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::load-datasets-library-success]
                  :on-failure      [:api-request-error :create-course]}}))

(re-frame/reg-event-fx
  ::load-datasets-library-success
  (fn [{:keys [db]} [_ result]]
    {:db         (assoc-in db (path-to-db [:datasets-library]) result)
     :dispatch-n (list [:complete-request :create-course])}))
