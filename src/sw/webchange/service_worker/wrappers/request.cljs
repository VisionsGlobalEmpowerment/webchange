(ns webchange.service-worker.wrappers.request)

(defn url
  [request]
  (->> request
       (.-url)
       (js/URL.)))

(defn pathname
  [request]
  (->> request
       (url)
       (.-pathname)))

