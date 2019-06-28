(ns webchange.service-worker.utils)

(defn log
  [& args]
  (apply js/console.log (into ["[ServiceWorker]"] args)))
