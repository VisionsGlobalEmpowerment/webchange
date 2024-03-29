(ns webchange.state.warehouse
  (:require
    [ajax.core :refer [json-request-format json-response-format raw-response-format] :as ajax]
    [camel-snake-kebab.core :refer [->Camel_Snake_Case_String]]
    [clojure.string :as string]
    [re-frame.core :as re-frame]
    [day8.re-frame.http-fx :as http-fx]
    [webchange.config :as config]
    [webchange.capacitor :as capacitor]
    [webchange.error-message.state :as error-message]
    [webchange.logger.index :as logger]
    [webchange.state.warehouse-native :as warehouse-native]))

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

(defn init!
  []
  (if (capacitor/native?)
    (warehouse-native/init!)
    (re-frame/reg-fx :cached-http-xhrio http-fx/http-effect)))

(defn- create-request
  "Params:
   - {integer} [delay]: set response delay in ms"
  [{:keys [key method uri body params request-type deprecated?] :as props}
   {:keys [on-success on-failure suppress-api-error?]
    :or   {suppress-api-error? false}}]
  (when (some? deprecated?)
    (logger/warn "Deprecated event" key ": " deprecated?))
  {:dispatch-n (cond-> [[::start-request key]]
                       (some? request-type) (conj [::set-sync-status {:key request-type :in-progress? true}]))
   :cached-http-xhrio (cond-> {:key             key
                               :method          method
                               :uri             uri
                               :format          (json-request-format)
                               :response-format (json-response-format {:keywords? true})
                               :on-success      [::generic-on-success-handler props on-success]
                               :on-failure      [::generic-failure-handler props on-failure suppress-api-error?]}
                              (some? params) (assoc (if (= method :get) :url-params :params) params)
                              (some? body) (assoc :body body))})

(re-frame/reg-event-fx
  ::generic-on-success-handler
  (fn [{:keys [_]} [_ {:keys [key request-type delay]} success-handler response]]
    (let [handle-success (conj success-handler response)]
      (cond-> {:dispatch-n (cond-> [[::finish-request key]]
                                   (some? request-type) (conj [::set-sync-status {:key request-type :in-progress? false}])
                                   (and (nil? delay)
                                        (sequential? success-handler)) (conj handle-success))}
              (and (sequential? success-handler)
                   (some? delay)) (assoc :timeout {:event handle-success
                                                   :time  delay})))))

(re-frame/reg-event-fx
  ::generic-failure-handler
  (fn [{:keys [_]} [_ {:keys [key request-type delay]} failure-handler suppress-api-error? response]]
    (cond-> {:dispatch-n (cond-> [[::finish-request key]]
                                 (not suppress-api-error?) (conj [::show-request-error key response])
                                 (some? request-type) (conj [::set-sync-status {:key request-type :in-progress? false}])
                                 (and (nil? delay)
                                      (sequential? failure-handler)) (conj (conj failure-handler response)))}
            (and (sequential? failure-handler)
                 (some? delay)) (assoc :timeout {:event failure-handler
                                                 :time  delay}))))

(defn response->errors
  [response]
  (let [errors (get-in response [:response :errors])]
    (if (some? errors)
      (let [errors-list (if (sequential? errors) errors [errors])]
        (->> errors-list
             (map vals)
             (flatten)))
      (-> response (get-in [:last-error])))))

(re-frame/reg-event-fx
  ::show-request-error
  (fn [{:keys [_]} [_ key response]]
    (let [title (-> key (clojure.core/name) (->Camel_Snake_Case_String) (string/replace "_" " ") (str " Error"))]
      (logger/group-folded title)
      (logger/trace "key" key)
      (logger/trace "response" response)
      (logger/group-end title)
      {:dispatch [::error-message/show title (response->errors response)]})))

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

;; Request status

(def request-status-path (path-to-db [:request-status]))

(re-frame/reg-event-fx
  ::start-request
  (fn [{:keys [db]} [_ key]]
    {:db (update-in db request-status-path assoc key {:in-progress? true})}))

(re-frame/reg-event-fx
  ::finish-request
  (fn [{:keys [db]} [_ key]]
    {:db (update-in db request-status-path dissoc key)}))

(defn request-in-progress?
  [db key]
  (-> (get-in db request-status-path)
      (contains? key)))

(re-frame/reg-sub
  ::request-in-progress?
  (fn [db [_ key]]
    (request-in-progress? db key)))

;;

(defn- create-fake-request
  "Params:
   - {integer} [delay]: set response delay in ms"
  ([props handlers]
   (create-fake-request props handlers {}))
  ([{:keys [key params uri] :as props}
    {:keys [on-success on-failure suppress-api-error?]}
    {:keys [delay result result-data]
     :or   {result :success}}]
   (let [message (str "Fake request " key)]
     (logger/group-folded message)
     (logger/log "uri" uri)
     (logger/log "params" params)
     (logger/group-end message))
   {:dispatch (case result
                :success [::generic-on-success-handler (assoc props :delay delay) on-success result-data]
                :failure [::generic-failure-handler (assoc props :delay delay) on-failure suppress-api-error? result-data])}))

;;

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
                                        attempts (if config/debug? 3 120)}}]
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

;; Auth

(re-frame/reg-event-fx
  ::login-with-credentials
  (fn [{:keys [_]} [_ {:keys [data]} handlers]]
    (create-request {:key    :login-with-credentials
                     :method :post
                     :params {:user data}
                     :uri    "/api/users/login"}
                    handlers)))

(re-frame/reg-event-fx
  ::load-current-user
  (fn [{:keys [_]} [_ handlers]]
    (create-request {:key    :load-current-user
                     :method :get
                     :uri    "/api/users/current"}
                    (assoc handlers :suppress-api-error? true))))

(re-frame/reg-event-fx
  ::logout
  (fn [{:keys [_]} [_ handlers]]
    (create-request {:key    :logout
                     :method :post
                     :uri    "/api/users/logout"}
                    handlers)))

;; Accounts

(re-frame/reg-event-fx
  ::create-account
  (fn [{:keys [_]} [_ data handlers]]
    (create-request {:key    :create-account
                     :method :post
                     :uri    "/api/accounts"
                     :params data}
                    handlers)))

(re-frame/reg-event-fx
  ::register-account
  (fn [{:keys [_]} [_ data handlers]]
    (create-request {:key    :register-account
                     :method :post
                     :uri    "/api/accounts/register"
                     :params data}
                    handlers)))

(re-frame/reg-event-fx
  ::load-account
  (fn [{:keys [_]} [_ {:keys [id]} handlers]]
    (create-request {:key    :load-account
                     :method :get
                     :uri    (str "/api/accounts/" id)}
                    handlers)))

(re-frame/reg-event-fx
  ::load-accounts-by-type
  (fn [{:keys [_]} [_ {:keys [type page query only-active]} handlers]]
    (create-request {:key    :load-accounts-by-type
                     :method :get
                     :params (cond-> {}
                                     page (assoc :page page)
                                     (seq query) (assoc :q query)
                                     (boolean? only-active) (assoc :only-active only-active))
                     :uri    (str "/api/accounts-by-type/" type)}
                    handlers)))

(re-frame/reg-event-fx
  ::set-account-status
  (fn [{:keys [_]} [_ {:keys [account-id active]} handlers]]
    (create-request {:key    :set-account-status
                     :method :put
                     :uri    (str "/api/accounts/" account-id "/status")
                     :params {:active active}}
                    handlers)))

(re-frame/reg-event-fx
  ::save-account
  (fn [{:keys [_]} [_ {:keys [id data]} handlers]]
    (create-request {:key    :save-account
                     :method :put
                     :uri    (str "/api/accounts/" id)
                     :params data}
                    handlers)))

(re-frame/reg-event-fx
  ::change-account-password
  (fn [{:keys [_]} [_ {:keys [id data]} handlers]]
    (create-request {:key    :change-account-password
                     :method :put
                     :uri    (str "/api/accounts/" id "/password")
                     :params data}
                    handlers)))

(re-frame/reg-event-fx
  ::delete-account
  (fn [{:keys [_]} [_ {:keys [id]} handlers]]
    (create-request {:key    :delete-account
                     :method :delete
                     :uri    (str "/api/accounts/" id)}
                    handlers)))

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
  (fn [{:keys [_]} [_ {:keys [course-slug scene-slug data]} handlers]]
    (create-request {:key          :update-activity
                     :method       :post
                     :uri          (str "/api/courses/" course-slug "/update-activity/" scene-slug)
                     :params       data
                     :request-type :update-activity}
                    handlers)))

;; Progress Data

(re-frame/reg-event-fx
  ::save-progress-data
  (fn [{:keys [_]} [_ {:keys [course-slug progress-data]} handlers]]
    {:pre [(string? course-slug)
           (contains? progress-data :progress)
           (contains? progress-data :events)]}
    (create-request {:key    :save-progress-data
                     :method :post
                     :params progress-data
                     :uri    (str "/api/courses/" course-slug "/current-progress")}
                    handlers)))

(re-frame/reg-event-fx
  ::load-progress-data
  (fn [{:keys [_]} [_ {:keys [course-slug]} handlers]]
    {:pre [(string? course-slug)]}
    (create-request {:key    :load-progress-data
                     :method :get
                     :uri    (str "/api/courses/" course-slug "/current-progress")}
                    handlers)))

(re-frame/reg-event-fx
  ::load-class-students-progress
  (fn [{:keys [_]} [_ {:keys [class-id]} handlers]]
    (create-request {:key    :load-class-students-progress
                     :method :get
                     :uri    (str "/api/class-students/" class-id "/progress")}
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
  ::load-class-course
  (fn [{:keys [_]} [_ {:keys [class-id]} handlers]]
    (create-request {:key    :load-class-course
                     :method :get
                     :uri    (str "/api/classes/" class-id "/course")}
                    handlers)))

(re-frame/reg-event-fx
  ::load-course-info
  (fn [{:keys [_]} [_ course-slug handlers]]
    (create-request {:key    :load-course-info
                     :method :get
                     :uri    (str "/api/courses/" course-slug "/info")}
                    handlers)))

(re-frame/reg-event-fx
  ::save-course-info
  (fn [{:keys [_]} [_ {:keys [course-id data]} handlers]]
    (create-request {:key    :save-course-info
                     :method :put
                     :uri    (str "/api/courses/" course-id "/info")
                     :params data}
                    handlers)))

(re-frame/reg-event-fx
  ::publish-course
  (fn [{:keys [_]} [_ course-slug handlers]]
    (create-request {:key    :publish-course
                     :method :post
                     :uri    (str "/api/courses/" course-slug "/publish")}
                    handlers)))

(re-frame/reg-event-fx
  ::archive-course
  (fn [{:keys [_]} [_ course-slug handlers]]
    (create-request {:key    :archive-course
                     :method :post
                     :uri    (str "/api/courses/" course-slug "/archive")}
                    handlers)))

(re-frame/reg-event-fx
  ::toggle-course-visibility
  (fn [{:keys [_]} [_ {:keys [course-slug visible]} handlers]]
    (create-request {:key    :toggle-course-visibility
                     :method :put
                     :params {:visible visible}
                     :uri    (str "/api/courses/" course-slug "/toggle-visibility")}
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
                     :uri    (str "/api/available-courses")}
                    handlers)))

(re-frame/reg-event-fx
  ::load-my-courses
  (fn [{:keys [_]} [_ handlers]]
    (create-request {:key    :load-available-courses
                     :method :get
                     :uri    (str "/api/my-courses")}
                    handlers)))

(re-frame/reg-event-fx
  ::load-visible-activities
  (fn [{:keys [_]} [_ {:keys [lang]} handlers]]
    (create-request {:key    :load-visible-activities
                     :method :get
                     :uri    (str "/api/visible-activities")
                     :params (when lang
                               {:lang lang})}
                    handlers)))

(re-frame/reg-event-fx
  ::load-my-activities
  (fn [{:keys [_]} [_ {:keys [lang]} handlers]]
    (create-request {:key    :load-my-activities
                     :method :get
                     :uri    (str "/api/my-activities")
                     :params {:lang lang}}
                    handlers)))

(re-frame/reg-event-fx
  ::load-activity
  (fn [{:keys [_]} [_ {:keys [activity-id]} handlers]]
    (create-request {:key    :load-activity
                     :method :get
                     :uri    (str "/api/activities/" activity-id)}
                    handlers)))

(re-frame/reg-event-fx
  ::save-activity
  (fn [{:keys [_]} [_ {:keys [activity-id data]} handlers]]
    (create-request {:key    :save-activity
                     :method :put
                     :uri    (str "/api/activities/" activity-id)
                     :params data}
                    handlers)))

(re-frame/reg-event-fx
  ::archive-activity
  (fn [{:keys [_]} [_ {:keys [activity-id]} handlers]]
    (create-request {:key    :archive-activity
                     :method :put
                     :params {:archive true}
                     :uri    (str "/api/activities/" activity-id "/archive")}
                    handlers)))

(re-frame/reg-event-fx
  ::toggle-activity-visibility
  (fn [{:keys [_]} [_ {:keys [activity-id visible]} handlers]]
    (create-request {:key    :toggle-activity-visibility
                     :method :put
                     :params {:visible visible}
                     :uri    (str "/api/activities/" activity-id "/toggle-visibility")}
                    handlers)))

(re-frame/reg-event-fx
  ::toggle-activity-locked
  (fn [{:keys [_]} [_ {:keys [activity-id locked]} handlers]]
    (create-request {:key    :toggle-activity-locked
                     :method :put
                     :params {:locked locked}
                     :uri    (str "/api/activities/" activity-id "/toggle-locked")}
                    handlers)))

(re-frame/reg-event-fx
  ::duplicate-activity
  (fn [{:keys [_]} [_ {:keys [activity-id data]} handlers]]
    (create-request {:key    :duplicate-activity
                     :method :post
                     :params data
                     :uri    (str "/api/activities/" activity-id "/duplicate")}
                    handlers)))

(re-frame/reg-event-fx
  ::create-book
  (fn [{:keys [_]} [_ {:keys [data]} handlers]]
    (create-request {:key    :create-book
                     :method :post
                     :params data
                     :uri    (str "/api/books")}
                    handlers)))

(re-frame/reg-event-fx
  ::apply-activity-template-options
  (fn [{:keys [_]} [_ {:keys [activity-id data]} handlers]]
    (create-request {:key    :apply-activity-template-options
                     :method :put
                     :params data
                     :uri    (str "/api/activities/" activity-id "/template-options")}
                    handlers)))

(re-frame/reg-event-fx
  ::activity-template-action
  (fn [{:keys [_]} [_ {:keys [activity-id action common-action? data] :or {common-action? true}} handlers]]
    (create-request {:key    :apply-activity-template-action
                     :method :post
                     :params {:common-action? common-action?
                              :action         action
                              :data           data}
                     :uri    (str "/api/activities/" activity-id "/template-actions")}
                    handlers)))

(re-frame/reg-event-fx
  ::activity-settings
  (fn [{:keys [_]} [_ {:keys [activity-id data]} handlers]]
    (create-request {:key    :set-activity-settings
                     :method :put
                     :params data
                     :uri    (str "/api/activities/" activity-id "/settings")}
                    handlers)))

(re-frame/reg-event-fx
  ::update-template
  (fn [{:keys [_]} [_ {:keys [activity-id]} handlers]]
    (create-request {:key    :update-template
                     :method :put
                     :params {:update true}
                     :uri    (str "/api/activities/" activity-id "/update-template")}
                    handlers)))

(re-frame/reg-event-fx
  ::load-available-books
  (fn [{:keys [_]} [_ {:keys [lang]} handlers]]
    (create-request {:key    :load-available-books
                     :method :get
                     :uri    (str "/api/available-books")
                     :params {:lang lang}}
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
  ::load-my-books
  (fn [{:keys [_]} [_ {:keys [lang]} handlers]]
    (create-request {:key    :load-my-books
                     :method :get
                     :uri    (str "/api/my-books")}
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

(re-frame/reg-event-fx
  ::assign-school-courses
  (fn [{:keys [_]} [_ {:keys [school-id courses-id]} handlers]]
    (create-request {:key    :assign-school-course
                     :method :put
                     :uri    (str "/api/schools/" school-id "/assign-courses")
                     :params {:courses-id courses-id}}
                    handlers)))

(re-frame/reg-event-fx
  ::unassign-school-course
  (fn [{:keys [_]} [_ {:keys [school-id course-id]} handlers]]
    (create-request {:key    :unassign-school-course
                     :method :delete
                     :uri    (str "/api/schools/" school-id "/assign-course")
                     :params {:course-id course-id}}
                    handlers)))
;;

(re-frame/reg-event-fx
  ::load-skills
  (fn [{:keys [_]} [_ {:keys [local]} handlers]]
    (create-request {:key    :load-skills
                     :method :get
                     :uri    (str "/api/skills/" local)} handlers)))

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
    (create-request {:key         :save-scene
                     :method      :put
                     :uri         (str "/api/courses/" course-slug "/scenes/" scene-slug)
                     :params      {:scene scene-data}
                     :deprecated? "Use 'save-activity-version' event instead"}
                    handlers)))

(re-frame/reg-event-fx
  ::save-scene-post
  (fn [{:keys [_]} [_ {:keys [course-id scene-id scene-data]} handlers]]
    (create-request {:key         :save-scene-post
                     :method      :post
                     :uri         (str "/api/courses/" course-id "/scenes/" scene-id)
                     :params      {:scene scene-data}
                     :deprecated? "Use 'save-activity-version' event instead"}
                    handlers)))

(re-frame/reg-event-fx
  ::save-activity-version
  (fn [{:keys [_]} [_ {:keys [activity-id activity-data]} handlers]]
    (create-request {:key    :save-activity
                     :method :post
                     :uri    (str "/api/activities/" activity-id "/version")
                     :params activity-data}
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

(re-frame/reg-event-fx
  ::load-activity-current-version
  (fn [{:keys [_]} [_ {:keys [activity-id]} handlers]]
    (create-request {:key    :load-activity-current-version
                     :method :get
                     :uri    (str "/api/activities/" activity-id "/current-version")}
                    handlers)))

;; Lesson sets

(re-frame/reg-event-fx
  ::load-lesson-sets
  (fn [{:keys [_]} [_ {:keys [course-slug]} handlers]]
    (create-request {:key    :load-lesson-sets
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
  ::duplicate-course
  (fn [_ [_ {:keys [course-id data]} handlers]]
    (create-request {:key    :duplicate-course
                     :method :post
                     :params data
                     :uri    (str "/api/courses/" course-id "/duplicate")}
                    handlers)))


(re-frame/reg-event-fx
  ::duplicate-course-activity
  (fn [_ [_ {:keys [course-id data]} handlers]]
    (create-request {:key    :duplicate-activity
                     :method :post
                     :uri    (str "/api/courses/" course-id "/duplicate-activity")
                     :params data} handlers)))

;; Static assets

(re-frame/reg-event-fx
  ::upload-file
  (fn [{:keys [_]} [_ {:keys [file form-params]
                       :or   {form-params []}}
                    handlers]]
    (let [form-data (get-form-data (concat [["file" file]] form-params))]
      (create-request {:key    :upload-file
                       :method :post
                       :uri    (str "/api/assets/")
                       :body   form-data}
                      handlers))))

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
                               :form-params [["type" "image"]
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
  (fn [{:keys [_]} [_ {:keys [tag tags type]} handlers]]
    (create-request {:key    :load-assets
                     :method :get
                     :uri    (cond-> (str "/api/courses/editor/assets")

                                     (some? tag) (str "?tag=" tag)
                                     (some? type) (str "?type=" type)
                                     (seq tags) (str "?tags=" (clojure.string/join "," tags)))}
                    handlers)))

(re-frame/reg-event-fx
  ::search-assets
  (fn [{:keys [_]} [_ {:keys [tag query type]} handlers]]
    (create-request {:key    :search-assets
                     :method :get
                     :uri    "/api/courses/editor/assets-search"
                     :params (->> {:tag  tag
                                   :q    query
                                   :type type}
                                  (filter second)
                                  (into {}))}
                    handlers)))

(re-frame/reg-event-fx
  ::load-assets-tags
  (fn [{:keys [_]} [_ handlers]]
    (create-request {:key    :load-assets-tags
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

(re-frame/reg-event-fx
  ::load-assets-tags-by-names
  (fn [{:keys [_]} [_ {:keys [tags]} handlers]]
    (create-request {:key    :load-tags-by-names
                     :method :get
                     :uri    (str "/api/courses/editor/tags-by-name")
                     :params {:tags tags}}
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
  ::load-activity-versions
  (fn [{:keys [_]} [_ {:keys [activity-id]} handlers]]
    (create-request {:key    :load-activity-versions
                     :method :get
                     :uri    (str "/api/activities/" activity-id "/versions")}
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
    (create-request {:key    :add-parent-student
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
  ::set-student-status
  (fn [{:keys [_]} [_ {:keys [student-id active]} handlers]]
    (create-fake-request {:key    :set-student-status
                          :method :put
                          :uri    (str "/api/students/" student-id "/status")
                          :params {:active active}}
                         handlers
                         {:delay 2000})))

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

;; Schools

(re-frame/reg-event-fx
  ::load-school
  (fn [{:keys [_]} [_ {:keys [school-id]} handlers]]
    (create-request {:key    :load-school
                     :method :get
                     :uri    (str "/api/schools/" school-id)}
                    handlers)))

(re-frame/reg-event-fx
  ::load-school-stats
  (fn [{:keys [_]} [_ {:keys [school-id]} handlers]]
    (create-request {:key    :load-school-stats
                     :method :get
                     :uri    (str "/api/schools/" school-id "/progress")}
                    handlers)))

(re-frame/reg-event-fx
  ::load-school-classes
  (fn [{:keys [_]} [_ {:keys [school-id]} handlers]]
    (create-request {:key    :load-school-classes
                     :method :get
                     :uri    (str "/api/schools/" school-id "/classes")}
                    handlers)))

(re-frame/reg-event-fx
  ::load-school-courses
  (fn [{:keys [_]} [_ {:keys [school-id]} handlers]]
    (create-request {:key    :load-school-courses
                     :method :get
                     :uri    (str "/api/schools/" school-id "/courses")}
                    handlers)))

(re-frame/reg-event-fx
  ::load-school-students
  (fn [{:keys [_]} [_ {:keys [school-id]} handlers]]
    (create-request {:key    :load-school-students
                     :method :get
                     :uri    (str "/api/schools/" school-id "/students")}
                    handlers)))

(re-frame/reg-event-fx
  ::load-schools
  (fn [{:keys [_]} [_ handlers]]
    (create-request {:key    :load-schools
                     :method :get
                     :uri    (str "/api/schools")}
                    handlers)))

(re-frame/reg-event-fx
  ::create-school
  (fn [{:keys [_]} [_ {:keys [data]} handlers]]
    (create-request {:key    :create-school
                     :method :post
                     :uri    (str "/api/schools")
                     :params data}
                    handlers)))

(re-frame/reg-event-fx
  ::edit-school
  (fn [{:keys [_]} [_ {:keys [school-id data]} handlers]]
    (create-request {:key    :edit-school
                     :method :put
                     :uri    (str "/api/schools/" school-id)
                     :params data}
                    handlers)))

(re-frame/reg-event-fx
  ::archive-school
  (fn [{:keys [_]} [_ {:keys [school-id]} handlers]]
    (create-request {:key    :archive-school
                     :method :put
                     :uri    (str "/api/schools/" school-id "/archive")}
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
  (fn [{:keys [_]} [_ {:keys [class-id]} handlers]]
    (create-request {:key    :load-class
                     :method :get
                     :uri    (str "/api/classes/" class-id)}
                    handlers)))

(re-frame/reg-event-fx
  ::load-class-stats
  (fn [{:keys [_]} [_ {:keys [class-id]} handlers]]
    (create-request {:key    :load-class-stats
                     :method :get
                     :uri    (str "/api/classes/" class-id "/progress")}
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
  (fn [{:keys [_]} [_ {:keys [school-id data]} handlers]]
    (create-request {:key    :create-class
                     :method :post
                     :uri    (str "/api/schools/" school-id "/classes")
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
  ::archive-class
  (fn [{:keys [_]} [_ {:keys [class-id]} handlers]]
    (create-request {:key    :archive-class
                     :method :put
                     :params {:archive true}
                     :uri    (str "/api/classes/" class-id "/archive")}
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
  ::add-students-to-class
  (fn [{:keys [_]} [_ {:keys [class-id data]} handlers]]
    (create-request {:key    :add-students-to-class
                     :method :put
                     :uri    (str "/api/classes/" class-id "/students")
                     :params data}
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
  ::student-login
  (fn [{:keys [_]} [_ {:keys [access-code school-id]} handlers]]
    (create-request {:key    :student-login
                     :method :post
                     :uri    (str "/api/students/login")
                     :params {:school-id   school-id
                              :access-code access-code}}
                    handlers)))

(re-frame/reg-event-fx
  ::load-unassigned-students
  (fn [{:keys [_]} [_ {:keys [school-id]} handlers]]
    (create-request {:key    :load-unassigned-students
                     :method :get
                     :uri    (str "/api/unassigned-students/" school-id)}
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
  ::load-class-student-progress
  (fn [{:keys [_]} [_ {:keys [student-id]} handlers]]
    (create-request {:key    :load-class-student
                     :method :get
                     :uri    (str "/api/class-student/" student-id "/progress")}
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
  ::create-student
  (fn [{:keys [_]} [_ {:keys [school-id data]} handlers]]
    (create-request {:key    :create-student
                     :method :post
                     :uri    (str "/api/schools/" school-id "/students")
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
    (create-request {:key    :archive-student
                     :method :put
                     :uri    (str "/api/students/" student-id "/archive")}
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
  ::generate-school-access-code
  (fn [{:keys [_]} [_ {:keys [school-id]} handlers]]
    (create-request {:key    :generate-school-access-code
                     :method :post
                     :uri    (str "/api/schools/" school-id "/next-access-code")}
                    handlers)))

(re-frame/reg-event-fx
  ::send-system-log
  (fn [{:keys [_]} [_ data handlers]]
    (create-request {:key    :send-system-log
                     :method :post
                     :uri    (str "/api/system/log")
                     :params data}
                    handlers)))


;; Teachers

(re-frame/reg-event-fx
  ::load-teacher
  (fn [{:keys [_]} [_ {:keys [teacher-id]} handlers]]
    (create-request {:key    :load-teacher
                     :method :get
                     :uri    (str "/api/teachers/" teacher-id)}
                    handlers)))

(re-frame/reg-event-fx
  ::create-teacher
  (fn [{:keys [_]} [_ {:keys [school-id data]} handlers]]
    (create-request {:key    :create-teacher
                     :method :post
                     :uri    (str "/api/schools/" school-id "/teachers")
                     :params data}
                    handlers)))

(re-frame/reg-event-fx
  ::edit-teacher
  (fn [{:keys [_]} [_ {:keys [teacher-id data]} handlers]]
    (create-request {:key    :create-teacher
                     :method :put
                     :uri    (str "/api/teachers/" teacher-id)
                     :params data}
                    handlers)))

(re-frame/reg-event-fx
  ::remove-teacher
  (fn [{:keys [_]} [_ {:keys [teacher-id]} handlers]]
    (create-request {:key    :remove-teacher
                     :method :delete
                     :uri    (str "/api/teachers/" teacher-id)}
                    handlers)))

(re-frame/reg-event-fx
  ::transfer-teacher
  (fn [{:keys [_]} [_ {:keys [school-id data]} handlers]]
    (create-request {:key    :create-teacher
                     :method :put
                     :uri    (str "/api/schools/" school-id "/transfer-teacher")
                     :params data}
                    handlers)))

(re-frame/reg-event-fx
  ::remove-teacher-from-class
  (fn [{:keys [_]} [_ {:keys [class-id teacher-id]} handlers]]
    (create-request {:key    :remove-teacher-from-class
                     :method :delete
                     :uri    (str "/api/teachers/" teacher-id "/classes/" class-id)}
                    handlers)))

(re-frame/reg-event-fx
  ::load-class-teachers
  (fn [{:keys [_]} [_ {:keys [class-id]} handlers]]
    (create-request {:key    :load-class-teachers
                     :method :get
                     :uri    (str "/api/classes/" class-id "/teachers")}
                    handlers)))

(re-frame/reg-event-fx
  ::load-school-teachers
  (fn [{:keys [_]} [_ {:keys [school-id]} handlers]]
    (create-request {:key    :load-school-teachers
                     :method :get
                     :uri    (str "/api/schools/" school-id "/teachers")}
                    handlers)))

(re-frame/reg-event-fx
  ::assign-teachers-to-class
  (fn [{:keys [_]} [_ {:keys [class-id data]} handlers]]
    (create-request {:key    :load-class-teachers
                     :method :put
                     :uri    (str "/api/classes/" class-id "/teachers")
                     :params data}
                    handlers)))

(re-frame/reg-event-fx
  ::set-teacher-status
  (fn [{:keys [_]} [_ {:keys [teacher-id active]} handlers]]
    (create-request {:key    :set-teacher-status
                     :method :put
                     :uri    (str "/api/teachers/" teacher-id "/status")
                     :params {:status (if active "active" "inactive")}}
                    handlers)))

(re-frame/reg-event-fx
  ::load-overall-statistics
  (fn [{:keys [_]} [_ handlers]]
    (create-request {:key    :load-overall-statistics
                     :method :get
                     :uri    "/api/overall-statistics"}
                    handlers)))

(re-frame/reg-event-fx
  ::reset-password-by-code
  (fn [{:keys [_]} [_ {:keys [code data]} handlers]]
    (create-request {:key    :reset-password-by-code
                     :method :post
                     :uri    (str "/api/accounts/reset-password/" code)
                     :params data}
                    handlers)))

(re-frame/reg-event-fx
  ::reset-password-by-email
  (fn [{:keys [_]} [_ data handlers]]
    (create-request {:key    :reset-password-by-email
                     :method :post
                     :uri    (str "/api/accounts/reset-password-by-email")
                     :params data}
                    handlers)))

(re-frame/reg-event-fx
  ::save-subtitles
  (fn [{:keys [_]} [_ data handlers]]
    (create-request {:key    :save-subtitles
                     :method :put
                     :uri    (str "/api/assets/subtitles")
                     :params data}
                    handlers)))

(re-frame/reg-event-fx
  ::sync-school
  (fn [{:keys [_]} [_ id data handlers]]
    (create-request {:key    :sync-school
                     :method :post
                     :uri    (str "/api/school/sync/" id)
                     :params data}
                    handlers)))

(re-frame/reg-event-fx
  ::sync-status
  (fn [{:keys [_]} [_ handlers]]
    (create-request {:key    :sync-status
                     :method :get
                     :uri    (str "/api/school/sync-status")}
                    handlers)))
