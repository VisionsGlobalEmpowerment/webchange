(ns webchange.lesson-builder.routes
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.state :as state]
    [webchange.utils.module-router :as module-router]))

(def routes {"/create" :create
             "/activity" {"" :create-activity
                          ["/" [#"[\w-%]+" :activity-id]] :lesson-builder}
             "/book" {"" :create-book
                      ["/" [#"[\w-%]+" :book-id]] :book-creator}})

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
