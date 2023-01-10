(ns webchange.secondary.updater
  (:require
    [clj-http.client :as http]
    [webchange.secondary.core :as core]
    [webchange.common.hmac-sha256 :as sign]
    [clojure.java.io :as io]
    [clojure.java.shell :as sh]
    [config.core :refer [env]]
    [clojure.tools.logging :as log]
    [clojure.core.async :refer [<!] :as async]
    [mount.core :as mount]))

(def restart-exit-code 42)

(defonce server-status (atom :initializing))

(defonce server-instance (atom nil))

(defn stop-server
  []
  (async/go
    (<! (async/timeout 5000))
    (log/debug "Trying to stop web server...")
    (.stop @server-instance)
    (log/debug "Web server stopped")
    (mount/stop)
    (log/debug "Components stopped")
    (System/exit restart-exit-code)))

(defn get-latest-binary
  []
  (let [url (core/make-url-absolute "api/software/latest")
        response (http/with-additional-middleware [#'sign/wrap-apikey]
                   (http/get url {:as :stream}))]
    (if (http/success? response)
      (-> response :body))))

(defn update-local-instance!
  []
  (when-let [local-binary-path (env :local-binary-path)]
    (sh/sh "cp" local-binary-path (str local-binary-path "." (System/currentTimeMillis) ".bak"))
    (with-open [latest-binary-in (get-latest-binary)
                local-binary-out (io/output-stream local-binary-path)]
      (io/copy latest-binary-in local-binary-out))
    (log/debug "About to restart service...")
    (stop-server)))

(defn start-sync!
  [school-id]
  (reset! server-status :update-app)
  (spit "update-content.lock" school-id)
  (update-local-instance!))

(defn resume-sync!
  []
  (if-let [update-content (slurp "update-content.lock")]
    (try
      (reset! server-status :update-content)
      (let [school-id (Integer/parseInt update-content)]
        (core/upload-stat school-id)
        (core/update-course-data! school-id (env :requested-courses))
        (core/update-assets! school-id (env :requested-courses) false)
        (io/delete-file "update-content.lock" true))
      (reset! server-status :ready)
      (catch Exception e
        (log/debug e)
        (reset! server-status :error)))
    (reset! server-status :ready)))

(defn get-sync-status
  []
  {:status @server-status})
