(ns webchange.login.routes
  (:require
    [re-frame.core :as re-frame]
    [webchange.login.state :as state]
    [webchange.utils.module-router :as module-router]))

(def module-location {:admin  "/admin"
                      :parent "/parents"})

(def routes {""               :sign-in
             "reset-password" :reset-password
             "accounts" {"/sign-up" :sign-up
                         "/sign-up-success" :sign-up-success
                         "/reset-password" {"" :reset-password-email
                                            ["/" [#"[\w-%]+" :code]] :reset-password-code}
                         ["/sign-in/" [#"[\w-%]+" :type]] :sign-in}})

(defonce router (atom nil))

(defn- dispatch-route
  [{:keys [handler route-params]}]
  (re-frame/dispatch [::state/set-current-page {:handler handler
                                                :props   route-params}]))

(defn init!
  [root-path]
  (reset! router (module-router/init! root-path routes dispatch-route)))

(re-frame/reg-event-fx
  ::redirect
  (fn [{:keys [_]} [_ & args]]
    {::module-router/redirect {:router          @router
                               :redirect-params args}}))

(re-frame/reg-fx
  :redirect-to-module
  (fn [user-type]
    (let [module (case user-type
                   "admin" :admin
                   "bbs-admin" :admin
                   :parent)
          {:keys [redirect-to]} @router]
      (->> (get module-location module)
           (set! (.-location js/document))
           (redirect-to)))))
