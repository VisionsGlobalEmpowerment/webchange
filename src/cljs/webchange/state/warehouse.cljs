(ns webchange.state.warehouse
  (:require
    [re-frame.core :as re-frame]
    [ajax.core :refer [json-request-format json-response-format]]
    [webchange.config :as config]))

(defn path-to-db
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

(defn dispatch-if-defined
  [handler & args]
  (if (some? handler)
    {:dispatch (-> handler (concat args) (vec))}
    {}))

(defn- create-request
  [{:keys [method uri body params request-type] :as props} {:keys [on-success on-failure suppress-api-error?] :or {suppress-api-error? false}}]
  {:dispatch-n (cond-> []
                       (some? request-type) (conj [::set-sync-status {:key request-type :in-progress? true}]))
   :http-xhrio (cond-> {:method          method
                        :uri             uri
                        :format          (json-request-format)
                        :response-format (json-response-format {:keywords? true})
                        :on-success      [::generic-on-success-handler props on-success]
                        :on-failure      [::generic-failure-handler props on-failure suppress-api-error?]}
                       (some? params) (assoc (if (= method :get) :url-params :params) params)
                       (some? body) (assoc :body body))})

(re-frame/reg-event-fx
  ::generic-on-success-handler
  (fn [{:keys [_]} [_ {:keys [request-type delay]} success-handler response]]
    (let [handle-success (conj success-handler response)]
      (cond-> {:dispatch-n (cond-> []
                                   (some? request-type) (conj [::set-sync-status {:key request-type :in-progress? false}])
                                   (and (nil? delay)
                                        (some? success-handler)) (conj handle-success))}
              (and (some? success-handler)
                   (some? delay)) (assoc :timeout {:event handle-success
                                                   :time  delay})))))

(re-frame/reg-event-fx
  ::generic-failure-handler
  (fn [{:keys [_]} [_ {:keys [key request-type]} failure-handler suppress-api-error? response]]
    {:dispatch-n (cond-> []
                         (not suppress-api-error?) (conj [:api-request-error key response])
                         (some? request-type) (conj [::set-sync-status {:key request-type :in-progress? false}])
                         (some? failure-handler) (conj (conj failure-handler response)))}))

(re-frame.core/reg-fx
  :timeout
  (fn [{:keys [event time]}]
    (js/setTimeout #(re-frame.core/dispatch event) time)))

(defn- multiply-and-shuffle
  [{:keys [data multiply]
    :or   {multiply 1}}]
  "- data: e.g. {0 {...} 1 {...}}"
  (->> (range multiply)
       (map #(->> (count data) (range)))
       (flatten)
       (shuffle)
       (map #(get data %))))

(defn- create-request-stub
  [_
   handlers
   {:keys [timeout] :or {timeout 0} :as stub-params}]
  {:timeout {:event [::request-stub-handler stub-params handlers]
             :time  timeout}})

(re-frame/reg-event-fx
  ::request-stub-handler
  (fn [{:keys [_]} [_
                    {:keys [data result]
                     :or   {result :success}}
                    {:keys [on-success on-failure]}]]
    (case result
      :success (dispatch-if-defined on-success data)
      :failure (dispatch-if-defined on-failure data))))

(def sync-status-path (path-to-db [:sync-status]))

(re-frame/reg-event-fx
  ::set-sync-status
  (fn [{:keys [db]} [_ {:keys [key in-progress?]}]]
    {:db (assoc-in db (conj sync-status-path key) in-progress?)}))

(re-frame/reg-sub
  ::sync-status
  (fn [db [_ key]]
    (get-in db (conj sync-status-path key))))

;; Poll

(defn- with-poll
  [event {:keys [timeout attempts] :or {timeout  (if config/debug? 3000 1000)
                                        attempts (if config/debug? 3 400)}}]
  (re-frame/dispatch (vec (concat [::poll-attempt] event [{:timeout timeout :attempts (dec attempts)}]))))

(re-frame/reg-event-fx
  ::poll-attempt
  (fn [{:keys [_]} [_ event data handlers poll-params]]
    {:dispatch [event data {:on-success          (:on-success handlers)
                            :on-failure          [::poll-attempt-failed event data handlers poll-params]
                            :suppress-api-error? true}]}))

(re-frame/reg-event-fx
  ::poll-attempt-failed
  (fn [{:keys [_]} [_ event data handlers {:keys [timeout attempts] :as poll-params}]]
    (if (= attempts 0)
      {:dispatch (:on-failure handlers)}
      {:timeout {:event [::poll-attempt event data handlers (update poll-params :attempts dec)]
                 :time  timeout}})))

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

(re-frame/reg-event-fx
  ::load-available-courses
  (fn [{:keys [_]} [_ handlers]]
    (create-request {:key    :load-available-courses
                     :method :get
                     :uri    (str "/api/courses/available")}
                    handlers)))

(re-frame/reg-event-fx
  ::load-books
  (fn [{:keys [_]} [_ {:keys [language]} handlers]]
    (create-request {:key    :load-course-books
                     :method :get
                     :uri    (->> (or language "all")
                                  (str "/api/book-library/"))}
                    handlers)))

(re-frame/reg-event-fx
  ::load-book-categories
  (fn [{:keys [_]} [_ handlers]]
    (create-request {:key    :load-book-categories
                     :method :get
                     :uri    (str "/api/book-library/categories")}
                    handlers)))

(re-frame/reg-event-fx
  ::load-book-languages
  (fn [{:keys [_]} [_ handlers]]
    (create-request {:key    :load-book-languages
                     :method :get
                     :uri    (str "/api/book-library/languages")}
                    handlers)))

(re-frame/reg-event-fx
  ::load-book-ages
  (fn [{:keys [_]} [_ handlers]]
    (create-request {:key    :load-book-ages
                     :method :get
                     :uri    (str "/api/book-library/ages")}
                    handlers)))

(re-frame/reg-event-fx
  ::load-book-genres
  (fn [{:keys [_]} [_ handlers]]
    (create-request {:key    :load-book-genres
                     :method :get
                     :uri    (str "/api/book-library/genres")}
                    handlers)))

(re-frame/reg-event-fx
  ::load-book-reading-levels
  (fn [{:keys [_]} [_ handlers]]
    (create-request {:key    :load-book-reading-levels
                     :method :get
                     :uri    (str "/api/book-library/reading-levels")}
                    handlers)))

(re-frame/reg-event-fx
  ::load-book-tags
  (fn [{:keys [_]} [_ handlers]]
    (create-request {:key    :load-book-tags
                     :method :get
                     :uri    (str "/api/book-library/tags")}
                    handlers)))

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
  ::load-first-scene
  (fn [{:keys [_]} [_ {:keys [course-slug]} handlers]]
    (create-request {:key    :load-first-activity
                     :method :get
                     :uri    (str "/api/courses/" course-slug "/first-scene")}
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

(re-frame/reg-event-fx
  ::set-activity-preview
  (fn [{:keys [_]} [_ {:keys [course-slug scene-slug preview]} handlers]]
    (create-request {:key    :set-activity-preview
                     :method :put
                     :uri    (str "/api/courses/" course-slug "/scenes/" scene-slug "/preview")
                     :params {:preview preview}} handlers)))

;; Lesson sets

(re-frame/reg-event-fx
  ::load-lesson-sets
  (fn [{:keys [_]} [_ {:keys [course-slug]} handlers]]
    (create-request {:key    ::load-lesson-sets
                     :method :get
                     :uri    (str "/api/courses/" course-slug "/lesson-sets")}
                    handlers)))

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

(re-frame/reg-event-fx
  ::duplicate-activity
  (fn [_ [_ {:keys [course-id data]} handlers]]
    (create-request {:key    :duplicate-activity
                     :method :post
                     :uri    (str "/api/courses/" course-id "/duplicate-activity")
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
  ::load-audio-script
  (fn [{:keys [_]} [_ {:keys [file start duration]} handlers]]
    (create-request {:key    :load-audio-script
                     :method :get
                     :uri    (str "/api/actions/get-subtitles")
                     :params (cond-> {:file file}
                                     (some? start) (assoc :start start)
                                     (some? duration) (assoc :duration duration))}
                    handlers)))

(re-frame/reg-event-fx
  ::load-audio-script-polled
  (fn [{:keys [_]} [_ data handlers poll-params]]
    (with-poll [::load-audio-script data handlers] poll-params)))

(re-frame/reg-event-fx
  ::load-assets
  (fn [{:keys [_]} [_ {:keys [tag tags]} handlers]]
    (create-request {:key    :load-backgrounds
                     :method :get
                     :uri    (cond-> (str "/api/courses/editor/assets")
                                     (some? tag) (str "?tag=" tag)
                                     (not (empty? tags)) (str "?tags=" (clojure.string/join "," tags)))}
                    handlers)))

(re-frame/reg-event-fx
  ::load-assets-tags
  (fn [{:keys [_]} [_ handlers]]
    (create-request {:key    :load-backgrounds
                     :method :get
                     :uri    (str "/api/courses/editor/tags")}
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

;; Students

(re-frame/reg-event-fx
  ::load-parent-students
  (fn [{:keys [_]} [_ handlers]]
    (create-request {:key    :load-parent-students
                     :method :get
                     :uri    (str "/api/parent/students")}
                    handlers)))

(re-frame/reg-event-fx
  ::add-parent-student
  (fn [{:keys [_]} [_ {:keys [data]} handlers]]
    (create-request {:key    :add-parent-studnet
                     :method :post
                     :uri    (str "/api/parent/students")
                     :params data}
                    handlers)))

(re-frame/reg-event-fx
  ::delete-parent-student
  (fn [{:keys [_]} [_ {:keys [id]} handlers]]
    (create-request {:key    :delete-parent-student
                     :method :delete
                     :uri    (str "/api/parent/students/" id)}
                    handlers)))

(re-frame/reg-event-fx
  ::login-as-parent-student
  (fn [{:keys [_]} [_ {:keys [data]} handlers]]
    (create-request {:key    :login-as-parent-student
                     :method :post
                     :uri    (str "/api/parent/students/login")
                     :params data}
                    handlers)))

(re-frame/reg-event-fx
  ::login-as-student-parent
  (fn [{:keys [_]} [_ _ handlers]]
    (create-request {:key    :login-as-student-parent
                     :method :put
                     :uri    (str "/api/child/parent/login")}
                    handlers)))

(re-frame/reg-event-fx
  ::retry-audio-recognition
  (fn [{:keys [_]} [_ data handlers]]
    (create-request {:key    :retry-audio-recognition
                     :method :put
                     :uri    (str "/api/assets/retry-voice-recognition")
                     :params data}
                    handlers)))

(re-frame/reg-event-fx
  ::load-parent-courses
  (fn [{:keys [_]} [_ handlers]]
    (create-request {:key    :load-parent-courses
                     :method :get
                     :uri    "/api/parent/courses"}
                    handlers)))

;; Classes

(re-frame/reg-event-fx
  ::load-classes
  (fn [{:keys [_]} [_ handlers]]
    (create-request {:key    :load-classes
                     :method :get
                     :uri    (str "/api/classes")}
                    handlers)))

(re-frame/reg-event-fx
  ::load-class
  (fn [{:keys [_]} [_ class-id handlers]]
    (create-request {:key    :load-class
                     :method :get
                     :uri    (str "/api/classes/" class-id)}
                    handlers)))

(re-frame/reg-event-fx
  ::load-class-profile
  (fn [{:keys [_]} [_ class-id course-slug handlers]]
    (create-request {:key    :load-class-profile
                     :method :get
                     :uri    (str "/api/class-profile/" class-id "/course/" course-slug)}
                    handlers)))

(re-frame/reg-event-fx
  ::create-class
  (fn [{:keys [_]} [_ {:keys [data]} handlers]]
    (create-request {:key    :create-class
                     :method :post
                     :uri    (str "/api/classes")
                     :params data}
                    handlers)))

(re-frame/reg-event-fx
  ::save-class
  (fn [{:keys [_]} [_ {:keys [class-id data]} handlers]]
    (create-request {:key    :save-class
                     :method :put
                     :uri    (str "/api/classes/" class-id)
                     :params data}
                    handlers)))

(re-frame/reg-event-fx
  ::delete-class
  (fn [{:keys [_]} [_ {:keys [class-id]} handlers]]
    (create-request {:key    :delete-class
                     :method :delete
                     :uri    (str "/api/classes/" class-id)}
                    handlers)))

(re-frame/reg-event-fx
  ::load-class-students
  (fn [{:keys [_]} [_ {:keys [class-id]} handlers]]
    (create-request {:key    :load-class-students
                     :method :get
                     :uri    (str "/api/classes/" class-id "/students")}
                    handlers)))

(re-frame/reg-event-fx
  ::remove-student-from-class
  (fn [{:keys [_]} [_ {:keys [student-id]} handlers]]
    (create-request {:key    :remove-student-from-class
                     :method :delete
                     :uri    (str "/api/students/" student-id "/class")}
                    handlers)))

;; Students

(re-frame/reg-event-fx
  ::load-unassigned-students
  (fn [{:keys [_]} [_ handlers]]
    (create-request {:key    :load-unassigned-students
                     :method :get
                     :uri    (str "/api/unassigned-students")}
                    handlers)))

(re-frame/reg-event-fx
  ::load-student
  (fn [{:keys [_]} [_ {:keys [student-id]} handlers]]
    (create-request {:key    :load-student
                     :method :get
                     :uri    (str "/api/students/" student-id)}
                    handlers)))

(re-frame/reg-event-fx
  ::load-student-profile
  (fn [{:keys [_]} [_ {:keys [student-id course-id]} handlers]]
    (create-request {:key    :load-student-profile
                     :method :get
                     :uri    (str "/api/individual-profile/" student-id "/course/" course-id)}
                    handlers)))

(re-frame/reg-event-fx
  ::add-student
  (fn [{:keys [_]} [_ {:keys [data]} handlers]]
    (create-request {:key    :add-student
                     :method :post
                     :uri    (str "/api/students")
                     :params data}
                    handlers)))

(re-frame/reg-event-fx
  ::edit-student
  (fn [{:keys [_]} [_ {:keys [student-id data]} handlers]]
    (create-request {:key    :edit-student
                     :method :put
                     :uri    (str "/api/students/" student-id)
                     :params data}
                    handlers)))

(re-frame/reg-event-fx
  ::delete-student
  (fn [{:keys [_]} [_ {:keys [student-id]} handlers]]
    (create-request {:key    :delete-student
                     :method :delete
                     :uri    (str "/api/students/" student-id)}
                    handlers)))

(re-frame/reg-event-fx
  ::complete-student-progress
  (fn [{:keys [_]} [_ {:keys [student-id course-name data]} handlers]]
    (create-request {:key    :complete-student-progress
                     :method :put
                     :uri    (str "/api/individual-profile/" student-id "/course/" course-name "/complete")
                     :params data}
                    handlers)))

(re-frame/reg-event-fx
  ::generate-access-code
  (fn [{:keys [_]} [_ handlers]]
    (create-request {:key    :generate-access-code
                     :method :post
                     :uri    (str "/api/next-access-code")}
                    handlers)))

(re-frame/reg-event-fx
  ::send-system-log
  (fn [{:keys [_]} [_ data handlers]]
    (create-request {:key    :send-system-log
                     :method :post
                     :uri    (str "/api/system/log")
                     :params data}
                    handlers)))
