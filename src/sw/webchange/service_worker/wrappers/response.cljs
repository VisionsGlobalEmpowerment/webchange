(ns webchange.service-worker.wrappers.response)

(defn clone
  "The clone() method of the Response interface creates a clone of a response object,
  identical in every way, but stored in a different variable."
  [& {:keys [response]}]
  (.clone response))
