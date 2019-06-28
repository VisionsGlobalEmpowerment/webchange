(ns webchange.service-worker.event-handlers.fetch
  (:require
    [clojure.string :refer [starts-with?]]
    [webchange.service-worker.config :refer [cache-names]]
    [webchange.service-worker.utils :refer [log]]
    [webchange.wrappers.cache :as cache]
    [webchange.wrappers.fetch :as fetch]
    [webchange.wrappers.response :as response]))

(def static-images-path "/raw/")

(defn static-image?
  [request-url]
  (let [pathname (.-pathname request-url)]
    (starts-with? pathname static-images-path)))

(defn serve-static-image
  [request]
  (cache/open
    :cache-name (:static cache-names)
    :then (fn [cache]
            (cache/match
              :cache cache
              :request request
              :then (fn [response]
                      (if response
                        response
                        (fetch/fetch
                          :request request
                          :then (fn [network-response]
                                  (cache/put
                                    :cache cache
                                    :request request
                                    :response (response/clone
                                                :response network-response))
                                  network-response))))))))

(defn fetch-event-handler
  [event]
  (let [request (.-request event)
        request-url (js/URL. (.-url request))]
    (cond
      (static-image? request-url) (.respondWith event (serve-static-image request))
      :else nil)))
