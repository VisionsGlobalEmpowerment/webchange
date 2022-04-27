(ns webchange.admin.routes
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.state :as state]
    [webchange.utils.module-router :as module-router]))

(def routes {""         :dashboard
             "/"        :dashboard
             "/login"   :login
             "/schools" {""                            :schools
                         ["/" [#"[\w-%]+" :school-id]] {""         :school-profile
                                                        "/classes" {["/" [#"[\w-%]+" :class-id]] :class-profile}}}})

(defn get-title
  [{:keys [handler props]}]
  (let [root "TabSchool"
        connector " / "
        s #(clojure.string/join connector (concat [root] %))]
    (case handler
      :dashboard (s [])
      :schools (s ["Schools"])
      :school-profile (s ["School Profile" (:school-id props)])
      :class-profile (s ["Class Profile" (:class-id props)])
      (s []))))

(defonce router (atom nil))

(defn- dispatch-route
  [{:keys [handler route-params]}]
  (re-frame/dispatch [::state/set-current-page {:handler handler
                                                :props   route-params}]))

(defn init!
  [root-path]
  (reset! router (module-router/init! root-path routes dispatch-route)))

(defn set-title!
  [page-params]
  (->> (get-title page-params)
       (set! (.-title js/document))))

(re-frame/reg-event-fx
  ::redirect
  (fn [{:keys [_]} [_ & args]]
    {::module-router/redirect {:router          @router
                               :redirect-params args}}))
