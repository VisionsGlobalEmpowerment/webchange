(ns webchange.course.handler
  (:require [compojure.api.sweet :refer [api context GET POST PUT DELETE defroutes routes swagger-routes]]
            [compojure.route :refer [resources not-found]]
            [ring.util.response :refer [resource-response response redirect]]
            [webchange.course.core :as core]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [clojure.tools.logging :as log]
            [webchange.common.handler :refer [handle current-user current-school validation-error]]
            [webchange.dataset.library :as datasets-library]
            [webchange.course.skills :as skills]
            [webchange.validation.validate :refer [validate]]
            [webchange.auth.core :as auth]
            [webchange.auth.website :as website]
            [spec-tools.data-spec :as ds]
            [schema.core :as s]
            [config.core :refer [env]]
            [webchange.common.hmac-sha256 :as sign]
            [compojure.api.middleware :as mw]
            [webchange.templates.core :as templates]
            [webchange.db.core :refer [*db*] :as db]
            [webchange.auth.roles :refer [is-admin?]]))

(defn handle-save-scene
  [course-slug scene-name request]
  (let [user-id (current-user request)
        save (fn [data] (core/save-scene-with-processing course-slug scene-name data user-id))]
    (when-not (core/collaborator-by-course-slug? user-id course-slug)
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
    (when-not (core/collaborator-by-course-slug? user-id course-slug)
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
    (when-not (core/collaborator-by-course-id? user-id course-id)
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
      (-> (core/localize course-id {:lang language :owner-id owner-id :website-user-id website-user-id})
          handle)
      (handle [false {:message "User not found"}]))))

(defn handle-create-course
  [data request]
  (let [owner-id (current-user request)
        course (core/create-course data owner-id)]
    (when (:concept-list-id data)
      (datasets-library/create-dataset! (-> course second :slug) (:concept-list-id data)))
    (handle course)))

(defn handle-create-activity
  [course-slug data request]
  (let [user-id (current-user request)
        activity (templates/activity-from-template data)
        metadata (templates/metadata-from-template data)]
    (when-not (core/collaborator-by-course-slug? user-id course-slug)
      (throw-unauthorized {:role :educator}))
    (-> (core/create-scene! activity metadata course-slug (:name data) (:skills data) user-id)
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

(defn handle-update-activity
  [course-slug scene-slug data request]
  (let [user-id (current-user request)]
    (when-not (core/collaborator-by-course-slug? user-id course-slug)
      (throw-unauthorized {:role :educator}))
    (-> (core/update-activity! course-slug scene-slug data user-id)
        handle)))

(defn handle-publish-course
  [course-slug request]
  (let [user-id (current-user request)]
    (when-not (core/collaborator-by-course-slug? user-id course-slug)
      (throw-unauthorized {:role eduction}))
    (-> (core/publish-course! course-slug)
        handle)))

(defn handle-review-course
  [course-slug review-result request]
  (let [user-id (current-user request)]
    (when-not (is-admin? user-id)
      (throw-unauthorized {:role eduction}))
    (-> (core/review-course! course-slug review-result)
        handle)))

(defn handle-get-on-review-courses
  [type status request]
  (let [user-id (current-user request)]
    (when-not (is-admin? user-id)
      (throw-unauthorized {:role eduction}))
    (-> (core/get-on-review-courses type status)
        handle)))

(s/defschema Course {:id s/Int :name s/Str :slug s/Str :image-src (s/maybe s/Str) :url s/Str :lang (s/maybe s/Str) (s/optional-key :level) s/Str (s/optional-key :subject) s/Str})
(s/defschema CreateCourse {:name s/Str :lang s/Str (s/optional-key :level) s/Str (s/optional-key :subject) s/Str (s/optional-key :concept-list-id) s/Int (s/optional-key :type) s/Str (s/optional-key :image-src) s/Str})
(s/defschema Translate {:user-id s/Int :language s/Str})
(s/defschema EditorTag {:id s/Int :name s/Str})
(s/defschema EditorAsset {:id s/Int :path s/Str :thumbnail-path s/Str :type (s/enum "single-background" "background" "surface" "decoration")})
(s/defschema CharacterSkin {:name s/Str :width s/Num :height s/Num :skins [s/Str] :animations [s/Str]})

(s/defschema CreateActivity {:name s/Str :template-id s/Int :skills [s/Int] s/Keyword s/Any})
(s/defschema CreateActivityVersion {:template-id s/Int s/Keyword s/Any})
(s/defschema CreateActivityPlaceholder {:name s/Str})
(s/defschema Activity {:id s/Int :name s/Str :scene-slug s/Str :course-slug s/Str})

(s/defschema Topic {:name s/Str :strand s/Keyword})
(s/defschema Skill {:id s/Int :name s/Str :abbr s/Str :grade s/Str :topic s/Keyword :tags [s/Str]})
(s/defschema Skills {:levels {s/Keyword s/Str} :subjects {s/Keyword s/Str} :skills [Skill] :topics {s/Keyword Topic} :strands {s/Keyword s/Str}})

(s/defschema ActivityWithSkills {:id s/Int :name s/Str :course-id s/Int :is-placeholder s/Bool :skills [Skill]})
(s/defschema ReviewResult {:status s/Str (s/optional-key :comment) s/Str})

(defn character-skins []
  (response (core/find-all-character-skins)))

(defn editor-assets [tag type]
  (response (core/editor-assets tag type)))

(defn find-all-tags []
  (response (core/find-all-tags)))

(s/defschema Error403 {:errors [{:message s/Str}]})

(s/defschema CoursesOrError (s/either [Course] Error403))

(defroutes editor-api-routes
  (api
    (swagger-routes {:ui   "/api-docs"
                     :data {:info {:title "TabSchools API"}
                            :tags [{:name "course", :description "Course APIs"}]}})
    (context "/api/courses" []
      :tags ["editor-assets"]
      (GET "/editor/assets" []
        :summary "Return list of available assets"
        :query-params [{type :- s/Str nil}, {tag :- s/Int nil}]
        :return [EditorAsset]
        (editor-assets tag type))
      (GET "/editor/tags" []
        :summary "Return list of available assets"
        :return [EditorTag]
        (find-all-tags))
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
    (POST "/:course-slug/review" request
      :path-params [course-slug :- s/Str]
      :return Course
      :body [review-result ReviewResult]
      :summary "Send review result. Publish or decline"
      (handle-review-course course-slug review-result request))
    (GET "/list/:type/:status" request
      :path-params [type :- s/Str status :- s/Str]
      :return CoursesOrError
      :summary "Retuns a list of courses on review"
      (handle-get-on-review-courses type status request)))
  (context "/api/books" []
    :tags ["book"]
    (GET "/library" []
      :return CoursesOrError
      :summary "Returns all published books"
      (-> (fn [request] (-> (core/get-book-library) response))
          sign/wrap-api-with-signature))
    (GET "/by-website-user/:website-user-id" request
      :path-params [website-user-id :- s/Int]
      :return [Course]
      :summary "Returns books by website user id"
      (-> (fn [request] (-> (core/get-books-by-website-user website-user-id) response))
          sign/wrap-api-with-signature))))

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
      (handle-create-activity course-slug activity-data request))
    (POST "/:course-slug/create-activity-placeholder" request
      :path-params [course-slug :- s/Str]
      :return Activity
      :body [activity-data CreateActivityPlaceholder]
      :summary "Creates a new activity placeholder"
      (handle-create-activity-placeholder course-slug activity-data request))
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
    (GET "/:course-slug/scenes-with-skills" []
      :path-params [course-slug :- s/Str]
      :tags ["skill"]
      :return [ActivityWithSkills]
      :summary "Returns list of skills for each scene inside given course"
      (-> course-slug core/get-course-scene-skills response)))
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

(defroutes course-pages-routes
  (GET "/courses/:course-slug/editor" request (collaborator-route request))
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
    (handle-restore-scene-version version-id request)))
