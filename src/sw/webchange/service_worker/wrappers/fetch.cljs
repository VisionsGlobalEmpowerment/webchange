(ns webchange.service-worker.wrappers.fetch)

(defn fetch
  "The fetch() method takes one mandatory argument, the path to the resource you want to fetch.
  It returns a Promise that resolves to the Response to that request, whether it is successful or not."
  [& {:keys [request then catch]}]
  (-> (js/fetch request)
      (.then then)
      (.catch catch)))
