(ns webchange.service-worker.cache-controller.cache-course
  (:require
    [webchange.service-worker.cache-controller.remove-extra-caches :as remove-extra]
    [webchange.service-worker.common.cache :refer [cache-app-resources
                                                   cache-game-resources
                                                   cache-endpoints]]
    [webchange.service-worker.common.fetch :refer [fetch-web-app-resources]]
    [webchange.service-worker.wrappers :refer [then promise-all js-fetch response-json]]))

(defn load-web-app-resources
  [course-name]
  (-> (fetch-web-app-resources)
      (then (fn [resources]
              (promise-all [(-> resources (aget "resources") (cache-app-resources course-name))
                            (-> resources (aget "endpoints") (cache-endpoints course-name))])))))

(defn cache-course
  [course-name]
  (-> (remove-extra/remove-extra-caches)
      (then (fn [] (load-web-app-resources course-name)))))
