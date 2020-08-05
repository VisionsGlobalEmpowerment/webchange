(ns webchange.service-worker.virtual-server.handlers.core
  (:require
    [bidi.bidi :as bidi]
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.virtual-server.handlers.current-progress :as current-progress]
    [webchange.service-worker.virtual-server.handlers.login :as login]
    [webchange.service-worker.virtual-server.handlers.current-user :as current-user]
    [webchange.service-worker.virtual-server.handlers.utils.responses-store :as store]
    [webchange.service-worker.wrappers :refer [js-fetch online? promise-all promise-resolve request-method request-pathname then]]))

(def routes ["/api/" {"courses" {["/" :course-id "/current-progress"] :current-progress}
                      "students" {"/login" :login}
                      "users" {"/current" :current-user}}])

(def handlers {:current-progress current-progress/handlers
               :login login/handlers
               :current-user current-user/handlers})

(defn- match-request
  [request-or-route]
  (let [route (if-not (string? request-or-route)
                (request-pathname request-or-route)
                request-or-route)]
    (bidi/match-route routes route)))

(defn- get-handler
  [request handler-name]
  (let [handlers-map (get handlers handler-name)
        method (request-method request)]
    (if (contains? handlers-map method)
      (if (online?)
        (get-in handlers-map [method :online])
        (get-in handlers-map [method :offline]))
      (-> (str "Method <" method "> is not defined ind <" handler-name "> handlers") js/Error. throw))))

(defn has-handler?
  [request-or-route]
  (boolean (match-request request-or-route)))

(defn handle-request
  [request]
  (let [match (match-request request)
        handler (get-handler request (:handler match))]
    (try
      (handler request (:route-params match))
      (catch js/Object e (logger/error e)))))

(defn prefetch
  [routes]
  (let [prefetch-route (fn [route] (-> (js-fetch route)
                                       (then #(store/put %))))]
    (promise-all (map prefetch-route routes))))

(defn flush-state
  []
  (doseq [handlers (vals handlers)]
    (when (contains? handlers :flush)
      ((:flush handlers)))))
