(ns webchange.service-worker.utils)

(defn group-promises
  [promises]
  (->> promises
       (clj->js)
       (js/Promise.all)))