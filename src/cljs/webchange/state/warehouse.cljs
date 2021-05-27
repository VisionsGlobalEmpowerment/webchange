(ns webchange.state.warehouse
  (:require
    [re-frame.core :as re-frame]
    [ajax.core :refer [json-request-format json-response-format]]))

(defn- path-to-db
  [relative-path]
  (->> relative-path
       (concat [:warehouse])
       (vec)))

(defn- get-form-data
  [form-params]
  (reduce (fn [form-data [key value]]
            (.append form-data key value)
            form-data)
          (js/FormData.)
          form-params))

(defn- create-request
  [{:keys [method uri body params request-type] :as props} {:keys [on-success on-failure]}]
  {:dispatch-n (cond-> []
                       (some? request-type) (conj [::set-sync-status {:key request-type :in-progress? true}]))
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
  (fn [{:keys [_]} [_ {:keys [request-type]} success-handler response]]
    {:dispatch-n (cond-> []
                         (some? request-type) (conj [::set-sync-status {:key request-type :in-progress? false}])
                         (some? success-handler) (conj (conj success-handler response)))}))

(re-frame/reg-event-fx
  ::generic-failure-handler
  (fn [{:keys [_]} [_ {:keys [key request-type]} failure-handler response]]
    {:dispatch-n (cond-> [[:api-request-error key response]]
                         (some? request-type) (conj [::set-sync-status {:key request-type :in-progress? false}])
                         (some? failure-handler) (conj (conj failure-handler response)))}))

(def sync-status-path (path-to-db [:sync-status]))

(re-frame/reg-event-fx
  ::set-sync-status
  (fn [{:keys [db]} [_ {:keys [key in-progress?]}]]
    {:db (assoc-in db (conj sync-status-path key) in-progress?)}))

(re-frame/reg-sub
  ::sync-status
  (fn [db [_ key]]
    (get-in db (conj sync-status-path key))))

;; Templates

(re-frame/reg-event-fx
  ::load-templates
  (fn [{:keys [_]} [_ handlers]]
    (create-request {:key    :load-templates
                     :method :get
                     :uri    (str "/api/templates")}
                    handlers)))

(re-frame/reg-event-fx
  ::update-activity
  (fn [{:keys [_]} [_ {:keys [course-id scene-id data]} handlers]]
    (create-request {:key          :update-activity
                     :method       :post
                     :uri          (str "/api/courses/" course-id "/update-activity/" scene-id)
                     :params       data
                     :request-type :update-activity}
                    handlers)))

;; Courses

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

(re-frame/reg-event-fx
  ::create-course
  (fn [{:keys [_]} [_ {:keys [course-data]} handlers]]
    {:pre [(map? course-data)
           (string? (:name course-data))
           (string? (:lang course-data))]}
    (create-request {:key    :create-course
                     :method :post
                     :uri    (str "/api/courses")
                     :params course-data} handlers)))

;;

(re-frame/reg-event-fx
  ::load-skills
  (fn [{:keys [_]} [_ handlers]]
    (create-request {:key    :load-skills
                     :method :get
                     :uri    (str "/api/skills")} handlers)))


;; Scene

(re-frame/reg-event-fx
  ::load-scene
  (fn [{:keys [_]} [_ {:keys [course-slug scene-slug]} handlers]]
    (create-request {:key    :load-activity
                     :method :get
                     :uri    (str "/api/courses/" course-slug "/scenes/" scene-slug)}
                    handlers)))

(re-frame/reg-event-fx
  ::create-activity
  (fn [{:keys [_]} [_ {:keys [course-slug activity-data]} handlers]]
    {:pre [(string? course-slug)
           (map? activity-data)
           (string? (:name activity-data))
           (sequential? (:skills activity-data))]}
    (create-request {:key    :create-activity
                     :method :post
                     :uri    (str "/api/courses/" course-slug "/create-activity")
                     :params activity-data} handlers)))

(re-frame/reg-event-fx
  ::save-scene
  (fn [{:keys [_]} [_ {:keys [course-slug scene-slug scene-data]} handlers]]
    (create-request {:key          :save-scene
                     :method       :put
                     :uri          (str "/api/courses/" course-slug "/scenes/" scene-slug)
                     :params       {:scene scene-data}
                     :request-type :update-activity}
                    handlers)))

(re-frame/reg-event-fx
  ::save-scene-post
  (fn [{:keys [_]} [_ {:keys [course-id scene-id scene-data]} handlers]]
    (create-request {:key          :save-scene
                     :method       :post
                     :uri          (str "/api/courses/" course-id "/scenes/" scene-id)
                     :params       {:scene scene-data}
                     :request-type :update-activity}
                    handlers)))

(re-frame/reg-event-fx
  ::update-scene-skills
  (fn [{:keys [_]} [_ {:keys [course-id scene-id skills-ids]} handlers]]
    (create-request {:key    :update-scene-skills
                     :method :post
                     :uri    (str "/api/courses/" course-id "/scenes/" scene-id "/skills")
                     :params {:skills skills-ids}} handlers)))

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

(re-frame/reg-event-fx
  ::load-backgrounds
  (fn [{:keys [_]} [_ handlers]]
    (create-request {:key    :load-backgrounds
                     :method :get
                     :uri    (str "/api/courses/editor/assets")}
                    handlers)))

(re-frame/reg-event-fx
  ::load-animation-skins
  (fn [{:keys [_]} [_ handlers]]
    (create-request {:key    :load-animation-skins
                     :method :get
                     :uri    (str "/api/courses/editor/character-skin")}
                    handlers)))

;; Scene History

(re-frame/reg-event-fx
  ::load-versions
  (fn [{:keys [_]} [_ {:keys [course-slug scene-slug]} handlers]]
    (create-request {:key    :load-versions
                     :method :get
                     :uri    (str "/api/courses/" course-slug "/scenes/" scene-slug "/versions")}
                    handlers)))

(re-frame/reg-event-fx
  ::restore-version
  (fn [{:keys [_]} [_ {:keys [scene-version-id]} handlers]]
    (create-request {:key    :restore-version
                     :method :post
                     :uri    (str "/api/scene-versions/" scene-version-id "/restore")}
                    handlers)))

(re-frame/reg-event-fx
  ::load-template
  (fn [{:keys [_]} [_ {:keys [template-id]} handlers]]
    (create-request {:key    :load-template
                     :method :get
                     :uri    (str "/api/templates/" template-id "/metadata")}
                    handlers)))
