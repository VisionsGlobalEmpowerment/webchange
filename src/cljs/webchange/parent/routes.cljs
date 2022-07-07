(ns webchange.parent.routes
  (:require
    [clojure.string :as str]
    [re-frame.core :as re-frame]
    [webchange.parent.state :as state]
    [webchange.utils.module-router :as module-router]))

(def routes {""             :students
             "/"            :students
             "/add-student" :student-add
             "/help"        :faq})


(defn get-title
  ([params]
   (get-title params {}))
  ([{:keys [handler props]} {:keys [with-root?] :or {with-root? true}}]
   (let [root "Admin"
         connector " / "
         s #(str/join connector (if with-root? (concat [root] %) %))]
     (case handler
       :dashboard (s ["Dashboard"])
       (s [(-> (or handler :unknown) (clojure.core/name) (clojure.string/replace "-" " ") (str/capitalize))])))))

(defonce router (atom nil))

(defn- dispatch-route
  [{:keys [handler route-params url-params]}]
  (re-frame/dispatch [::state/set-current-page {:handler handler
                                                :props   (cond-> route-params
                                                                 (some? url-params) (assoc :url-params url-params))}]))

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
