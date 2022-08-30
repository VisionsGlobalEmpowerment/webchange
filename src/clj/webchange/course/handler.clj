(ns webchange.course.handler
  (:require
    [buddy.auth :refer [throw-unauthorized]]
    [clojure.string :as str]
    [clojure.tools.logging :as log]
    [compojure.api.sweet :refer [DELETE GET POST PUT api context defroutes swagger-routes]]
    [ring.util.response :refer [redirect resource-response response]]
    [ring.util.codec :refer [url-encode]]
    [schema.core :as s]
    [webchange.assets.core :as assets]
    [webchange.auth.core :as auth]
    [webchange.auth.roles :refer [is-admin? is-super-admin?]]
    [webchange.auth.website :as website]
    [webchange.common.handler :refer [current-user handle]]
    [webchange.common.hmac-sha256 :as sign]
    [webchange.course.core :as core]
    [webchange.course.skills :as skills]
    [webchange.templates.core :as templates]
    [webchange.school.core :as school]
    [webchange.validation.specs.activity :as activity-spec]
    [webchange.validation.specs.book :as book-spec]
    [webchange.validation.specs.course-spec :as course-spec]))

(defn handle-save-scene
  [course-slug scene-name request]
  (let [user-id (current-user request)
        save (fn [data] (core/save-scene-with-processing course-slug scene-name data user-id))]
    (when-not (or (is-admin? user-id)
                  (core/collaborator-by-course-slug? user-id course-slug))
      (throw-unauthorized {:role :educator}))
    (-> request
        :body
        :scene
        save
        handle)))

(defn handle-update-scene
  [course-slug scene-name request]
  (let [user-id (current-user request)
        save (fn [data] (core/update-scene! course-slug scene-name data user-id))]
    (when-not (or (is-admin? user-id)
                  (core/collaborator-by-course-slug? user-id course-slug))
      (throw-unauthorized {:role :educator}))
    (-> request
        :body
        :scene
        save
        handle)))

(defn handle-update-scene-skills
  [course-slug scene-name request]
  (let [user-id (current-user request)
        save (fn [data] (core/update-scene-skills! course-slug scene-name data user-id))]
    (when-not (core/collaborator-by-course-slug? user-id course-slug)
      (throw-unauthorized {:role :educator}))
    (-> request
        :body
        :skills
        save
        handle)))

(defn handle-save-course
  [course-slug request]
  (let [user-id (current-user request)
        save (fn [data] (core/save-course! course-slug data user-id))]
    (when-not (core/collaborator-by-course-slug? user-id course-slug)
      (throw-unauthorized {:role :educator}))
    (-> request
        :body
        :course
        save
        handle)))

(defn handle-save-course-info
  [course-id request]
  (let [course-id (Integer/parseInt course-id)
        user-id (current-user request)
        save (fn [data] (core/save-course-info! course-id data))]
    (when-not (or (is-admin? user-id) (core/collaborator-by-course-id? user-id course-id))
      (throw-unauthorized {:role :educator}))
    (-> request
        :body
        save
        handle)))

(defn handle-restore-course-version
  [version-id request]
  (let [version-id (Integer/parseInt version-id)
        user-id (current-user request)]
    (when-not (core/collaborator-by-course-version? user-id version-id)
      (throw-unauthorized {:role :educator}))
    (-> (core/restore-course-version! version-id user-id)
        handle)))

(defn handle-restore-scene-version
  [version-id request]
  (let [version-id (Integer/parseInt version-id)
        user-id (current-user request)]
    (when-not (core/collaborator-by-scene-version? user-id version-id)
      (throw-unauthorized {:role :educator}))
    (-> (core/restore-scene-version! version-id user-id)
        handle)))

(defn handle-localize-course
  [course-id data request]
  (let [website-user-id (:user-id data)
        language (:language data)
        owner-id (some-> website-user-id
                         website/get-user-by-id
                         auth/get-user-id-by-website!)]
    (if owner-id
      (-> (core/localize course-id {:lang            language
                                    :owner-id        owner-id
                                    :website-user-id website-user-id})
          handle)
      (handle [false {:message "User not found"}]))))

(defn handle-create-course
  [{:keys [image-src concept-list-id] :as data} request]
  (let [owner-id (current-user request)
        course (cond-> data
                       (not-empty image-src) (assoc :image-src
                                                    (assets/make-thumbnail image-src :course))
                       :always (core/create-course owner-id))]
    (handle course)))

(defn handle-create-course-activity
  [course-slug data request]
  (let [user-id (current-user request)
        activity (templates/activity-from-template data)
        metadata (templates/metadata-from-template data)]
    (when-not (core/collaborator-by-course-slug? user-id course-slug)
      (throw-unauthorized {:role :educator}))
    (-> (core/create-scene! activity metadata course-slug (:name data) (:skills data) user-id)
        handle)))

(defn handle-duplicate-course-activity
  [course-slug data request]
  (let [user-id (current-user request)
        scene-data (core/get-scene-latest-version course-slug (:old-name data))
        metadata (templates/metadata-from-template (:metadata scene-data))
        skills (:skills scene-data)]
    (when-not (core/collaborator-by-course-slug? user-id course-slug)
      (throw-unauthorized {:role :educator}))
    (-> (core/create-scene! scene-data metadata course-slug (:new-name data) skills (current-user request))
        handle)))

(defn handle-create-activity-placeholder
  [course-slug data request]
  (let [user-id (current-user request)]
    (when-not (core/collaborator-by-course-slug? user-id course-slug)
      (throw-unauthorized {:role :educator}))
    (-> (core/create-activity-placeholder! course-slug (:name data))
        handle)))

(defn handle-create-activity-version
  [course-slug scene-slug data request]
  (let [user-id (current-user request)
        activity (templates/activity-from-template data)
        metadata (templates/metadata-from-template data)]
    (when-not (core/collaborator-by-course-slug? user-id course-slug)
      (throw-unauthorized {:role :educator}))
    (-> (core/create-activity-version! activity metadata course-slug scene-slug user-id)
        handle)))

(defn handle-set-activity-preview
  [course-slug scene-slug data request]
  (let [user-id (current-user request)]
    (when-not (core/collaborator-by-course-slug? user-id course-slug)
      (throw-unauthorized {:role :educator}))
    (-> (core/set-activity-preview! course-slug scene-slug data user-id)
        handle)))

(defn handle-update-activity
  [course-slug scene-slug data request]
  (let [user-id (current-user request)]
    (when-not (or (is-admin? user-id)
                  (core/collaborator-by-course-slug? user-id course-slug))
      (throw-unauthorized {:role :educator}))
    (-> (core/update-activity! course-slug scene-slug data user-id)
        handle)))

(defn handle-update-template
  [course-slug scene-slug request]
  (let [user-id (current-user request)]
    (when-not (core/collaborator-by-course-slug? user-id course-slug)
      (throw-unauthorized {:role :educator}))
    (-> (core/update-course-activity-template! course-slug scene-slug user-id)
        handle)))

(defn handle-publish-course
  [course-slug request]
  (let [user-id (current-user request)]
    (when-not (core/collaborator-by-course-slug? user-id course-slug)
      (throw-unauthorized {:role :educator}))
    (-> (core/publish-course! course-slug)
        handle)))

(defn handle-archive-course
  [course-slug request]
  (let [user-id (current-user request)
        {owner-id :owner-id course-id :id} (core/get-course-info course-slug)]
    (when-not (or (is-admin? user-id) (= user-id owner-id))
      (throw-unauthorized {:role :educator}))
    (-> (core/archive-course! course-slug course-id)
        handle)))

(defn handle-review-course
  [course-id review-result request]
  (let [user-id (current-user request)]
    (when-not (is-admin? user-id)
      (throw-unauthorized {:role :educator}))
    (-> (core/review-course! course-id review-result)
        handle)))

(defn handle-get-on-review-courses
  [type status request]
  (let [user-id (current-user request)]
    (when-not (is-admin? user-id)
      (throw-unauthorized {:role :educator}))
    (-> (core/get-on-review-courses type status)
        handle)))

(s/defschema Course {:id                          s/Int :name s/Str :slug s/Str :image-src (s/maybe s/Str)
                     :url                         s/Str :lang (s/maybe s/Str)
                     :owner-id                    (s/maybe s/Int)
                     (s/optional-key :metadata)   (s/maybe s/Any)
                     (s/optional-key :updated-at) s/Str
                     (s/optional-key :level)      s/Str
                     (s/optional-key :subject)    s/Str
                     (s/optional-key :status)     s/Str})
(s/defschema CreateCourse {:name s/Str :lang s/Str (s/optional-key :level) s/Str (s/optional-key :subject) s/Str (s/optional-key :concept-list-id) s/Int (s/optional-key :type) s/Str (s/optional-key :image-src) s/Str})
(s/defschema Translate {:user-id s/Int :language s/Str})
(s/defschema EditorTag {:id s/Int :name s/Str})
(s/defschema EditorAsset {:id s/Int :path s/Str :thumbnail-path s/Str :type (s/enum "single-background" "background" "surface" "decoration" "etc")})
(s/defschema EditorAssetWithTags {:id s/Int :path s/Str :thumbnail-path s/Str :type (s/enum "single-background" "background" "surface" "decoration" "etc") :tags [EditorTag]})
(s/defschema CharacterSkin {:name                     s/Str
                            :width                    s/Num
                            :height                   s/Num
                            (s/optional-key :preview) s/Str
                            :skins                    [{:name                     s/Str
                                                        (s/optional-key :preview) s/Str}]
                            :resources                [s/Str]
                            :animations               [s/Str]})

(s/defschema CreateActivity {:name s/Str :template-id s/Int :skills [s/Int] s/Keyword s/Any})
(s/defschema CreateActivityVersion {:template-id s/Int s/Keyword s/Any})
(s/defschema CreateActivityPlaceholder {:name s/Str})
(s/defschema DuplicateActivity {:old-name s/Str :new-name s/Str})
(s/defschema SetActivityPreview {:preview s/Str})
(s/defschema Activity {:id s/Int :name s/Str :scene-slug s/Str :course-slug s/Str})

(s/defschema Topic {:name s/Str :strand s/Keyword})
(s/defschema Skill {:id s/Int :name s/Str :abbr s/Str :grade s/Str :topic s/Keyword :tags [s/Str]})
(s/defschema Skills {:levels {s/Keyword s/Str} :subjects {s/Keyword s/Str} :skills [Skill] :topics {s/Keyword Topic} :strands {s/Keyword s/Str}})

(s/defschema ActivityWithSkills {:id s/Int :name s/Str :course-id s/Int :is-placeholder s/Bool :skills [Skill]})
(s/defschema ReviewResult {:status s/Str (s/optional-key :comment) s/Str})

(defn character-skins []
  (response (core/find-all-character-skins)))

(defn editor-assets [tag tags type]
  (let [tags (->> (str/split tags #",")
                  (remove empty?)
                  (map #(Integer/parseInt %)))]
    (response (core/editor-assets tag tags type))))

(defn editor-assets-search
  [query tag type]
  (-> (core/editor-assets-search query tag type)
      (response)))

(defn find-all-tags []
  (response (core/find-all-tags)))

(defn find-tag-by-name
  [tags]
  (response (core/find-tags-by-name tags)))

(s/defschema Error403 {:errors [{:message s/Str}]})

(s/defschema CoursesOrError (s/cond-pre [Course] Error403))

(defroutes editor-api-routes
  (api
    (swagger-routes {:ui   "/api-docs"
                     :data {:info {:title "TabSchools API"}
                            :tags [{:name "course", :description "Course APIs"}]}})
    (context
        "/api/courses" []
      :tags ["editor-assets"]
      (GET "/editor/assets" []
        :summary "Return list of available assets"
        :query-params [{type :- s/Str nil}, {tag :- s/Int nil}, {tags :- s/Str ""}]
        :return [EditorAsset]
        (editor-assets tag tags type))
      (GET "/editor/assets-search" []
        :summary "Return list of available assets"
        :query-params [{type :- s/Str nil}, {q :- s/Str ""} {tag :- s/Int nil}]
        :return [EditorAssetWithTags]
        (editor-assets-search q tag type))             
      (GET "/editor/tags" []
        :summary "Return list of available tags"
        :return [EditorTag]
        (find-all-tags))
      (GET "/editor/tags-by-name" []
        :summary "Return tag by name"
        :query-params [{tags :- [s/Str] []}]
        :return [EditorTag]
        (find-tag-by-name tags))
      (GET "/editor/character-skin" []
        :summary "Return skins available for objects"
        :return [CharacterSkin]
        (character-skins)))))

(defroutes website-api-routes
  (context "/api/courses" []
    :tags ["course"]
    ;should go before general "/api/courses/:course-slug" to be accessible
    (GET "/available" []
      :return CoursesOrError
      :summary "Returns all available courses"
      (-> (fn [request] (-> (core/get-available-courses) response))
          sign/wrap-api-with-signature))
    (POST "/:course-id/translate" request
      :path-params [course-id :- s/Int]
      :return Course
      :body [translate Translate]
      :summary "Starts course translation"
      (handle-localize-course course-id translate request))
    (GET "/by-website-user/:website-user-id" request
      :path-params [website-user-id :- s/Int]
      :return [Course]
      :summary "Returns courses by website user id"
      (-> (fn [request] (-> (core/get-courses-by-website-user website-user-id) response))
          sign/wrap-api-with-signature))
    (POST "/:course-slug/publish" request
      :path-params [course-slug :- s/Str]
      :return Course
      :summary "Send request for publish on review"
      (handle-publish-course course-slug request))
    (POST "/:course-slug/archive" request
      :path-params [course-slug :- s/Str]
      :return Course
      :summary "Send request for archive"
      (handle-archive-course course-slug request))
    (POST "/:course-id/review" request
      :path-params [course-id :- s/Int]
      :return Course
      :body [review-result ReviewResult]
      :summary "Send review result. Publish or decline. Or unpubish"
      (handle-review-course course-id review-result request))
    (GET "/list/:type/:status" request
      :path-params [type :- s/Str status :- s/Str]
      :return CoursesOrError
      :summary "Retuns a list of courses on review"
      (handle-get-on-review-courses type status request))))

(defroutes courses-api-routes
  (context "/api/courses" []
           :tags ["course"]
           (POST "/" request
                 :return Course
                 :body [course-data CreateCourse]
                 :summary "Creates a new course"
                 (handle-create-course course-data request))
           (POST "/:course-slug/create-activity" request
                 :path-params [course-slug :- s/Str]
                 :return Activity
                 :body [activity-data CreateActivity]
                 :summary "Creates a new activity from template"
                 (handle-create-course-activity course-slug activity-data request))
           (POST "/:course-slug/create-activity-placeholder" request
                 :path-params [course-slug :- s/Str]
                 :return Activity
                 :body [activity-data CreateActivityPlaceholder]
                 :summary "Creates a new activity placeholder"
                 (handle-create-activity-placeholder course-slug activity-data request))
           (POST "/:course-slug/duplicate-activity" request
                 :path-params [course-slug :- s/Str]
                 :return Activity
                 :body [data DuplicateActivity]
                 :summary "Creates a new activity as a copy of an existing one"
                 (handle-duplicate-course-activity course-slug data request))
           (POST "/:course-slug/scenes/:scene-slug/versions" request
                 :path-params [course-slug :- s/Str scene-slug :- s/Str]
                 :return Activity
                 :body [data CreateActivityVersion]
                 :summary "Creates a new activity version from template"
                 (handle-create-activity-version course-slug scene-slug data request))
           (POST "/:course-slug/update-activity/:scene-slug" request
                 :path-params [course-slug :- s/Str scene-slug :- s/Str]
                 :return s/Any
                 :body [activity-data s/Any]
                 :summary "Updates activity using template"
                 (handle-update-activity course-slug scene-slug activity-data request))
           (POST "/:course-slug/update-template/:scene-slug" request
                 :path-params [course-slug :- s/Str scene-slug :- s/Str]
                 :return s/Any
                 :summary "Updates activity template to latest version"
                 (handle-update-template course-slug scene-slug request))
           (GET "/:course-slug/scenes-with-skills" []
                :path-params [course-slug :- s/Str]
                :tags ["skill"]
                :summary "Returns list of skills for each scene inside given course"
                (-> course-slug core/get-course-scene-skills response))
           (PUT "/:course-slug/scenes/:scene-slug/preview" request
                :path-params [course-slug :- s/Str scene-slug :- s/Str]
                :return Activity
                :body [data SetActivityPreview]
                :summary "Sets activity preview image url"
                (handle-set-activity-preview course-slug scene-slug data request)))
  (GET "/api/skills" []
       :tags ["skill"]
       :return Skills
       :summary "Returns list of skills with strands and topics"
       (-> (skills/get-skills) response)))

(defn collaborator-route
  [{{course-slug :course-slug} :route-params :as request}]
  (let [user-id (current-user request)]
    (when-not (core/collaborator-by-course-slug? user-id course-slug)
      (throw-unauthorized {:role :educator})))
  (resource-response "index.html" {:root "public"}))

(defn- editor-url
  [{:keys [course-slug scene-slug]}]
  (str "/courses/" (url-encode course-slug) "/editor-v2/" (url-encode scene-slug)))

(defn- sandbox-url
  [{:keys [course-slug scene-slug]}]
  (str "/s/" (url-encode course-slug) "/" (url-encode scene-slug)))

(defroutes course-pages-routes
  (GET "/courses/:course-slug/edit" [course-slug] (-> course-slug core/first-activity editor-url redirect))
  (GET "/courses/:course-slug/play" [course-slug] (-> course-slug core/first-activity sandbox-url redirect))
  (GET "/courses/:course-slug/editor" request (collaborator-route request))
  (GET "/courses/:course-slug/editor/:scene-id" request (collaborator-route request))
  (GET "/courses/:course-slug/editor/concepts/:concept-id" request (collaborator-route request))
  (GET "/courses/:course-slug/editor/add-concept" request (collaborator-route request))
  (GET "/courses/:course-slug/editor/levels/:level-id/lessons/:lesson-id" request (collaborator-route request))
  (GET "/courses/:course-slug/editor/levels/:level-id/add-lesson" request (collaborator-route request))

  (GET "/courses/:course-slug/editor-v2" request (collaborator-route request))
  (GET "/courses/:course-slug/editor-v2/:scene-id" request (collaborator-route request))
  (GET "/courses/:course-slug/editor-v2/concepts/:concept-id" request (collaborator-route request))
  (GET "/courses/:course-slug/editor-v2/add-concept" request (collaborator-route request))
  (GET "/courses/:course-slug/editor-v2/levels/:level-id/lessons/:lesson-id" request (collaborator-route request))
  (GET "/courses/:course-slug/editor-v2/levels/:level-id/add-lesson" request (collaborator-route request))
  (GET "/courses/:course-slug/table" request (collaborator-route request))
  (GET "/courses/:course-slug/scenes-crossing" request (collaborator-route request)))

(defroutes course-routes
  (GET "/api/courses/:course-slug" [course-slug] (-> course-slug core/get-course-data response))

  ;; Scenes
  (GET "/api/courses/:course-slug/scenes/:scene-name" [course-slug scene-name]
    (-> (core/get-scene-data course-slug scene-name) response))
  (GET "/api/courses/:course-slug/first-scene" [course-slug]
    (-> (core/get-first-scene-data course-slug) response))
  (POST "/api/courses/:course-slug/scenes/:scene-name" [course-slug scene-name :as request]
    (handle-save-scene course-slug scene-name request))
  (PUT "/api/courses/:course-slug/scenes/:scene-name" [course-slug scene-name :as request]
    (handle-update-scene course-slug scene-name request))
  (POST "/api/courses/:course-slug/scenes/:scene-name/skills" [course-slug scene-name :as request]
    (handle-update-scene-skills course-slug scene-name request))

  (POST "/api/courses/:course-slug" [course-slug :as request]
    (handle-save-course course-slug request))

  (GET "/api/courses/:course-slug/info" [course-slug]
    (-> course-slug core/get-course-info response))
  (PUT "/api/courses/:course-id/info" [course-id :as request]
    (handle-save-course-info course-id request))

  (GET "/api/courses/:course-slug/versions" [course-slug] (-> course-slug core/get-course-versions response))
  (GET "/api/courses/:course-slug/scenes/:scene-name/versions" [course-slug scene-name]
    (-> (core/get-scene-versions course-slug scene-name) response))
  (POST "/api/course-versions/:version-id/restore" [version-id :as request]
    (handle-restore-course-version version-id request))
  (POST "/api/scene-versions/:version-id/restore" [version-id :as request]
    (handle-restore-scene-version version-id request))

  (GET "/api/available-courses" request
    (-> (core/get-available-courses) response))

  (POST "/api/courses/:course-id/duplicate" request
    :coercion :spec
    :path-params [course-id :- ::course-spec/id]
    :body [data ::course-spec/duplicate]
    (let [user-id (current-user request)]
      (when-not (is-admin? user-id)
        (throw-unauthorized {:role :educator}))
      (-> (core/duplicate-course course-id data user-id)
          response)))
  (GET "/api/my-courses" request
    :coercion :spec
    (let [user-id (current-user request)
          result (if (is-super-admin? user-id)
                   (core/my-courses-admin user-id)
                   (core/my-courses user-id))]
      (-> result
          response)))
  
  (PUT "/api/schools/:school-id/assign-course" request
    :coercion :spec
    :path-params [school-id :- ::course-spec/school-id]
    :body [data ::course-spec/assign-school-course]
    :return ::course-spec/school-course
    (let [user-id (current-user request)]
      (when-not (is-admin? user-id)
        (throw-unauthorized {:role :educator}))
      (-> (core/assign-school-course school-id data)
          response)))

  (PUT "/api/schools/:school-id/assign-courses" request
    :coercion :spec
    :path-params [school-id :- ::course-spec/school-id]
    :body [data ::course-spec/assign-school-courses]
    :return ::course-spec/school-courses
    (let [user-id (current-user request)]
      (when-not (is-admin? user-id)
        (throw-unauthorized {:role :educator}))
      (-> (core/assign-school-courses school-id data)
          response)))

  (DELETE "/api/schools/:school-id/assign-course" request
    :coercion :spec
    :path-params [school-id :- ::course-spec/school-id]
    :body [data ::course-spec/unassign-school-course]
    :return ::course-spec/school-course
    (let [user-id (current-user request)]
      (when-not (is-admin? user-id)
        (throw-unauthorized {:role :educator}))
      (-> (core/unassign-school-course school-id data)
          response)))

  (GET "/api/schools/:school-id/courses" request
    :coercion :spec
    :path-params [school-id :- ::course-spec/school-id]
    :return ::course-spec/courses
    (let [user-id (current-user request)]
      (when-not (or (is-admin? user-id) (school/school-teacher? school-id user-id))
        (throw-unauthorized {:role :educator}))
      (-> (core/get-school-courses school-id user-id)
          response)))
  (GET "/api/classes/:class-id/course" request
    :coercion :spec
    :path-params [class-id :- ::course-spec/class-id]
    (let [user-id (current-user request)]
      (when-not (or (is-admin? user-id) (school/class-teacher? class-id user-id))
        (throw-unauthorized {:role :educator}))
      (-> (core/get-class-course class-id)
          response)))
  (GET "/api/available-activities" request
    :coercion :spec
    :query-params [{lang :- string? nil}]
    (let [user-id (current-user request)]
      (-> (core/get-available-activities lang)
          response)))
  (GET "/api/visible-activities" request
    :coercion :spec
    :query-params [{lang :- string? nil}]
    (let [user-id (current-user request)]
      (-> (core/get-visible-activities lang)
          response)))
  (GET "/api/available-books" request
    :coercion :spec
    :query-params [{lang :- string? nil}]
    (let [user-id (current-user request)]
      (-> (core/get-available-books lang)
          response)))
  (GET "/api/my-activities" request
    :coercion :spec
    :query-params [{lang :- string? nil}]
    (let [user-id (current-user request)
          result (if (is-super-admin? user-id)
                   (core/my-activities-admin user-id lang)
                   (core/my-activities user-id lang))]
      (-> result
          response)))
  (GET "/api/my-books" request
    :coercion :spec
    :query-params [{lang :- string? nil}]
    (let [user-id (current-user request)
          result (if (is-super-admin? user-id)
                   (core/my-books-admin user-id lang)
                   (core/my-books user-id lang))]
      (-> result
          response)))
  (GET "/api/activities/:activity-id/current-version" request
    :coercion :spec
    :path-params [activity-id :- ::activity-spec/id]
    (-> (core/get-activity-current-version activity-id)
        response))
  (GET "/api/activities/:activity-id/versions" request
    :coercion :spec
    :path-params [activity-id :- ::activity-spec/id]
    (-> (core/get-activity-versions activity-id)
        response))
  (GET "/api/activities/:activity-id" request
    :coercion :spec
    :path-params [activity-id :- ::activity-spec/id]
    (-> (core/get-activity activity-id)
        response))
  (PUT "/api/activities/:activity-id" request
    :coercion :spec
    :path-params [activity-id :- ::activity-spec/id]
    :body [data ::activity-spec/edit-activity]
    (let [user-id (current-user request)]
      (when-not (is-admin? user-id)
        (throw-unauthorized {:role :educator}))
      (-> (core/edit-activity activity-id data)
          response)))
  (PUT "/api/activities/:activity-id/archive" request
    :coercion :spec
    :path-params [activity-id :- ::activity-spec/id]
    :body [data ::activity-spec/archive-activity]
    (let [user-id (current-user request)]
      (when-not (is-admin? user-id)
        (throw-unauthorized {:role :educator}))
      (-> (core/archive-activity activity-id data)
          response)))
  (PUT "/api/activities/:activity-id/toggle-visibility" request
    :coercion :spec
    :path-params [activity-id :- ::activity-spec/id]
    :body [data ::activity-spec/toggle-visibility]
    (let [user-id (current-user request)]
      (when-not (is-admin? user-id)
        (throw-unauthorized {:role :educator}))
      (-> (core/toggle-activity-visibility activity-id data)
          response)))
  (PUT "/api/activities/:activity-id/toggle-locked" request
    :coercion :spec
    :path-params [activity-id :- ::activity-spec/id]
    :body [data ::activity-spec/toggle-locked]
    (let [user-id (current-user request)]
      (when-not (is-admin? user-id)
        (throw-unauthorized {:role :educator}))
      (-> (core/toggle-activity-locked activity-id data)
          response)))
  (POST "/api/activities/:activity-id/duplicate" request
    :coercion :spec
    :path-params [activity-id :- ::activity-spec/id]
    :body [data ::activity-spec/duplicate]
    (let [user-id (current-user request)]
      (when-not (is-admin? user-id)
        (throw-unauthorized {:role :educator}))
      (-> (core/duplicate-activity activity-id data user-id)
          response)))
  (POST "/api/activities/:activity-id/version" request
    :coercion :spec
    :path-params [activity-id :- ::activity-spec/id]
    :body [data ::activity-spec/activity-data]
    (let [user-id (current-user request)]
      (when-not (is-admin? user-id)
        (throw-unauthorized {:role :educator}))
      (-> (core/save-activity-version activity-id data user-id)
          response)))
  (POST "/api/books" request
    :coercion :spec
    :body [data ::book-spec/create-book]
    (let [user-id (current-user request)]
      (when-not (is-admin? user-id)
        (throw-unauthorized {:role :educator}))
      (-> (core/create-book data user-id)
          response)))
  (PUT "/api/activities/:activity-id/template-options" request
    :coercion :spec
    :path-params [activity-id :- ::activity-spec/id]
    :body [data ::activity-spec/template-options]
    (let [user-id (current-user request)]
      (when-not (is-admin? user-id)
        (throw-unauthorized {:role :educator}))
      (-> (core/apply-template-options activity-id data user-id)
          response)))
  (PUT "/api/activities/:activity-id/update-template" request
    :coercion :spec
    :path-params [activity-id :- ::activity-spec/id]
    :body [data ::activity-spec/update-template]
    (let [user-id (current-user request)]
      (when-not (is-admin? user-id)
        (throw-unauthorized {:role :educator}))
      (-> (core/update-activity-template! activity-id user-id)
          response)))
  (POST "/api/activities/:activity-id/template-actions" request
    :coercion :spec
    :path-params [activity-id :- ::activity-spec/id]
    :body [data ::activity-spec/template-action]
    (let [user-id (current-user request)]
      (when-not (is-admin? user-id)
        (throw-unauthorized {:role :educator}))
      (-> (core/apply-template-action activity-id data user-id)
          response)))
  (PUT "/api/activities/:activity-id/settings" request
    :coercion :spec
    :path-params [activity-id :- ::activity-spec/id]
    :body [data ::activity-spec/update-settings]
    (let [user-id (current-user request)]
      (when-not (is-admin? user-id)
        (throw-unauthorized {:role :educator}))
      (-> (core/update-activity-settings! activity-id data user-id)
          response))))
