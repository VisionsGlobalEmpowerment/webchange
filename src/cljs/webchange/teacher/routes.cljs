(ns webchange.teacher.routes
  (:require
    [re-frame.core :as re-frame]
    [webchange.teacher.state :as state]
    [webchange.utils.module-router :as module-router]))

(def routes {""         :dashboard
             "/"        :dashboard
             "/classes" :classes})

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
