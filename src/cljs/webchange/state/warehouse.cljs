(ns webchange.state.warehouse
  (:require
    [re-frame.core :as re-frame]
    [ajax.core :refer [json-request-format json-response-format]]
    [webchange.sync-status.state :as sync-status]))

(defn- get-form-data
  [form-params]
  (reduce (fn [form-data [key value]]
            (.append form-data key value)
            form-data)
          (js/FormData.)
          form-params))

(defn- create-request
  [{:keys [method uri body params show-sync?] :as props} {:keys [on-success on-failure]}]
  {:dispatch-n (cond-> []
                       show-sync? (conj [::sync-status/show]))
   :http-xhrio (cond-> {:method          method
                        :uri             uri
                        :format          (json-request-format)
                        :response-format (json-response-format {:keywords? true})
                        :on-success      [::generic-on-success-handler props on-success]
                        :on-failure      [::generic-failure-handler props on-failure]}
                       (some? params) (assoc :params params)
                       (some? body) (assoc :body body))})

(re-frame/reg-event-fx
  ::generic-on-success-handler
  (fn [{:keys [_]} [_ {:keys [show-sync?]} success-handler response]]
    {:dispatch-n (cond-> []
                         show-sync? (conj [::sync-status/hide])
                         (some? success-handler) (conj (conj success-handler response)))}))

(re-frame/reg-event-fx
  ::generic-failure-handler
  (fn [{:keys [_]} [_ {:keys [key show-sync?]} failure-handler response]]
    {:dispatch-n (cond-> [[:api-request-error key response]]
                         show-sync? (conj [::sync-status/hide])
                         (some? failure-handler) (conj (conj failure-handler response)))}))

(re-frame/reg-event-fx
  ::load-skills
  (fn [{:keys [_]} [_ handlers]]
    (create-request {:key    :load-skills
                     :method :get
                     :uri    (str "/api/skills")} handlers)))

(re-frame/reg-event-fx
  ::update-scene-skills
  (fn [{:keys [_]} [_ {:keys [course-id scene-id skills-ids]} handlers]]
    (create-request {:key    :update-scene-skills
                     :method :post
                     :uri    (str "/api/courses/" course-id "/scenes/" scene-id "/skills")
                     :params {:skills skills-ids}} handlers)))

(re-frame/reg-event-fx
  ::load-course
  (fn [{:keys [_]} [_ course-slug handlers]]
    (create-request {:key    :load-course
                     :method :get
                     :uri    (str "/api/courses/" course-slug)}
                    handlers)))

(re-frame/reg-event-fx
  ::load-course-info
  (fn [{:keys [_]} [_ course-slug handlers]]
    (create-request {:key    :load-course-info
                     :method :get
                     :uri    (str "/api/courses/" course-slug "/info")}
                    handlers)))

(re-frame/reg-event-fx
  ::publish-course
  (fn [{:keys [_]} [_ course-slug handlers]]
    (create-request {:key    :publish-course
                     :method :post
                     :uri    (str "/api/courses/" course-slug "/publish")}
                    handlers)))

(re-frame/reg-event-fx
  ::save-course
  (fn [{:keys [_]} [_ {:keys [course-slug course-data]} handlers]]
    (create-request {:key    :save-course
                     :method :post
                     :uri    (str "/api/courses/" course-slug)
                     :params {:course course-data}} handlers)))

;; Scene

(re-frame/reg-event-fx
  ::save-scene
  (fn [{:keys [_]} [_ {:keys [course-id scene-id scene-data]} handlers]]
    (create-request {:key        :save-scene
                     :method     :put
                     :uri        (str "/api/courses/" course-id "/scenes/" scene-id)
                     :params     {:scene scene-data}
                     :show-sync? true}
                    handlers)))

;; Lesson sets

(re-frame/reg-event-fx
  ::create-lesson-set
  (fn [{:keys [_]} [_ {:keys [dataset-id name data]} handlers]]
    (create-request {:key    :create-lesson-set
                     :method :post
                     :uri    (str "/api/lesson-sets")
                     :params {:dataset-id dataset-id
                              :name       name
                              :data       data}}
                    handlers)))

(re-frame/reg-event-fx
  ::update-lesson-set
  (fn [{:keys [_]} [_ {:keys [id data]} handlers]]
    (create-request {:key    :update-lesson-set
                     :method :put
                     :uri    (str "/api/lesson-sets/" id)
                     :params {:data data}} handlers)))

(re-frame/reg-event-fx
  ::delete-lesson-set
  (fn [{:keys [_]} [_ {:keys [id]} handlers]]
    (create-request {:key    :delete-lesson-set
                     :method :delete
                     :uri    (str "/api/lesson-sets/" id)}
                    handlers)))

(re-frame/reg-event-fx
  ::create-activity-placeholder
  (fn [_ [_ {:keys [course-id data]} handlers]]
    (create-request {:key    :create-activity-placeholder
                     :method :post
                     :uri    (str "/api/courses/" course-id "/create-activity-placeholder")
                     :params data} handlers)))

;; Static assets

(re-frame/reg-event-fx
  ::upload-file
  (fn [{:keys [db]} [_ {:keys [file form-params]
                        :or   {form-params []}} handlers]]
    (let [form-data (get-form-data (concat [["file" file]] form-params))]
      (create-request {:key    :upload-file
                       :method :post
                       :uri    (str "/api/assets/")
                       :body   form-data} handlers))))

(re-frame/reg-event-fx
  ::upload-audio-blob
  (fn [{:keys [_]} [_ {:keys [blob]} handlers]]
    {:dispatch [::upload-file {:file        blob
                               :form-params [["type" "blob"]
                                             ["blob-type" "audio"]]} handlers]}))

(re-frame/reg-event-fx
  ::upload-image-blob
  (fn [{:keys [_]} [_ {:keys [blob]} handlers]]
    {:dispatch [::upload-file {:file        blob
                               :form-params [["type" "blob"]
                                             ["blob-type" "image"]]} handlers]}))

;; Activity Template

(re-frame/reg-event-fx
  ::update-activity
  (fn [{:keys [_]} [_ {:keys [course-id scene-id data]} handlers]]
    (create-request {:key        :update-activity
                     :method     :post
                     :uri        (str "/api/courses/" course-id "/update-activity/" scene-id)
                     :params     data
                     :show-sync? true}
                    handlers)))
