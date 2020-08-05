(ns webchange.service-worker.requests.core
  (:require
    [clojure.string :refer [join]]
    [webchange.service-worker.logger :as logger]
    [webchange.service-worker.wrappers :refer [catch then js-fetch json-stringify promise-resolve promise-reject response-json require-status-ok!]]))

(defn get-params-str
  [params]
  (if-not (empty? params)
    (->> params
         (map (fn [[param-name param-value]]
                (str (name param-name) "=" param-value)))
         (join "&")
         (str "?"))
    ""))

(defn http-get
  ([url]
   (http-get url {}))
  ([url {:keys [raw?]}]
   (logger/debug "Get from url: " url)
   (-> (js-fetch url (clj->js {:method  "GET"
                               :headers {"Content-Type" "application/json"}}))
       (then require-status-ok!)
       (then (fn [response]
               (if raw?
                 (promise-resolve response)
                 (-> (response-json response)
                     (then (fn [data]
                             (let [prepared-data (js->clj data :keywordize-keys true)]
                               (logger/debug-folded (str "Response from url: " url) prepared-data)
                               (promise-resolve prepared-data))))
                     (catch (fn [error]
                              (promise-reject error))))))))))

(defn http-post
  [url data]
  (logger/debug "Post to url: " url)
  (-> (js-fetch url (clj->js {:method  "POST"
                              :headers {"Content-Type" "application/json"}
                              :body    (json-stringify (clj->js data))}))
      (then require-status-ok!)))
