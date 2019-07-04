(ns webchange.service-worker.wrappers.promise)

(defn all
  [promises]
  (->> promises
       (clj->js)
       (js/Promise.all)))