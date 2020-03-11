(ns webchange.editor-v2.events
  (:require
    [re-frame.core :as re-frame]
    [ajax.core :refer [json-request-format json-response-format]]
    [webchange.interpreter.events :as ie]
    [webchange.editor-v2.translator.events :as translator-events]))

(re-frame/reg-event-fx
  ::init-editor
  (fn [_ [_ course-id scene-id]]
    {:dispatch-n (list [::ie/start-course course-id scene-id]
                       [::load-lesson-sets course-id]
                       [::set-diagram-mode :phrases])})) ;; :full-scene / :phrases

(re-frame/reg-event-fx
  ::load-lesson-sets
  (fn [{:keys [db]} [_ course-id]]
    {:db (-> db
             (assoc-in [:loading :lesson-sets] true))
     :http-xhrio {:method          :get
                  :uri             (str "/api/courses/" course-id "/lesson-sets")
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::load-lesson-sets-success]
                  :on-failure      [:api-request-error :lesson-sets]}}))

(re-frame/reg-event-fx
  ::load-lesson-sets-success
  (fn [{:keys [db]} [_ result]]
    {:db (-> db
             (assoc-in [:editor :course-datasets] (:datasets result))
             (assoc-in [:editor :course-dataset-items] (:items result))
             (assoc-in [:editor :course-lesson-sets] (:lesson-sets result)))
     :dispatch-n (list [:complete-request :lesson-sets])}))

(re-frame/reg-event-fx
  ::set-diagram-mode
  (fn [{:keys [db]} [_ mode]]
    {:db (assoc-in db [:editor-v2 :diagram-mode] mode)}))

(re-frame/reg-event-fx
  ::set-current-action
  (fn [{:keys [db]} [_ action]]
    {:db (assoc-in db [:editor-v2 :current-action] action)}))

(re-frame/reg-event-fx
  ::show-translator-form
  (fn [{:keys [_]} [_ action]]
    {:dispatch-n (list [::set-current-action action]
                       [::translator-events/open-translator-modal])}))
