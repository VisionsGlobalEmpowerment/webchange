(ns webchange.ui.routes
  (:require
    [clojure.string :as str]
    [re-frame.core :as re-frame]
    [webchange.ui.pages.index :refer [pages]]
    [webchange.ui.state :as state]
    [webchange.utils.module-router :as module-router]))

(def routes (->> (dissoc pages :404)
                 (map first)
                 (map (fn [page-key]
                        [(str "/" (name page-key)) page-key]))
                 (into {})
                 (merge {""  :dashboard
                         "/" :dashboard})))

(defn get-title
  ([params]
   (get-title params {}))
  ([{:keys [handler]} {:keys [with-root?] :or {with-root? true}}]
   (let [root "UI"
         connector " / "
         s #(str/join connector (if with-root? (concat [root] %) %))]
     (case handler
       (s [(-> (or handler :unknown) (clojure.core/name) (str/capitalize))])))))

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
  :ui-redirect
  (fn [{:keys [_]} [_ & args]]
    {::module-router/redirect {:router          @router
                               :redirect-params args}}))
