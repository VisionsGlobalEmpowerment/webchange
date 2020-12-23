(ns webchange.service-worker.virtual-server.handlers.current-progress
  (:require
    [promesa.core :as p]
    [webchange.service-worker.broadcast.core :as bc]
    [webchange.service-worker.db.progress :as db-progress]
    [webchange.service-worker.db.events :as db-events]
    [webchange.service-worker.db.users :as db-users]
    [webchange.service-worker.virtual-server.logger :as logger]
    [webchange.service-worker.requests.api :as api]
    [webchange.service-worker.wrappers :refer [request-clone js-fetch promise-all promise-resolve promise-reject body-json require-status-ok! then catch data->response]]))

(defn- store-current-progress!
  [{progress :progress events :events offline :offline}]
  (-> (db-users/get-current-user)
      (then (fn [user]
              (if-not (nil? user)
                (do (db-progress/save-progress progress user offline)
                    (when offline
                      (doseq [event events] (db-events/save-event event user))))
                (logger/warn "Progress can not be stored: user is not defined."))))))

(defn- get-current-progress
  "Get current progress. Will redirect to student-login page if no user is stored in cache"
  []
  (-> (db-users/get-current-user)
      (then (fn [current-user]
              (logger/debug "[current-progress] current-user" current-user)
              (if-not (nil? current-user)
                (db-progress/get-progress current-user)
                (do (logger/warn "Can not get current progress: current user is not defined")
                    (bc/redirect-to-login)))))))

(defn- offline?
  "Return if offline mode is enabled in cache.
  If no cache available will return nil. Which is essentially no offline mode"
  []
  (-> (db-users/get-current-user)
      (then #(if %
               (db-progress/get-progress %)
               nil))
      (then #(get % :offline))))

(defn- store-body!
  [request offline]
  (let [cloned (request-clone request)]
    (-> (body-json cloned)
        (then (fn [body-cloned]
                (-> body-cloned
                    (js->clj :keywordize-keys true)
                    (assoc :offline offline)
                    (store-current-progress!))))
        (catch (fn [error]
                 (logger/error "Store current progress failed:" error))))
    (promise-resolve request)))

(defn- get-offline
  [request]
  (logger/debug "[current-progress] [GET] [offline]")
  (-> (get-current-progress)
      (then data->response)))

(defn- post-offline
  [request]
  (logger/debug "[current-progress] [POST] [offline]")
  (store-body! request true)
  (data->response {}))

(defn- get-online
  [request]
  (logger/debug "[current-progress] [GET] [online]")
  (let [cloned (request-clone request)]
    (p/let [offline (offline?)]
           (if offline
             (get-offline request)
             (-> (api/get-current-progress {:raw? true})
                 (then #(store-body! % false))
                 (catch (fn [error]
                          (logger/warn "[current-progress] [GET] [online]" error)
                          (get-offline cloned))))))))

(defn- post-online
  [request]
  (logger/debug "[current-progress] [POST] [online]")
  (let [cloned (request-clone request)]
    (store-body! request false)
    (-> (js-fetch request)
        (then require-status-ok!)
        (catch #(post-offline cloned)))))

(defn- is-dirty?
  [progress-data]
  (and (:progress progress-data) (:offline progress-data)))

(defn flush-current-progress
  []
  (logger/debug "[current-progress] Flush current progress ")
  (-> (db-users/get-current-user)
      (then (fn [current-user]
              (if-not (nil? current-user)
                (promise-all [(db-progress/get-progress current-user)
                              (db-events/get-events current-user)])
                (promise-reject "Cant not flush progress data. User is undefined."))))
      (then (fn [[progress-data events-data]]
              (if (is-dirty? progress-data)
                (let [progress (merge progress-data {:events (map :data events-data)})]
                  (logger/debug "Flush progress" (clj->js progress))
                  (-> (api/post-current-progress progress)
                      (then (fn []
                              (doseq [event events-data]
                                (logger/debug "Remove stored event" (clj->js event))
                                (db-events/remove-by-date (get-in event [:data :created-at])))))))
                (do (logger/debug "Progress is empty. Skip flush")
                    (promise-resolve)))))
      (catch (fn [error]
               (logger/warn "Flush progress failed" error)))))

(def handlers {"GET"  {:online  get-online
                       :offline get-offline}
               "POST" {:online  post-online
                       :offline post-offline}
               :flush flush-current-progress})
