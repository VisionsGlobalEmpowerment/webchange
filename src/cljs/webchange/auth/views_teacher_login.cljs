(ns webchange.auth.views-teacher-login
  (:require
    [webchange.auth.views-auth-page :refer [auth-page]]
    [webchange.routes :as routes]
    [webchange.auth.views-sign-in :refer [sign-in-form]]
    [webchange.auth.views-sign-up :refer [sign-up-form]]))

(defn teacher-login-page
  [action]
  [auth-page
   (case action
     :sign-in [sign-in-form]
     :sign-up [sign-up-form]
     (routes/redirect-to :page-404))])
