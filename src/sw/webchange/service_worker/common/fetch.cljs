(ns webchange.service-worker.common.fetch
  (:require
    [webchange.service-worker.wrappers :refer [then js-fetch json-stringify response-json]]))

(defn- get-resources-from-api
  [url]
  (-> (js-fetch url)
      (then response-json)))

(defn post-data
  [url data]
  (js-fetch url (clj->js {:method  "POST"
                          :headers {"Content-Type" "application/json"}
                          :body    (json-stringify (clj->js data))})))

(defn fetch-web-app-resources
  []
  (get-resources-from-api "/api/resources/student-dashboard"))
