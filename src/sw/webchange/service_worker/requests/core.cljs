(ns webchange.service-worker.requests.core
  (:require
    [clojure.string :refer [join]]
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.wrappers :refer [then js-fetch json-stringify response-json]]))

(defn get-params-str
  [params]
  (if-not (empty? params)
    (->> params
         (map (fn [[param-name param-value]]
                (str (name param-name) "=" param-value)))
         (join "&")
         (str "?"))
    ""))

(defn get
  [url]
  (logger/debug "Get from url: " url)
  (-> (js-fetch url)
      (then response-json)
      (then (fn [data]
              (let [prepared-data (js->clj data :keywordize-keys true)]
                (logger/debug-folded (str "Response from url: " url) prepared-data)
                prepared-data)))))

(defn post
  [url data]
  (js-fetch url (clj->js {:method  "POST"
                          :headers {"Content-Type" "application/json"}
                          :body    (json-stringify (clj->js data))})))
