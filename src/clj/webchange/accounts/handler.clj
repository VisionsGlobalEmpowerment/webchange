(ns webchange.accounts.handler
  (:require [buddy.auth :refer [throw-unauthorized]]
            [compojure.api.sweet :refer [context GET POST PUT DELETE defroutes]]
            [ring.util.response :refer [response redirect]]
            [clojure.tools.logging :as log]
            [webchange.common.handler :refer [current-user]]
            [webchange.auth.roles :refer [is-admin?]]
            [webchange.validation.specs.account :as account-spec]
            [webchange.accounts.core :as core]))

(defroutes accounts-routes
  (GET "/api/accounts-by-type/:type" request
       :coercion :spec
       :path-params [type :- ::account-spec/type]
       :query-params [{page :- int? 1}]
       (let [user-id (current-user request)]
         (when-not (is-admin? user-id)
           (throw-unauthorized {:role :educator}))
         (-> (core/accounts-by-type type page)
             response)))
  (GET "/api/accounts/:id" request
       :coercion :spec
       :path-params [id :- ::account-spec/id]
       (let [user-id (current-user request)]
         (when-not (is-admin? user-id)
           (throw-unauthorized {:role :educator}))
         (-> (core/get-account id)
             response)))
  (DELETE "/api/accounts/:id" request
          :coercion :spec
          :path-params [id :- ::account-spec/id]
          (let [user-id (current-user request)]
            (when-not (is-admin? user-id)
              (throw-unauthorized {:role :educator}))
            (-> (core/delete-account id)
                response)))
  (POST "/api/accounts" request
        :coercion :spec
        :body [data ::account-spec/create-account]
        (let [user-id (current-user request)]
          (when-not (is-admin? user-id)
            (throw-unauthorized {:role :educator}))
          (-> (core/create-account data)
              response)))
  (POST "/api/accounts/register" request
        :coercion :spec
        :body [data ::account-spec/register-account]
        (-> (core/register-account data)
            response))

  (PUT "/api/accounts/:id" request
       :coercion :spec
       :path-params [id :- ::account-spec/id]
       :body [data ::account-spec/edit-account]
       (let [user-id (current-user request)]
         (when-not (is-admin? user-id)
           (throw-unauthorized {:role :educator}))
         (-> (core/edit-account id data)
             response)))
  (PUT "/api/accounts/:id/password" request
       :coercion :spec
       :path-params [id :- ::account-spec/id]
       :body [data ::account-spec/change-password]
       (let [user-id (current-user request)]
         (when-not (is-admin? user-id)
           (throw-unauthorized {:role :educator}))
         (-> (core/change-password id data)
             response)))
  (PUT "/api/accounts/:id/status" request
       :coercion :spec
       :path-params [id :- ::account-spec/id]
       :body [data ::account-spec/set-status]
       (let [user-id (current-user request)]
         (when-not (is-admin? user-id)
           (throw-unauthorized {:role :educator}))
         (-> (core/set-account-status id data)
             response))))

(defroutes accounts-pages-routes
  (GET "/accounts/confirm-email/:code" request
       :coercion :spec
       :path-params [code :- string?]
       (if (core/confirm-email code)
         (redirect "/")
         (redirect "/"))))
