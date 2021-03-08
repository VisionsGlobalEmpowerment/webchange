(ns webchange.secondary.updater
  (:require
    [clj-http.client :as http]
    [webchange.secondary.core :as core]
    [webchange.common.hmac-sha256 :as sign]
    [clojure.java.io :as io]
    [clojure.java.shell :as sh]
    [config.core :refer [env]]
    [clojure.tools.logging :as log]))

(def restart-exit-code 42)

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
    (System/exit restart-exit-code)))



